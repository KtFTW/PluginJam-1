package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import org.bukkit.Material
import kotlin.random.Random

class FindOresTask : FindMaterialTask() {

    override fun start() {
        materials[Material.IRON_INGOT] = Random.nextInt(onlinePlayers.size * 2, onlinePlayers.size * 3)
        materials[Material.GOLD_INGOT] = Random.nextInt(onlinePlayers.size / 2 + 1, onlinePlayers.size + 1)
        materials[Material.COAL] = Random.nextInt((materials[Material.IRON_INGOT]!! + materials[Material.GOLD_INGOT]!!) / 12,
            (materials[Material.IRON_INGOT]!! + materials[Material.GOLD_INGOT]!!) / 6)
        super.start()
    }

    override fun stop() {

        super.stop()
    }
}
