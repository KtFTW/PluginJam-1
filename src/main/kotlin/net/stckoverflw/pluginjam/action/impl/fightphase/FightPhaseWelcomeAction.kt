package net.stckoverflw.pluginjam.action.impl.fightphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class FightPhaseWelcomeAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i>Beende den Parkour und schnapp dir den Amethysten!</i>", delay = 5.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
