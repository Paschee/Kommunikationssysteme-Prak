package tests.test1.stack.layer.idu

import tests.test1.stack.layer.pdu.PDU

/**
 * IDU von IP zu UDP
 */
class IU_IDU extends IDU {

    /** Quell-IP-Adresse */
    String srcIpAddr

    /** Quell-Port */
    int srcPort

    /** zu transportierende Map */
    PDU sdu

    /** Liefert die Textdarstellung der IDU */
    String toString() {
        return String.format("IU_IDU: [srcIpAddr: ${srcIpAddr}, srcPort: ${srcPort}, sdu: ${sdu}]")
    }
}
