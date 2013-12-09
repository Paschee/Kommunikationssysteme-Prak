package tests.test1.hosts.client

import common.utils.Utils
import tests.test1.stack.Stack

/**
 * Einfacher-Client.<br/>
 * Sendet einen Text zu einem Server.
 */
class Client {

    //========================================================================================================
    // Vereinbarungen ANFANG
    //========================================================================================================

    // Der Netzwerk-Protokoll-Stack
    Stack stack

    /** Konfigurations-Objekt */
    ConfigObject config

    //------------------------------------------------------------------------------

    /** Ziel-IP-Adresse */
    String serverName

    /** Zielportadresse */
    int serverPort

    //========================================================================================================
    // Hauptprogramm ANFANG
    //========================================================================================================

    //------------------------------------------------------------------------------
    /**
     * Start der Anwendung
     */
    static void main(String[] args) {
        // Client-Klasse instanziieren
        Client application = new Client()
        // und starten
        application.client()
    }
    //------------------------------------------------------------------------------

    //------------------------------------------------------------------------------
    /**
     * Client-Seite der Anwendung initialisieren.<br/>
     * Als Transport-Protokoll wird UDP verwendet.
     */
    void client() {

        // HTTP-Header fuer GET-Request
        final String text = "Hallo, hier sendet Ernie!"
		final String ttext="senden";
        /** Anwendungs-PDU */
        String apdu
		String dstIPAddr;
		String dstPort;
        /** Anwendungsdaten */
        String data

        // ------------------------------------------------------------

        // Konfiguration holen
        // Erster Parameter: der Name des Verzeichnisses, der den Versuch enthält
        // Zweiter Parameter: Name der Konfiguation fuer dieses Gerät in der Konfigurationsdatei
        config = Utils.getConfig("test1", "client")

        // ------------------------------------------------------------

        // Netzwerkstack initialisieren
        stack = new Stack()
        stack.start(config)

        // ------------------------------------------------------------

        Utils.writeLog("Client", "client", "startet", 1)

        // ------------------------------------------------------------

        // IPv4-Adresse des Servers vom Namensdienst holen
        stack.udpSend(dstIpAddr: "0.0.0.0", dstPort: 0,
                        srcPort: 0, sdu: ttext)
		
		while(true){
			// Auf UDP-Empfang warten
			(dstIPAddr, dstPort, data) = stack.udpReceive()

			// Übertragenen Text ausgeben
			println("Client hat empfangen: "+data.toString())
		}
		
    }
    //------------------------------------------------------------------------------
}
