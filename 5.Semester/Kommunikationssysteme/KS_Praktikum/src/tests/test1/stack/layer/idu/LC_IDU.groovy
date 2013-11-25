package tests.test1.stack.layer.idu

import tests.test1.stack.layer.pdu.PDU

/**
 * IDU von Link zu Anschluessen
 */
class LC_IDU extends IDU {

    /** Zu transportierende PDU */
    PDU sdu

    /** Liefert die Textdarstellung der IDU */
    String toString() {
        return String.format("LC_IDU: [sdu: ${sdu}]")
    }
}
