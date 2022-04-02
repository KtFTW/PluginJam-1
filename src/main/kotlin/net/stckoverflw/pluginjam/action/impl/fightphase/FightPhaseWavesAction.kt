package net.stckoverflw.pluginjam.action.impl.fightphase

import net.axay.kspigot.event.SingleListener
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.main.KSpigotMainInstance
import net.axay.kspigot.runnables.sync
import net.stckoverflw.pluginjam.DevcordJamPlugin
import net.stckoverflw.pluginjam.action.Action
import org.bukkit.Difficulty
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class FightPhaseWavesAction : Action() {
    private var damageListener: SingleListener<EntityDamageByEntityEvent>? = null
    private var deathListener: SingleListener<EntityDeathEvent>? = null
    private var dropListener: SingleListener<PlayerDropItemEvent>? = null
    private var interactListener: SingleListener<PlayerInteractEvent>? = null
    override fun execute(): Action {
        val positionsConfig = DevcordJamPlugin.instance.configManager.postionsConfig
        val spawnPosition = positionsConfig.getLocation("fight_pillager_spawn")
        giveItems()
        sync {
            KSpigotMainInstance.server.worlds.forEach {
                it.difficulty = Difficulty.EASY
            }
            spawnPillagers(spawnPosition)
        }

        var count = 0

        damageListener = listen(EventPriority.HIGHEST) { event ->
            event.isCancelled = false
        }

        deathListener = listen { event ->
            val entity = event.entity

            if (entity.type != EntityType.PILLAGER) return@listen

            count += 1

            println(count)

            if (count >= 50) {
                println("Completed")
                complete()
                return@listen
            }

            if (count == 10 || count == 20 || count == 30 || count == 40) {
                spawnPillagers(spawnPosition)
            }
        }

        dropListener = listen {
            it.isCancelled = true
        }

        interactListener = listen { event ->
            val type = event.clickedBlock?.type ?: return@listen
            if (type != Material.SPRUCE_DOOR) return@listen

            event.isCancelled = true
        }
        return this
    }

    private fun giveItems() {
        KSpigotMainInstance.server.onlinePlayers.forEach {
            it.inventory.clear()

            it.inventory.addItem(ItemStack(Material.DIAMOND_SWORD))

            it.inventory.addItem(ItemStack(Material.BOW))

            it.inventory.addItem(
                itemStack(Material.ARROW) {
                    amount = 64
                }
            )

            it.inventory.addItem(
                itemStack(Material.ARROW) {
                    amount = 64
                }
            )

            it.inventory.addItem(
                itemStack(Material.ARROW) {
                    amount = 64
                }
            )

            it.inventory.helmet = ItemStack(Material.IRON_HELMET)
            it.inventory.chestplate = ItemStack(Material.IRON_CHESTPLATE)
            it.inventory.leggings = ItemStack(Material.IRON_LEGGINGS)
            it.inventory.boots = ItemStack(Material.IRON_BOOTS)
        }
    }

    private fun spawnPillagers(location: Location) {
        for (i in 1..10) {
            location.world.spawnEntity(location, EntityType.PILLAGER)
        }
    }

    override fun complete() {
        KSpigotMainInstance.server.onlinePlayers.forEach {
            it.inventory.clear()
        }

        damageListener?.unregister()
        deathListener?.unregister()
        dropListener?.unregister()
        interactListener?.unregister()
        super.complete()
    }
}
