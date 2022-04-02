package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import net.stckoverflw.pluginjam.util.Conversation
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
        count = Random.Default.nextInt(onlinePlayers.size * 4, onlinePlayers.size * 8)
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Wir brauchen auch noch etwas essbares.", "<blue>Dorfbewohner</blue>", 3.seconds)
            .addMessage("Sucht nach $count Brot und bringt es zu mir!", "<blue>Dorfbewohner</blue>", 3.seconds)
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
