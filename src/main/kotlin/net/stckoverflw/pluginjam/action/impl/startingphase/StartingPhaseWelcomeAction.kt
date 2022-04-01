package net.stckoverflw.pluginjam.action.impl.startingphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class StartingPhaseWelcomeAction : Action() {

    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Guten Tag, Fremder", 1.seconds)
            .addMessage("Es spricht sich herum, dass du ein guter Krieger bist.", 2.seconds)
            .addMessage("Das ganze Dorf braucht dringend deine Hilfe!", 2.seconds)
            .addMessage("Wir können das nicht auf offener Straße besprechen, folg mir in mein Haus und ich erkläre dir alles!", 5.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
