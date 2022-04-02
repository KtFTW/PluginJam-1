package net.stckoverflw.pluginjam.gamephase.impl

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.util.ListenerHolder
import org.bukkit.event.Listener

object EndPhase : GamePhase(null), ListenerHolder {
    private val postionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    override val listeners: MutableList<Listener> = mutableListOf()

    override fun start() {
    }

    override fun end() {
    }
}
