package net.stckoverflw.pluginjam.task.impl

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlinePlayers
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.gamephase.GamePhaseManager
import net.stckoverflw.pluginjam.gamephase.impl.TaskPhase
import net.stckoverflw.pluginjam.task.Task
import net.stckoverflw.pluginjam.task.TaskResult
import net.stckoverflw.pluginjam.util.Conversation
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.reset
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
            onlinePlayers.forEach {
                it.reset()
            }
        }

        if (materials.isEmpty()) {
            Conversation(DevcordJamPlugin.instance)
                .addMessage("Perfekt!", "<blue>Dorfbewohner</blue>", 1.seconds)
                .start()
                .whenComplete { _, _ ->
                    TaskPhase.taskDone(TaskResult.SUCCESS)
                }
        }
    }

    private fun Player.giveItems() {
        inventory.addItem(
            ItemStack(Material.STONE_SWORD),
            ItemStack(Material.STONE_PICKAXE),
            ItemStack(Material.STONE_AXE),
            ItemStack(Material.STONE_SHOVEL),
            ItemStack(Material.STONE_HOE)
        )
    }

    override fun start() {
        onlinePlayers.forEach {
            it.giveItems()
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
    }
}
