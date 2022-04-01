package org.example.exampleplugin

import net.axay.kspigot.extensions.bukkit.plainText
import net.axay.kspigot.extensions.bukkit.render
import net.axay.kspigot.main.KSpigot
import net.kyori.adventure.text.Component
import org.example.exampleplugin.command.ExampleCommand
import org.example.exampleplugin.config.ConfigManager
import org.example.exampleplugin.i18n.TranslationsProvider
import org.example.exampleplugin.listener.ExampleListener
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
