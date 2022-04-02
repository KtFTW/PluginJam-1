package net.stckoverflw.pluginjam.action.impl.twistphase

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.particles.particle
import net.axay.kspigot.runnables.taskRunLater
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.util.Vector

class TwistPhaseGamemasterDespawnAction(private val gamemaster: GamemasterEntity) : Action() {
    override fun execute(): Action {

        taskRunLater(1) {
            gamemaster.bukkitEntity?.location
                ?.clone()
                ?.add(0.0, 0.5, 0.0)
                ?.particle(Particle.CLOUD) {
                    extra = 0.3
                    amount = 1000
                    offset = Vector(1.0, 1.0, 1.0)
                }

            onlinePlayers.forEach {
                it.playSound(it.location, Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1.0f, 1.0f)
            }
            gamemaster.despawn()
            complete()
        }

        return this
    }
}
