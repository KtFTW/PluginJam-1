package org.example.exampleplugin.config.impl

import org.example.exampleplugin.ExamplePlugin
import org.example.exampleplugin.config.AbstractConfig
import java.util.Locale

class TranslationsConfig(plugin: ExamplePlugin) : AbstractConfig(plugin, "translations", "translations.yml") {

    fun locales(): List<Locale> {
        return yaml.getStringList("locales").mapNotNull { Locale.forLanguageTag(it) }
    }
}
