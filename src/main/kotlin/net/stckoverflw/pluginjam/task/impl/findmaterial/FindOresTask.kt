package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.axay.kspigot.event.listen
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.playersWithoutSpectators
import net.stckoverflw.pluginjam.util.smeltItemInHand
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class FindOresTask : FindMaterialTask() {

    var ironCount = 10
    var goldCount = 10
    var coalCount = 10

    override fun start() {
        materials[Material.IRON_INGOT] = ironCount
        materials[Material.GOLD_INGOT] = goldCount
        materials[Material.COAL] = coalCount

        addListener(
            listen<PlayerInteractEvent> {
                if (! it.hasBlock()) return@listen
                if (it.clickedBlock !!.type != Material.FURNACE) return@listen
                it.isCancelled = true
                it.player.smeltItemInHand()
            }
        )
        super.start()
    }

    override fun introduce() {
        ironCount = Random.nextInt(playersWithoutSpectators.size * 2, playersWithoutSpectators.size * 4) // 2
        goldCount = Random.nextInt(playersWithoutSpectators.size / 2 + 2, playersWithoutSpectators.size + 4) // 2
        coalCount = Random.nextInt(
            (ironCount + goldCount) / 12 + 1,
            ((ironCount + goldCount) / 6) + 2
        )
        Conversation(DevcordJamPlugin.instance)
            .addMessage(
                "<tr:task_ores_1:<red><tr:leviatans></red>>",
                "<tr:villager>",
                3.seconds
            )
            .addMessage("<tr:task_ores_2>", "<tr:villager>", 3.seconds)
            .addMessage(
                "<tr:task_ores_3:$coalCount:$ironCount:$goldCount>",
                "<tr:villager>",
                3.seconds
            )
            .addMessage(
                "<tr:task_ores_4>",
                "<tr:villager>",
                3.seconds
            )
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
