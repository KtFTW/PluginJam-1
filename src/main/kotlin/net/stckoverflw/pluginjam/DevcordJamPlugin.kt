package net.stckoverflw.pluginjam

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.axay.kspigot.extensions.bukkit.plainText
import net.axay.kspigot.extensions.bukkit.render
import net.axay.kspigot.main.KSpigot
import net.kyori.adventure.text.Component
import net.stckoverflw.pluginjam.command.PositionCommand
import net.stckoverflw.pluginjam.command.ReloadCommands
import net.stckoverflw.pluginjam.config.ConfigManager
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.i18n.TranslationsProvider
import net.stckoverflw.pluginjam.listener.protectionListener
import java.util.Locale

class DevcordJamPlugin : KSpigot() {

    companion object {
        lateinit var instance: DevcordJamPlugin
    }

    val ioScope = CoroutineScope(Dispatchers.IO)
    val defaultScope = CoroutineScope(Dispatchers.Default)

    lateinit var translationsProvider: TranslationsProvider
    lateinit var configManager: ConfigManager

    override fun startup() {
        instance = this
        configManager = ConfigManager(this)

        GamePhaseManager.init()

        ReloadCommands(this)
        PositionCommand(configManager.postionsConfig)

        translationsProvider = TranslationsProvider(this)

        println(Component.translatable("language").render(Locale.ENGLISH).plainText())
        println(Component.translatable("language").render(Locale.GERMAN).plainText())
        protectionListener()
    }

    override fun shutdown() {
    }
}
