package com.slinky.jellysmash.model.physics;

/**
 * Marker interface for all components in the Entity-Component-System (ECS)
 * architecture.
 * 
 * <p>
 * This interface is used to define and identify all component classes within
 * the system. Components implementing this interface represent distinct data
 * stores for various attributes and behaviors of entities. As a marker
 * interface, it serves to ensure type safety by distinguishing component
 * classes from other object types within the system.
 * </p>
 * 
 * <p>
 * Implementing this interface allows the system to manage, categorise, and
 * manipulate component data effectively, supporting the flexible and dynamic
 * composition of entities from various components.
 * </p>
 * 
 * @version  1.0
 * @since    0.0.1
 * @author   Kheagen Haskins
 * @see      ComponentManager
 */
public interface Component {}