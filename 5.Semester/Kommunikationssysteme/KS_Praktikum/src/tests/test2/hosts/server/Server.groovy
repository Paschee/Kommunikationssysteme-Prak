#!/usr/bin/env groovy

package tests.test2.hosts.server

import common.utils.Utils
import tests.test2.stack.Stack

import java.util.regex.Matcher

/**
 * Ein einfacher HTTP-Server
 * Liefert das Ende des Pfades der URL im GET-Request als Vorname in einem Satz zurück
 * http://adresse/pfad/Ernie -> "Ernie ist hier!"
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
        application.start()
    }
    //------------------------------------------------------------------------------

    //------------------------------------------------------------------------------
    /**
     * Ein HTTP-Server mit rudimentärer Implementierung des Protokolls HTTP (Hypertext Transfer Protocol)
     */
    void start() {

        /** ID der TCP-Verbindung */
        int connId

        /** Der im HTTP-Request gelieferte Name */
        String name

        /** IP-Adresse und Portnummer des Absenders<br/>
         * Diese Informationen werden von der Anwendung normalerweise <br/>
         * dazu verwended festzustellen, ob eine Verbindung von diesem <br/>
         * Rechner/Port zugelassen werden soll.
         */
        String dstIpAddr
        String dstPort

        /** Anwendungsprotokolldaten */
        String apdu

        /** Anwendungsprotokolldaten als String */
        String data

        /** Antwort */
        String reply
        String replyA =
            """\
HTTP/1.1 200 OK
Content-Length: %d
Content-Type: text/plain

"""
        String replyB =
            """\
%s ist hier!
"""

        /** Ein Matcher-Objekt zur Verwendung regulärer Ausdruecke */
        Matcher matcher

        /** Daten empfangen solange false */
        boolean ready = false

        // ------------------------------------------------------------

        // Konfiguration holen
        config = Utils.getConfig("test2", "server")

        // ------------------------------------------------------------

        // Netzwerkstack initialisieren
        stack = new Stack()
        stack.start(config)

        //------------------------------------------------

        Utils.writeLog("Server", "start", "startet", 1)

        //------------------------------------------------

        while (run) {
            // Auf das Öffnen einer TCP-Verbindung warten
            Map aidu = stack.tcpListen()

            // ...
        }
    }
    //------------------------------------------------------------------------------
}
