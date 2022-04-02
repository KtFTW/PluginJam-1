package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import org.bukkit.Material
import kotlin.random.Random

class FindWoodTask: FindMaterialTask() {

    override fun start() {
        materials[Material.OAK_WOOD] = Random.Default.nextInt(onlinePlayers.size * 12, onlinePlayers.size * 24)

        super.start()
    }

    override fun stop() {
        super.stop()
    }
}
