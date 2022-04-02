package net.stckoverflw.pluginjam.gamephase.impl

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.particles.particle
import net.axay.kspigot.sound.sound
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.impl.destroyphase.DestroyPhaseWelcomeAction
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.ListenerHolder
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.util.Vector

object DestroyPhase : GamePhase(EndPhase), ListenerHolder {
    override val listeners: MutableList<Listener> = mutableListOf()

    private var amount: Int = 2

    override fun start() {
        DestroyPhaseWelcomeAction()
            .execute()
            .whenComplete {
                broadcast("")
            }

        addListener(
            listen<EntityDamageEvent> { event ->
                if (GamePhaseManager.activeGamePhase !is DestroyPhase) return@listen
                if (event.entity !is Item) return@listen
                if ((event.entity as Item).itemStack.type != Material.AMETHYST_SHARD) return@listen
                if (!(event.cause == EntityDamageEvent.DamageCause.LAVA || event.cause == EntityDamageEvent.DamageCause.FIRE || event.cause == EntityDamageEvent.DamageCause.FIRE_TICK)) return@listen
                event.entity.remove()
                event.isCancelled = true
                amount--
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
                        sound(Sound.BLOCK_CAMPFIRE_CRACKLE) {
                            volume = 1.0f
                            pitch = 1.0f
                            playAt(event.entity.location)
                        }
                    }
                    delay(200)
                    particle(Particle.FLASH) {
                        amount = 100
                        offset = Vector(.1, .1, .1)
                        spawnAt(event.entity.location)
                    }
                    sound(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE) {
                        volume = 1.0f
                        pitch = 1.0f
                        playAt(event.entity.location)
                    }
                    if (amount > 0) return@launch
                    Conversation(DevcordJamPlugin.instance)
                        .addMessage("<i>Ihr habt die Kristalle zerstört!</i>")
                        .start()
                        .whenComplete { _, _ ->
                            GamePhaseManager.nextPhase()
                        }
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
