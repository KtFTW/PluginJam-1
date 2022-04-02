package net.stckoverflw.pluginjam.listener

import net.axay.kspigot.runnables.task
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

class GamemasterVelocity(private val gamemaster: GamemasterEntity) {
    init {
        task(period = 5) {
            if (gamemaster.bukkitEntity?.isDead == true ||
                gamemaster.bukkitEntity == null
            ) return@task
            val gamemasterLocation = gamemaster.bukkitEntity?.location ?: return@task

            gamemasterLocation.getNearbyEntities(0.5, 0.5, 0.5).forEach {
                if (it.type == EntityType.PLAYER) {
                    it.velocity = Vector(0.5, 0.5, 0.5)
                }
            }
        }
    }
}
