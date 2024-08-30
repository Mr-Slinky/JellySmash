package com.slinky.jellysmash.model.physics.comps;

/**
 * The {@code Vector} interface represents a mathematical vector in a
 * two-dimensional space, encapsulating both an x-coordinate and a y-coordinate.
 * This interface extends the {@link Component} interface, making it a part of
 * the component-based architecture used in the Entity Component System (ECS)
 * design pattern.
 * 
 * <p>
 * Vectors are fundamental in physics simulations, providing a way to represent
 * directions, velocities, accelerations, and forces. Implementing classes are
 * expected to manage the state of the vector, offering methods to retrieve and
 * modify the x and y coordinates.
 * </p>
 *
 * @see Component
 * @see Position
 * @see Vector2D
 */
public interface Vector extends Component {

    // ============================== Getters =============================== //
    /**
     * Retrieves the x-coordinate of the vector.
     * <p>
     * The x-coordinate represents the horizontal component of the vector in a
     * two-dimensional Cartesian coordinate system.
     * </p>
     *
     * @return the x-coordinate of the vector.
     */
    double getX();

    /**
     * Retrieves the y-coordinate of the vector.
     * <p>
     * The y-coordinate represents the vertical component of the vector in a
     * two-dimensional Cartesian coordinate system.
     * </p>
     *
     * @return the y-coordinate of the vector.
     */
    double getY();

    // ============================== Setters =============================== //
    /**
     * Sets the x-coordinate of the vector.
     * <p>
     * This method allows modification of the horizontal component of the
     * vector. It's typically used to adjust the vector's direction or magnitude
     * in simulations where the x-component plays a role.
     * </p>
     *
     * @param x the new x-coordinate value to be set for this vector.
     */
    void setX(double x);

    /**
     * Sets the y-coordinate of the vector.
     * <p>
     * This method allows modification of the vertical component of the vector.
     * It's typically used to adjust the vector's direction or magnitude in
     * simulations where the y-component plays a role.
     * </p>
     *
     * @param y the new y-coordinate value to be set for this vector.
     */
    void setY(double y);

    /**
     * Sets both the x and y coordinates of the vector.
     * <p>
     * This method provides a convenient way to update both components of the
     * vector simultaneously, which can be more efficient than setting each
     * component individually. It's particularly useful in scenarios where a
     * vector's direction or magnitude needs to be recalculated based on new
     * data.
     * </p>
     *
     * @param x the new x-coordinate value to be set.
     * @param y the new y-coordinate value to be set.
     */
    void setComponents(double x, double y);

}