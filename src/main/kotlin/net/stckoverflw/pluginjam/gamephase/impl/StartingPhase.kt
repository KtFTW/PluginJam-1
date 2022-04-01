package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.stckoverflw.pluginjam.DevcordJamPlugin.Companion.instance
import net.stckoverflw.pluginjam.action.impl.startingphase.StartingPhaseWelcomeAction
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import org.bukkit.event.player.PlayerJoinEvent

object StartingPhase : GamePhase(TaskPhase) {
    private val listeners = mutableListOf<SingleListener<*>>()
    private var step = 0
    private var blocked = false

    private val gamemaster = GamemasterEntity {
        if (blocked) return@GamemasterEntity
        when (step) {
            0 -> {
                blocked = true
                StartingPhaseWelcomeAction()
                    .execute()
                    .whenComplete {
                        step++
                    }
            }
        }
    }

    override fun start() {
        gamemaster.spawnEntity(instance.configManager.postionsConfig.get("starting_gamemaster_0"))

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
