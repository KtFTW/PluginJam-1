package net.stckoverflw.pluginjam.gamephase.impl

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.particles.particle
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.impl.destroyphase.DestroyPhaseWelcomeAction
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.util.ListenerHolder
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Item
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityCombustEvent
import org.bukkit.util.Vector

object DestroyPhase : GamePhase(EndPhase), ListenerHolder {
    private val postionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    override val listeners: MutableList<Listener> = mutableListOf()

    override fun start() {
        DestroyPhaseWelcomeAction()
            .execute()
            .whenComplete {
                broadcast("")
            }

        addListener(
            listen<EntityCombustEvent> { event ->
                if (GamePhaseManager.activeGamePhase !is DestroyPhase) return@listen
                if (event.entity !is Item) return@listen
                if ((event.entity as Item).itemStack.type != Material.AMETHYST_SHARD) return@listen
                event.entity.remove()
                event.isCancelled = true
                DevcordJamPlugin.instance.defaultScope.launch {
                    repeat(10) {
                        delay(200)
                        particle(Particle.ELECTRIC_SPARK) {
                            amount = 100
                            offset = Vector(1.0, 1.0, 1.0)
                            spawnAt(event.entity.location)
                        }
                        particle(Particle.WHITE_ASH) {
                            amount = 100
                            offset = Vector(1.0, 1.0, 1.0)
                            spawnAt(event.entity.location)
                        }
                        particle(Particle.SOUL_FIRE_FLAME) {
                            amount = 100
                            offset = Vector(1.0, 1.0, 1.0)
                            spawnAt(event.entity.location)
                        }
                    }
                    delay(200)
                    particle(Particle.FLASH) {
                        amount = 100
                        offset = Vector(.1, .1, .1)
                        spawnAt(event.entity.location)
                    }
                    GamePhaseManager.nextPhase()
                }
            }
        )
    }

    override fun end() {
        listeners.forEach {
            it.unregister()
        }
    }
}
