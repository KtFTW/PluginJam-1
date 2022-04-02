package net.stckoverflw.pluginjam.action.impl.prisionphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation

class PrisonPhaseWelcomeAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Ihr fragt euch jetzt bestimmt, warum ihr hier seid.", "<blue>Gamemaster</blue>")
            .addMessage("Nunja, <red>das Leben von uns und von unseren Familien steht auf dem Spiel und muss gerettet werden.</red>", "<blue>Gamemaster</blue>")
            .addMessage("Die COOLER_NAME_EINFÜGEN greifen uns seit Jahren im Untergrund an und bereiten gerade einen Überfall auf uns vor.", "<blue>Gamemaster</blue>")
            .addMessage("Es gibt 2 Teile vom <pink>magischen Amethyst</pink>. Vereinzelt sind diese wertlos, doch vereint besitzen sie unvorstellbare Macht.", "<blue>Gamemaster</blue>")
            .addMessage("Den einen Teil haben wir, den anderen haben die COOLER_NAME_EINFÜGEN.", "<blue>Gamemaster</blue>")
            .addMessage("Wir müssen unbedingt, verhindern, dass sie diesen Teil bekommen.", "<blue>Gamemaster</blue>")
            .addMessage("Bei der Macht, die diese besitzen würden, würde dies das Leben <red>ALLER</red> Dorfbewohner kosten.", "<blue>Gamemaster</blue>")
            .addMessage("Um unsere Familien zu beschützen, müssen wir zusammen arbeiten.", "<blue>Gamemaster</blue>")
            .addMessage("Mit genug Vorbereitung und den richtigen Waffen können wir sie besiegen, doch dafür müssen wir an einem Strang ziehen.", "<blue>Gamemaster</blue>")
            .addMessage("Wenn diese Vorbereitungen getroffen sind, können wir einen Gegenangriff vorbereiten, womit sie nicht rechnen. Das ist unsere einzige Chance", "<blue>Gamemaster</blue>")
            .addMessage("Ich entschuldige mich noch einmal für meine harschen Methoden, doch dieses Thema ist echt wichtig und es geht um uns alle.", "<blue>Gamemaster</blue>")
            .addMessage("Ich sah mich gezwungen auf solche Methoden zurückzugreifen um uns alle.", "<blue>Gamemaster</blue>")
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
