package net.stckoverflw.pluginjam.action.impl.fightphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation

class FightPhaseWavesIntroductionAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i>Oh nein! Die <red>Leviatanen</red> haben deinen Diebstahlversuch bemerkt!</i>")
            .addMessage("<i>Bekämpfe alle Krieger der <red>Leviatane</red> um den Zugang zum zweiten Teil des <light_purple>magischen Amethysten</light_purple> zu erlangen.</i>")
            .start()
            .whenComplete { _, _ ->
                complete()
            }

        return this
    }
}
