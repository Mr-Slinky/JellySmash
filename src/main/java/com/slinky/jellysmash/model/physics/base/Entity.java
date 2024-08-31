package com.slinky.jellysmash.model.physics.base;

/**
 * The {@code Entity} class represents a uniquely identifiable object within the
 * system. Each instance of {@code Entity} is assigned a unique identifier at
 * the time of creation, which ensures that every entity can be distinctly
 * identified and managed within the system.
 *
 * <p>
 * The unique identifier for each {@code Entity} is generated and assigned by
 * the external {@link EntityFactory} class. This design enforces the integrity
 * of the identifier, ensuring that no two entities share the same identifier.
 * The {@link EntityFactory} is solely responsible for the creation of
 * {@code Entity} instances, which promotes consistency and centralises control
 * over the entity creation process.
 * </p>
 *
 * <p>
 * The constructor of the {@code Entity} class is deliberately made
 * package-private, restricting direct instantiation by other classes. This
 * design choice ensures that entities can only be created through the
 * {@link EntityFactory}, further enforcing the uniqueness and integrity of the
 * entity identifiers.
 * </p>
 *
 * <p>
 * This class is a core component of the system's architecture, as it provides
 * the foundational building block for entities managed by the system. Entities
 * created using this class are typically managed by the {@link EntityFactory},
 * which handles the lifecycle, storage, and retrieval of entities.
 * </p>
 *
 * @version 2.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see EntityFactory
 */
public final class Entity {

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
     * The ID is assigned by the {@link EntityFactory} at the time of creation
     * and cannot be modified afterwards, reinforcing the integrity of the
     * entity's identity.
     * </p>
     */
    private final long ID;

    /**
     * Constructs an {@code Entity} with the specified unique identifier.
     * <p>
     * This constructor is package-private to restrict the creation of entities
     * to the {@link EntityFactory} class only. By passing the responsibility of
     * generating and assigning the ID to {@link EntityFactory}, this design
     * ensures that each {@code Entity} is assigned a unique and consistent
     * identifier at the time of its creation.
     * </p>
     *
     * @param id The unique identifier to be assigned to this entity. This value
     * is managed and provided by {@link EntityFactory}.
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

}