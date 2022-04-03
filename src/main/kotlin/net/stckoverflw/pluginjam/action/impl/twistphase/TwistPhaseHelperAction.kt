package net.stckoverflw.pluginjam.action.impl.twistphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class TwistPhaseHelperAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i>Der Gamemaster ist verschwunden.")
            .addMessage("<i>Bevor es zu spät ist müsst ihr ihn aufhalten.")
            .addMessage("<i>Dazu müsst ihr erstmal aus dem Gefängnis ausbrechen.")
            .addMessage("<i>Ihr müsst ausserdem auch einen Weg finden, den Gamemaster zu lokalisieren.")
            .addMessage("<i>Schaut euch mal in diesem Raum um.")
            .addMessage("<i>Ihr habt nicht viel Zeit, bevor er die Amethysten anwendet.", null, 0.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
