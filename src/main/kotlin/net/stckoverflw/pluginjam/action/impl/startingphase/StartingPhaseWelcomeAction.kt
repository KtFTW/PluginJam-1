package net.stckoverflw.pluginjam.action.impl.startingphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class StartingPhaseWelcomeAction : Action() {

    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<tr:hello_1:'<green><tr:foreigners></green>'>", "<tr:villager>", 2.seconds)
            .addMessage("<tr:hello_2>", "<tr:villager>")
            .addMessage("<tr:hello_3>", "<tr:villager>")
            .addMessage(
                "<tr:hello_4>",
                "<tr:villager>", 0.seconds
            )
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
