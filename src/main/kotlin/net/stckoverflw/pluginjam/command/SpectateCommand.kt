package net.stckoverflw.pluginjam.command

import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import net.stckoverflw.pluginjam.util.sendMini
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player

class SpectateCommand {

    val command = command("spectate-game") {
        runs {
//            player.gameMode = GameMode.SPECTATOR
            val world = Bukkit.getWorld("pluginjam") ?: return@runs player.sendMini("<red><tr:world_resetting>")
            player.teleportAsync(
                if (world.entities.filterIsInstance<Player>().any { it.gameMode == GameMode.SURVIVAL }) {
                    world.entities.filterIsInstance<Player>().filter { it.gameMode == GameMode.SURVIVAL }.random().location
                } else {
                    world.spawnLocation
                }
            )
            player.gameMode = GameMode.SPECTATOR
        }
    }
}
