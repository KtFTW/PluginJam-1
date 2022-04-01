package net.stckoverflw.pluginjam.config

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.config.impl.PostionsConfig
import net.stckoverflw.pluginjam.config.impl.TranslationsConfig

class ConfigManager(private val plugin: DevcordJamPlugin) {
    lateinit var translationsConfig: TranslationsConfig
    lateinit var postionsConfig: PostionsConfig

    init {
        loadConfigs()
    }

    fun loadConfigs() {
        translationsConfig = TranslationsConfig(plugin)
        postionsConfig = PostionsConfig(plugin)
    }
}
