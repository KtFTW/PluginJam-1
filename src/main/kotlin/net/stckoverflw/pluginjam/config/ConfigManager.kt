package net.stckoverflw.pluginjam.config

import net.stckoverflw.pluginjam.ExamplePlugin
import net.stckoverflw.pluginjam.config.impl.ExampleConfig
import net.stckoverflw.pluginjam.config.impl.TranslationsConfig

class ConfigManager(private val plugin: ExamplePlugin) {

    lateinit var exampleConfig: ExampleConfig
    lateinit var translationsConfig: TranslationsConfig

    init {
        loadConfigs()
    }

    private fun loadConfigs() {
        exampleConfig = ExampleConfig(plugin)
        translationsConfig = TranslationsConfig(plugin)
    }
}
