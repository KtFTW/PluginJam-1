package net.stckoverflw.pluginjam.action.impl.twistphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class TwistPhaseTwistLocationHelperAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<i>Ihr habt die geheime Basis des Gamemasters gefunden.")
            .addMessage(
                "<i>Der Gamemaster scheint nicht hier zu sein, aber vielleicht findet ihr hier die Kristalle.",
                delay = 4.seconds
            )
            .addMessage("<i>Aber passt auf! Der Gamemaster darf auf gar keinen Fall erfahren, dass ihr hier seit.")
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
