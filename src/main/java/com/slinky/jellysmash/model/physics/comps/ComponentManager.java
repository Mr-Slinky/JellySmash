package com.slinky.jellysmash.model.physics.comps;

import com.slinky.jellysmash.model.physics.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code ComponentManager} class is responsible for managing the storage,
 * retrieval, and life-cycle of components associated with entities within the
 * Entity-Component-System (ECS) architecture of the JellySmash physics engine.
 * <p>
 * This class provides a centralised mechanism to add, access, and remove
 * components tied to specific entities, enabling flexible and efficient
 * management of entity behaviour. By organising components within a {@link Map}
 * structure, it ensures that each entity's components are stored contiguously
 * in memory, promoting better cache coherence and optimising access patterns,
 * which is crucial for performance in computation-heavy systems.
 * </p>
 * <p>
 * The {@code ComponentManager} adheres to the principles of Data-Oriented
 * Programming (DOP), where the organisation of data is optimised for processing
 * efficiency. This approach is particularly beneficial in large-scale physics
 * simulations where rapid retrieval and modification of component data are
 * essential.
 * </p>
 * <p>
 * Through its generic methods, {@code ComponentManager} provides type-safe
 * operations to interact with components, ensuring that the correct types are
 * used during the addition, retrieval, and removal processes, thus reducing
 * runtime errors and improving code maintainability.
 * </p>
 *
 * @author Kheagen Haskins
 *
 * @see Component
 * @see Entity
 * @see Map
 * @see HashMap
 * 
 */
public class ComponentManager {

    // ============================== Fields ================================ //
    /**
     * A map that associates each entity's unique identifier with a nested map
     * of components.
     * <p>
     * The outer {@code Map<Long, Map<Class<? extends Component>, Component>>}
     * stores components for each entity, identified by the entity's unique
     * {@code Long} ID. The inner map associates each component type with its
     * corresponding component instance, allowing for efficient retrieval and
     * management of an entity's components. This structure supports the
     * organisation of component data in a way that enhances memory locality and
     * access speed, aligning with the principles of Data-Oriented Programming
     * (DOP) for optimised performance.
     */
    private Map<Long, Map<Class<? extends Component>, Component>> components = new HashMap<>();

// ============================ API Methods ============================= //
    /**
     * Adds a component to the specified entity.
     * <p>
     * This method associates the provided component with the specified entity
     * by storing it in the {@code components} map. If the entity does not
     * already have a component map, a new one is created and associated with
     * the entity's ID. The component is then stored in this inner map, keyed by
     * its class type, allowing for efficient retrieval and management.
     * <p>
     * By structuring the component storage in this way, the method ensures that
     * component data is stored contiguously in memory, improving cache
     * coherence and access speed, which is critical in performance-sensitive
     * applications like physics engines.
     *
     * @param entity the entity to which the component should be added
     * @param component the component to add
     * @param <T> the type of the component
     */
    public <T extends Component> void addComponent(Entity entity, T component) {
        Map<Class<? extends Component>, Component> entityComponents = components.computeIfAbsent(entity.id(), k -> new HashMap<>());
        entityComponents.put(component.getClass(), component);
    }

    /**
     * Retrieves a component of a specified type from the specified entity.
     * <p>
     * This method accesses the internal storage map to retrieve a component
     * associated with the given entity and of the specified type. The method
     * returns the component if found, or {@code null} if no component of that
     * type exists for the entity. By leveraging the organised, contiguous data
     * structure, this method ensures efficient retrieval, which is crucial for
     * performance in systems where frequent access to component data is
     * required.
     * <p>
     * The use of generics ensures type safety, allowing the method to cast the
     * retrieved component to the correct type automatically, reducing the risk
     * of runtime errors.
     *
     * @param entity the entity from which to retrieve the component
     * @param componentClass the class of the component to retrieve
     * @param <T> the type of the component
     * @return the component of the specified type, or {@code null} if no such
     * component exists
     */
    public <T extends Component> T getComponent(Entity entity, Class<T> componentClass) {
        Map<Class<? extends Component>, Component> entityComponents = components.get(entity.id());
        if (entityComponents != null) {
            return componentClass.cast(entityComponents.get(componentClass));
        }
        return null;
    }

    /**
     * Removes a component of a specified type from the specified entity.
     * <p>
     * This method removes the component of the specified type from the entity's
     * associated component map. If the entity or the component does not exist,
     * the method performs no action. This removal process is efficiently
     * handled within the underlying data structure, maintaining the integrity
     * of the organised data layout.
     *
     * @param entity the entity from which to remove the component
     * @param componentClass the class of the component to remove
     * @param <T> the type of the component
     */
    public <T extends Component> void removeComponent(Entity entity, Class<T> componentClass) {
        Map<Class<? extends Component>, Component> entityComponents = components.get(entity.id());
        if (entityComponents != null) {
            entityComponents.remove(componentClass);
        }
    }

    /**
     * Checks if the specified entity has a component of a specified type.
     * <p>
     * This method checks whether the entity has a component of the given type
     * by querying the organised component map. It returns {@code true} if the
     * component is found, or {@code false} otherwise. The efficient data
     * structure underlying this method ensures that these checks are performed
     * with minimal overhead, which is essential for maintaining performance in
     * complex systems where such checks may be frequent.
     *
     * @param entity the entity to check
     * @param componentClass the class of the component to check for
     * @param <T> the type of the component
     * @return {@code true} if the entity has the component, {@code false}
     * otherwise
     */
    public <T extends Component> boolean hasComponent(Entity entity, Class<T> componentClass) {
        Map<Class<? extends Component>, Component> entityComponents = components.get(entity.id());
        return entityComponents != null && entityComponents.containsKey(componentClass);
    }

}