package net.stckoverflw.pluginjam.action.impl.taskphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation

class TaskPhaseEndConversationAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Das habt ihr sehr schön gemacht!", "Dorfbewohner")
            .addMessage("Jetzt wo wir die nötige Ausrüstung für den Gegenangriff haben, brauchen wir nur noch den zweiten Teil des <light_purple>magischen Amethysten<light_purple>.", "Dorfbewohner")
            .addMessage("Begebe dich dafür in die Basis der <red>Levitanen</red> und stiehl ihn möglichst unbemerkt.", "Dorfbewohner")
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
