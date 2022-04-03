package net.stckoverflw.pluginjam.gamephase.impl

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.extensions.geometry.vec
import net.axay.kspigot.extensions.worlds
import net.axay.kspigot.particles.particle
import net.axay.kspigot.runnables.task
import net.axay.kspigot.sound.sound
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.impl.destroyphase.DestroyPhaseWelcomeAction
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.TaskHolder
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import net.stckoverflw.pluginjam.util.reset
import net.stckoverflw.pluginjam.util.sendMini
import org.bukkit.Color
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

object DestroyPhase : GamePhase(EndPhase), ListenerHolder, TaskHolder {
    private val positionConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    override val listeners: MutableList<Listener> = mutableListOf()
    override val tasks: MutableList<BukkitRunnable> = mutableListOf()

    private var amethystsLeft: Int = 2

    private val laserLocations = (0..6).map { positionConfig.getLocation("twist_laser_$it") }
    private val laserBoundingBoxes = laserLocations.map { BoundingBox.of(it, 10.0, 0.1, 0.1) }

    override fun start() {
        worlds.forEach {
            it.time = 0
        }
        DestroyPhaseWelcomeAction()
            .execute()
            .whenComplete {
            }

        addListener(
            listen<EntityDamageEvent> { event ->
                if (GamePhaseManager.activeGamePhase !is DestroyPhase) return@listen
                if (event.entity !is Item) return@listen
                if ((event.entity as Item).itemStack.type != Material.AMETHYST_SHARD) return@listen
                if (!(event.cause == EntityDamageEvent.DamageCause.LAVA || event.cause == EntityDamageEvent.DamageCause.FIRE || event.cause == EntityDamageEvent.DamageCause.FIRE_TICK)) return@listen
                event.entity.remove()
                event.isCancelled = true
                amethystsLeft -= (event.entity as Item).itemStack.amount
                DevcordJamPlugin.instance.defaultScope.launch {
                    repeat(20) {
                        delay(100)
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
                    if (amethystsLeft <= 0) {
                        Conversation(DevcordJamPlugin.instance)
                            .addMessage("<i>Ihr habt die Kristalle zerstört, gut gemacht!</i>")
                            .start()
                            .whenComplete { _, _ ->
                                GamePhaseManager.nextPhase()
                            }
                    }
                }
            }
        )

        addTask(
            task(period = 2) {
                laserLocations.forEach {
                    particle(Particle.REDSTONE) {
                        amount = 200
                        extra = 1
                        offset = vec(4, 0, 0)
                        data = Particle.DustOptions(Color.RED, 0.6f)
                        spawnAt(it)
                    }
                }
            }!!
        )

        addTask(
            task(period = 3) {
                pluginJamPlayers.forEach { player ->
                    if (player.gameMode == GameMode.CREATIVE) return@forEach
                    if (laserBoundingBoxes.any { it.overlaps(player.boundingBox) }) {
                        player.teleport(positionConfig.getLocation("twist_location"))
                        player.sendMini("<red>Aua! Du darfst die Laser nicht berühren!")
                        player.playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 2f)
                        player.playSound(player.location, Sound.BLOCK_GLASS_BREAK, 1f, 2f)
                    }
                }
            }!!
        )
    }

    override fun end() {
        pluginJamPlayers.forEach {
            it.reset()
        }
        listeners.forEach {
            it.unregister()
        }
    }
}
