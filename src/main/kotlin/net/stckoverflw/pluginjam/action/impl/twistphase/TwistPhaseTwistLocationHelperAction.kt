package net.stckoverflw.pluginjam.action.impl.twistphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class TwistPhaseTwistLocationHelperAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i><tr:twist_location_helper_1></i>")
            .addMessage(
                "<i><tr:twist_location_helper_2></i>",
                delay = 4.seconds
            )
            .addMessage("<i><tr:twist_location_helper_3></i>", delay = 0.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
