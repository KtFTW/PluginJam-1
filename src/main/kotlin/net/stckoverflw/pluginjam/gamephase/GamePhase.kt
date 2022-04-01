package net.stckoverflw.pluginjam.gamephase

import org.bukkit.event.Listener

abstract class GamePhase(val next: GamePhase?) : Listener {

    abstract fun start()

    abstract fun end()
}
