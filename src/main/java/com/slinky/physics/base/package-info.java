/**
 * <p>
 * This base package provides the core entity management functionality for the Physics ECS (Entity Component System)
 * architecture. It includes essential classes for handling entities and components, as well as managing their lifecycle
 * within the simulation.
 * </p>
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>
 *     <b>Entity</b> - 
 *     Represents a unique entity within the ECS system. Upon instantiation, an entity is associated with one or more
 *     {@link Component} implementations. Each entity is responsible for interacting with the ECS framework through
 *     its components, defining its behaviour within the system.
 *     <p>
 *     This class maintains a reference to the {@link ComponentManager} singleton instance, ensuring that all
 *     component-related operations are centralised and managed efficiently. The {@code ComponentManager} is
 *     package-private, which restricts its direct access to the {@code Entity} class, enforcing a tight coupling
 *     between these two classes. This design promotes encapsulation by limiting component management to the {@code Entity}
 *     class, reducing the likelihood of mismanagement.
 *     </p>
 *   </li>
 *   <li>
 *     <b>Entities</b> - 
 *     A utility class responsible for managing the lifecycle of entities within the ECS. It provides functionality for
 *     creating, retrieving, and destroying entities in a thread-safe manner. The {@code Entities} class ensures that
 *     each entity is assigned a unique identifier through the use of an {@link AtomicLong}, ensuring no two entities
 *     share the same ID.
 *     <p>
 *     Internally, entities are stored in a map for efficient retrieval and management. Additionally, {@code Entities}
 *     offers specialised methods for creating entities with specific configurations, such as those represented by 
 *     points or vectors (e.g., {@link Vector2D}). This separation of concerns keeps the entity management clean and
 *     extensible while delegating component management to the {@link ComponentManager}.
 *     </p>
 *   </li>
 *   <li>
 *     <b>EntityType</b> - 
 *     Defines and categorises different types of entities within the JellySmash game. Each {@code EntityType} is associated
 *     with a set of {@link Component} classes, which determine the entity's behaviour and properties. The design allows
 *     for efficient filtering, grouping, and management of entities within the ECS by associating them with specific
 *     components.
 *     <p>
 *     For example, the {@code SOLID_BALL} entity type is linked with both the {@link PointMass} and {@link Circle} components,
 *     making it straightforward to filter and manage all ball-type entities within the game.
 *     </p>
 *   </li>
 * </ul>
 *
 * <h2>Component Management</h2>
 * <p>
 * Component management in this package is centralised through the {@code ComponentManager} class, which is package-private,
 * ensuring that only the {@code Entity} class can directly interact with it. By keeping the component management tightly 
 * coupled with entities, the design encourages encapsulation and reduces the risk of errors related to component handling.
 * </p>
 * 
 * <h2>Thread-Safe Entity Management</h2>
 * <p>
 * The {@code Entities} utility ensures that entity creation is thread-safe by using an {@link AtomicLong} for unique ID generation.
 * This approach guarantees that entity IDs are unique across the system, even in concurrent environments, and allows for
 * efficient retrieval of entities from an internal map, facilitating large-scale simulations.
 * </p>
 *
 * <h2>Entity Types in JellySmash</h2>
 * <p>
 * The {@code EntityType} enum categorises entities by the components they possess. Each {@code EntityType} is linked with specific
 * {@link Component} classes that define the entity's characteristics. For instance, the {@code SOLID_BALL} entity type
 * combines the {@link PointMass} and {@link Circle} components to represent solid balls in the game. This design allows for 
 * easy filtering of entities, improving the efficiency of system interactions within the ECS.
 * </p>
 */
package com.slinky.physics.base;