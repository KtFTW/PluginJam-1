package net.stckoverflw.pluginjam.action.impl.fightphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class FightPhaseWelcomeAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i>Du bist in der Basis der <red>Leviatanen</red>.</i>", delay = 2.seconds)
            .addMessage("<i>Am Ende des Parkours findest du den zweiten Amethysten. Diesen musst du klauen!</i>", delay = 5.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
