package tests.test1.stack.layer.pdu

/**
 * MAC-Frame (Link-PDU)<br/>
 * Unvollständig
 */
class L_PDU extends PDU {
    static final long serialVersionUID = 1L

    String srcMacAddr
    String dstMacAddr
    int type
    PDU sdu

    // Liefert die Textdarstellung der PDU
    String toString() {
        return String.format("L_PDU:[dstMacAddr:${dstMacAddr}, srcMacAddr:${srcMacAddr}, type:${type}, sdu:${sdu}]")
    }
}
