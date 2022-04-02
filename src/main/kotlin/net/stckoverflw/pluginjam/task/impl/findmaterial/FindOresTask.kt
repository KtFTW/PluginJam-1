package net.stckoverflw.pluginjam.task.impl.findmaterial

import kotlinx.coroutines.launch
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

    override fun start() {
        materials[Material.IRON_INGOT] = Random.nextInt(onlinePlayers.size * 2, onlinePlayers.size * 3)
        materials[Material.GOLD_INGOT] = Random.nextInt(onlinePlayers.size / 2 + 1, onlinePlayers.size + 1)

        addListener(
            listen <PlayerInteractEvent>{
                if (!it.hasBlock()) return@listen
                if (it.clickedBlock!!.type != Material.FURNACE) return@listen
                DevcordJamPlugin.instance.defaultScope.launch {
                    it.player.smeltItemInHand()
                }
            }
        )

        materials[Material.COAL] = Random.nextInt(
            (materials[Material.IRON_INGOT]!! + materials[Material.GOLD_INGOT]!!) / 12,
            (materials[Material.IRON_INGOT]!! + materials[Material.GOLD_INGOT]!!) / 6
        )
        super.start()
    }

    override fun stop() {

        super.stop()
    }

    override fun introduce() {
        Conversation(DevcordJamPlugin.instance)
            .addMessage("Um eine Chance gegen die Pillager zu haben brauchen wir Waffen und Rüstungen!", "Gamemaster", 3.seconds)
            .addMessage("Dafür müsst ihr verschiedene Erze finden und abbauen.", "Gamemaster", 3.seconds)
            .addMessage("Die rohen Erze könnt ihr mit Rechtsklick auf einen Ofen direk schmelzen.", "Gamemaster", 3.seconds)
            .addMessage("Bringt mir dann die ingots!", "Gamemaster", 3.seconds)
            .start()
            .whenComplete { _, _ ->
                start()
            }
    }
}
