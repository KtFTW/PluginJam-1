package net.stckoverflw.pluginjam.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.extensions.server
import net.axay.kspigot.main.KSpigotMainInstance
import net.axay.kspigot.runnables.sync
import net.kyori.adventure.text.Component
import net.stckoverflw.pluginjam.DevcordJamPlugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.WorldCreator
import org.bukkit.block.Block
import org.bukkit.block.data.type.Door
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerLoginEvent
import java.nio.file.Files
import kotlin.io.path.div

val pluginJamPlayers: List<Player>
    get() = onlinePlayers.filter { it.world.name == "pluginjam" && it.gameMode == GameMode.SURVIVAL }

val playersWithoutSpectators: List<Player>
    get() = onlinePlayers.filter { it.world.name == "pluginjam" && it.gameMode != GameMode.SPECTATOR }

fun broadcastToPluginJamPlayers(message: Component) {
    pluginJamPlayers.forEach { it.sendMessage(message) }
}

fun broadcastMini(message: String) = broadcastToPluginJamPlayers(message.deserializeMini())

fun resetWorld(worldName: String) {
    DevcordJamPlugin.instance.ioScope.launch {
        println("Resetting world $worldName")
        println("Registering PlayerLoginEvent listener")
        val listener = listen<PlayerLoginEvent> {
            it.disallow(PlayerLoginEvent.Result.KICK_OTHER, "<red>World is resetting".deserializeMini())
        }
        println("Depopulating $worldName")
        sync {
            depopulateWorld(worldName)
        }
        delay(5000)
        println("Unloading $worldName")
        var isUnloaded = false
        sync { isUnloaded = unloadWorld(worldName) }
        while (!isUnloaded) {
            println("Could not unload $worldName, trying again in 3 second")
            delay(3000)
            println("Unloading $worldName")
            sync { isUnloaded = unloadWorld(worldName) }
        }
        delay(10000)
        println("Deleting $worldName")
        deleteWorld(worldName)
        delay(10000)
        println("Loading $worldName from templates")
        loadSavedWorld(worldName)
        delay(10000)
        println("Creating $worldName from templates")
        sync {
            Bukkit.createWorld(WorldCreator(worldName))
        }
        delay(10000)
        println("Unregistering PlayerLoginEvent listener")
        listener.unregister()
        println("Repopulating $worldName")
        sync {
            repopulateWorld(worldName)
        }
        delay(5000)
        println("Reloading server")
        sync {
            server.reload()
        }
    }
}

fun repopulateWorld(worldName: String) {
    val pluginJamWworld = Bukkit.getWorld(worldName) ?: return
    onlinePlayers.forEach {
        it.teleportAsync(pluginJamWworld.spawnLocation)
    }
}

fun depopulateWorld(worldName: String) {
    val world = Bukkit.getWorld("world") ?: return
    val pluginJamWorld = Bukkit.getWorld(worldName) ?: return
    pluginJamWorld.entities.filterIsInstance<Player>().forEach {
        it.reset()
        it.teleportAsync(world.spawnLocation)
    }
}

fun unloadWorld(worldName: String): Boolean {
    return Bukkit.unloadWorld(worldName, false)
}

fun deleteWorld(worldName: String) {
    val worldPath = Bukkit.getWorldContainer().toPath() / worldName
    val worldFile = worldPath.toFile()
    worldFile.deleteRecursively()
    Files.createDirectories(worldPath)
}

fun loadSavedWorld(worldName: String) {
    val templateFile = (Bukkit.getWorldContainer().toPath() / "templates" / worldName).toFile()
    val worldFile = (Bukkit.getWorldContainer().toPath() / worldName).toFile()
    templateFile.copyRecursively(
        worldFile,
        true
    ) { file, _ ->
        KSpigotMainInstance.logger.warning("Failed to load world $worldName")
        KSpigotMainInstance.logger.warning("Couldn't copy ${file.name}")
        OnErrorAction.SKIP
    }
}

fun Block.setOpenIfDoor(open: Boolean) {
    sync {
        val door = this.blockData as? Door ?: return@sync
        door.isOpen = open
        this.setBlockData(door, true)
    }
}
