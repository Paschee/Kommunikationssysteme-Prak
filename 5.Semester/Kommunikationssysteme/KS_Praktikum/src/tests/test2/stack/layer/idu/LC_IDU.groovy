package tests.test2.stack.layer.idu

import tests.test2.stack.layer.pdu.PDU

/**
 * IDU von Link zu Anschluessen
 */
class LC_IDU extends IDU {

    /** Zu transportierende PDU */
    PDU sdu

    /** Konvertieren in Text */
    String toString() {
        return String.format("LC_IDU: [sdu: ${sdu}]")
    }
}
