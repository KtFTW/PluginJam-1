package net.stckoverflw.pluginjam.action.impl.prisionphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class PrisonPhaseWelcomeAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<tr:prison_1>", "<tr:villager>")
            .addMessage("<tr:prison_2:'<red><tr:the_life></red>'>", "<tr:villager>", 4.seconds)
            .addMessage("<tr:prison_3:'<red><tr:leviatans></red>'>", "<tr:villager>", 4.seconds)
            .addMessage("<tr:prison_4:'<light_purple><tr:magic_amethyst></light_purple>'>", "<tr:villager>", 6.seconds)
            .addMessage("<tr:prison_5:'<red><tr:leviatans></red>'>", "<tr:villager>", 4.seconds)
            .addMessage("<tr:prison_6>", "<tr:villager>", 4.seconds)
            .addMessage("<tr:prison_7:'<red><tr:all></red>'>", "<tr:villager>", 4.seconds)
            .addMessage("<tr:prison_8>", "<tr:villager>", 3.seconds)
            .addMessage("<tr:prison_9>", "<tr:villager>", 4.seconds)
            .addMessage("<tr:prison_10>", "<tr:villager>", 5.seconds)
            .addMessage("<tr:prison_11>", "<tr:villager>", 4.seconds)
            .addMessage("<tr:prison_12>", "<tr:villager>", 4.seconds)
            .addMessage("<tr:prison_13:'<red><tr:leviatans></red>'>", "<tr:villager>", 0.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
