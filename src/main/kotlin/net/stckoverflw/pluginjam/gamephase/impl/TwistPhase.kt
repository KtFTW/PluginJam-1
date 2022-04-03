package net.stckoverflw.pluginjam.gamephase.impl

import com.destroystokyo.paper.MaterialTags
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.extensions.geometry.blockLoc
import net.axay.kspigot.extensions.geometry.vec
import net.axay.kspigot.particles.particle
import net.axay.kspigot.runnables.task
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.ActionPipeline
import net.stckoverflw.pluginjam.action.impl.global.FastGasPipelineAction
import net.stckoverflw.pluginjam.action.impl.global.WaitAction
import net.stckoverflw.pluginjam.action.impl.prisionphase.TwistPhasePrisonTeleportAction
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
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import net.stckoverflw.pluginjam.util.sendMini
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.BoundingBox

object TwistPhase : GamePhase(DestroyPhase), TaskHolder, ListenerHolder {
    private val positionConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    private val gamemaster: GamemasterEntity = GamemasterEntity(true)
    override val tasks: MutableList<BukkitRunnable> = mutableListOf()
    override val listeners: MutableList<Listener> = mutableListOf()
    private var totalAmethysts = 0

    private val twistLocationArea = LocationArea(
        positionConfig.getLocation("twist_location_0"), positionConfig.getLocation("twist_location_1")
    )

    private val laserLocations = (0..6).map { positionConfig.getLocation("twist_laser_$it") }
    private val laserBoundingBoxes = laserLocations.map { BoundingBox.of(it, 10.0, 0.1, 0.1) }

    enum class State {
        NONE, FIND_GAMEMASTER, GET_AMETHYST
    }

    private var state = State.NONE

    override fun start() {
        val amethystFrameBlock = positionConfig.getLocation("twist_amethyst_frame").block
        listOf(BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH).forEach {
            val relative = amethystFrameBlock.getRelative(it)
            if (MaterialTags.TORCHES.isTagged(relative.type)) {
                relative.type = Material.AIR
            }
        }
        amethystFrameBlock.world.getNearbyEntities(amethystFrameBlock.location, 1.0, 1.0, 1.0).forEach {
            if (it is ItemFrame && it.location.block.getRelative((it.attachedFace)) == amethystFrameBlock) {
                it.remove()
            }
        }
        amethystFrameBlock.type = Material.AIR
        amethystFrameBlock.getRelative(BlockFace.DOWN).type = Material.AIR

        gamemaster.spawnEntity(positionConfig.getLocation("prison_gamemaster_2"))

        ActionPipeline().add(
            FastGasPipelineAction(
                positionConfig.getLocation("prison_pipe_0"),
                positionConfig.getLocation("prison_pipe_1")
            )
        )
            .add(
                TwistPhasePrisonTeleportAction(
                    gamemaster,
                    positionConfig.getLocation("prison_prison"),
                    positionConfig.getLocation("prison_gamemaster")
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
                if (pluginJamPlayers.any { twistLocationArea.isInArea(it.location) }) {
                    state = State.GET_AMETHYST

                    ActionPipeline().add(WaitAction(100)).add(TwistPhaseTwistLocationHelperAction()).start()
                    return@task
                }
            }!!
        )

        pluginJamPlayers.forEach {
            it.compassTarget = positionConfig.getLocation("twist_location")
        }

        addTask(
            task(period = 5) {
                val message = when (state) {
                    State.FIND_GAMEMASTER -> "Finde den Gamemaster"
                    State.GET_AMETHYST -> "Klaue die Amethysten"
                    else -> null
                }

                if (message != null) {
                    pluginJamPlayers.forEach { it.sendActionBar(mini(message)) }
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

        addListener(
            listen<PlayerAttemptPickupItemEvent> {
                if (it.item.itemStack.type != Material.AMETHYST_SHARD) return@listen
                totalAmethysts += it.item.itemStack.amount
                if (totalAmethysts >= 2) {
                    GamePhaseManager.nextPhase()
                }
            }
        )

        addListener(
            listen<PlayerJoinEvent> {
                it.player.compassTarget = positionConfig.getLocation("twist_location")
            }
        )

        addListener(
            listen<PlayerInteractEvent> {
                it.isCancelled = false

                val block = it.clickedBlock ?: return@listen

                when (block.location.blockLoc) {
                    positionConfig.getLocation("prison_pickaxes").block.location -> {
                        val inventory = Bukkit.createInventory(null, 3 * 9)
                        for (i in 0..26) {
                            inventory.setItem(i, ItemStack(Material.WOODEN_PICKAXE))
                        }
                        it.player.openInventory(inventory)

                        it.isCancelled = true
                    }
                    positionConfig.getLocation("prison_compasses").block.location -> {
                        val inventory = Bukkit.createInventory(null, 3 * 9)
                        for (i in 0..26) {
                            inventory.setItem(i, ItemStack(Material.COMPASS))
                        }
                        it.player.openInventory(inventory)

                        it.isCancelled = true
                    }
                }
            }
        )

        addListener(
            listen<BlockBreakEvent>(EventPriority.HIGHEST) {
                if (it.player.gameMode == GameMode.CREATIVE) return@listen

                if (it.player.inventory.itemInMainHand.type == Material.WOODEN_PICKAXE && it.block.type == Material.IRON_BARS) {
                    it.isCancelled = false
                }
            }
        )
    }

    override fun end() {
        gamemaster.despawn()
        removeAllTasks()
        unregisterAllListeners()
    }
}
