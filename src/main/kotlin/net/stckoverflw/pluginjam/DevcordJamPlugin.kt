package net.stckoverflw.pluginjam

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.runnables.sync
import net.stckoverflw.pluginjam.command.PositionCommand
import net.stckoverflw.pluginjam.command.PositionTpCommand
import net.stckoverflw.pluginjam.command.ReloadCommands
import net.stckoverflw.pluginjam.command.ResetCommand
import net.stckoverflw.pluginjam.command.SkipPhaseCommand
import net.stckoverflw.pluginjam.command.SpectateCommand
import net.stckoverflw.pluginjam.config.ConfigManager
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.i18n.TranslationsProvider
import net.stckoverflw.pluginjam.listener.protectionListener
import net.stckoverflw.pluginjam.util.loadSavedWorld
import net.stckoverflw.pluginjam.util.repopulateWorld
import net.stckoverflw.pluginjam.util.sendMini
import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.GameMode
import org.bukkit.GameRule
import org.bukkit.WorldCreator
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerJoinEvent
import kotlin.io.path.div

class DevcordJamPlugin : KSpigot() {

    companion object {
        lateinit var instance: DevcordJamPlugin
    }

    val ioScope = CoroutineScope(Dispatchers.IO)
    val defaultScope = CoroutineScope(Dispatchers.Default)
    val mainScope = CoroutineScope(Dispatchers.Main)

    var allowWorldJoin = true

    private lateinit var translationsProvider: TranslationsProvider
    lateinit var configManager: ConfigManager

    override fun startup() {
        val worldName = "pluginjam"
        val path = Bukkit.getWorldContainer().toPath() / worldName
        if (! path.toFile().exists()) {
            ioScope.launch {
                loadSavedWorld(worldName)
                delay(10000)
                println("Creating $worldName from templates")
                sync {
                    Bukkit.createWorld(WorldCreator(worldName))
                }
                delay(10000)
                println("Repopulating $worldName")
                sync {
                    repopulateWorld(worldName)
                }
            }.invokeOnCompletion { e ->
                if (e == null || e is CancellationException) {
                    sync {
                        Bukkit.getWorld("pluginjam")?.apply {
                            difficulty = Difficulty.PEACEFUL
                            time = 0
                            setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
                            setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                            setGameRule(GameRule.DO_MOB_SPAWNING, false)
                            setGameRule(GameRule.KEEP_INVENTORY, true)
                            setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                        }
                        onlinePlayers.forEach { it.gameMode = GameMode.SURVIVAL }
                        onlinePlayers.filter { it.world.name != "pluginjam" }.forEach {
                            it.teleport(Bukkit.getWorld("pluginjam") !!.spawnLocation)
                            it.sendMini("<green><tr:new_game_started>")
                        }
                    }
                }
            }
        } else {
            Bukkit.createWorld(WorldCreator("pluginjam"))
            Bukkit.getWorld("pluginjam")?.apply {
                difficulty = Difficulty.PEACEFUL
                time = 0
                setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
                setGameRule(GameRule.DO_WEATHER_CYCLE, false)
                setGameRule(GameRule.DO_MOB_SPAWNING, false)
                setGameRule(GameRule.KEEP_INVENTORY, true)
                setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
            }

            onlinePlayers.forEach { it.gameMode = GameMode.SURVIVAL }
            onlinePlayers.filter { it.world.name != "pluginjam" }.forEach {
                it.teleport(Bukkit.getWorld("pluginjam") !!.spawnLocation)
                it.sendMini("<green><tr:new_game_started>")
            }
        }

        instance = this
        configManager = ConfigManager(this)

        GamePhaseManager.init()

        ReloadCommands(this)
        PositionCommand(configManager.postionsConfig)
        PositionTpCommand(configManager.postionsConfig)
        SkipPhaseCommand()
        ResetCommand()
        SpectateCommand()

        translationsProvider = TranslationsProvider(this)

        protectionListener()

        listen<PlayerJoinEvent> {
            val world = if (allowWorldJoin) {
                Bukkit.createWorld(WorldCreator("pluginjam")) ?: Bukkit.getWorld("world")
            } else {
                it.player.sendMini("<green><tr:game_already_running:'<click:run_command:/spectate-game><red><tr:here></click>'>")
                Bukkit.getWorld("world")
            }
            world?.spawnLocation?.let { it1 -> it.player.teleportAsync(it1) }
        }
    }

    override fun shutdown() {
        Bukkit.getWorlds().forEach { world ->
            world.entities.filterIsInstance<Villager>().forEach {
                it.remove()
            }
        }
        GamePhaseManager.activeGamePhase?.end()
        ioScope.cancel()
        defaultScope.cancel()
        mainScope.cancel()
    }
}
