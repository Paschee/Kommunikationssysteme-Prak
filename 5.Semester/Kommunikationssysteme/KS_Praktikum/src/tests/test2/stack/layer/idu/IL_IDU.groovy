package tests.test2.stack.layer.idu

import tests.test2.stack.layer.pdu.PDU

/**
 * IDU von IP zu Link
 */
class IL_IDU extends IDU {

    /** Name des Link-Ports über den empfangen wurde */
    String lpName

    /** Ip-Adresse des nächsten Ziels */
    String nextHopAddr

    /** Zu transportierende PDU */
    PDU sdu

    /** Konvertieren in Text */
    String toString() {
        return String.format("IL_IDU: [lpName: ${lpName}, nextHopAddr: ${nextHopAddr}, sdu: ${sdu}]")
    }
}
