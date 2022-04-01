package org.example.exampleplugin.config

import org.example.exampleplugin.ExamplePlugin
import org.example.exampleplugin.config.impl.ExampleConfig
import org.example.exampleplugin.config.impl.TranslationsConfig

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
