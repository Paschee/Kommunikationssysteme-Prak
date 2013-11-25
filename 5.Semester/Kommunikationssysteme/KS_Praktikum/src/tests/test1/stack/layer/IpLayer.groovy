package tests.test1.stack.layer

import common.utils.Utils
import tests.test1.stack.layer.idu.IL_IDU
import tests.test1.stack.layer.idu.IU_IDU
import tests.test1.stack.layer.idu.LI_IDU
import tests.test1.stack.layer.idu.TRI_IDU
import tests.test1.stack.layer.pdu.I_PDU

import java.util.concurrent.LinkedBlockingQueue as MQueue

/**
 * IPv4-Schicht
 */
class IpLayer {
    //========================================================================================================
    // Konstanten ANFANG
    //========================================================================================================

    // Protokolle übergeben ihren Typ
    /** Protokolltyp TCP */
    public static final int PROTO_TCP = 6
    /** Protokolltyp UDP */
    public static final int PROTO_UDP = 17

    //========================================================================================================
    // Daten ANFANG
    //========================================================================================================

    /** Stoppen der Threads wenn false */
    Boolean run = true

    //========================================================================================================

    // Message queues in Richtung der TCP- UDP-Schicht
    /** Message-Queue von TCP/UDP zu IP */
    MQueue<TRI_IDU> fromTcpUdpQ = new MQueue(Utils.MAXQUEUE)
    /** Message-Queue von Link zu IP */
    MQueue<LI_IDU> fromLinkQ = new MQueue(Utils.MAXQUEUE)

    //------------------------------------------------------------------------------

    // message queues zu Nachbarschichten
    /** Message-Queues von IP zu TCP und UDP */
    Map<Integer, MQueue> toTcpUdpQ = [(PROTO_TCP): null, (PROTO_UDP): null]
    /** Message-Queues von IP zu Link */
    MQueue<IL_IDU> toLinkQ = null

    //========================================================================================================

    /** Routing-Tabelle */
    List<List> routingTable

    /** Eigene IP-Adressen (eine IPv4-Adresse je Anschluss) */
    Map<String, String> ownIpAddrs = [:]

    /** Einheitliche Subnetz-Maske */
    String globalNetMask

    //========================================================================================================
    // Methoden ANFANG
    //========================================================================================================

    /**
     * Empfängt Daten von der Link-Schicht
     */
    void receive() {

        // IDU von Link
        LI_IDU li_idu

        // IDU zu UDP
        IU_IDU iu_idu

        // IP-PDU
        I_PDU i_pdu

        // Name des zu verwendenden Link-Ports
        String linkPortName

        // IP-Adresse des nächsten Gerätes ("hops") auf der Route
        String nextHopAddr

        while (run) {
            // blockierendes Lesen von Link-Schicht
            li_idu = fromLinkQ.take()

            // IP-PDU entnehmen
            i_pdu = li_idu.sdu as I_PDU

            Utils.writeLog("IpLayer", "receive", "uebernimmt von Link: ${li_idu}", 4)

            // Hier Test auf eigene IP-Adresse (oder IP-Broadcast) als Ziel-Adresse
            // u.U. "forwarden"
            // ... findNextHop(i_pdu.dstIpAddr)
            // ...

            // Daten an UDP uebergeben
            iu_idu = new IU_IDU()
            iu_idu.sdu = i_pdu.sdu
            toTcpUdpQ[PROTO_UDP].put(iu_idu)
        }
    }

    //------------------------------------------------------------------------------

    /**
     * Übergibt Daten von der TCP- oder UDP-Schicht an die Link-Schicht
     */
    void send() {

        // IDU von TCP oder UDP
        TRI_IDU ti_idu

        // IDU zu Link
        IL_IDU il_idu

        // IP-PDU
        I_PDU i_pdu

        while (run) {
            // blockierendes Lesen von Anwendung
            ti_idu = fromTcpUdpQ.take()

            Utils.writeLog("IpLayer", "send", "uebernimmt von TCP/UDP: ${ti_idu}", 4)

            // Hier z.B. nächsten Hop suchen
            // ... findNextHop(ti_idu.dstIpAddr)
            // ...

            // IP-PDU erzeugen
            i_pdu = new I_PDU()

            // Anwendungsdaten übernehmen
            i_pdu.sdu = ti_idu.sdu

            // IDU zur Link-Schicht erzeugen
            il_idu = new IL_IDU()

            // i_pdu eintragen
            il_idu.sdu = i_pdu

            // Daten an Link-Schicht uebergeben
            toLinkQ.put(il_idu)
        }
    }

    //========================================================================================================

    /**
     * Routing-Tablle holen, um z.B. durch Routingprotokolle modifiziert zu werden
     * @return
     */
    List<List> getRoutingTable() {
        return routingTable
    }

    //------------------------------------------------------------------------------

    /**
     * Routing-Tabelle setzen, z.B. nach Modifizierung durch Routingprotokoll <br/>
     * oder zur Initialisierung
     * @param table
     */
    void setRoutingTable(List routingTable) {
        this.routingTable = routingTable
    }

    //------------------------------------------------------------------------------

    /**
     * Liefert die Message-Queue in Senderichtung
     * @return
     */
    MQueue<TRI_IDU> getFromTcpUdpQ() {
        return fromTcpUdpQ
    }

    //------------------------------------------------------------------------------

    /**
     * Liefert die Message-Queue in Empfangsrichtung
     * @return
     */
    MQueue<LI_IDU> getFromLinkQ() {
        return fromLinkQ
    }

    //------------------------------------------------------------------------------

    /**
     * Starten des IP-Protokolls
     * @param toTcpUdpQ
     * @param toLinkQ
     * @param config
     */
    void start(Map toTcpUdpQ, MQueue<IL_IDU> toLinkQ, ConfigObject config) {

        this.toTcpUdpQ = toTcpUdpQ
        this.toLinkQ = toLinkQ

        // Tabelle der IP-Adressen erzeugen
        config.networkConnectors.each { conn ->
            ownIpAddrs[conn.lpName] = conn.ipAddr
        }

        // Start der Threads
        Thread.start { receive() }
        Thread.start { send() }
    }

    //------------------------------------------------------------------------------

    /**
     * Stoppen der Sende- und Empfang-Threads
     */
    void stop() {
        run = false
    }
}
