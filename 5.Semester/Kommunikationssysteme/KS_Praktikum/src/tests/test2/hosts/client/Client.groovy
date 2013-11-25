#!/usr/bin/env groovy

package tests.test2.hosts.client

import tests.test2.stack.Stack
import common.utils.Utils

import java.util.regex.Matcher

/**
 * Einfacher HTTP-Client.<br/>
 * Sendet einen HTTP-GET-Request an einen HTTP-Server und gibt das empfangene Dokument an das Terminal aus.
 */
class Client {

    //========================================================================================================
    // Vereinbarungen ANFANG
    //========================================================================================================

    // Der Netzwerk-Protokoll-Stack
    Stack stack

    /** Konfigurations-Objekt */
    ConfigObject config

    /** Stoppen der Threads wenn false */
    Boolean run = true

    //------------------------------------------------------------------------------

    /** Ziel-IP-Adresse */
    String serverIpAddr

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
        application.start()
    }

    //------------------------------------------------------------------------------

    /**
     * Ein HTTP-Client mit rudimentärer Implementierung des Protokolls HTTP (Hypertext Transfer Protocol).<br/>
     * Verwendet das TCP-Protokoll.
     */
    void start() {

        // HTTP-Header fuer GET-Request
        final String request =
"""\
GET / HTTP/1.1
Host: www.sesam-strasse.com

"""

        /** Eigene Portadresse */
        int ownPort

        String replyIpAddr
        int replyPort

        /** Anwendungs-PDU */
        String apdu

        /** Anwendungsprotokolldaten als String */
        String data

        /** Länge des HTTP-Body's */
        int bodyLength = 0

        /** Beginn des HTTP-Body's */
        int bodyStart = 0

        /** Aktuelle Länge des HTTP-Body's */
        int curBodyLength = -1

        // Zustände
        final int WAIT_LENGTH = 100
        // Datenlänge bestimmen
        final int WAIT_DNL = 200
        // Leerzeile (Double NewLine) feststellen
        final int WAIT_DATA = 300
        // Restlich Daten erwarten
        int state

        /** Behandlung Regulärer Ausdruecke */
        Matcher matcher

        /** ID der TCP-Verbindung, wenn 0: Fehler */
        int connId = 0

        // ------------------------------------------------------------

        // Konfiguration holen
        // erster Parameter: der Name des Verzeichnisses, der den Versuch enthält
        // zweiter Parameter: Name der Konfiguation fuer dieses Gerät in der Konfigurationsdatei
        config = Utils.getConfig("test2", "client")

        // ------------------------------------------------------------

        // IPv4-Adresse und Portnummer des HTTP-Dienstes
        serverIpAddr = config.serverIpAddr
        serverPort = config.serverPort

        // Netzwerkstack initialisieren
        stack = new Stack()
        stack.start(config)

        // ------------------------------------------------------------

        Utils.writeLog("Client", "start", "startet", 1)

        // ------------------------------------------------------------

        // Eine TCP-Verbindung öffnen
        connId = stack.tcpOpen(dstIpAddr: serverIpAddr, dstPort: serverPort)

        // Hat es funtioniert?
        if (connId > 0) {
            // Ja

            Utils.writeLog("Client", "start", "sendet: ${request}", 1)

            // Datenempfang vorbereiten
            data = ""
            state = WAIT_LENGTH

            // Senden der Anwendungs-Protokolldaten (HTTP-Request)
            stack.tcpSend(connId: connId, sdu: request)

            // Empfang
            while (curBodyLength < bodyLength) {
                // Auf Empfang warten
                Map tidu = stack.tcpReceive(connId: connId)

                // Es wurden längere Zeit keine Daten empfangen oder die Datenlänge ist 0
                // Die Verbindung wird geschlossen
                if (!tidu.sdu)
                    // Nein, abbrechen
                    break

                // A-PDU uebernehmen
                apdu = tidu.sdu

                Utils.writeLog("Client", "start", "empfängt: ${new String(apdu)}", 1)

                // Daten ergänzen
                data += new String(apdu)

                if (state == WAIT_LENGTH) {
                    // Suchen nach Header-Feld "Content-Length"
                    matcher = (data =~ /Content-Length:\s*(\d+)\D/)

                    // Wurde das Header-Feld gefunden?
                    if (matcher) {
                        // Ja
                        // Länge des HTTP-Body's holen
                        bodyLength = (matcher[0] as List<String>)[1].toInteger()
                        state = WAIT_DNL
                    }
                }

                if (state == WAIT_DNL) {

                    // Suchen nach Leerzeile (HTTP-Header-Ende)
                    matcher = (data =~ /\n\n|\r\r|\r\n\r\n/)

                    // Wurde die Leerzeile gefunden?
                    if (matcher) {
                        // Ja, Beginn des HTTP-Body's gefunden

                        bodyStart = matcher.start() + 2 // Index (Anfang) des HTTP-Body's
                        curBodyLength = data.size() - bodyStart
                        state = WAIT_DATA
                    }
                } else if (state == WAIT_DATA) {
                    curBodyLength = data.size() - bodyStart
                }
            } // while

            if (data) Utils.writeLog("Client", "start", "HTTP-Body empfangen: ${data[bodyStart..-1]}", 1)
        } // if

        // Verbindung schliessen
        stack.tcpClose(connId: connId)
    }
}
