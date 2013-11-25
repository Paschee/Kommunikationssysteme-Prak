package tests.test1.stack.layer

import common.utils.Utils
import tests.test1.stack.connector.Connector
import tests.test1.stack.layer.idu.CL_IDU
import tests.test1.stack.layer.idu.IL_IDU
import tests.test1.stack.layer.idu.LC_IDU
import tests.test1.stack.layer.idu.LI_IDU
import tests.test1.stack.layer.pdu.L_PDU

import java.util.concurrent.LinkedBlockingQueue as MQueue

/**
 * Die Link-Schicht (MAC-Framing und ARP)
 */
class LinkLayer {

    //========================================================================================================
    // Vereinbarungen ANFANG
    //========================================================================================================

    //------------------------------------------------------------------------------
    /** Dient der Bestimmung der MAC-Adresse eines Geräts auf Grund seiner IP-Adresse */
    Map arpTable
    //------------------------------------------------------------------------------

    /** Stoppen der Threads wenn false */
    Boolean run = true

    //========================================================================================================

    /** message queues in Richtung der IP-Schicht */
    MQueue<IL_IDU> fromIpQ = new MQueue(Utils.MAXQUEUE)

    /** message queue von den Anschluessen */
    MQueue<CL_IDU> fromConnQ = new MQueue(Utils.MAXQUEUE)

    //------------------------------------------------------------------------------

    /** message queues zu Nachbarschichten */
    MQueue<LI_IDU> toIpQ = null

    //========================================================================================================

    /** Die Anschluesse, adressierbar über den internen Namen des Anschlusses */
    Map<String, Connector> connectors

    //========================================================================================================
    // Methoden ANFANG
    //========================================================================================================

    /**
     * Empfängt Daten von den Anschluessen
     */
    void receive() {
        while (run) {
            /** IDU von Anschluessen */
            CL_IDU cl_idu

            /** IDU zu Anschluessen */
            LC_IDU lc_idu

            /** IDU zu IP */
            LI_IDU li_idu

            //------------------------------------------------------------------------------

            // blockierendes Lesen der Anschluesse
            cl_idu = fromConnQ.take()

            // Mac-Frame (L-PDU) entnehmen
            L_PDU macFrame = cl_idu.sdu as L_PDU

            Utils.writeLog("LinkLayer", "receive", "uebernimmt  von Anschluss ${cl_idu.lpName}: ${cl_idu}", 5)

            // Hier Test auf eingene MAC-Adresse oder MAC-Broadcast
            // ...

            // IP-PDU

            // IDU erzeugen
            li_idu = new LI_IDU()
            li_idu.sdu = macFrame.sdu

            // IDU an IP uebergeben
            toIpQ.put(li_idu)
        }
    }

    //------------------------------------------------------------------------------

    /**
     * Holt Daten von der IP-Schicht und uebergibt sie an einen Anschluss
     */
    void send() {

        /** Name des Linkports */
        String lpName

        /** IDU von IP */
        IL_IDU il_idu

        /** IDU zu Anschluessen */
        LC_IDU lc_idu

        /** Ein MAC-Frame */
        L_PDU macFrame

        /** Der zu verwendende Anschluss */
        Connector connector

        while (run) {
            // Blockierendes Lesen von IP-Schicht
            il_idu = fromIpQ.take()

            Utils.writeLog("LinkLayer", "send", "uebernimmt  von IP: ${il_idu}", 5)

            // Anschluss bestimmen
            // Der Name des zu verwendenden Link-Ports (Anschlusses) wird eigentlich
            // von der IP-Schicht geliefert!
            connector = connectors["lp1"]

            // MAC-Frame erzeugen
            macFrame = new L_PDU()
            // Hier muss z.B. die eigene MAC-Adresse bestimmt werden:
            // connector.getMacAddr()

            // IDU zu Anschluss erzeugen
            lc_idu = new LC_IDU()
            lc_idu.sdu = macFrame // L_PDU eintragen

            // Die MAC-Adresse des Ziel wird aus einer Tabelle entnommen deren Inhalt per ARP
            // (Address Resolution Protocol) dynamisch bestimmt wird.
            // Hier verwenden Sie die in "config" vorgegebene ARP-Tabelle
            // macFrame.dstMacAddr = arpTable[il_idu.nextHopAddr]

            macFrame.sdu = il_idu.sdu // PDU entnehmen

            Utils.writeLog("LinkLayer", "send", "uebergibt  an Anschluss lp1: ${lc_idu}", 5)

            // Daten an Anschluss uebergeben
            connector.send(lc_idu)
        }
    }

    //========================================================================================================

    /**
     * Liefert
     * @return
     */
    MQueue<IL_IDU> getFromIpQ() {
        return fromIpQ
    }

    //------------------------------------------------------------------------------

    /**
     * Liefert
     * @return
     */
    MQueue<CL_IDU> getFromConnQ() {
        return fromConnQ
    }

    //------------------------------------------------------------------------------

    /**
     * Starten lder Link-Schicht
     * @param toIpQ
     * @param connectors
     * @param config
     */
    void start(MQueue<LI_IDU> toIpQ, Map<String, Connector> connectors, ConfigObject config) {

        // Parameteruebernahme
        this.connectors = connectors
        this.toIpQ = toIpQ
        this.arpTable = config.arpTable

        /** Start der Threads */
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
