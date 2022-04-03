package net.stckoverflw.pluginjam.action.impl.fightphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class FightPhaseWelcomeAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i><tr:fight_welcome_1:'<red><tr:leviatans></red>'></i>", delay = 2.seconds)
            .addMessage("<i><tr:fight_welcome_2></i>", delay = 0.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
