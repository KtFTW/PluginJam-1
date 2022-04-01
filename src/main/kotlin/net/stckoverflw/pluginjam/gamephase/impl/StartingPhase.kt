package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.util.teleportAsyncBlind
import org.bukkit.Location
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerJoinEvent

object StartingPhase : GamePhase(TaskPhase) {
	private val listeners = mutableListOf<SingleListener<*>>()

	override fun start() {
		listeners += listen<PlayerJoinEvent> {
			if (GamePhaseManager.activeGamePhase !is StartingPhase) {
				return@listen
			}
			it.player.teleportAsyncBlind(Location(it.player.world, 0.0, 0.0, 0.0))
		}
		listeners += listen<PlayerInteractAtEntityEvent> {
			// TODO: check if entity is npc
			// TODO: make starting logic or whatever
			GamePhaseManager.nextPhase()
		}
	}

	override fun end() {
		listeners.forEach(SingleListener<*>::unregister)
	}
}
