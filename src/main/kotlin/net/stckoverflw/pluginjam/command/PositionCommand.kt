package net.stckoverflw.pluginjam.command

import com.mojang.brigadier.arguments.StringArgumentType
import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.stckoverflw.pluginjam.config.impl.PostionsConfig

class PositionCommand(val postionsConfig: PostionsConfig) {
    init {
        command("position") {
            requiresPermission("pluginjam.position")
            argument("name", StringArgumentType.string()) {
                runs {
                    val name = getArgument<String>("name")
                    postionsConfig.modify(name, player.location)
                    player.sendMessage("<tr:position_saved>")
                }
            }
        }
    }
}
