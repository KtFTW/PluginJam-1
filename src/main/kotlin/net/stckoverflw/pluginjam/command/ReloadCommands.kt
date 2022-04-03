package net.stckoverflw.pluginjam.command

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.util.sendMini

class ReloadCommands(private val plugin: DevcordJamPlugin) {

    @Suppress("unused")
    val reloadConfig = command("reload-configs") {
        requiresPermission("pluginjam.reload-configs")
        runs {
            plugin.ioScope.launch {
                plugin.configManager.loadConfigs()
            }.invokeOnCompletion {
                player.sendMini(
                    when (it) {
                        null, is CancellationException -> { // job done
                            "<green><tr:configs_reloaded></green>"
                        }
                        else -> { // job failed
                            plugin.logger.warning("<red><tr:configs_not_reloaded_1></red>")
                            plugin.logger.warning(it.stackTraceToString())
                            "<red><tr:configs_not_reloaded_2></red>"
                        }
                    }
                )
            }
        }
    }
}
