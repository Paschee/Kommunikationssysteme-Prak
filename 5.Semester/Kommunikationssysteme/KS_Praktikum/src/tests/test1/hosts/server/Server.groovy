package tests.test1.hosts.server

import common.utils.Utils
import tests.test1.stack.Stack

/**
 * Empfängt einen Text und schreibt ihn auf das Terminal
 */
class Server {

    //========================================================================================================
    // Vereinbarungen ANFANG
    //========================================================================================================

    // Der Netzwerk-Protokoll-Stack
    Stack stack

    /** Konfigurations-Objekt */
    ConfigObject config

    /** Stoppen der Threads wenn false */
    Boolean run = true

    //========================================================================================================
    // Methoden ANFANG
    //========================================================================================================

    //------------------------------------------------------------------------------
    /**
     * Start der Anwendung
     */
    static void main(String[] args) {
        // Client-Klasse instanziieren
        Server application = new Server()
        // und starten
        application.server()
    }
    //------------------------------------------------------------------------------

    //------------------------------------------------------------------------------
    /**
     * Server-Seite der Anwendung initialisieren.<br/>
     * Als Transport-Protokoll wird UDP verwendet.
     */
    void server() {

        /** Empfangsdaten */
        String data

        // IPv4-Adresse des Absenders
        String dstIPAddr

        // Port des Absenders
        int dstPort

        //------------------------------------------------

        // Konfiguration holen
        config = Utils.getConfig("test1", "server")

        // ------------------------------------------------------------

        // Netzwerkstack initialisieren
        stack = new Stack()
        stack.start(config)

        Utils.writeLog("Server", "server", "startet", 1)

        //------------------------------------------------

        while (run) {
            // Auf UDP-Empfang warten
            (dstIPAddr, dstPort, data) = stack.udpReceive()

            // Übertragenen Text ausgeben
            println(data)
        }
    }
    //------------------------------------------------------------------------------
}
