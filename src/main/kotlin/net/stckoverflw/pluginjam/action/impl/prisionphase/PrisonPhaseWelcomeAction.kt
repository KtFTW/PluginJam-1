package net.stckoverflw.pluginjam.action.impl.prisionphase

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.Conversation
import kotlin.time.Duration.Companion.seconds

class PrisonPhaseWelcomeAction : Action() {
    override fun execute(): Action {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Ihr fragt euch jetzt bestimmt, warum ihr hier seid.", "Dorfbewohner")
            .addMessage("Nunja, <red>das Leben</red> von uns und von unseren Familien steht auf dem Spiel und muss gerettet werden.", "Dorfbewohner", 4.seconds)
            .addMessage("Die <red>Leviatanen</red> greifen uns seit Jahren im Untergrund an und bereiten gerade einen Überfall auf uns vor.", "Dorfbewohner", 4.seconds)
            .addMessage("Es gibt 2 Teile vom <light_purple>magischen Amethyst</light_purple>. Vereinzelt sind diese wertlos, doch vereint besitzen sie unvorstellbare Macht.", "Dorfbewohner", 6.seconds)
            .addMessage("Den einen Teil haben wir, der andere ist im Besitz der <red>Leviatanen</red>.", "Dorfbewohner", 4.seconds)
            .addMessage("Wir müssen unbedingt verhindern, dass sie diesen Teil bekommen.", "Dorfbewohner", 4.seconds)
            .addMessage("Bei der Macht, die diese besitzen würden, würde dies das Leben <red>ALLER</red> Dorfbewohner kosten.", "Dorfbewohner", 4.seconds)
            .addMessage("Um unsere Familien zu beschützen, müssen wir zusammen arbeiten.", "Dorfbewohner", 3.seconds)
            .addMessage("Mit genug Vorbereitung und den richtigen Waffen können wir sie besiegen, doch dafür müssen wir an einem Strang ziehen.", "Dorfbewohner", 4.seconds)
            .addMessage("Wenn diese Vorbereitungen getroffen sind, können wir einen Gegenangriff vorbereiten, womit sie nicht rechnen. Das ist unsere einzige Chance", "Dorfbewohner", 5.seconds)
            .addMessage("Ich entschuldige mich noch einmal für meine harschen Methoden, doch dieses Thema ist echt wichtig und es geht um uns alle.", "Dorfbewohner", 4.seconds)
            .addMessage("Ich sah mich gezwungen auf solche Methoden zurückzugreifen um uns alle zu schützen.", "Dorfbewohner", 4.seconds)
            .addMessage("Um die <red>Leviatanen</red> zu besiegen, müssen wir ein paar Ressourcen besorgen. Also los gehts!", "Dorfbewohner", 1.seconds)
            .start()
            .whenComplete { _, _ ->
                complete()
            }
        return this
    }
}
