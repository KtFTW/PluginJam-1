package net.stckoverflw.pluginjam.action.impl.fightphase

import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.runnables.KSpigotRunnable
import net.axay.kspigot.runnables.task
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDamageEvent

class FightPhaseParkourAction : Action() {
    private lateinit var task: KSpigotRunnable

    override fun execute(): Action {
        val positionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
        val listener = listen<EntityDamageEvent> { event ->
            if (event.entityType != EntityType.PLAYER) return@listen

            if (event.cause != EntityDamageEvent.DamageCause.LAVA) return@listen

            val position = positionsConfig.getLocation("fight_lava_spawn")

            event.entity.teleport(position)
        }

        task = task(period = 2) {
            val position = positionsConfig.getLocation("fight_parkour_end").block.location
            pluginJamPlayers.forEach {
                if (it.location.block.location != position) return@forEach
                listener.unregister()
                complete()
            }
        }!!

        return this
    }

    override fun complete() {
        task.cancel()
        super.complete()
    }
}
