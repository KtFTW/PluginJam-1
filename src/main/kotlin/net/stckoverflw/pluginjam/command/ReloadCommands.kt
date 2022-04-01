package net.stckoverflw.pluginjam.command

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.util.sendMini

class ReloadCommands(private val plugin: DevcordJamPlugin) {

    @Suppress("unused")
    val reloadConfig = command("reload-configs") {
        runs {
            plugin.ioScope.launch {
                plugin.configManager.loadConfigs()
            }.invokeOnCompletion {
                player.sendMini(
                    when (it) {
                        null -> { // job done
                            "<green>Konfigurationen wurden neu geladen.</green>"
                        }
                        is CancellationException -> { // job cancelled normally
                            "<orange>Der Job zum Neuladen der Konfigurationen wurde abgebrochen.</orange>"
                        }
                        else -> { // job failed
                            plugin.logger.warning("Konfigurationen konnten nicht neu geladen werden")
                            plugin.logger.warning(it.stackTraceToString())
                            "<red>Konfigurationen konnten nicht neu geladen werden (mehr Informationen in der Console)</red>"
                        }
                    }
                )
            }
        }
    }
}
