package net.stckoverflw.pluginjam.util

import net.axay.kspigot.event.unregister
import org.bukkit.event.Listener

interface ListenerHolder {
    val listeners: MutableList<Listener>

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun unegisterAllListeners() {
        listeners.forEach { it.unregister() }
    }
}
