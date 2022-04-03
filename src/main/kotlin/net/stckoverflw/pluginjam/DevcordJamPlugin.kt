package net.stckoverflw.pluginjam

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.axay.kspigot.event.listen
import net.axay.kspigot.main.KSpigot
import net.stckoverflw.pluginjam.command.PositionCommand
import net.stckoverflw.pluginjam.command.PositionTpCommand
import net.stckoverflw.pluginjam.command.ReloadCommands
import net.stckoverflw.pluginjam.command.ResetCommand
import net.stckoverflw.pluginjam.command.SkipPhaseCommand
import net.stckoverflw.pluginjam.config.ConfigManager
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.i18n.TranslationsProvider
import net.stckoverflw.pluginjam.listener.protectionListener
import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.WorldCreator
import org.bukkit.entity.Villager
import org.bukkit.event.player.PlayerJoinEvent

class DevcordJamPlugin : KSpigot() {

    companion object {
        lateinit var instance: DevcordJamPlugin
    }

    val ioScope = CoroutineScope(Dispatchers.IO)
    val defaultScope = CoroutineScope(Dispatchers.Default)
    val mainScope = CoroutineScope(Dispatchers.Main)

    private lateinit var translationsProvider: TranslationsProvider
    lateinit var configManager: ConfigManager

    override fun startup() {
        Bukkit.createWorld(WorldCreator("pluginjam"))
        instance = this
        configManager = ConfigManager(this)

        GamePhaseManager.init()

        ReloadCommands(this)
        PositionCommand(configManager.postionsConfig)
        PositionTpCommand(configManager.postionsConfig)
        SkipPhaseCommand()
        ResetCommand()

        translationsProvider = TranslationsProvider(this)

        protectionListener()

        Bukkit.getWorlds().first().apply {
            difficulty = Difficulty.PEACEFUL
            time = 0
            setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            setGameRule(GameRule.DO_WEATHER_CYCLE, false)
            setGameRule(GameRule.DO_MOB_SPAWNING, false)
            setGameRule(GameRule.KEEP_INVENTORY, true)
        }

        listen<PlayerJoinEvent> {
            val pluginjamWorld = Bukkit.createWorld(WorldCreator("pluginjam"))
            it.player.sendMessage("§aWelcome to the §bPluginJam§a!")
            it.player.sendMessage("pluginjam world is ${if (pluginjamWorld == null) "not" else ""} loaded")
            pluginjamWorld?.spawnLocation?.let { it1 -> it.player.teleportAsync(it1) }
        }
    }

    override fun shutdown() {
        for (world in Bukkit.getWorlds()) {
            world.entities.filterIsInstance<Villager>().forEach { it.remove() }
        }
        GamePhaseManager.activeGamePhase?.end()
    }
}
