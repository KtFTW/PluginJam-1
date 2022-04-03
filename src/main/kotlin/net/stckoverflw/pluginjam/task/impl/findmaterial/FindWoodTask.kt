package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.playersWithoutSpectators
import org.bukkit.Material
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class FindWoodTask : FindMaterialTask() {

    private var count: Int = 10

    override fun start() {
        materials[Material.OAK_LOG] = count

        super.start()
    }

    override fun introduce() {
        count = Random.Default.nextInt(playersWithoutSpectators.size * 12, playersWithoutSpectators.size * 24)
        Conversation(DevcordJamPlugin.instance)
            .addMessage("<tr:task_wood_1>", "<tr:villager>", 3.seconds)
            .addMessage("<tr:task_wood_2:$count>", "<tr:villager>", 0.seconds)
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
