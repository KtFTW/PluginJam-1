package net.stckoverflw.pluginjam.gamephase.impl

import com.destroystokyo.paper.MaterialTags
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
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import net.stckoverflw.pluginjam.util.reset
import net.stckoverflw.pluginjam.util.teleportAsyncBlind
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.ItemFrame
import org.bukkit.event.Listener

object FightPhase : GamePhase(TwistPhase), ListenerHolder {
    private val positionConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    override val listeners: MutableList<Listener> = mutableListOf()

    override fun start() {
        val amethystFrameBlock = positionConfig.getLocation("twist_amethyst_frame").block
        listOf(BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH).forEach {
            val relative = amethystFrameBlock.getRelative(it)
            if (MaterialTags.TORCHES.isTagged(relative.type)) {
                relative.type = Material.AIR
            }
        }
        amethystFrameBlock.world.getNearbyEntities(amethystFrameBlock.location, 1.0, 1.0, 1.0).forEach {
            if (it is ItemFrame && it.location.block.getRelative((it.attachedFace)) == amethystFrameBlock) {
                it.remove()
            }
        }
        amethystFrameBlock.type = Material.AIR
        amethystFrameBlock.getRelative(BlockFace.DOWN).type = Material.AIR

        positionConfig.getLocation("prison_iron_bar").block.apply {
            type = Material.IRON_BARS
        }

        pluginJamPlayers.forEach {
            it.teleportAsyncBlind(positionConfig.getLocation("fight_spawn"))
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
        pluginJamPlayers.forEach {
            it.reset()
        }
    }
}
