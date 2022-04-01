package net.stckoverflw.pluginjam.config

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.config.impl.TranslationsConfig

class ConfigManager(private val plugin: DevcordJamPlugin) {

    lateinit var translationsConfig: TranslationsConfig

    init {
        loadConfigs()
    }

    fun loadConfigs() {
        translationsConfig = TranslationsConfig(plugin)
    }
}
