package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.extensions.geometry.vec
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.particles.particle
import net.axay.kspigot.runnables.task
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.ActionPipeline
import net.stckoverflw.pluginjam.action.impl.global.GasPipelineAction
import net.stckoverflw.pluginjam.action.impl.global.WaitAction
import net.stckoverflw.pluginjam.action.impl.twistphase.TwistPhaseGamemasterAction
import net.stckoverflw.pluginjam.action.impl.twistphase.TwistPhaseGamemasterDespawnAction
import net.stckoverflw.pluginjam.action.impl.twistphase.TwistPhaseHelperAction
import net.stckoverflw.pluginjam.action.impl.twistphase.TwistPhaseTwistLocationHelperAction
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.TaskHolder
import net.stckoverflw.pluginjam.util.mini
import net.stckoverflw.pluginjam.util.sendMini
import net.stckoverflw.pluginjam.util.teleportAsyncBlind
import org.bukkit.Color
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.BoundingBox

object TwistPhase : GamePhase(DestroyPhase), TaskHolder, ListenerHolder {
    private val postionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    private val gamemaster: GamemasterEntity = GamemasterEntity(true)
    override val tasks: MutableList<BukkitRunnable> = mutableListOf()
    override val listeners: MutableList<Listener> = mutableListOf()

    private val twistLocationArea = LocationArea(
        postionsConfig.getLocation("twist_location_0"),
        postionsConfig.getLocation("twist_location_1")
    )

    private val laserLocations = (0..6).map { postionsConfig.getLocation("twist_laser_$it") }
    private val laserBoundingBoxes = laserLocations.map { BoundingBox.of(it, 10.0, 0.1, 0.1) }

    enum class State {
        NONE,
        FIND_GAMEMASTER,
        GET_AMETHYST
    }

    private var state = State.NONE

    override fun start() {
        gamemaster.spawnEntity(postionsConfig.getLocation("prison_gamemaster"))

        ActionPipeline()
            .add(
                GasPipelineAction(
                    gamemaster,
                    postionsConfig.getLocation("prison_pipe_0"),
                    postionsConfig.getLocation("prison_pipe_1"),
                    null
                )
            )
            .add(WaitAction(50))
            .add(TwistPhaseGamemasterAction())
            .add(TwistPhaseGamemasterDespawnAction(gamemaster))
            .add(WaitAction(40))
            .add(TwistPhaseHelperAction())
            .start()
            .whenComplete {
                state = State.FIND_GAMEMASTER
            }

        addTask(
            task(period = 20) {
                if (state != State.FIND_GAMEMASTER) return@task
                if (onlinePlayers.any { twistLocationArea.isInArea(it.location) }) {
                    state = State.GET_AMETHYST

                    ActionPipeline()
                        .add(WaitAction(100))
                        .add(TwistPhaseTwistLocationHelperAction())
                        .start()
                    return@task
                }
            }!!
        )

        onlinePlayers.forEach {
            it.teleportAsyncBlind(postionsConfig.getLocation("prison_prison"))
            it.compassTarget = postionsConfig.getLocation("twist_location")
        }

        addTask(
            task(period = 5) {
                val message = when (state) {
                    State.FIND_GAMEMASTER -> "Finde den Gamemaster"
                    State.GET_AMETHYST -> "Klaue die Amethysten"
                    else -> null
                }

                if (message != null) {
                    onlinePlayers.forEach { it.sendActionBar(mini(message)) }
                }
            }!!
        )

        addTask(
            task(period = 2) {
                if (state == State.GET_AMETHYST) {
                    laserLocations.forEach {
                        particle(Particle.REDSTONE) {
                            amount = 200
                            extra = 1
                            offset = vec(4, 0, 0)
                            data = Particle.DustOptions(Color.RED, 0.6f)
                            spawnAt(it)
                        }
                    }
                }
            }!!
        )

        addTask(
            task(period = 3) {
                onlinePlayers.forEach { player ->
                    if (player.gameMode == GameMode.CREATIVE) return@forEach
                    if (laserBoundingBoxes.any { it.overlaps(player.boundingBox) }) {
                        player.teleport(postionsConfig.getLocation("twist_location"))
                        player.sendMini("<red>Aua! Du darfst die Laser nicht berühren!")
                        player.playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 2f)
                        player.playSound(player.location, Sound.BLOCK_GLASS_BREAK, 1f, 2f)
                    }
                }
            }!!
        )

        var totalAmethysts = 0
        addListener(
            listen<EntityPickupItemEvent> {
                if (it.entity !is Player) return@listen
                if (it.item.itemStack.type != Material.AMETHYST_SHARD) return@listen
                totalAmethysts ++
                if (totalAmethysts == 2) {
                    GamePhaseManager.nextPhase()
                }
            }
        )

        addListener(
            listen<PlayerInteractEvent> {
                if (it.player.gameMode != GameMode.CREATIVE) {
                    it.isCancelled = false
                }
            }
        )

        addListener(
            listen<PlayerJoinEvent> {
                it.player.compassTarget = postionsConfig.getLocation("twist_location")
            }
        )
    }

    override fun end() {
        gamemaster.despawn()
        removeAllTasks()
    }
}
