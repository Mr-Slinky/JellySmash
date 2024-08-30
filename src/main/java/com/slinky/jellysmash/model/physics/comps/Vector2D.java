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
    public double getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the position.
     *
     * @return the y coordinate
     */
    public double getY() {
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

}
