package tests.test1.stack.layer.idu

/**
 * IDU von Anwendung zu UDP
 */
class AU_IDU extends IDU {

    /** Ziel-IP-Adresse */
    String dstIpAddr

    /** Ziel-Port */
    int dstPort

    /** Eigener Port */
    int srcPort

    /** Anwendungsdaten */
    String sdu

    /** Liefert die Textdarstellung der IDU */
    String toString() {
        return String.format("AU_IDU: [dstIpAddr: ${dstIpAddr}, dstPort: ${dstPort}, srcPort: ${srcPort}, sdu: ${sdu}]")
    }
}
