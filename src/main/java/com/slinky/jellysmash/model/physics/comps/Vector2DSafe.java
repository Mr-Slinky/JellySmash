package com.slinky.jellysmash.model.physics.comps;

/**
 * The {@code Vector2DSafe} class is a thread-safe subclass of the
 * {@link Vector2D} class, representing a 2-dimensional vector in a Cartesian
 * coordinate system.
 *
 * <p>
 * This class provides thread-safe methods for manipulating the vector's x and y
 * components. It is useful in a wide variety of contexts, including physics
 * simulations, computer graphics, and game development, especially in
 * multithreaded environments.
 * </p>
 *
 * <p>
 * Internally, thread safety is achieved through synchronisation, ensuring that
 * the state of the vector remains consistent even when accessed by multiple
 * threads simultaneously.
 * </p>
 *
 * @version 1.0
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 *
 * @see     Vector
 * @see     Vector2D
 * @see     Component
 * @see     Position2DSafe
 */
public class Vector2DSafe extends Position2DSafe implements Vector {

    /**
     * Constructs a new {@code Vector2D} with the specified x and y components.
     *
     * @param x the x-component of the vector.
     * @param y the y-component of the vector.
     */
    public Vector2DSafe(double x, double y) {
        super(x, y);
    }

}