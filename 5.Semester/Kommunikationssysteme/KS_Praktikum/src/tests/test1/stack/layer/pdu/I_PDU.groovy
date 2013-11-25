package tests.test1.stack.layer.pdu

/**
 * IP-PDU<br/>
 * Unvollständig
 */
class I_PDU extends PDU {
    static final long serialVersionUID = 1L

    String dstIpAddr
    String srcIpAddr
    int offset
    int id
    int protocol
    PDU sdu

    // Liefert die Textdarstellung der PDU
    String toString() {
        return String.format("I_PDU:[dstIpAddr:${dstIpAddr}, srcIpAddr:${srcIpAddr}, protocol:${protocol}, sdu:${sdu}]")
    }
}
