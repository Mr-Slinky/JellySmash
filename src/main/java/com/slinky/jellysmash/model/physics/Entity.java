package com.slinky.jellysmash.model.physics;

/**
 * The {@code Entity} class represents a uniquely identifiable entity within the system.
 * 
 * <p>
 * Each instance of {@code Entity} is assigned a unique identifier upon creation. 
 * The unique identifier is managed through a static counter, ensuring that 
 * each entity is distinctly identifiable. This class is particularly useful in 
 * scenarios where entities need to be tracked or managed uniquely within a 
 * system, such as in games, simulations, or data management contexts.
 * </p>
 * 
 * <p>
 * The class leverages the Java record feature, which is a compact and immutable 
 * data structure, making it an ideal choice for representing simple entities 
 * with only an identifier.
 * </p>
 * 
 * <p>
 * This class provides a constructor for creating an entity with a specified 
 * identifier, and it maintains a static counter to keep track of the number of 
 * entities created. Additionally, it provides a static method to retrieve the 
 * current count of entities, which can be useful for various tracking or 
 * debugging purposes.
 * </p>
 * 
 * @version 1.0
 * @since   0.0.1
 * @author  Kheagen Haskins
 * @see     EntityManager
 */
public record Entity(long id) {

    /**
     * A static counter that is used to generate unique identifiers for entities.
     * 
     * <p>
     * This counter is incremented automatically each time a new entity is created 
     * via the default constructor (not provided here). The counter ensures that 
     * each entity has a distinct identifier. The value of this counter represents 
     * the total number of entities that have been instantiated.
     * </p>
     */
    private static long globalIDCounter = 0; // Counter to generate unique IDs

    /**
     * Returns the current number of entities that have been created. 
     * 
     * <p>
     * The value returned by this method is equivalent to the number of 
     * entities that have been instantiated, which also corresponds to 
     * the next unique identifier that will be assigned.
     * </p>
     * 
     * @return the current count of entities created, which represents the 
     *         next unique identifier to be assigned.
     */
    public static long getEntityCount() {
        return globalIDCounter;
    }

    /**
     * Constructs a new {@code Entity} with the specified identifier.
     * 
     * <p>
     * This constructor allows for the creation of an entity with a pre-defined 
     * identifier. This might be useful in scenarios where entities need to be 
     * restored from a persistent state or when specific IDs need to be assigned 
     * for testing or debugging purposes.
     * </p>
     * 
     * @param id the unique identifier to be assigned to this entity. It is 
     *           expected that the identifier is unique within the context of 
     *           the system managing these entities.
     */
    public Entity(long id) {
        this.id = id;
        globalIDCounter++;
    }

}