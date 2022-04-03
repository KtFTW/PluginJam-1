package net.stckoverflw.pluginjam.config.impl

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.config.AbstractConfig
import org.bukkit.Bukkit
import org.bukkit.Location

class PostionsConfig(plugin: DevcordJamPlugin) : AbstractConfig(plugin, "postions", "postions.yml") {

    fun getLocation(name: String): Location {
        return yaml.getLocation(name)
            ?: Bukkit.getWorld("pluginjam")!!.spawnLocation
    }

    fun modify(name: String, location: Location) {
        yaml.set(name, location)
        save()
    }
}
