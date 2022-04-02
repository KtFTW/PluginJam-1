package net.stckoverflw.pluginjam.action.impl.startingphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class StartingPhaseWelcomeAction : Action() {

    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Guten Tag, <green>Fremde.", "Dorfbewohner", 2.seconds)
            .addMessage("Es spricht sich herum, dass ihr gute Krieger seid.", "Dorfbewohner")
            .addMessage("Das ganze Dorf braucht dringend eure Hilfe!", "Dorfbewohner")
            .addMessage(
                "Wir können das nicht auf offener Straße besprechen, folgt mir in mein Haus und ich erkläre euch alles!",
                "Dorfbewohner"
            )
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
