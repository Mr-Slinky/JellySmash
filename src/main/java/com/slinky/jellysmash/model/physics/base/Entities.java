package com.slinky.jellysmash.model.physics.base;

import com.slinky.jellysmash.model.physics.comps.Component;
import com.slinky.jellysmash.model.physics.comps.Particle2D;
import com.slinky.jellysmash.model.physics.comps.BallComponent;
import com.slinky.jellysmash.model.physics.comps.Vector2D;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A utility class responsible for managing the life cycle of entities within
 * the ECS architecture. It provides functionality for creating, retrieving, and
 * destroying entities.
 *
 * <p>
 * The {@code Entities} ensures that each entity is assigned a unique identifier
 * in a thread-safe manner using an {@link AtomicLong} for ID generation.
 * Entities are stored in an internal map, which allows for efficient retrieval
 * and management of entities within the system.
 * </p>
 *
 * <p>
 * This class also includes methods for creating specific types of entities,
 * such as points and vectors, with corresponding components like
 * {@link Vector2D}. By delegating component management to the
 * {@code ComponentManager}, the {@code Entities} maintains a clear separation
 * of concerns, making it easier to manage and extend the ECS.
 * </p>
 *
 * @version 2.0
 * @since   0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see Entity
 * @see ComponentManager
 * @see Vector2D
 */
public class Entities {

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
    private static final AtomicLong nextId = new AtomicLong(1);

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
    private static final Map<Long, Entity> entities = new HashMap<>();

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
    public static synchronized Entity getEntity(long id) {
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
    public static synchronized Map<Long, Entity> getAllEntities() {
        return Collections.unmodifiableMap(entities);
    }

    // ============================ API Methods ============================= //
    /**
     * Creates a new entity with a unique identifier and no associated
     * components.
     *
     * <p>
     * This method generates a new entity with a unique ID, safely incremented
     * using an atomic counter. The newly created entity is stored in the
     * internal entity map and returned to the caller.
     * </p>
     *
     * @return the newly created entity
     */
    public static synchronized Entity newEntity() {
        long id = nextId.getAndIncrement(); // Safely increments the ID
        Entity newEntity = new Entity(id);
        entities.put(id, newEntity);
        return newEntity;
    }

    /**
     * Creates a new {@link Entity} with a unique ID and optionally adds
     * specified components to it.
     * <p>
     * This method generates a new entity with a unique ID using an atomic
     * increment to ensure thread safety. The newly created entity is then added
     * to the internal storage of entities. If any components are provided in
     * the method call, they are associated with the newly created entity using
     * the {@link ComponentManager}.
     * </p>
     * <p>
     * The method can handle any number of components. If no components are
     * provided, the entity is created without any associated components.
     * </p>
     *
     * @param components a varargs parameter representing the components to add
     * to the new entity. Each component is associated with the entity in the
     * order they are provided. If no components are provided, the entity is
     * created with no components.
     * @return the newly created {@link Entity} with the assigned ID and any
     * components added.
     *
     * @see Component
     * @see ComponentManager
     * @see Entity
     *
     */
    public static synchronized Entity newEntity(Component... components) {
        long id = nextId.getAndIncrement(); // Safely increments the ID
        Entity newEntity = new Entity(id);
        entities.put(id, newEntity);

        if (components != null) {
            for (int i = 0; i < components.length; i++) {
                if (components[i] == null) {
                    throw new IllegalArgumentException("Cannot create a new entity with a null component (at varargs component array index " + i + " )");
                }

                newEntity.addComponent(components[i]);
            }
        }

        return newEntity;
    }
    
    /**
     * Destroys an entity, removing it from the system.
     * <p>
     * This method removes the specified entity from the internal map,
     * effectively destroying the entity within the system. Any components
     * associated with the entity are also removed by the
     * {@code ComponentManager} as part of the entity's life-cycle management.
     * </p>
     *
     * <p>
     * <b>Note:</b> While this method removes the given entity from the internal
     * map and cleans up its components, any external references to the entity
     * will keep it alive in memory. Therefore, if there are other global
     * references to the entity, it will not be completely destroyed. The entity
     * will only be removed from the internal map, but it will still exist in
     * memory if referenced elsewhere.
     * </p>
     *
     * @param entity the entity to destroy
     * @return {@code true} if the entity was found and removed, {@code false}
     * if the entity was not found.
     */
    public static synchronized boolean destroyEntity(Entity entity) {
        boolean removed = entities.remove(entity.id()) != null;
        entity.removeAllComponents();
        return removed;
    }

    /**
     * Removes all entities and their associated components from the system.
     *
     * <p>
     * This method iterates through all entities managed by the system and
     * invokes {@code removeAllComponents} on each entity to clear their
     * associated components. After clearing all components, it removes each
     * entity from the internal storage, effectively cleaning up the entire
     * entity-component mapping.
     * </p>
     *
     * <p>
     * This is useful for resetting the state of the system, for example, when
     * starting a new simulation or clearing resources for shutdown.
     * </p>
     *
     * <p>
     * <b>Note:</b> that this operation cannot be undone, and all entities and
     * their associated components will be removed from the system.
     * </p>
     */
    public static synchronized void clean() {
        for (Map.Entry<Long, Entity> entry : entities.entrySet()) {
            entry.getValue().removeAllComponents();
        }

        entities.clear();
        nextId.set(1);
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
    public static Entity createPointMass(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double damping, double restitution, boolean isStatic) {
        Entity particleEntity = newEntity();
        Particle2D particleComponent = new Particle2D(position, velocity, acceleration, mass, damping, restitution, isStatic);
        particleEntity.addComponent(particleComponent);

        return particleEntity;
    }

    /**
     * Creates an entity using the given position vector and mass to build a
     * moveable (non-static) Ball at rest.
     *
     * @param position
     * @param mass
     * @return
     */
    public static Entity createSolidBall(Vector2D position, double mass) {
        return newEntity(new BallComponent(position, mass));
    }

}