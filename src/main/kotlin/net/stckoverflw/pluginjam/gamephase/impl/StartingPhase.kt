package net.stckoverflw.pluginjam.gamephase.impl

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.runnables.task
import net.stckoverflw.pluginjam.DevcordJamPlugin.Companion.instance
import net.stckoverflw.pluginjam.action.ActionPipeline
import net.stckoverflw.pluginjam.action.impl.global.GasPipelineAction
import net.stckoverflw.pluginjam.action.impl.global.WalkingAction
import net.stckoverflw.pluginjam.action.impl.startingphase.StartingPhaseWelcomeAction
import net.stckoverflw.pluginjam.entities.GamemasterEntity
import net.stckoverflw.pluginjam.gamephase.GamePhase
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.listener.GamemasterVelocity
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.mini
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import net.stckoverflw.pluginjam.util.reset
import net.stckoverflw.pluginjam.util.setOpenIfDoor
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

object StartingPhase : GamePhase(PrisonPhase), ListenerHolder {
    private val postionsConfig = instance.configManager.postionsConfig
    override val listeners: MutableList<Listener> = mutableListOf()
    private var blocked = false

    private val gamemaster: GamemasterEntity = GamemasterEntity(false)

    override fun start() {
        gamemaster.interactCallback = interactCallback@{
            if (blocked) return@interactCallback
            blocked = true
            pluginJamPlayers.forEach {
                it.reset()
            }
            instance.allowWorldJoin = false

            ActionPipeline()
                .add(StartingPhaseWelcomeAction())
                .add(WalkingAction(gamemaster, postionsConfig.getLocation("starting_gamemaster_1")))
                .start()
                .whenComplete {
                    val area = LocationArea(
                        postionsConfig.getLocation("starting_gamemaster_house_0"),
                        postionsConfig.getLocation("starting_gamemaster_house_1")
                    )
                    task(period = 5) {
                        pluginJamPlayers.forEach { player -> player.sendActionBar(mini("Gehe in das Haus")) }
                        if (pluginJamPlayers.all { player -> area.isInArea(player.location) }) {

                            val door0 = postionsConfig.getLocation("starting_door_0").add(0.0, 1.0, 0.0).block
                            val door1 = postionsConfig.getLocation("starting_door_1").add(0.0, 1.0, 0.0).block

                            door0.setOpenIfDoor(false)
                            door1.setOpenIfDoor(false)

                            GasPipelineAction(
                                gamemaster,
                                postionsConfig.getLocation("starting_pipe_0"),
                                postionsConfig.getLocation("starting_pipe_1"),
                                postionsConfig.getLocation("starting_gamemaster_target")
                            )
                                .execute()
                                .whenComplete {
                                    GamePhaseManager.nextPhase()
                                }
                            it.cancel()
                        }
                    }
                }
        }

        gamemaster.spawnEntity(postionsConfig.getLocation("starting_gamemaster_0"))
        GamemasterVelocity(gamemaster)

        addListener(
            listen<PlayerJoinEvent> {
                it.player.reset()
                // it.player.sendMini("<i>Wilkommen auf unserem PluginJam Server.")
                // it.player.sendMini("<i>Klicke auf den Villager um das Spiel zu starten.")
                // it.player.sendMini("<i>Viel Spaß!")
            }
        )

        addListener(
            listen<PlayerInteractEvent> {
                if (it.player.gameMode != GameMode.CREATIVE) {
                    it.isCancelled = false
                }
            }
        )
    }

    override fun end() {
        unregisterAllListeners()
        gamemaster.despawn()
        Bukkit.getWorld("pluginjam")!!
            .apply {
                time = 0
            }
    }
}
