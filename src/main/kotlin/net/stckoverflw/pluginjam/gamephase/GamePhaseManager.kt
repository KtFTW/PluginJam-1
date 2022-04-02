package net.stckoverflw.pluginjam.gamephase

import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.runnables.sync
import net.stckoverflw.pluginjam.gamephase.impl.StartingPhase

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
            activeGamePhase?.end()
            activeGamePhase = activeGamePhase?.next
            if (activeGamePhase != null) {
                activeGamePhase?.start()
            } else {
                // TODO: End game (idk do sth) (this is after the Ending Phase)
                broadcast("Game has ended!")
            }
        }
    }
}
