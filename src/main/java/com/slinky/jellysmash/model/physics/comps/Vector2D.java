package com.slinky.jellysmash.model.physics.comps;

/**
 * Represents a two-dimensional vector component within the ECS (Entity
 * Component System) design pattern used in the JellySmash game. This class
 * encapsulates the x and y coordinates of a vector, providing basic
 * functionalities to get and set these values.
 *
 * <p>
 * This class is primarily used to represent positional data or movement vectors
 * in the physics calculations of JellySmash. By implementing the
 * {@code Component} interface, it integrates seamlessly into the ECS framework,
 * allowing for flexible manipulation and combination with other components.
 * </p>
 *
 * <p>
 * The {@code Vector2D} class provides constructors for initialising vectors and
 * methods for modifying and retrieving the x and y coordinates. This class is
 * designed to be extendable and reusable across different systems that require
 * two-dimensional vector data, making it a core part of the physics engine in
 * JellySmash.
 * </p>
 *
 * @version 1.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see Vector2D
 * @see Component
 */
public class Vector2D implements Component {

    // ============================== Static ================================ //
    /**
     * A static, immutable instance of {@code Vector2D} representing the zero
     * vector (0, 0). This vector is useful as a default or neutral value in
     * various physics calculations where no movement or displacement is
     * desired. It is also particularly valuable for equality checks and testing
     * scenarios where a consistent and unmodifiable reference to the zero
     * vector is required.
     *
     * <p>
     * Since this vector is immutable, any attempt to modify its components will
     * result in an {@code UnsupportedOperationException}.</p>
     */
    public static final Vector2D ZERO = new ImmutableVector(0, 0);

    // ============================== Fields ================================ //
    /**
     * The x coordinate of the position.
     */
    protected double x;

    /**
     * The y coordinate of the position.
     */
    protected double y;

    // =========================== Constructors ============================= //
    /**
     * Constructs a {@code Position2D} with the specified x and y coordinates.
     *
     * @param x the x coordinate of the position
     * @param y the y coordinate of the position
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // ============================== Getters =============================== //
    /**
     * Returns the x coordinate of the position.
     *
     * @return the x coordinate
     */
    public double x() {
        return x;
    }

    /**
     * Returns the y coordinate of the position.
     *
     * @return the y coordinate
     */
    public double y() {
        return y;
    }

    // ============================== Setters =============================== //
    /**
     * Sets the x coordinate of the position.
     *
     * @param x the new x coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate of the position.
     *
     * @param y the new y coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Sets both the x and y coordinates of the position.
     *
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void setComponents(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // ============================ API Methods ============================= //
    /**
     * Compares this {@code Vector2D} object to the specified {@code Vector2D}
     * object for equality. The vectors are considered equal if both their x and
     * y coordinates are exactly the same.
     *
     * <p>
     * This method performs a direct comparison using the {@code ==} operator,
     * so it does not account for potential floating-point precision issues. It
     * is primarily intended for cases where exact equality is required, such as
     * when comparing vectors with known discrete values.
     * </p>
     *
     * @param otherVector the {@code Vector2D} object to be compared with this
     * vector
     * @return {@code true} if the x and y coordinates of both vectors are
     * equal; {@code false} otherwise
     */
    public boolean equals(Vector2D otherVector) {
        return x == otherVector.x && y == otherVector.y;
    }

    // ============================== Inner Classes ================================ //
    /**
     * A private inner class representing an immutable vector. The
     * {@code ImmutableVector} extends {@code Vector2D} but overrides the setter
     * methods to prevent any modification to its components. Any attempt to
     * change the x or y coordinates of this vector will result in an
     * {@code UnsupportedOperationException}.
     *
     * <p>
     * This class is used to create immutable instances of {@code Vector2D},
     * such as {@link Vector2D#ZERO}, ensuring that they remain constant and
     * unchanged throughout their lifecycle.
     * </p>
     */
    private static class ImmutableVector extends Vector2D {

        /**
         * Constructs an immutable vector with the specified x and y
         * coordinates.
         *
         * @param x the x coordinate of the vector
         * @param y the y coordinate of the vector
         */
        public ImmutableVector(double x, double y) {
            super(x, y);
        }

        @Override
        public void setX(double x) {
            throw new UnsupportedOperationException("Vector2D.ZERO is final and cannot be mutated");
        }

        @Override
        public void setY(double y) {
            throw new UnsupportedOperationException("Vector2D.ZERO is final and cannot be mutated");
        }

        @Override
        public void setComponents(double x, double y) {
            throw new UnsupportedOperationException("Vector2D.ZERO is final and cannot be mutated");
        }
    }

}