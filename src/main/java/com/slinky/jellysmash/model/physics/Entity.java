package com.slinky.jellysmash.model.physics;

/**
 * Represents an entity with a unique identifier. The identifier is
 * automatically incremented for each new instance of {@code Entity}.
 * <p>
 * This record encapsulates the unique identifier for each entity, ensuring that
 * each entity can be distinctly identified within the system. It uses a static
 * counter to assign increasing IDs automatically to each new entity created
 * through the no-argument constructor.
 *
 * @param id The unique identifier for the entity.
 */
public record Entity(long id) {

    private static int globalID = 0; // Counter to generate unique IDs

    /**
     * Returns the current count of entities created. This count also represents
     * the value of the next unique identifier to be assigned.
     *
     * @return the number of entities created so far.
     */
    public static int getEntityCount() {
        return globalID;
    }

    /**
     * Constructs a new {@code Entity} with an automatically incremented
     * identifier. This constructor increments the internal counter and uses its
     * value as the entity's unique identifier.
     */
    public Entity() {
        this(++globalID);
    }

}