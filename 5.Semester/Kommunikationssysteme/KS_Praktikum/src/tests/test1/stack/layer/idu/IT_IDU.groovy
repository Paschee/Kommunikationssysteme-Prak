package tests.test1.stack.layer.idu

import tests.test1.stack.layer.pdu.PDU

/**
 * IDU von IP zu TCP
 */
class IT_IDU extends IDU {

    /** Quell-IP-Adresse */
    String srcIpAddr

    /** Quell-Port */
    int srcPort

    /** Zu transportierende PDU */
    PDU sdu

    /** Liefert die Textdarstellung der IDU */
    String toString() {
        return String.format("IT_IDU: [srcIpAddr: ${srcIpAddr}, srcPort: ${srcPort}, sdu: ${sdu}]")
    }
}
