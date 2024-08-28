package com.slinky.jellysmash.model.physics.comps;

/**
 * Represents a 2D position with x and y coordinates. This interface defines the
 * methods required for accessing and modifying these coordinates.
 * <p>
 * Implementations of this interface can provide additional functionality or
 * behaviour, but they must at least provide the basic operations defined here.
 * </p>
 * <p>
 * This interface extends the {@link Component} marker interface, so all
 * implementers of {@code Position} will be marked as components.
 * </p>
 *
 * @author Kheagen Haskins
 */
public interface Position extends Component {

    // ============================== Getters =============================== //
    /**
     * Returns the x coordinate of the position.
     *
     * @return the x coordinate
     */
    double getX();

    /**
     * Returns the y coordinate of the position.
     *
     * @return the y coordinate
     */
    double getY();

    // ============================== Setters =============================== //
    /**
     * Sets the x coordinate of the position.
     *
     * @param x the new x coordinate
     */
    void setX(double x);

    /**
     * Sets the y coordinate of the position.
     *
     * @param y the new y coordinate
     */
    void setY(double y);

    /**
     * Sets both the x and y coordinates of the position.
     *
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    void setPosition(double x, double y);

}