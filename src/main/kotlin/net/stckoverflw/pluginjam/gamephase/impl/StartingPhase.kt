package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.task
import net.axay.kspigot.structures.fillBlocks
import net.stckoverflw.pluginjam.DevcordJamPlugin.Companion.instance
import net.stckoverflw.pluginjam.action.ActionPipeline
import net.stckoverflw.pluginjam.action.impl.global.GasPipelineAction
import net.stckoverflw.pluginjam.action.impl.startingphase.StartingPhaseWalkingAction
import net.stckoverflw.pluginjam.action.impl.startingphase.StartingPhaseWelcomeAction
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.listener.GamemasterVelocity
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.mini
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.block.data.Openable
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object StartingPhase : GamePhase(PrisonPhase), ListenerHolder {
    private val postionsConfig = instance.configManager.postionsConfig
    override val listeners: MutableList<Listener> = mutableListOf()
    private var blocked = false

    private val gamemaster: GamemasterEntity = GamemasterEntity(false)

    override fun start() {
        gamemaster.interactCallback = interactCallback@{
            if (blocked) return@interactCallback
            blocked = true

            ActionPipeline()
                .add(StartingPhaseWelcomeAction())
                .add(StartingPhaseWalkingAction(gamemaster, postionsConfig.getLocation("starting_gamemaster_1")))
                .start()
                .whenComplete {
                    val area = LocationArea(
                        postionsConfig.getLocation("starting_gamemaster_house_0"),
                        postionsConfig.getLocation("starting_gamemaster_house_1")
                    )
                    task(period = 5) {
                        onlinePlayers.forEach { player -> player.sendActionBar(mini("Gehe in das Haus")) }
                        if (onlinePlayers.all { player -> area.isInArea(player.location) }) {
                            it.cancel()
                            area.fillBlocks.forEach { block ->
                                val blockData = block.blockData
                                if (blockData is Openable) {
                                    blockData.isOpen = false
                                }
                            }
                            GasPipelineAction(
                                gamemaster,
                                postionsConfig.getLocation("starting_pipe_0"),
                                postionsConfig.getLocation("starting_pipe_1"),
                                postionsConfig.getLocation("starting_gamemaster_target")
                            )
                                .execute()
                                .whenComplete {
                                    GamePhaseManager.nextPhase()
                                }
                        }
                    }
                }
        }

        gamemaster.spawnEntity(postionsConfig.getLocation("starting_gamemaster_0"))
        GamemasterVelocity(gamemaster)

        addListener(
            listen<PlayerInteractEvent> {
                if (it.player.gameMode != GameMode.CREATIVE) {
                    it.isCancelled = false
                }
            }
        )
    }

    override fun end() {
        unregisterAllListeners()
        gamemaster.despawn()
        Bukkit.getWorlds().first()
            .apply {
                time = 0
            }
    }
}
