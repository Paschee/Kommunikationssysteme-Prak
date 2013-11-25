package tests.test1.stack.layer.pdu

/**
 * UDP-PDU<br/>
 * Unvollständig
 */
class U_PDU extends PDU  {
    static final long serialVersionUID = 1L

    int dstPort
    int srcPort
    String sdu

    // Liefert die Textdarstellung der PDU
    String toString() {
        return String.format("UPDU:[dstPort:${dstPort}, srcPort:${srcPort}, sdu:${sdu}]")
    }
}

