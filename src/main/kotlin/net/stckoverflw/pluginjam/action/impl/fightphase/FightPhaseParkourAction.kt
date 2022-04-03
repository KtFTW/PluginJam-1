package net.stckoverflw.pluginjam.action.impl.fightphase

import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.runnables.KSpigotRunnable
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.task
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import net.stckoverflw.pluginjam.util.playersWithoutSpectators
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.scoreboard.Team

class FightPhaseParkourAction : Action() {
    private lateinit var task: KSpigotRunnable
    private val scoreboard = Bukkit.getScoreboardManager().newScoreboard

    override fun execute(): Action {

        sync {
            val team = scoreboard
                .registerNewTeam("no_collision")
                .apply {
                    setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
                }

            pluginJamPlayers.forEach {
                it.scoreboard = scoreboard
                team.addPlayer(it)
            }
        }

        val positionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
        val listener = listen<EntityDamageEvent> { event ->
            if (event.entityType != EntityType.PLAYER) return@listen

            if (event.cause != EntityDamageEvent.DamageCause.LAVA) return@listen

            val position = positionsConfig.getLocation("fight_lava_spawn")

            event.entity.teleport(position)
        }

        task = task(period = 2) {
            val position = positionsConfig.getLocation("fight_parkour_end").block.location
            playersWithoutSpectators.forEach {
                if (it.location.block.location != position) return@forEach
                listener.unregister()
                complete()
            }
        }!!

        return this
    }

    override fun complete() {
        scoreboard.getTeam("no_collision")
            ?.unregister()
        task.cancel()
        super.complete()
    }
}
