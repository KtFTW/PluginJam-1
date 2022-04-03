package net.stckoverflw.pluginjam.listener

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent.ItemFrameChangeAction
import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.gamephase.impl.FightPhase
import net.stckoverflw.pluginjam.gamephase.impl.TwistPhase
import org.bukkit.GameMode
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Painting
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.player.PlayerInteractEvent

private val listeners = mutableListOf<SingleListener<*>>()

fun protectionListener() {

    listeners += listen<HangingBreakEvent> {
        it.isCancelled = true
    }

    listeners += listen<HangingBreakByEntityEvent> {
        it.isCancelled = true
    }

    listeners += listen<PlayerItemFrameChangeEvent> {
        if (it.action == ItemFrameChangeAction.REMOVE && (
            GamePhaseManager.activeGamePhase is FightPhase ||
                GamePhaseManager.activeGamePhase is TwistPhase
            )
        ) {
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

    listeners += listen<EntityDamageByEntityEvent>(EventPriority.LOWEST) {
        if (it.entity is ItemFrame) {
            it.isCancelled = true
            return@listen
        } else if (it.entity is Painting) {
            it.isCancelled = true
            return@listen
        }
        it.isCancelled = true
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
        if (it.entity is ItemFrame) {
            it.isCancelled = false
            return@listen
        }
        if (it.damager is Player) {
            if ((it.damager as Player).gameMode == GameMode.CREATIVE) return@listen
            it.isCancelled = true
        }

        it.isCancelled = true
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
