package net.stckoverflw.pluginjam.task.impl

import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.sync
import net.kyori.adventure.text.Component
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.gamephase.impl.TaskPhase
import net.stckoverflw.pluginjam.task.Task
import net.stckoverflw.pluginjam.task.TaskResult
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.Scoreboard
import net.stckoverflw.pluginjam.util.mini
import net.stckoverflw.pluginjam.util.playersWithoutSpectators
import net.stckoverflw.pluginjam.util.pluginJamPlayers
import net.stckoverflw.pluginjam.util.reset
import net.stckoverflw.pluginjam.util.scoreboard
import org.apache.commons.lang.WordUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack
import kotlin.time.Duration.Companion.seconds

abstract class FindMaterialTask : Task(), ListenerHolder {

    override val listeners: MutableList<Listener> = mutableListOf()
    protected val materials = hashMapOf<Material, Int>()
    private val indices = hashMapOf<Material, Int>()
    private val total = hashMapOf<Material, Int>()
    lateinit var scoreboard: Scoreboard

    private fun foundMaterial(player: Player, material: Material, amount: Int) {
        if (!materials.containsKey(material)) {
            return
        }
        materials[material] = materials[material]!! - amount
        player.inventory.contents = player.inventory.contents?.filter { it?.type != material }?.toTypedArray()
        val addBack = player.inventory.contents!!.filter { it?.type == material }.sumOf { it?.amount ?: 0 }
        if (addBack - amount > 0) {
            player.inventory.addItem(ItemStack(material, addBack - amount))
        }
        if ((materials[material] ?: 1) <= 0) {
            materials.remove(material)
        }
        if (materials.isEmpty()) {
            pluginJamPlayers.forEach {
                it.reset()
            }
        }

        scoreboard.set(indices[material]!!, createScoreboardEntry(material))

        if (materials.isEmpty()) {
            Conversation(DevcordJamPlugin.instance)
                .addMessage("<tr:task_finished>", "<tr:villager>", 1.seconds)
                .start()
                .whenComplete { _, _ ->
                    TaskPhase.taskDone(TaskResult.SUCCESS)
                }
        }
    }

    private fun Player.giveItems() {
        inventory.addItem(
            ItemStack(Material.IRON_PICKAXE),
            ItemStack(Material.IRON_AXE),
        )
    }

    private fun createScoreboardEntry(material: Material): Component {
        val count = total[material]!! - (materials[material] ?: 0)

        return mini(
            "<green>${
            WordUtils.capitalize(
                material.name.lowercase().replace("_", " ")
            )
            } <gray>- <red>$count<gray>/<red>${total[material]!!}"
        )
    }

    override fun start() {
        sync {
            var i = 1

            materials.forEach {
                total[it.key] = it.value
            }

            scoreboard = scoreboard {
                displayName = mini("<green><tr:task>")
                set(0, "")

                materials.forEach {
                    indices[it.key] = i
                    set(i, createScoreboardEntry(it.key))
                    i++
                }

                set(
                    i,
                    when (materials.size) {
                        1 -> "<green><tr:task_get_item>"
                        else -> "<green><tr:task_get_items>"
                    }
                )

                if (materials.size <= 1) {
                    println("1")
                } else {
                    println("2")
                }
                set(i + 1, "")
            }

            playersWithoutSpectators.forEach {
                it.giveItems()
            }

            pluginJamPlayers.forEach {
                scoreboard.applyScoreboard(it)
            }
            println(scoreboard.entries)
        }
        addListener(
            listen<PlayerInteractEntityEvent> {
                if (GamePhaseManager.activeGamePhase !is TaskPhase) return@listen
                it.player.inventory.contents?.filterNotNull()?.forEach { itemStack ->
                    foundMaterial(it.player, itemStack.type, itemStack.amount)
                }
            }
        )
        addListener(
            listen<BlockBreakEvent>(EventPriority.HIGHEST) {
                if (GamePhaseManager.activeGamePhase !is TaskPhase) return@listen
            }
        )
        addListener(
            listen<BlockPlaceEvent>(EventPriority.HIGHEST) {
                if (GamePhaseManager.activeGamePhase !is TaskPhase) return@listen
            }
        )
    }

    override fun stop() {
        unregisterAllListeners()
        scoreboard.removeScoreboard()
    }
}
