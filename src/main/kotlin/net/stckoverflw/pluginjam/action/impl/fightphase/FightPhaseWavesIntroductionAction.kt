package net.stckoverflw.pluginjam.action.impl.fightphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation

class FightPhaseWavesIntroductionAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i>Bekämpfe alle Wellen der Monster um die Tür freizuschalten.</i>")
            .start()
            .whenComplete { _, _ ->
                complete()
            }

        return this
    }
}
