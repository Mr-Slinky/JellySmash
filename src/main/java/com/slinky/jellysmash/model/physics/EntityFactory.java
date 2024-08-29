package com.slinky.jellysmash.model.physics;

import com.slinky.jellysmash.model.physics.comps.ComponentManager;
import com.slinky.jellysmash.model.physics.comps.Position;
import com.slinky.jellysmash.model.physics.comps.Position2D;
import com.slinky.jellysmash.model.physics.comps.Vector;
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
 * <p>
 * The {@code EntityFactory} is essential for systems that require dynamic
 * creation and destruction of entities, such as in simulations, games, or any
 * application leveraging the ECS pattern. It is designed to integrate
 * seamlessly with the overall architecture, ensuring that entities are properly
 * initialised and maintained throughout their life cycle.
 * </p>
 *
 * @version  1.0
 * @since    0.1.0
 * 
 * @author   Kheagen Haskins
 * 
 * @see     Entity
 * @see     ComponentManager
 * @see     Position2D
 * @see     Vector2D
 */
public class EntityFactory {

    // ============================== Static ================================ //
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
    // ============================== Setters =============================== //
    // ============================ API Methods ============================= //
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
     * Creates a new entity with a unique identifier.
     * <p>
     * This method generates a new entity with a unique ID, safely incremented
     * using an atomic counter. The newly created entity is stored in the
     * internal entity map and returned to the caller.
     * </p>
     *
     * @return the newly created entity
     */
    public Entity createEntity() {
        long id = nextId.getAndIncrement(); // Safely increments the ID
        Entity newEntity = new Entity(id);
        entities.put(id, newEntity);
        return newEntity;
    }

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

    /**
     * Creates a new entity representing a point with a specific position.
     * <p>
     * This method creates a new entity, assigns a {@link Position2D} component
     * representing the point's position in 2D space, and stores the entity in
     * the system. The new entity is then returned to the caller.
     * </p>
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return the newly created point entity
     */
    public Entity createPoint(double x, double y) {
        Entity pointEntity = createEntity();
        Position position = new Position2D(x, y);
        componentManager.addComponent(pointEntity, position);
        return pointEntity;
    }

    /**
     * Creates a new entity representing a vector with specific x and y
     * components.
     * <p>
     * This method creates a new entity, assigns a {@link Vector2D} component
     * representing the vector in 2D space, and stores the entity in the system.
     * The new vector entity is then returned to the caller.
     * </p>
     *
     * @param x the x-component of the vector
     * @param y the y-component of the vector
     * @return the newly created vector entity
     */
    public Entity createVector(double x, double y) {
        Entity vectorEntity = createEntity();
        Vector vector = new Vector2D(x, y);
        componentManager.addComponent(vectorEntity, vector);
        return vectorEntity;
    }

}
