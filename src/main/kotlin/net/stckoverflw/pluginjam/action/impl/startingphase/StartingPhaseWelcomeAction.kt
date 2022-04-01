package net.stckoverflw.pluginjam.action.impl.startingphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class StartingPhaseWelcomeAction : Action() {

    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Guten Tag, <green>Fremde", "Dorfbewohner", 1.seconds)
            .addMessage("Es spricht sich herum, dass ihr gute Krieger seid.", "Dorfbewohner", 2.seconds)
            .addMessage("Das ganze Dorf braucht dringend eure Hilfe!", "Dorfbewohner", 2.seconds)
            .addMessage(
                "Wir können das nicht auf offener Straße besprechen, folgt mir in mein Haus und ich erkläre euch alles!",
                "Dorfbewohner",
                0.seconds
            )
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
