package net.stckoverflw.pluginjam.config.impl

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.config.AbstractConfig
import java.util.Locale

class TranslationsConfig(plugin: DevcordJamPlugin) : AbstractConfig(plugin, "translations", "translations.yml") {

    fun locales(): List<Locale> {
        return yaml.getStringList("locales").mapNotNull { Locale.forLanguageTag(it) }
    }
}
