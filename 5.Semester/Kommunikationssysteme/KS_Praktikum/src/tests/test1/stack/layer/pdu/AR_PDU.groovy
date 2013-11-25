package tests.test1.stack.layer.pdu

/**
 * ARP-PDU<br/>
 * Unvollständig
 */
class AR_PDU extends PDU {
    static final long serialVersionUID = 1L

    String targetHardAddr
    String senderHardAddr
    String targetProtoAddr
    String senderProtoAddr
    int operation

    // Liefert die Textdarstellung der PDU
    String toString() {
        return String.format("AR_PDU:[operation:${operation}, targetHardAddr:${targetHardAddr}, targetProtoAddr:${targetProtoAddr}, " +
                "senderHardAddr:${senderHardAddr}, senderProtoAddr:${senderProtoAddr}]")
    }
}
