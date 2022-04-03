package net.stckoverflw.pluginjam.gamephase

import net.axay.kspigot.runnables.sync
import net.stckoverflw.pluginjam.gamephase.impl.StartingPhase
import net.stckoverflw.pluginjam.util.broadcastMini
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import net.stckoverflw.pluginjam.util.resetWorld

object GamePhaseManager {

    var activeGamePhase: GamePhase? = null

    fun init(): Boolean {
        return if (activeGamePhase == null) {
            activeGamePhase = StartingPhase
            sync {
                activeGamePhase?.start()
            }
            true
        } else {
            false
        }
    }

    fun nextPhase() {
        sync {
            if (pluginJamPlayers.isEmpty()) {
                resetWorld("pluginjam")
                broadcastMini("<red><tr:no_players_left_reset>")
            } else {
                activeGamePhase?.end()
                activeGamePhase = activeGamePhase?.next
                if (activeGamePhase != null) {
                    activeGamePhase?.start()
                } else {
                    broadcastMini("<green><tr:game_ended>")
                    broadcastMini("<green><tr:game_ended_reset>")
                    resetWorld("pluginjam")
                }
            }
        }
    }
}
