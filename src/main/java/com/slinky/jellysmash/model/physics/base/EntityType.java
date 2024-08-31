package com.slinky.jellysmash.model.physics.base;

/**
 * The {@code EntityType} enum represents the various types of entities that can
 * exist within the physics system of the Jelly Smash game. Each entity type has
 * distinct characteristics and behaviors that influence how it interacts with
 * other entities and the game environment.
 *
 * <p>
 * The entities defined here are fundamental components within the physics
 * simulation, providing the basis for more complex structures and interactions.
 * This enum may be extended in the future to include additional entity types as
 * the physics system evolves.
 * </p>
 *
 * <p>
 * Example usage:</p>
 * <pre><code>
 *     void someLogicOnEntities(EntityType e) {
 *         switch (e) {
 *             case PARTICLE:
 *                 // Handle particle entity logic
 *                 break;
 *             default:
 *                 throw new UnsupportedOperationException("Unknown entity type: " + entityType);
 *         }
 *     }
 * </code></pre>
 *
 * @version 1.0
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 *
 * @see Entity
 * @see EntityManager
 *
 */
public enum EntityType {

    /**
     * Represents a particle, which is a basic physical body in the system,
     * characterized by properties such as mass, position, velocity, and
     * acceleration.
     * <p>
     * Particles are subject to the laws of physics, such as Newton's laws of
     * motion, and interact with forces and other particles in the simulation.
     * They are the foundation for simulating physical phenomena such as
     * collisions, gravity, and momentum.
     * </p>
     * <p>
     * Use cases for {@code PARTICLE} entities include:</p>
     * <ul>
     * <li>Simulating small objects or particles within the game
     * environment</li>
     * <li>Creating more complex physical bodies by combining multiple
     * particles</li>
     * <li>Modelling the effects of forces and interactions over time</li>
     * </ul>
     */
    PARTICLE;

    // Additional entity types may be added here as the physics system is expanded.
}