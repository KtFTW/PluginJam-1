package net.stckoverflw.pluginjam.action.impl.destroyphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation

class DestroyPhaseWelcomeAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i><tr:destroy_1></i>")
            .addMessage("<i><tr:destroy_2></i>")
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
