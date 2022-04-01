package net.stckoverflw.pluginjam.config.impl

import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.config.AbstractConfig
import org.bukkit.Bukkit
import org.bukkit.Location

class PostionsConfig(plugin: DevcordJamPlugin) : AbstractConfig(plugin, "postions", "postions.yml") {

    fun get(name: String): Location {
        return yaml.getLocation(name)
            ?: Bukkit.getWorld("world") !!.spawnLocation
    }

    fun modify(name: String, location: Location) {
        yaml.set("positions.$name", location)
        save()
    }
}
