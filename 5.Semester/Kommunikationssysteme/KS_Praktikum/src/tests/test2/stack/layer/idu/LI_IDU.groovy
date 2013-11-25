package tests.test2.stack.layer.idu

import tests.test2.stack.layer.pdu.PDU

/**
 * Daten zwischen IP-Schicht und Link-Schicht
 */
class LI_IDU extends IDU {

    /** Name des Link-Ports über den empfangen wurde */
    String lpName

    /** Zu transportierende PDU */
    PDU sdu

    /** Konvertieren in Text */
    String toString() {
        return String.format("LI_IDU: [lpName: ${lpName}, sdu: ${sdu}]")
    }
}
