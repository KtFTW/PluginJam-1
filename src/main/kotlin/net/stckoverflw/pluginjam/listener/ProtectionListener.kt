package net.stckoverflw.pluginjam.listener

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent.ItemFrameChangeAction
import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import org.bukkit.GameMode
import org.bukkit.entity.ItemFrame
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

    listeners += listen<PlayerItemFrameChangeEvent> {
        if (it.action == ItemFrameChangeAction.REMOVE) {
            it.isCancelled = false
            return@listen
        } else {
            it.isCancelled = it.player.gameMode != GameMode.CREATIVE
        }
    }

    listeners += listen<EntityDamageEvent>(EventPriority.HIGHEST) {
        if (it.entity is ItemFrame) {
            it.isCancelled = false
            return@listen
        }
    }

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

    listeners += listen<BlockBreakEvent>(EventPriority.LOWEST) {
        if (it.player.gameMode != GameMode.CREATIVE) {
            it.isCancelled = true
        }
    }

    listeners += listen<BlockPlaceEvent>(EventPriority.LOWEST) {
        if (it.player.gameMode != GameMode.CREATIVE) {
            it.isCancelled = true
        }
    }

    listeners += listen<PlayerInteractEvent>(EventPriority.LOWEST) {
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
