package net.stckoverflw.pluginjam

import net.axay.kspigot.extensions.bukkit.plainText
import net.axay.kspigot.extensions.bukkit.render
import net.axay.kspigot.main.KSpigot
import net.kyori.adventure.text.Component
import net.stckoverflw.pluginjam.command.ExampleCommand
import net.stckoverflw.pluginjam.config.ConfigManager
import net.stckoverflw.pluginjam.i18n.TranslationsProvider
import net.stckoverflw.pluginjam.listener.ExampleListener
import java.util.Locale

class ExamplePlugin : KSpigot() {

    lateinit var translationsProvider: TranslationsProvider
    lateinit var configManager: ConfigManager

    override fun startup() {
        // register commands
        ExampleCommand()

        // register listeners
        ExampleListener()

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
