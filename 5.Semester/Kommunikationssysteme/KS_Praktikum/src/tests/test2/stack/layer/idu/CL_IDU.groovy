package tests.test2.stack.layer.idu

import tests.test2.stack.layer.pdu.PDU

/**
 * IDU von Anschluessen zu Link
 */
class CL_IDU extends IDU {

    /** Interner Name des Netzwerkanschlusses */
    String lpName

    /** Zu transportierende PDU */
    PDU sdu

    /** Konvertieren in Text */
    String toString() {
        return String.format("CL_IDU: [lpName: ${lpName}, sdu: ${sdu}]")
    }
}
