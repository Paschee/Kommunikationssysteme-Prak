package tests.test1.stack

import common.utils.Utils
import tests.test1.stack.connector.Connector
import tests.test1.stack.connector.ConnectorToEthernet
import tests.test1.stack.connector.ConnectorToVirtual
import tests.test1.stack.layer.IpLayer
import tests.test1.stack.layer.LinkLayer
import tests.test1.stack.layer.UdpLayer
import tests.test1.stack.layer.idu.AU_IDU
import tests.test1.stack.layer.idu.UA_IDU

import java.util.concurrent.LinkedBlockingQueue as MQueue

/**
 * Der Netzwerkstack.
 * <p/>
 * Beinhaltet Fragmente der Protokolle UDP, IP und Link (IEEE 802.3 ("Ethernet-MAC")).<br/>
 * UDP - User Datagram Protocol, IP - Internet Protocol,<br/>
 * IEEE - Institut of Electrical and Electronics Engineers,<br/>
 * Ethernet - Produktbezeichnung eines häufig verwendeten LAN,<br/>
 * LAN - Local Area Network, MAC - Medium Access Control<br/>
 * <p/>
 * Für weitere Erläuterungen siehe die Kommentare im Quelltext.
 * <p/>
 */
public class Stack {

    //========================================================================================================
    // NETZWERKADAPTER ANFANG
    //========================================================================================================

    /**
     * Eine Map von Netzwerkadaptern. <br/>
     * Der Schlüssel (Name) wird in der Routingtabelle hinterlegt und von der Link-Schicht beim Senden zur
     * Auswahl des Adapters verwendet
     */
    Map<String,Connector> connectors = [:]

    //========================================================================================================
    // NETZWERKADAPTER ENDE
    //========================================================================================================


    //========================================================================================================
    // Vereinbarungen ANFANG
    //========================================================================================================

    //----------------------------------------------------------
    // Protokoll-Schichten
    /** UDP-Schicht */
    UdpLayer udp = new UdpLayer()
    /** IP-Schicht */
    IpLayer ip = new IpLayer()
    /** Link-Schicht */
    LinkLayer link = new LinkLayer()

    //----------------------------------------------------------

    /** Message-Queue von UDP zur Anwendung  */
    MQueue<UA_IDU> fromUdpQ = new MQueue(Utils.MAXQUEUE)

    /** Message-Queue zu UDP */
    MQueue<AU_IDU> toUdpQ

    //========================================================================================================
    // Methoden ANFANG
    //========================================================================================================

    //----------------------------------------------------------
    /**
     * Starten des Netzwerkstacks
     * @param config Konfiguration aus Datei "config"
     */
    void start(ConfigObject config) {
        // Message Queue Anschluesse -> Link-Schicht
        MQueue conQ = link.getFromConnQ()

        // -----------------------------------------------------------------------------------------------
        // Netzwerkanschluesse initialiiseren

        config.networkConnectors.each { con ->
            if (con.virtual) {

                // Anschluss in einen virtuellen HUB (virtuelles LAN)
                connectors[con.lpName] = new ConnectorToVirtual(con.lpName, con.link, con.connector, con.macAddr, conQ)
            }
            else {
                // Anschluss in ein reales LAN
                connectors[con.lpName] = new ConnectorToEthernet(con.lpName, con.deviceName, con.macAddr, con.recvFilter, conQ)
            }
        }

        // Netzwerkanschluesse starten
        connectors.values().each {
            it.start()
        }

        // -----------------------------------------------------------------------------------------------

        // Protokollschichten verbinden
        // Link-Schicht
        link.start(ip.getFromLinkQ(), connectors, config)

        // IP-Schicht
        ip.setRoutingTable(config.routingTable)
        ip.start([(IpLayer.PROTO_UDP): udp.getFromIpQ()], link.getFromIpQ(), config)

        // UDP-Schicht wird mit Stack verbunden
        udp.start(fromUdpQ, ip.getFromTcpUdpQ(), config)
        toUdpQ = udp.getFromAppQ()
    }
    //----------------------------------------------------------

    //----------------------------------------------------------
    /**
     * Stoppen des Netzwerkstacks
     */
    void stop() {

        // Schichten stoppen
        udp.stop()
        ip.stop()
        link.stop()

        // Netzwerkanschluesse stoppen
        connectors.values().each {it.stop()}
    }
    //----------------------------------------------------------

    //=== Schnittstelle zur UDP-Schicht ========================
    //----------------------------------------------------------
    /**
     * Auf UDP-Empfang warten
     * @return [SDU]
     */
    List udpReceive() {
        // Warten auf Empfang
        List uidu = udp.receiving()

        return uidu
    }
    //----------------------------------------------------------

    //----------------------------------------------------------
    /**
     * Über UDP-Senden
     * @param AU_IDU
     */
    void udpSend(Map idu) {
        AU_IDU au_idu = new AU_IDU()
        // Parameter uebernehmen
        au_idu.dstIpAddr = idu.dstIpAddr
        au_idu.dstPort = idu.dstPort
        au_idu.srcPort = idu.srcPort
        au_idu.sdu = idu.sdu
        // IDU an UDP uebergeben
        toUdpQ.put(au_idu)
    }
    //----------------------------------------------------------

    //=== IP-Schicht ===========================================
    //----------------------------------------------------------
    /**
     * Routingtabelle von IP-Schicht lesen
     */
    List<List> getRoutingTable() {
        ip.getRoutingTable()
    }
    //----------------------------------------------------------

    //----------------------------------------------------------
    /**
     * Routingtabelle der IP-Schicht schreiben
     */
    void setRoutingTable(List<List> table) {
        ip.setRoutingTable(table)
    }
    //----------------------------------------------------------
 }
