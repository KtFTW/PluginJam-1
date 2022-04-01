package net.stckoverflw.pluginjam.config

import org.bukkit.configuration.file.YamlConfiguration
import net.stckoverflw.pluginjam.DevcordJamPlugin
import java.io.File
import java.io.IOException

abstract class AbstractConfig(plugin: DevcordJamPlugin, path: String, name: String) {

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
