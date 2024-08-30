package com.slinky.jellysmash.model.physics;

import com.slinky.jellysmash.model.physics.comps.ComponentManager;
import com.slinky.jellysmash.model.physics.comps.Particle2D;
import com.slinky.jellysmash.model.physics.comps.Vector2D;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The {@code EntityFactory} class is responsible for managing the life cycle of
 * entities within an Entity Component System (ECS) architecture. It provides
 * functionality for creating, retrieving, and destroying entities, as well as
 * managing their associated components through integration with a
 * {@link ComponentManager}.
 *
 * <p>
 * The {@code EntityFactory} ensures that each entity is assigned a unique
 * identifier in a thread-safe manner using an {@link AtomicLong} for ID
 * generation. Entities are stored in an internal map, which allows for
 * efficient retrieval and management of entities within the system.
 * </p>
 *
 * <p>
 * This class also includes methods for creating specific types of entities,
 * such as points and vectors, with corresponding components like
 * {@link Position2D} and {@link Vector2D}. By delegating component management
 * to the {@code ComponentManager}, the {@code EntityFactory} maintains a clear
 * separation of concerns, making it easier to manage and extend the ECS.
 * </p>
 *
 * @version 1.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see Entity
 * @see ComponentManager
 * @see Vector2D
 */
public class EntityFactory {

    // ============================== Fields ================================ //
    /**
     * An {@link AtomicLong} used to generate unique identifiers for entities.
     *
     * <p>
     * The {@code nextId} field ensures that each entity receives a unique ID in
     * a thread-safe manner. The initial value is set to 1, and it is
     * incremented atomically for each new entity created. This approach
     * guarantees that ID generation remains consistent and free from race
     * conditions in a multi-threaded environment.
     * </p>
     */
    private final AtomicLong nextId = new AtomicLong(1);

    /**
     * A map that stores all entities, keyed by their unique identifier.
     *
     * <p>
     * The {@code entities} map provides quick access to entities via their ID.
     * This map forms the core of the entity management system, allowing for
     * efficient retrieval, creation, and destruction of entities within the ECS
     * architecture.
     * </p>
     */
    private final Map<Long, Entity> entities = new HashMap<>();

    /**
     * The {@link ComponentManager} instance used to manage components
     * associated with entities.
     * <p>
     * This field is responsible for handling the addition, retrieval, and
     * removal of components for entities managed by this factory. By delegating
     * component management to a dedicated {@code ComponentManager}, the
     * {@code EntityFactory} maintains a clear separation of concerns, improving
     * modularity and maintainability.
     * </p>
     */
    private ComponentManager componentManager;

    // =========================== Constructors ============================= //
    /**
     * Constructs a new {@code EntityFactory} with the specified
     * {@link ComponentManager}.
     *
     * <p>
     * This constructor initialises the factory with a {@code ComponentManager},
     * which is used to handle the component life-cycle for entities created by
     * this factory. By passing in a {@code ComponentManager}, the factory can
     * operate within the context of an existing ECS setup, ensuring that
     * entities are properly integrated into the system.
     * </p>
     *
     * @param componentManager the {@link ComponentManager} responsible for
     * managing components of entities
     */
    public EntityFactory(ComponentManager componentManager) {
        this.componentManager = componentManager;
    }

    // ============================== Getters =============================== //
    /**
     * Retrieves an entity by its ID.
     * <p>
     * This method looks up and returns the entity associated with the specified
     * ID. If no entity with the given ID exists, the method returns
     * {@code null}.
     * </p>
     *
     * @param id the ID of the entity to retrieve
     * @return the entity if found, {@code null} otherwise
     */
    public Entity getEntity(long id) {
        return entities.get(id);
    }

    /**
     * Returns an unmodifiable view of all entities.
     * <p>
     * This method provides an unmodifiable view of the current map of entities,
     * allowing the caller to safely access the entity data without risking
     * modification. This is useful for situations where read-only access to all
     * entities is required.
     * </p>
     *
     * @return an unmodifiable map of entity IDs to entities
     */
    public Map<Long, Entity> getAllEntities() {
        return Collections.unmodifiableMap(entities);
    }

    // ============================ API Methods ============================= //
    /**
     * Destroys an entity, removing it from the system.
     * <p>
     * This method removes the specified entity from the internal map,
     * effectively destroying the entity within the system. Any components
     * associated with the entity should also be removed by the
     * {@code ComponentManager} as part of the entity's lifecycle management.
     * </p>
     *
     * @param entity the entity to destroy
     */
    public void destroyEntity(Entity entity) {
        entities.remove(entity.id());
        // TODO REMOVE COMPONENTS
    }

    // ========================== Factory Methods =========================== //
    /**
     * Creates a new entity representing a point mass with specified physical
     * properties.
     *
     * <p>
     * This method allows for the creation of a fully customised particle entity
     * within the Entity Component System (ECS). The particle's position,
     * velocity, acceleration, mass, damping coefficient, restitution, and
     * static flag are all specified, providing fine-grained control over its
     * behaviour in the simulation. The method creates a new entity, attaches a
     * {@link Particle2D} component with the given properties, and adds it to
     * the system.
     * </p>
     *
     * @param position the initial position of the particle in the simulation
     * space, represented as a {@link Position}.
     * @param velocity the initial velocity of the particle, determining its
     * speed and direction, represented as a {@link Vector2D}.
     * @param acceleration the initial acceleration of the particle,
     * representing the rate of change of velocity, represented as a
     * {@link Vector2D}.
     * @param mass the mass of the particle, which must be a non-negative value.
     * @param damping the damping coefficient, controlling how quickly the
     * particle's motion decays, which must be a non-negative value.
     * @param restitution the restitution (bounciness) of the particle,
     * determining how much kinetic energy is conserved in collisions, which
     * must be a non-negative value.
     * @param isStatic a flag indicating whether the particle is static
     * (immovable). If {@code true}, the particle will not respond to forces or
     * acceleration.
     *
     * @return the newly created particle entity.
     *
     * @throws IllegalArgumentException if mass, damping, or restitution are
     * negative.
     */
    public Entity createPointMass(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double damping, double restitution, boolean isStatic) {
        Entity particleEntity = createEntity();
        Particle2D particleComponent = new Particle2D(position, velocity, acceleration, mass, damping, restitution, isStatic);
        componentManager.addComponent(particleEntity, particleComponent);
        return particleEntity;
    }

    /**
     * Creates a new entity representing a point mass with a specified mass and
     * static flag.
     *
     * <p>
     * This method provides a convenient way to create a particle entity with
     * default motion parameters. The particle's mass and static state are
     * specified, while its position is set to the origin, and both velocity and
     * acceleration are initialized to zero. This method creates a new entity,
     * attaches a {@link Particle2D} component with the given mass and static
     * flag, and adds it to the system.
     * </p>
     *
     * @param mass the mass of the particle, which must be a non-negative value.
     * @param isStatic a flag indicating whether the particle is static
     * (immovable). If {@code true}, the particle will not respond to forces or
     * acceleration.
     *
     * @return the newly created particle entity.
     *
     * @throws IllegalArgumentException if mass is negative.
     */
    public Entity createParticle(double mass, boolean isStatic) {
        Entity particleEntity = createEntity();
        Particle2D particleComponent = new Particle2D(mass, isStatic);
        componentManager.addComponent(particleEntity, particleComponent);
        return particleEntity;
    }

    /**
     * Creates a new entity representing a particle with a specified mass.
     *
     * <p>
     * This method creates a particle entity with the given mass and default
     * values for other properties. The particle's position is initialized at
     * the origin, and both velocity and acceleration are set to zero, making it
     * static by default. This method is useful for quickly creating simple
     * particles in the simulation. The method creates a new entity, attaches a
     * {@link Particle2D} component with the specified mass, and adds it to the
     * system.
     * </p>
     *
     * @param mass the mass of the particle, which must be a non-negative value.
     *
     * @return the newly created particle entity.
     *
     * @throws IllegalArgumentException if mass is negative.
     */
    public Entity createParticle(double mass) {
        Entity particleEntity = createEntity();
        Particle2D particleComponent = new Particle2D(mass);
        componentManager.addComponent(particleEntity, particleComponent);
        return particleEntity;
    }

    // ========================== Helper Methods ============================ //
    /**
     * Creates a new entity with a unique identifier.
     * <p>
     * This method generates a new entity with a unique ID, safely incremented
     * using an atomic counter. The newly created entity is stored in the
     * internal entity map and returned to the caller.
     * </p>
     *
     * @return the newly created entity
     */
    private Entity createEntity() {
        long id = nextId.getAndIncrement(); // Safely increments the ID
        Entity newEntity = new Entity(id);
        entities.put(id, newEntity);
        return newEntity;
    }

}