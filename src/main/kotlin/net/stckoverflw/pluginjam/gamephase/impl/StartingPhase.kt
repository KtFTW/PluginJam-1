package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.task
import net.stckoverflw.pluginjam.DevcordJamPlugin.Companion.instance
import net.stckoverflw.pluginjam.action.ActionList
import net.stckoverflw.pluginjam.action.impl.startingphase.StartingPhaseEndAction
import net.stckoverflw.pluginjam.action.impl.startingphase.StartingPhaseWalkingAction
import net.stckoverflw.pluginjam.action.impl.startingphase.StartingPhaseWelcomeAction
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.listener.GamemasterVelocity
import net.stckoverflw.pluginjam.util.mini
import org.bukkit.event.player.PlayerJoinEvent

object StartingPhase : GamePhase(PrisonPhase) {
    private val postionsConfig = instance.configManager.postionsConfig
    private val listeners = mutableListOf<SingleListener<*>>()
    private var blocked = false

    private val gamemaster: GamemasterEntity = GamemasterEntity(false)

    override fun start() {
        gamemaster.interactCallback = interactCallback@{
            if (blocked) return@interactCallback
            blocked = true

            ActionList()
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
                            StartingPhaseEndAction(
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

        listeners += listen<PlayerJoinEvent> {
            if (GamePhaseManager.activeGamePhase !is StartingPhase) {
                return@listen
            }
//            it.player.teleportAsyncBlind(Location(it.player.world, 0.0, 0.0, 0.0))
        }
//        listeners += listen<PlayerInteractAtEntityEvent> {
//            // TODO: check if entity is npc
//            // TODO: make starting logic or whatever
//            GamePhaseManager.nextPhase()
//        }
    }

    override fun end() {
        listeners.forEach(SingleListener<*>::unregister)
        gamemaster.despawn()
    }
}
