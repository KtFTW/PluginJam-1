package net.stckoverflw.pluginjam.action.impl.prisionphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class PrisonPhaseWelcomeAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Ihr fragt euch jetzt bestimmt, warum ihr hier seid.", "Gamemaster")
            .addMessage("Nunja, <red>das Leben<red/> von uns und von unseren Familien steht auf dem Spiel und muss gerettet werden.</red>", "Gamemaster", 5.seconds)
            .addMessage("Die COOLER_NAME_EINFÜGEN greifen uns seit Jahren im Untergrund an und bereiten gerade einen Überfall auf uns vor.", "Gamemaster", 5.seconds)
            .addMessage("Es gibt 2 Teile vom <light_purple>magischen Amethyst</light_purple>. Vereinzelt sind diese wertlos, doch vereint besitzen sie unvorstellbare Macht.", "Gamemaster", 5.seconds)
            .addMessage("Den einen Teil haben wir, den anderen haben die COOLER_NAME_EINFÜGEN.", "Gamemaster", 4.seconds)
            .addMessage("Wir müssen unbedingt, verhindern, dass sie diesen Teil bekommen.", "Gamemaster", 5.seconds)
            .addMessage("Bei der Macht, die diese besitzen würden, würde dies das Leben <red>ALLER</red> Dorfbewohner kosten.", "Gamemaster", 5.seconds)
            .addMessage("Um unsere Familien zu beschützen, müssen wir zusammen arbeiten.", "Gamemaster", 5.seconds)
            .addMessage("Mit genug Vorbereitung und den richtigen Waffen können wir sie besiegen, doch dafür müssen wir an einem Strang ziehen.", "Gamemaster", 5.seconds)
            .addMessage("Wenn diese Vorbereitungen getroffen sind, können wir einen Gegenangriff vorbereiten, womit sie nicht rechnen. Das ist unsere einzige Chance", "Gamemaster", 6.seconds)
            .addMessage("Ich entschuldige mich noch einmal für meine harschen Methoden, doch dieses Thema ist echt wichtig und es geht um uns alle.", "Gamemaster", 6.seconds)
            .addMessage("Ich sah mich gezwungen auf solche Methoden zurückzugreifen um uns alle.", "Gamemaster", 5.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
