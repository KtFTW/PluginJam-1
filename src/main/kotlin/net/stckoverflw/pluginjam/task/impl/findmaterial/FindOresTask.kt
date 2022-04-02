package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import net.stckoverflw.pluginjam.util.Conversation
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
        super.start()
        materials[Material.IRON_INGOT] = ironCount
        materials[Material.GOLD_INGOT] = goldCount
        materials[Material.COAL] = coalCount

        addListener(
            listen <PlayerInteractEvent> {
                println("smelt interact hasBlock: ${it.hasBlock()}")
                println("smelt interact clickedBlock type: ${it.clickedBlock?.type}")
                if (!it.hasBlock()) return@listen
                if (it.clickedBlock!!.type != Material.FURNACE) return@listen
                it.isCancelled = true
                it.player.smeltItemInHand()
            }
        )

        materials[Material.COAL] = Random.nextInt(
            (materials[Material.IRON_INGOT]!! + materials[Material.GOLD_INGOT]!!) / 12,
            (materials[Material.IRON_INGOT]!! + materials[Material.GOLD_INGOT]!!) / 6
        )
    }

    override fun introduce() {
        ironCount = Random.nextInt(onlinePlayers.size * 2, onlinePlayers.size * 4) // 2
        goldCount = Random.nextInt(onlinePlayers.size / 2 + 2, onlinePlayers.size + 4) // 2
        coalCount = Random.nextInt(
            (ironCount + goldCount) / 12 + 1,
            ((ironCount + goldCount) / 6) + 2
        )
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Um eine Chance gegen die <red>Leviatanen</red> zu haben brauchen wir Waffen und Rüstungen!", "<blue>Dorfbewohner</blue>", 3.seconds)
            .addMessage("Dafür müsst ihr verschiedene Erze finden und abbauen.", "<blue>Dorfbewohner</blue>", 3.seconds)
            .addMessage("Bringt mir bitte $coalCount Kohle, $ironCount Eisen-Ingots und $goldCount Gold-Ingots", "<blue>Dorfbewohner</blue>", 3.seconds)
            .addMessage("Die rohen Erze könnt ihr mit Rechtsklick auf einen Ofen direkt schmelzen.", "<blue>Dorfbewohner</blue>", 3.seconds)
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
