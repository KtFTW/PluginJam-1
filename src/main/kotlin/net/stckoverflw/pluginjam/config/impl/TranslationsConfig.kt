package net.stckoverflw.pluginjam.config.impl

import net.stckoverflw.pluginjam.ExamplePlugin
import net.stckoverflw.pluginjam.config.AbstractConfig
import java.util.Locale

class TranslationsConfig(plugin: ExamplePlugin) : AbstractConfig(plugin, "translations", "translations.yml") {

    fun locales(): List<Locale> {
        return yaml.getStringList("locales").mapNotNull { Locale.forLanguageTag(it) }
    }
}
