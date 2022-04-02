package net.stckoverflw.pluginjam.command

import com.mojang.brigadier.arguments.StringArgumentType
import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.stckoverflw.pluginjam.config.impl.PostionsConfig

class PositionTpCommand(val postionsConfig: PostionsConfig) {
    init {
        command("positiontp") {
            requiresPermission("pluginjam.positiontp")
            argument("name", StringArgumentType.string()) {
                runs {
                    val name = getArgument<String>("name")
                    val location = postionsConfig.getLocation(name)
                    player.teleport(location)
                }
            }
        }
    }
}
