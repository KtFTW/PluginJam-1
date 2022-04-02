package net.stckoverflw.pluginjam.task.impl.killentity

import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.task.impl.KillEntityTask
import org.bukkit.entity.EntityType
import kotlin.random.Random

class KillPillagersTask: KillEntityTask() {

    override fun start() {
        entities[EntityType.PILLAGER] = Random.Default.nextInt(onlinePlayers.size * 8, onlinePlayers.size * 16)

        super.start()
    }

    override fun stop() {
        super.stop()
    }
}
