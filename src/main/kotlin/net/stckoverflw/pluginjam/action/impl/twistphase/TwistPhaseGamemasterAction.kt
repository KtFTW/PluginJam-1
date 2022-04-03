package net.stckoverflw.pluginjam.action.impl.twistphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class TwistPhaseGamemasterAction : Action() {
    override fun execute(): Action {
        println("prision converation")
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<tr:twist_gamemaster_1>", "<tr:gamemaster>")
            .addMessage("<tr:twist_gamemaster_2>", "<tr:gamemaster>", 4.seconds)
            .addMessage("<tr:twist_gamemaster_3>", "<tr:gamemaster>")
            .addMessage(
                "<tr:twist_gamemaster_4>",
                "<tr:gamemaster>",
                2.seconds
            )
            .addMessage("<tr:twist_gamemaster_5>", "<tr:gamemaster>", 0.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
