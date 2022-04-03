package net.stckoverflw.pluginjam.action.impl.global

import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.particles.particle
import net.axay.kspigot.runnables.taskRunLater
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class FastGasPipelineAction(
    private val pipelineLocation0: Location,
    private val pipelineLocation1: Location,
) :
    Action() {

    override fun execute(): Action {

        val listener = listen<PlayerInteractEvent> {
            if (it.player.gameMode == GameMode.CREATIVE) return@listen
            it.isCancelled = true
        }
        var base = 40.toLong()

        taskRunLater(base) {
            pluginJamPlayers.forEach {
                it.playSound(it.location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1f, 1f)
            }
        }

        base += 30
        taskRunLater(base) {
            pluginJamPlayers.forEach {
                it.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 120, 10, false, false))
                it.playSound(it.location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f)

                taskRunLater(20) {
                    it.addPotionEffect(PotionEffect(PotionEffectType.CONFUSION, 100, 10, false, false))
                }
            }
            pipelineLocation0.particle(Particle.CAMPFIRE_COSY_SMOKE) {
                amount = 5000
                offset = Vector(1.0, 1.0, 1.0)
                extra = 0.1
            }

            pipelineLocation1.particle(Particle.CAMPFIRE_COSY_SMOKE) {
                amount = 5000
                offset = Vector(1.0, 1.0, 1.0)
                extra = 0.1
            }
        }

        base += 10
        taskRunLater(base) {
            listener.unregister()
            complete()
        }
        return this
    }
}
