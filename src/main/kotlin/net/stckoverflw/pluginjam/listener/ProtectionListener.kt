package net.stckoverflw.pluginjam.listener

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerInteractEvent

private val listeners = mutableListOf<SingleListener<*>>()

fun protectionListener() {

    listeners += listen<EntityDamageEvent> {
        if (it.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
            it.cause == EntityDamageEvent.DamageCause.VOID
        ) return@listen
        if (it.entity is Player) {
            it.isCancelled = true
        }
    }
    listeners += listen<EntityDamageByEntityEvent> {
        if (it.damager is Player) {
            if ((it.damager as Player).gameMode == GameMode.CREATIVE) return@listen
            it.isCancelled = true
        }
    }
    listeners += listen<BlockBreakEvent> {
        if (it.player.gameMode != GameMode.CREATIVE) {
            it.isCancelled = true
        }
    }
    listeners += listen<BlockPlaceEvent> {
        if (it.player.gameMode != GameMode.CREATIVE) {
            it.isCancelled = true
        }
    }

    listen<PlayerInteractEvent>(EventPriority.LOWEST) {
        if (it.player.gameMode != GameMode.CREATIVE) {
            it.isCancelled = true
        }
    }
    listeners += listen<FoodLevelChangeEvent> {
        it.isCancelled = true
    }
}

fun unregisterProtectionListener() {
    listeners.forEach { it.unregister() }
}
