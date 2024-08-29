package com.slinky.jellysmash.model.physics.systems;

/**
 * The {@code System} interface serves as a marker interface for all systems
 * within the Entity Component System (ECS) architecture.
 *
 * <p>
 * In the context of an ECS, a system is responsible for processing entities
 * that possess a specific set of components. The {@code System} interface
 * provides a common type for all systems, allowing them to be managed and
 * operated upon collectively. By implementing this interface, a class is
 * recognised as a system within the ECS framework.
 * </p>
 *
 * <p>
 * As a marker interface, {@code System} does not define any methods. Its
 * primary purpose is to provide a type that identifies a class as a system in
 * the ECS pattern, facilitating type-safe operations and system management.
 * </p>
 *
 * @version 1.0
 * @since   0.0.1
 *
 * @author Kheagen Haskins
 *
 * @see    Component
 * @see    Entity
 */
public interface System {}