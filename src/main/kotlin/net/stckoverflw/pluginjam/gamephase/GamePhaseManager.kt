package net.stckoverflw.pluginjam.gamephase

import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.runnables.sync
import net.stckoverflw.pluginjam.gamephase.impl.StartingPhase
import net.stckoverflw.pluginjam.util.broadcastMini
import net.stckoverflw.pluginjam.util.deserializeMini
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
                broadcast("<red>No players left, resetting world...".deserializeMini())
            } else {
                activeGamePhase?.end()
                activeGamePhase = activeGamePhase?.next
                if (activeGamePhase != null) {
                    activeGamePhase?.start()
                } else {
                    broadcastMini("<green>Game has ended! Thanks for playing!")
                    broadcastMini("<green>Starting a reset, please wait... (this may take about 60 seconds)")
                    resetWorld("pluginjam")
                }
            }
        }
    }
}
