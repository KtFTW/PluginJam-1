package net.stckoverflw.pluginjam

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.axay.kspigot.main.KSpigot
import net.stckoverflw.pluginjam.command.PositionCommand
import net.stckoverflw.pluginjam.command.PositionTpCommand
import net.stckoverflw.pluginjam.command.ReloadCommands
import net.stckoverflw.pluginjam.config.ConfigManager
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.i18n.TranslationsProvider
import net.stckoverflw.pluginjam.listener.protectionListener
import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.GameRule

class DevcordJamPlugin : KSpigot() {

    companion object {
        lateinit var instance: DevcordJamPlugin
    }

    val ioScope = CoroutineScope(Dispatchers.IO)
    val defaultScope = CoroutineScope(Dispatchers.Default)
    val mainScope = CoroutineScope(Dispatchers.Main)

    lateinit var translationsProvider: TranslationsProvider
    lateinit var configManager: ConfigManager

    override fun startup() {
        instance = this
        configManager = ConfigManager(this)

        GamePhaseManager.init()

        ReloadCommands(this)
        PositionCommand(configManager.postionsConfig)
        PositionTpCommand(configManager.postionsConfig)

        translationsProvider = TranslationsProvider(this)

        protectionListener()

        Bukkit.getWorlds().first().apply {
            difficulty = Difficulty.PEACEFUL
            time = 0
            setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        }
    }

    override fun shutdown() {
        GamePhaseManager.activeGamePhase?.end()
    }
}
