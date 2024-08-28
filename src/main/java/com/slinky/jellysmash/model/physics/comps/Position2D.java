package com.slinky.jellysmash.model.physics.comps;

/**
 * Represents a 2D position in a Cartesian coordinate system, defined by x and y
 * coordinates. This class serves as a concrete implementation of the
 * {@link Position} interface, which itself extends the {@link Component} marker
 * interface, enabling instances of this class to be used as components
 * within a larger system.
 * <p>
 * The {@code Position2D} class provides precise methods for accessing and
 * modifying the x and y coordinates individually, as well as a convenient
 * method to set both coordinates simultaneously. This makes it suitable for use
 * in various physics or graphics-related contexts where positional data is
 * essential.
 * </p>
 * <p>
 * As an implementer of the {@link Position} interface, {@code Position2D}
 * objects inherently qualify as components, allowing them to be integrated
 * seamlessly into systems or frameworks that rely on the {@link Component}
 * marker interface.
 * </p>
 *
 * @author Kheagen Haskins
 */
public class Position2D implements Position {

    // ============================== Fields ================================ //
    /**
     * The x coordinate of the position.
     */
    private double x;

    /**
     * The y coordinate of the position.
     */
    private double y;

    // =========================== Constructors ============================= //
    /**
     * Constructs a {@code Position2D} with the specified x and y coordinates.
     *
     * @param x the x coordinate of the position
     * @param y the y coordinate of the position
     */
    public Position2D(double x, double y) {
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
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

}