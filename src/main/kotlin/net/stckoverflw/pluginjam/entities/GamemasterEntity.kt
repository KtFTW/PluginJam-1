package net.stckoverflw.pluginjam.entities

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import com.destroystokyo.paper.event.entity.EntityPathfindEvent
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.pluginKey
import net.axay.kspigot.extensions.server
import net.stckoverflw.pluginjam.util.ListenerHolder
import net.stckoverflw.pluginjam.util.mini
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.VillagerCareerChangeEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.EnumSet
import java.util.UUID

internal val gamemasterKey = pluginKey("gamemaster")
internal val goalKey = pluginKey("villager.goal")

var Villager.isGamemaster: Boolean
    get() = if (persistentDataContainer.has(gamemasterKey, PersistentDataType.BYTE)) persistentDataContainer.get(
        gamemasterKey,
        PersistentDataType.BYTE
    ) == 1.toByte() else false
    set(value) {
        persistentDataContainer.set(gamemasterKey, PersistentDataType.BYTE, if (value) 1.toByte() else 0.toByte())
    }

val LOCATION_TAG_TYPE = LocationTagType()

class LocationTagType : PersistentDataType<PersistentDataContainer, Location> {
    private val worldKey: NamespacedKey = pluginKey("world_uuid")
    private val xKey: NamespacedKey = pluginKey("x")
    private val yKey: NamespacedKey = pluginKey("y")
    private val zKey: NamespacedKey = pluginKey("z")
    private val pitchKey: NamespacedKey = pluginKey("pitch")
    private val yawKey: NamespacedKey = pluginKey("yaw")

    override fun getPrimitiveType(): Class<PersistentDataContainer> {
        return PersistentDataContainer::class.java
    }

    override fun getComplexType(): Class<Location> {
        return Location::class.java
    }

    override fun toPrimitive(complex: Location, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        container.set(worldKey, PersistentDataType.STRING, complex.world.uid.toString())
        container.set(xKey, PersistentDataType.DOUBLE, complex.x)
        container.set(yKey, PersistentDataType.DOUBLE, complex.y)
        container.set(zKey, PersistentDataType.DOUBLE, complex.z)
        container.set(pitchKey, PersistentDataType.FLOAT, complex.pitch)
        container.set(yawKey, PersistentDataType.FLOAT, complex.yaw)
        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): Location {
        val worldUuid = UUID.fromString(primitive.get(worldKey, PersistentDataType.STRING))
        val x = primitive.get(xKey, PersistentDataType.DOUBLE)
        val y = primitive.get(yKey, PersistentDataType.DOUBLE)
        val z = primitive.get(zKey, PersistentDataType.DOUBLE)
        val pitch = primitive.get(pitchKey, PersistentDataType.FLOAT)
        val yaw = primitive.get(yawKey, PersistentDataType.FLOAT)
        return Location(server.getWorld(worldUuid), x ?: 0.0, y ?: 0.0, z ?: 0.0, yaw ?: 0F, pitch ?: 0F)
    }
}

var Villager.goal: Location?
    get() = if (persistentDataContainer.has(goalKey)) {
        persistentDataContainer.get(goalKey, LOCATION_TAG_TYPE)
    } else {
        null
    }
    set(value) {
        if (value == null) {
            persistentDataContainer.remove(goalKey)
        } else {
            persistentDataContainer.set(goalKey, LOCATION_TAG_TYPE, value)
        }
    }

class GameMasterGoal(private val villager: Villager, private val callback: () -> Unit) : Goal<Villager> {

    private val villagerGoalKey = GoalKey.of(Villager::class.java, pluginKey("gamemaster_goal"))
    private var lastDistances: ArrayList<Double> = arrayListOf()

    override fun shouldActivate(): Boolean {
        return villager.goal != null && villager.isGamemaster
    }

    override fun getKey(): GoalKey<Villager> {
        return villagerGoalKey
    }

    override fun getTypes(): EnumSet<GoalType> {
        return EnumSet.of(GoalType.MOVE, GoalType.JUMP, GoalType.LOOK)
    }

    override fun shouldStayActive(): Boolean {
        return shouldActivate()
    }

    override fun stop() {
        if (villager.goal == null) return
        villager.pathfinder.stopPathfinding()
        villager.teleportAsync(villager.goal !!)
        villager.goal = null
        villager.setAI(false)
        callback.invoke()
    }

    override fun tick() {
        if (villager.goal == null ||
            lastDistances.average() < 1.0
        ) {
            stop()
            return
        }
        if (lastDistances.size >= 10) {
            lastDistances.removeAt(0)
        }
        lastDistances.add(villager.location.distanceSquared(villager.goal !!))
        if (villager.location.distanceSquared(villager.goal !!) <= 0.5) {
            stop()
        } else {
            villager.pathfinder.moveTo(villager.goal !!, 0.6)
        }
    }
}

class GamemasterEntity(private val nameKnown: Boolean) : ListenerHolder {
    override val listeners: MutableList<Listener> = mutableListOf()
    private var isPathFinding = false
    var bukkitEntity: Villager? = null
    var interactCallback: ((PlayerInteractEntityEvent) -> Unit)? = null

    fun spawnEntity(location: Location) {
        if (bukkitEntity != null) return
        bukkitEntity =
            location.world.spawnEntity(location, EntityType.VILLAGER, CreatureSpawnEvent.SpawnReason.CUSTOM) as Villager
        bukkitEntity?.apply {
            customName(
                mini(
                    if (nameKnown) "<blue>Gamemaster" else "<#FF5733>Dorfbewohner"
                )
            )
            profession = Villager.Profession.LIBRARIAN
            isCustomNameVisible = true
            setAI(false)
            isGamemaster = true
        }

        addListener(
            listen<PlayerInteractEntityEvent> {
                if (it.rightClicked != bukkitEntity) return@listen
                it.isCancelled = true
                interactCallback?.invoke(it)
            }
        )

        addListener(
            listen<EntityDamageEvent> {
                if (it.entity != bukkitEntity) return@listen
                if (it.cause == DamageCause.VOID) return@listen
                it.isCancelled = true
            }
        )

        addListener(
            listen<VillagerCareerChangeEvent> {
                if (it.entity != bukkitEntity) return@listen
                it.isCancelled = true
            }
        )

        addListener(
            listen<EntityPathfindEvent> {
                if (it.entity != bukkitEntity) return@listen
                if (! isPathFinding) return@listen
                it.isCancelled = true
            }
        )
    }

    fun teleport(location: Location) {
        bukkitEntity?.teleport(location)
    }

    fun walkTo(location: Location, callback: () -> Unit) {
        bukkitEntity?.goal = location
        val goal = GameMasterGoal(bukkitEntity !!, callback)
        Bukkit.getMobGoals().removeAllGoals(bukkitEntity !!)
        Bukkit.getMobGoals().addGoal(bukkitEntity !!, 3, goal)
        bukkitEntity?.setAI(true)
        goal.start()
    }

    fun despawn() {
        if (bukkitEntity != null) {
            bukkitEntity?.remove()
        }

        unregisterAllListeners()
    }
}
