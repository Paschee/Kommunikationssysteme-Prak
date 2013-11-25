package tests.test2.stack.layer.pdu

/**
 * ARP-PDU<br/>
 * Unvollständig
 */
class AR_PDU extends PDU {
    String targetHardAddr
    String senderHardAddr
    String targetProtoAddr
    String senderProtoAddr
    int operation

    String toString() {
        return String.format("AR_PDU:[operation:${operation}, targetHardAddr:${targetHardAddr}, targetProtoAddr:${targetProtoAddr}, " +
                "senderHardAddr:${senderHardAddr}, senderProtoAddr:${senderProtoAddr}]")
    }
}
