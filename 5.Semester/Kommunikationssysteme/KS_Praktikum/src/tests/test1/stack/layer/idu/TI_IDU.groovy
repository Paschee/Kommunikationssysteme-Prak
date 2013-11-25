package tests.test1.stack.layer.idu

import tests.test1.stack.layer.pdu.PDU

/**
 * IDU von Transportschicht (TCP oder UDP) zu IP
 */
class TI_IDU extends IDU {

    /** Ziel-IP-Adresse */
    String dstIpAddr

    /** Zu transportierender Protokolltyp */
    int protocol

    /** Zu transportierende PDU */
    PDU sdu

    /** Liefert die Textdarstellung der IDU */
    String toString() {
        return String.format("TI_IDU: [dstIpAddr: ${dstIpAddr}, protocol: ${protocol}, sdu: ${sdu}]")
    }
}
