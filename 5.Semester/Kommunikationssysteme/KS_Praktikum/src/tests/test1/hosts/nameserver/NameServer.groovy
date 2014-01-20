package tests.test1.hosts.nameserver

import common.utils.Utils
import tests.test1.stack.Stack

/**
 * Ein Server der Gerätenamen in IPv4-Adressen auflöst. Als Transport-Protokoll wird UDP verwendet.
 */
class NameServer {

    //========================================================================================================
    // Vereinbarungen ANFANG
    //========================================================================================================

    /** Der Netzwerk-Protokoll-Stack */
    Stack stack

    /** Konfigurations-Objekt */
    ConfigObject config

    /** Stoppen der Threads wenn false */
    Boolean run = true

    /** Tabelle zur Umsetzung von Namen in IP-Adressen */
    Map<String, String> nameTable = [
            server: "192.168.2.10",
            client: "192.168.1.10",
    ]

    //========================================================================================================
    // Methoden ANFANG
    //========================================================================================================

    //------------------------------------------------------------------------------
    /**
     * Start der Anwendung
     */
    static void main(String[] args) {
        // Client-Klasse instanziieren
        NameServer application = new NameServer()
        // und starten
        application.server()
    }
    //------------------------------------------------------------------------------

    /**
     * Der Namens-Dienst
     */
    void server() {

        //------------------------------------------------

        // Konfiguration holen
        config = Utils.getConfig("test1", "nameserver")

        // ------------------------------------------------------------

        // Netzwerkstack initialisieren
        stack = new Stack()
        stack.start(config)

        Utils.writeLog("NameServer", "server", "startet", 1)
        println("Gleich kommts :-)")
        println(config.size())
        println("Test (Portnummer ausgeben) : " + config.getProperty("ownPort"))
        println("Nameserver-IP : " + config.getProperty("networkConnectors").getAt("ipAddr"))

        while (run) {
            // Hier Protokoll implentieren

            // ...
            sleep(Utils.sec1) // hier nur damit die Schleife nicht ständig laueft
        }
    }
    //------------------------------------------------------------------------------
}
