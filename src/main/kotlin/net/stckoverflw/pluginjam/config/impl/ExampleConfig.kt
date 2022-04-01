package net.stckoverflw.pluginjam.config.impl

import net.stckoverflw.pluginjam.ExamplePlugin
import net.stckoverflw.pluginjam.config.AbstractConfig

class ExampleConfig(plugin: ExamplePlugin) : AbstractConfig(plugin, "settings", "example.yml")
