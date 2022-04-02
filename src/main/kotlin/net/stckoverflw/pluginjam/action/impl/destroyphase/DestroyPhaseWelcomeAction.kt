package net.stckoverflw.pluginjam.action.impl.destroyphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation

class DestroyPhaseWelcomeAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i>Der sicherste Weg um sicherzustellen, dass die Kristalle nicht in falschen Händen landen, ist es, sie zu zerstören.</i>")
            .addMessage("<i>Macht ein Feuer und zerstört die Kristalle in diesem!</i>")
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
