package com.slinky.jellysmash.model.physics.comps;

/**
 * The {@code Vector2D} class is a concrete implementation of the {@link Vector}
 * interface, representing a 2-dimensional vector in a Cartesian coordinate
 * system.
 * 
 * <p>
 * This class provides methods for manipulating the vector's x and y components,
 * performing arithmetic operations such as addition and scaling, and
 * calculating vector properties like magnitude and normalisation. It is useful
 * in a wide variety of contexts, including physics simulations, computer
 * graphics, and game development.
 * </p>
 * 
 * <p>
 * The {@code Vector2D} class is specifically designed to adhere to the
 * principles of Data-Oriented Programming (DOP), enabling the organisation of
 * vector data into contiguous memory blocks. By structuring vector data in this
 * way, the physics engine can achieve enhanced cache efficiency, leading to
 * improved performance, particularly in computation-heavy scenarios. This
 * design decision allows the engine to process large volumes of vector data,
 * such as forces or velocities, more efficiently by reducing cache misses and
 * optimising memory access patterns.
 * </p>
 * 
 * @version 1.0
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 *
 * @see     Vector
 * @see     Position
 * @see     Component
 */
public class Vector2D extends Position2D implements Vector {

    public Vector2D(double x, double y) {
        super(x, y);
    }

}