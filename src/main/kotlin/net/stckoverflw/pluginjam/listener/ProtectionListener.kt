package net.stckoverflw.pluginjam.listener

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent

private val listeners = mutableListOf<SingleListener<*>>()

fun protectionListener() {
    listeners += listen<EntityDamageByEntityEvent> {
        if (it.damager is Player) {
            it.isCancelled = true
        }
    }
    listeners += listen<BlockBreakEvent> {
        if (it.player.hasPermission("pluginjam.protection")) {
            it.isCancelled = true
        }
    }
    listeners += listen<BlockPlaceEvent> {
        if (it.player.hasPermission("pluginjam.protection")) {
            it.isCancelled = true
        }
    }
}

fun unregisterProtectionListener() {
    listeners.forEach { it.unregister() }
}
