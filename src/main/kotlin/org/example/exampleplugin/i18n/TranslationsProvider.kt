package org.example.exampleplugin.i18n

import net.axay.kspigot.extensions.pluginManager
import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import org.example.exampleplugin.ExamplePlugin
import java.io.File
import java.net.MalformedURLException
import java.net.URLClassLoader
import java.util.Locale
import java.util.ResourceBundle
import kotlin.io.path.div
import kotlin.io.path.notExists

class TranslationsProvider(private val plugin: ExamplePlugin) {

    private val key = Key.key("org.example.exampleplugin")

    private var translationRegistry = TranslationRegistry.create(key).apply {
        defaultLocale(Locale.ENGLISH)
    }

    private val defaultTranslations = listOf(Locale.ENGLISH, Locale.GERMAN)
    private val locales = mutableListOf<Locale>()

    init {
        saveDefaultTranslationsIfNotExists()
        loadTranslations()
    }

    private fun saveDefaultTranslationsIfNotExists() {
        defaultTranslations.forEach {
            if ((plugin.dataFolder.toPath() / "translations" / "general_${it.language}.properties").notExists()) {
                plugin.saveResource("translations/general_${it.language}.properties", false)
            }
        }
    }

    fun loadTranslations() {
        val locales = plugin.configManager.translationsConfig.locales()

        if (locales.isEmpty()) {
            plugin.logger.severe("No translations found. Please add at least one translation (default available are 'en' and 'de') to the config.yml")
            pluginManager.disablePlugin(plugin)
        }

        unregisterTranslations(locales.first())
        this.locales.clear()
        this.locales.addAll(locales)
        registerTranslations()
    }

    fun setDefault(locale: Locale) {
        translationRegistry.defaultLocale(locale)
    }

    fun unregisterTranslations(defaultLocale: Locale) {
        if (GlobalTranslator.translator().sources().contains(translationRegistry)) {
            GlobalTranslator.translator().removeSource(translationRegistry)
            translationRegistry = TranslationRegistry.create(key).apply {
                defaultLocale(defaultLocale)
            }
        }
    }

    fun registerTranslations() {
        locales.forEach {
            translationRegistry.registerAll(
                it,
                resourceBundleFromClassLoader(plugin.dataFolder.resolve("translations").path, "general", it),
                false
            )
        }
        GlobalTranslator.translator().addSource(translationRegistry)
    }
}

@Throws(MalformedURLException::class)
private fun resourceBundleFromClassLoader(dir: String, bundleName: String, locale: Locale): ResourceBundle {
    val file = File(dir)
    val urls = arrayOf(file.toURI().toURL())
    val loader: ClassLoader = URLClassLoader(urls)
    return ResourceBundle.getBundle(bundleName, locale, loader)
}
