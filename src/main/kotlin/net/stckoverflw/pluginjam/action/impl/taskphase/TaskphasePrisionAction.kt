package net.stckoverflw.pluginjam.action.impl.taskphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class TaskphasePrisionAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Ich bin der Gamemaster, ihr müsst mir helfen etc.", "Gamemaster", 1.seconds)
        return this
    }
}
