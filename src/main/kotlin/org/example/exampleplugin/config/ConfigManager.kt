package org.example.exampleplugin.config

import org.example.exampleplugin.ExamplePlugin
import org.example.exampleplugin.config.impl.ExampleConfig

class ConfigManager(private val plugin: ExamplePlugin) {

    lateinit var exampleConfig: ExampleConfig

    operator fun invoke() {
        reloadConfigs()
    }

    private fun reloadConfigs() {
        exampleConfig = ExampleConfig(plugin)
    }
}
