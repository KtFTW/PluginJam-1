package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.runnables.taskRunLater
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.ActionPipeline
import net.stckoverflw.pluginjam.action.impl.prisionphase.PrisonPhaseWelcomeAction
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import net.stckoverflw.pluginjam.util.reset
import net.stckoverflw.pluginjam.util.teleportAsyncBlind

object PrisonPhase : GamePhase(TaskPhase) {
    private val postionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
    private val gamemaster: GamemasterEntity = GamemasterEntity(false)

    override fun start() {
        gamemaster.spawnEntity(postionsConfig.getLocation("prison_gamemaster"))
        pluginJamPlayers.forEach {
            it.teleportAsyncBlind(postionsConfig.getLocation("prison_prison"))
        }
        taskRunLater(100) {
            ActionPipeline()
                .add(PrisonPhaseWelcomeAction())
                .start()
                .whenComplete {
                    taskRunLater(60) {
                        GamePhaseManager.nextPhase()
                    }
                }
        }
    }

    override fun end() {
        pluginJamPlayers.reset()
        gamemaster.despawn()
    }
}
