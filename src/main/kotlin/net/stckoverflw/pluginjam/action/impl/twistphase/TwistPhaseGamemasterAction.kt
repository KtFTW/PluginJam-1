package net.stckoverflw.pluginjam.action.impl.twistphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class TwistPhaseGamemasterAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Ihr seid schon wieder darauf reingefallen!", "Gamemaster")
            .addMessage("Ihr habt doch nicht wirklich geglaubt, dass Frieden mein Ziel ist.", "Gamemaster", 4.seconds)
            .addMessage("Ich werde euch und eure Familien nicht beschützen, ich werde sie töten.", "Gamemaster")
            .addMessage(
                "Da ich jetzt beide Teile des Amethysten habe, kann mich keiner mehr aufhalten.",
                "Gamemaster",
                2.seconds
            )
            .addMessage("Niemand! MUHAHAHA", "Gamemaster", 0.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
