package net.stckoverflw.pluginjam.action.impl.taskphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class TaskPhaseEndConversationAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<tr:task_end_1>", "<tr:villager>")
            .addMessage("<tr:task_end_2:'<light_purple><tr:magic_amethyst><light_purple>'>", "<tr:villager>")
            .addMessage("<tr:task_end_3:'<red><tr:leviatans></red>'>", "<tr:villager>", 0.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
