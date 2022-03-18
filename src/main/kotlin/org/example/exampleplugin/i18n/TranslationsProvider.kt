package org.example.exampleplugin.i18n

import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import java.util.Locale
import java.util.ResourceBundle

class TranslationsProvider {

    private val key = Key.key("org.example.exampleplugin")

    private val translationRegistry = TranslationRegistry.create(key).apply {
        defaultLocale(Locale.US)
    }

    private val locales = listOf(Locale.US, Locale.GERMANY)

    fun setDefault(locale: Locale) {
        translationRegistry.defaultLocale(locale)
    }

    fun registerTranslations() {
        val bundleNames = mutableListOf(
            "translations.general",
        )

        locales.forEach { locale ->
            bundleNames.forEach { bundleName ->
                translationRegistry.registerAll(
                    locale,
                    ResourceBundle.getBundle(bundleName, locale),
                    false
                )
            }
        }
        GlobalTranslator.get().addSource(translationRegistry)
    }
}
