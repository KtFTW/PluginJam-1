package net.stckoverflw.pluginjam.action.impl.twistphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class TwistPhaseHelperAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i><tr:twist_phase_helper_1></i>")
            .addMessage("<i><tr:twist_phase_helper_2></i>")
            .addMessage("<i><tr:twist_phase_helper_3></i>")
            .addMessage("<i><tr:twist_phase_helper_4></i>")
            .addMessage("<i><tr:twist_phase_helper_5></i>")
            .addMessage("<i><tr:twist_phase_helper_6></i>", delay = 0.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
