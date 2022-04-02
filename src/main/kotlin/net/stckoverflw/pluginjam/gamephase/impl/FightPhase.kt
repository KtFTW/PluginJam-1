package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.extensions.worlds
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.ActionPipeline
import net.stckoverflw.pluginjam.action.impl.fightphase.FightDeliverAmethystAction
import net.stckoverflw.pluginjam.action.impl.fightphase.FightPhaseCollectAmethystAction
import net.stckoverflw.pluginjam.action.impl.fightphase.FightPhaseParkourAction
import net.stckoverflw.pluginjam.action.impl.fightphase.FightPhaseWavesAction
import net.stckoverflw.pluginjam.action.impl.fightphase.FightPhaseWavesIntroductionAction
import net.stckoverflw.pluginjam.action.impl.fightphase.FightPhaseWelcomeAction
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.reset
import net.stckoverflw.pluginjam.util.teleportAsyncBlind
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.event.Listener

object FightPhase : GamePhase(TwistPhase), ListenerHolder {
    private val positionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    override val listeners: MutableList<Listener> = mutableListOf()

    override fun start() {
        positionsConfig.getLocation("prison_iron_bar").block.apply {
            type = Material.IRON_BARS
        }

        onlinePlayers.forEach {
            it.teleportAsyncBlind(positionsConfig.getLocation("fight_spawn"))
        }

        worlds.forEach {
            it.setGameRule(GameRule.KEEP_INVENTORY, true)
        }

        ActionPipeline()
            .add(FightPhaseWelcomeAction())
            .add(FightPhaseParkourAction())
            .add(FightPhaseWavesIntroductionAction())
            .add(FightPhaseWavesAction())
            .add(FightPhaseCollectAmethystAction())
            .add(FightDeliverAmethystAction())
            .start()
            .whenComplete {
                GamePhaseManager.nextPhase()
            }
    }

    override fun end() {
        onlinePlayers.forEach {
            it.reset()
        }
    }
}
