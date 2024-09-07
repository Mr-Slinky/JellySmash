package com.slinky.physics.base;

import com.slinky.physics.comps.Circle;
import com.slinky.physics.comps.Component;
import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;
import static java.lang.Math.sqrt;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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
 * @author  Kheagen Haskins
 *
 * @see     Entity
 * @see     Vector2D
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
     * 
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
     * 
     * <p>
     * This method provides an unmodifiable view of the current map of entities,
     * allowing the caller to safely access the entity data without risking
     * modification. This is useful for situations where read-only access to all
     * entities is required.
     * </p>
     *
     * @return an unmodifiable map of entity IDs to entities
     */
    public static synchronized Map<Long, Entity> getEntityMap() {
        return Collections.unmodifiableMap(entities);
    }

    /**
     * Retrieves a list of entities that contain all of the specified component
     * types.
     *
     * <p>
     * This method filters through the existing entities within the system and
     * returns those that have all the specified components. The method uses
     * Java Streams to iterate over the entities, applying a filter that checks
     * whether each entity contains the required components. The result is a
     * list of entities that match the given criteria.
     * </p>
     *
     * <p>
     * This method is particularly useful in scenarios where a system or process
     * needs to operate only on entities that have a specific set of components.
     * For example, in a physics simulation, you might need to find all entities
     * that have both a {@code Position} and a {@code Velocity} component to
     * update their positions based on their velocities.
     * </p>
     *
     * @param componentClasses The classes of the components to check for in
     * each entity. The method will return only those entities that contain all
     * the specified components.
     * @param <T> The type of the components being checked. This can be any
     * class that implements the {@link Component} interface.
     * @return A list of {@link Entity} objects that contain all of the
     * specified component types. If no entities match the criteria, an empty
     * list is returned.
     *
     * @see Entity#hasComponents(Class[])
     */
    public static synchronized <T extends Component> List<Entity> getEntitiesWith(Class<T>... componentClasses) {
        return entities.values().stream()
                .filter(entity -> entity.hasComponents(componentClasses))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of entities that match the specified {@link EntityType}.
     *
     * <p>
     * This method filters and returns all entities that contain the set of
     * components associated with the given {@code EntityType}. The filtering is
     * based on the component classes defined in the {@link EntityType}. This
     * allows for a more generic and type-safe way of querying entities, making
     * it easier to retrieve entities based on their type rather than directly
     * specifying component classes.
     * </p>
     *
     * <p>
     * By using the {@link EntityType#getComponentClasses()} method, this
     * approach leverages the predefined configuration of components associated
     * with each {@code EntityType}, ensuring that the returned entities conform
     * to the expected structure defined by the type. This method enhances the
     * flexibility of the ECS architecture by simplifying the process of
     * retrieving entities with specific characteristics.
     * </p>
     *
     * <p>
     * <b>Example Usage:</b>
     * <pre>{@code
     *     List<Entity> balls = Entities.getEntitiesOfType(EntityType.BALL);
     *     // Iterate through the list of ball entities and perform operations
     * }</pre>
     * </p>
     *
     * @param type the {@link EntityType} that defines the components to filter
     * entities by
     * @param <T> the type of the component (inferred from the
     * {@code EntityType})
     * @return a list of {@link Entity} objects that match the specified
     * {@code EntityType}
     *
     * @see EntityType
     * @see #getEntitiesWith(Class[])
     */
    public static <T extends Component> List<Entity> getEntitiesOfType(EntityType type) {
        return getEntitiesWith(type.getComponentClasses());
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
     * 
     * <p>
     * This method generates a new entity with a unique ID using an atomic
     * increment to ensure thread safety. The newly created entity is then added
     * to the internal storage of entities. If any components are provided in
     * the method call, they are associated with the newly created entity using
     * the {@link ComponentManager}.
     * </p>
     * 
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
     * 
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
     * Creates a solid ball entity with specified position, velocity, mass,
     * damping, restitution, radius, and static state.
     * <p>
     * This method allows full control over the properties of the ball, such as
     * its initial position, velocity, mass, damping (which controls the energy
     * loss over time), restitution (which governs how much the ball bounces
     * upon collisions), radius, and whether it is static (immovable). It
     * returns an {@link Entity} that combines a {@link PointMass} for the
     * physics and a {@link Circle} for the visual representation.
     * </p>
     *
     * @param x         The initial x-coordinate of the ball's position.
     * @param y         The initial y-coordinate of the ball's position.
     * @param vx        The initial velocity in the x-direction (horizontal velocity)
     *                  of the ball.
     * @param vy        The initial velocity in the y-direction (vertical velocity) of
     *                  the ball.
     * @param mass      The mass of the ball, which affects its inertia and
     *                  resistance to acceleration.
     * @param damping   The damping factor, which simulates friction or air
     *                  resistance that gradually reduces the velocity.
     * @param rest      The restitution coefficient, which determines how elastic
     *                  (bouncy) the collisions are (1 is perfectly elastic, 0 is perfectly
     *                  inelastic).
     * @param radius    The radius of the ball, used for collision detection and
     *                  rendering.
     * @param isStatic  A boolean flag that indicates whether the ball is static
     *                  (true) or dynamic (false). A static ball does not move.
     * 
     * @return An {@link Entity} representing a solid ball in the physics
     * engine, composed of a {@link PointMass} for physics and a {@link Circle}
     * for rendering.
     */
    public static Entity createSolidBall(double x, double y, double vx, double vy, double mass, double damping, double rest, double radius, boolean isStatic) {
        PointMass pointMass = new PointMass(
                new Vector2D(x, y),
                new Vector2D(vx, vy),
                new Vector2D(0, 0),
                mass,
                damping,
                rest,
                isStatic
        );

        return newEntity(pointMass, new Circle(radius));
    }

    /**
     * Creates a solid ball entity with default velocity, damping, restitution,
     * radius, and static state, but specified position and mass.
     * <p>
     * This overloaded method simplifies the creation of a ball by only
     * requiring the position and mass. The ball is created with default values
     * for velocity (stationary), damping (no energy loss), restitution
     * (perfectly elastic collisions), and radius (calculated based on the
     * square root of the mass). The ball is dynamic (not static) by default.
     * </p>
     *
     * @param x     The initial x-coordinate of the ball's position.
     * @param y     The initial y-coordinate of the ball's position.
     * @param mass  The mass of the ball, which affects its inertia and
     *              resistance to acceleration.
     * 
     * @return An {@link Entity} representing a solid ball in the physics
     * engine, composed of a {@link PointMass} for physics and a {@link Circle}
     * for rendering, with default velocity, damping, and restitution values.
     *
     * @see #createSolidBall(double, double, double, double, double, double,
     * double, double, boolean) for full customisation of the ball's properties.
     */
    public static Entity createSolidBall(double x, double y, double mass) {
        return createSolidBall(
                x, y, // position
                0, 0, // velocity (at rest)
                mass, // mass
                0, // damping
                1, // restitution
                sqrt(mass) * 10, // radius (calculated based on mass)
                false // is dynamic (not static)
        );
    }


    // ========================== Helper Methods ============================ //
    
}