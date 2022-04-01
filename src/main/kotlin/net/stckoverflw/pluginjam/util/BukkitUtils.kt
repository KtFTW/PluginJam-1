package net.stckoverflw.pluginjam.util

import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.Bukkit
import org.bukkit.World
import java.nio.file.Files
import kotlin.io.path.div

fun broadcastMini(message: String) = broadcast(message.deserializeMini())

fun resetWorld(world: World) {
	deleteWorld(world)
	loadSavedWorld(world.name)
}

fun deleteWorld(world: World) {
	val worldPath = Bukkit.getWorldContainer().toPath() / world.name
	val worldFile = worldPath.toFile()
	worldFile.deleteRecursively()
	Files.createDirectories(worldPath)
	Files.createDirectories(worldPath / "data")
	Files.createDirectories(worldPath / "datapacks")
	Files.createDirectories(worldPath / "playerdata")
	Files.createDirectories(worldPath / "poi")
	Files.createDirectories(worldPath / "region")
}

fun loadSavedWorld(worldName: String) {
	val worldFile = (Bukkit.getWorldContainer().toPath() / worldName).toFile()
	val templateFile = (Bukkit.getWorldContainer().toPath() / "templates" / worldName).toFile()
	templateFile.copyRecursively(
		worldFile,
		true
	) { file, _ ->
		KSpigotMainInstance.logger.warning("Failed to load world $worldName")
		KSpigotMainInstance.logger.warning("Couldn't copy ${file.name}")
		OnErrorAction.SKIP
	}
}
