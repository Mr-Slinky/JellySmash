package com.slinky.jellysmash.model.physics.base;

import com.slinky.jellysmash.model.physics.comps.Component;
import java.util.Map;

/**
 * Represents a unique entity within the JellySmash backend ECS system that is
 * associated with various {@link Component} implementations upon instantiation.
 * The creation and management of entities are handled by the {@link Entities}
 * utility class, which provides functionality for creating, retrieving, and
 * destroying entities within the system.
 *
 * <p>
 * This class maintains a reference to the {@link ComponentManager} singleton
 * instance, ensuring that all component-related operations are centralised and
 * managed efficiently. The {@code ComponentManager} is package-private, which
 * restricts its direct access to the {@code Entity} class, enforcing a tight
 * coupling between these two classes. This design ensures that component
 * management is handled exclusively through the {@code Entity} class, promoting
 * encapsulation and reducing the potential for mismanagement of components.
 * </p>
 *
 * <p>
 * The {@code ComponentManager} class provides a centralised mechanism for
 * adding, accessing, and removing components tied to specific entities. When
 * component-related methods are called on an {@code Entity}, the entity passes
 * a reference to itself to the {@code ComponentManager}, allowing components to
 * be managed in a way that appears as if each entity has its own component
 * manager. This design simplifies the interaction with components, allowing
 * them to be accessed directly from the {@code Entity} instance while
 * maintaining a clear separation of concerns between entity management and
 * component management.
 * </p>
 *
 * <p>
 * By centralising entity and component management within a single
 * {@code ComponentManager} instance, this design ensures that all entities
 * share a common mechanism for managing components, optimising resource usage
 * and enhancing the performance of the system.
 * </p>
 *
 * @version 3.0
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 *
 * @see     Entities
 * @see     Component
 * @see     ComponentManager
 */
public final class Entity {

    // ============================== Static ================================ //
    /**
     * Made static so that a single instance exists at a time, optimising the
     * locality of the components.
     */
    private static final ComponentManager componentManager = new ComponentManager();

    // ============================== Fields ================================ //
    /**
     * A unique identifier assigned to each {@code Entity} instance.
     * <p>
     * The {@code ID} is a long value that uniquely identifies the entity within
     * the system. This ID is immutable and is set during the construction of
     * the entity. It is intended to be globally unique, ensuring that no two
     * entities share the same identifier.
     * </p>
     *
     * <p>
     * The ID is assigned by the {@link Entities} at the time of creation and
     * cannot be modified afterwards, reinforcing the integrity of the entity's
     * identity.
     * </p>
     */
    private final long ID;

    /**
     * Constructs an {@code Entity} with the specified unique identifier.
     * <p>
     * This constructor is package-private to restrict the creation of entities
     * to the {@link Entities} class only. By passing the responsibility of
     * generating and assigning the ID to {@link Entities}, this design ensures
     * that each {@code Entity} is assigned a unique and consistent identifier
     * at the time of its creation.
     * </p>
     *
     * @param id The unique identifier to be assigned to this entity. This value
     * is managed and provided by {@link Entities}.
     */
    Entity(long id) {
        this.ID = id;
    }

    /**
     * Retrieves the unique identifier of this {@code Entity}.
     * <p>
     * The {@code id} method returns the immutable ID that was assigned to this
     * entity at the time of its creation. This ID is used to uniquely identify
     * the entity within the system, allowing it to be referenced, stored, and
     * retrieved reliably.
     * </p>
     *
     * @return The unique identifier of this entity.
     */
    public long id() {
        return ID;
    }

    /**
     * Retrieves a component of a specified type from this entity.
     * <p>
     * This method delegates to the {@link ComponentManager} to access the
     * component associated with this entity and of the specified type. It
     * returns the component if it exists, or {@code null} if the component of
     * that type is not present for this entity. The use of generics ensures
     * that the retrieved component is correctly typed, reducing runtime errors.
     * </p>
     * <p>
     * <b>Example Usage:</b>
     * <pre><code>
     *     // Retrieve a Vector2D component from the entity
     *     Vector2D vector = entity.getComponent(Vector2D.class);
     * </code></pre>
     * </p>
     *
     * @param componentClass the class of the component to retrieve
     * @param <T> the type of the component
     * @return the component of the specified type, or {@code null} if no such
     * component exists for this entity
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        return componentManager.getComponent(this, componentClass);
    }

    /**
     * Adds a component to this entity.
     * <p>
     * This method delegates to the {@link ComponentManager} to associate the
     * provided component with this entity. If a component of the same type is
     * already associated with the entity, an exception will be thrown. This
     * method ensures that each entity only has a single instance of a specific
     * component type, preventing accidental overwriting.
     * </p>
     * <p>
     * <b>Example Usage:</b>
     * <pre><code>
     *     // Create a Vector2D component
     *     Vector2D vector = new Vector2D(5, 10);
     *
     *     // Add the component to the entity
     *     entity.addComponent(vector);
     * </code></pre>
     * </p>
     *
     * @param component the component to add to this entity
     * @param <T> the type of the component
     * @throws IllegalArgumentException if the component or entity is
     * {@code null}
     * @throws IllegalStateException if the entity already has a component of
     * the same type
     */
    public <T extends Component> void addComponent(T component) {
        componentManager.addComponent(this, component);
    }

    /**
     * Removes a component of a specified type from this entity.
     * <p>
     * This method delegates to the {@link ComponentManager} to remove the
     * component associated with this entity and of the specified type. If the
     * component is not found, the method performs no action and returns
     * {@code false}.
     * </p>
     * <p>
     * <b>Example Usage:</b>
     * <pre><code>
     *     // Remove the Vector2D component from the entity
     *     boolean removed = entity.removeComponent(Vector2D.class);
     * </code></pre>
     * </p>
     *
     * @param componentClass the class of the component to remove
     * @param <T> the type of the component
     * @return {@code true} if the component was successfully removed,
     * {@code false} if the component was not found
     */
    public <T extends Component> boolean removeComponent(Class<T> componentClass) {
        return componentManager.removeComponent(this, componentClass);
    }

    /**
     * Checks if this entity has a component of a specified type.
     * <p>
     * This method delegates to the {@link ComponentManager} to determine if the
     * entity contains a component of the given type. It returns {@code true} if
     * the component exists, or {@code false} otherwise. This check is
     * efficient, leveraging the underlying data structure to minimise overhead.
     * </p>
     * <p>
     * <b>Example Usage:</b>
     * <pre><code>
     *     // Check if the entity has a Vector2D component
     *     boolean hasVector = entity.hasComponent(Vector2D.class);
     * </code></pre>
     * </p>
     *
     * @param componentClass the class of the component to check for
     * @param <T> the type of the component
     * @return {@code true} if the entity has the component, {@code false}
     * otherwise
     */
    public <T extends Component> boolean hasComponent(Class<T> componentClass) {
        return componentManager.hasComponent(this, componentClass);
    }

    /**
     * Checks if this entity contains all of the specified component types.
     * <p>
     * This method delegates to the {@link ComponentManager} to determine if the
     * entity contains all of the given component types. It returns {@code true}
     * only if the entity has all the specified components; otherwise, it
     * returns {@code false}.
     * </p>
     *
     * <p>
     * <b>Example Usage:</b>
     * <pre><code>
     *     // Check if the entity has both a Vector2D and Particle2D component
     *     boolean hasAll = entity.hasComponents(Vector2D.class, Particle2D.class);
     *
     *     if (hasAll) {
     *     // Proceed with logic requiring both components
     *     }
     * </code></pre>
     * </p>
     *
     * @param componentClasses The classes of the components to check for
     * @param <T> the type of the components
     * @return {@code true} if the entity has all the specified components,
     * {@code false} otherwise
     */
    public <T extends Component> boolean hasComponents(Class<T>... componentClasses) {
        return componentManager.hasComponents(this, componentClasses);
    }

    /**
     * Removes all components associated with this entity.
     * <p>
     * This method delegates to the {@link ComponentManager} to remove all
     * components that are associated with this entity. It clears all component
     * data for the entity, effectively resetting it to a state without any
     * components.
     * </p>
     * <p>
     * <b>Example Usage:</b>
     * <pre><code>
     *     // Remove all components from the entity
     *     entity.removeAllComponents();
     * </code></pre>
     * </p>
     *
     * @see ComponentManager#removeAllComponents(Entity)
     */
    public void removeAllComponents() {
        componentManager.removeAllComponents(this);
    }

    /**
     * Retrieves all components associated with this entity.
     * <p>
     * This method delegates to the {@link ComponentManager} to obtain a map of
     * all components associated with this entity. The returned map contains the
     * components keyed by their class types. If the entity has no components,
     * an empty map is returned.
     * </p>
     * <p>
     * <b>Example Usage:</b>
     * <pre>{@code
     *     // Retrieve all components of the entity
     *     Map<Class<? extends Component>, Component> componentsMap = entity.getAllComponents();
     *
     *     // Process each component
     *     for (Map.Entry<Class<? extends Component>, Component> entry : componentsMap.entrySet()) {
     *         // Handle components
     *     }
     * }</pre>
     * </p>
     *
     * @return a map where the keys are the classes of the components and the
     * values are the component instances associated with this entity. If the
     * entity has no components, an empty map is returned
     *
     * @see ComponentManager#getAllComponents(Entity)
     */
    public Map<Class<? extends Component>, Component> getAllComponents() {
        return componentManager.getAllComponents(this);
    }

}