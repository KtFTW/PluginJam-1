package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.playersWithoutSpectators
import org.bukkit.Material
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class FindFoodTask : FindMaterialTask() {

    var count = 10

    override fun start() {
        materials[Material.BREAD] = count

        super.start()
    }

    override fun stop() {
        super.stop()
    }

    override fun introduce() {
        count = Random.Default.nextInt(playersWithoutSpectators.size * 4, playersWithoutSpectators.size * 8)
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Wir brauchen auch noch etwas essbares.", "Dorfbewohner", 3.seconds)
            .addMessage("Sucht nach $count Brot und bringt es zu mir!", "Dorfbewohner", 3.seconds)
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
