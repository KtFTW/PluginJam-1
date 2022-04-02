package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.event.listen
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.util.ListenerHolder
import org.bukkit.GameMode
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object FightPhase : GamePhase(TwistPhase), ListenerHolder {
    override val listeners: MutableList<Listener> = mutableListOf()
    override fun start() {
        addListener(listen<PlayerInteractEvent> {
            if (it.player.gameMode != GameMode.CREATIVE) {
                it.isCancelled = false
            }
        })
    }

    override fun end() {
    }
}
