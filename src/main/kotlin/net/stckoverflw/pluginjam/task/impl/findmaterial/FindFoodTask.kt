package net.stckoverflw.pluginjam.task.impl.findmaterial

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.task.impl.FindMaterialTask
import org.bukkit.Material
import kotlin.random.Random

class FindFoodTask: FindMaterialTask() {

    override fun start() {
        materials[Material.BREAD] = Random.Default.nextInt(onlinePlayers.size * 4, onlinePlayers.size * 8)

        super.start()
    }

    override fun stop() {
        super.stop()
    }
}
