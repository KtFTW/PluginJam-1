package org.example.exampleplugin.config

import org.bukkit.configuration.file.YamlConfiguration
import org.example.exampleplugin.ExamplePlugin
import java.io.File
import java.io.IOException

abstract class AbstractConfig(plugin: ExamplePlugin, path: String, name: String) {

    private val file: File
    private val dir: File = File("${plugin.dataFolder.path}/$path")
    val yaml: YamlConfiguration

    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
        file = File(dir, name)
        if (!file.exists()) {
            plugin.saveResource("$path/$name", false)
        }
        yaml = YamlConfiguration()
        try {
            yaml.load(file)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun save() {
        try {
            yaml.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
