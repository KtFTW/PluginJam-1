package net.stckoverflw.pluginjam

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.axay.kspigot.extensions.bukkit.plainText
import net.axay.kspigot.extensions.bukkit.render
import net.axay.kspigot.main.KSpigot
import net.kyori.adventure.text.Component
import net.stckoverflw.pluginjam.command.ReloadCommands
import net.stckoverflw.pluginjam.config.ConfigManager
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.i18n.TranslationsProvider
import java.util.Locale

class DevcordJamPlugin : KSpigot() {

    val ioScope = CoroutineScope(Dispatchers.IO)
    val defaultScope = CoroutineScope(Dispatchers.Default)

    lateinit var translationsProvider: TranslationsProvider
    lateinit var configManager: ConfigManager

    override fun startup() {

        GamePhaseManager.init()

        ReloadCommands(this)

        // load configs
        configManager = ConfigManager(this)

        // load and register translations
        translationsProvider = TranslationsProvider(this)

        println(Component.translatable("language").render(Locale.ENGLISH).plainText())
        println(Component.translatable("language").render(Locale.GERMAN).plainText())
    }

    override fun shutdown() {
    }
}
