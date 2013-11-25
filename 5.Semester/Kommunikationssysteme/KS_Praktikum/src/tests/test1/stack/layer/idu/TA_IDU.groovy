package tests.test1.stack.layer.idu

/**
 * IDU von TCP zu Anwendung
 */
class TA_IDU extends IDU {

    /** ID der Verbindung */
    int connId

    /** Quell-IP-Adresse */
    String srcIpAddr

    /** Quell-Port */
    int srcPort

    /** Anwendungsdaten */
    String sdu

    /** Liefert die Textdarstellung der IDU */
    String toString() {
        return String.format("TU_IDU: [srcIpAddr: ${srcIpAddr}, srcPort: ${srcPort}, sdu: ${sdu}]")
    }
}
