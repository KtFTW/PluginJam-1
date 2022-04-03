package net.stckoverflw.pluginjam.action.impl.fightphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class FightPhaseWavesIntroductionAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i><tr:fight_waves_welcome_1:<red><tr:leviatans></red>></i>")
            .addMessage("<i><tr:fight_waves_welcome_2:<red><tr:leviatans></red>:<light_purple><tr:magic_amethyst></light_purple>></i>", delay = 0.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }

        return this
    }
}
