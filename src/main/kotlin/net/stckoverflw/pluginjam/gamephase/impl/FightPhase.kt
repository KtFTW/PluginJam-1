package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.main.KSpigotMainInstance
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.ActionPipeline
import net.stckoverflw.pluginjam.action.impl.fightphase.FightPhaseCollectAmethystAction
import net.stckoverflw.pluginjam.action.impl.fightphase.FightPhaseParkourAction
import net.stckoverflw.pluginjam.action.impl.fightphase.FightPhaseWavesAction
import net.stckoverflw.pluginjam.action.impl.fightphase.FightPhaseWavesIntroductionAction
import net.stckoverflw.pluginjam.action.impl.fightphase.FightPhaseWelcomeAction
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.teleportAsyncBlind
import org.bukkit.GameMode
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object FightPhase : GamePhase(TwistPhase), ListenerHolder {
    private val positionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    override val listeners: MutableList<Listener> = mutableListOf()
    private val server = KSpigotMainInstance.server

    override fun start() {
        val position = positionsConfig.getLocation("fight_spawn")

        server.onlinePlayers.forEach {
            it.teleportAsyncBlind(position)
        }

        ActionPipeline()
            .add(FightPhaseWelcomeAction())
            .add(FightPhaseParkourAction())
            .add(FightPhaseWavesIntroductionAction())
            .add(FightPhaseWavesAction())
            .add(FightPhaseCollectAmethystAction())
            .start()
            .whenComplete {
                GamePhaseManager.nextPhase()
            }
        addListener(
            listen<PlayerInteractEvent> {
                if (it.player.gameMode != GameMode.CREATIVE) {
                    it.isCancelled = false
                }
            }
        )
    }

    override fun end() {
        onlinePlayers.forEach { player -> player.activePotionEffects.forEach { player.removePotionEffect(it.type) } }
    }
}
