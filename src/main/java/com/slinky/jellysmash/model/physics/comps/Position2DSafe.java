package com.slinky.jellysmash.model.physics.comps;

/**
 * The {@code Position2DSafe} class provides a thread-safe implementation of the
 * {@link Position2D} class. This class is designed to be used in environments
 * where multiple threads may concurrently access and modify the same position
 * data. By synchronising access to the {@code x} and {@code y} coordinates,
 * {@code Position2DSafe} ensures that all operations on these coordinates are
 * atomic, preventing race conditions and ensuring data consistency.
 * 
 * <p>
 * It is important to note that the use of synchronisation mechanisms introduces
 * a slight performance overhead compared to its non-thread-safe counterpart,
 * {@link Position2D}. As a result, {@code Position2DSafe} should be used in
 * scenarios where thread safety is a priority, and the minor performance
 * trade-off is acceptable.
 * </p>
 * 
 * <p>
 * This class is suitable for use in multithreaded environments such as
 * real-time physics simulations, games, or any application where position data
 * is frequently updated and accessed by multiple threads.
 * </p>
 *
 * @version 1.0
 * @since   0.0.1
 *
 * @author Kheagen Haskins
 * 
 * @see Position
 * @see Component
 * @see Position2D
 */
public class Position2DSafe extends Position2D {
    // =========================== Constructors ============================= //

    /**
     * Constructs a new {@code Position2DSafe} instance with the specified
     * {@code x} and {@code y} coordinates.
     *
     * @param x the initial x coordinate of the position
     * @param y the initial y coordinate of the position
     */
    public Position2DSafe(double x, double y) {
        super(x, y);
    }

    // ============================== Getters =============================== //
    /**
     * Returns the current value of the x coordinate in a thread-safe manner.
     * The access to this method is synchronised, ensuring that the value
     * returned is the most recent value set by any thread.
     *
     * @return the current x coordinate
     */
    @Override
    public synchronized double getX() {
        return super.getX();
    }

    /**
     * Returns the current value of the y coordinate in a thread-safe manner.
     * The access to this method is synchronised, ensuring that the value
     * returned is the most recent value set by any thread.
     *
     * @return the current y coordinate
     */
    @Override
    public synchronized double getY() {
        return super.getY();
    }

    // ============================== Setters =============================== //
    /**
     * Sets the x coordinate in a thread-safe manner. The access to this method
     * is synchronised, ensuring that no other thread can simultaneously modify
     * the x coordinate while it is being updated.
     *
     * @param x the new x coordinate
     */
    @Override
    public synchronized void setX(double x) {
        super.setX(x);
    }

    /**
     * Sets the y coordinate in a thread-safe manner. The access to this method
     * is synchronised, ensuring that no other thread can simultaneously modify
     * the y coordinate while it is being updated.
     *
     * @param y the new y coordinate
     */
    @Override
    public synchronized void setY(double y) {
        super.setY(y);
    }

    /**
     * Sets both the x and y coordinates in a thread-safe manner. The access to
     * this method is synchronised, ensuring that both coordinates are updated
     * atomically, preventing other threads from seeing an intermediate state
     * where only one of the coordinates has been updated.
     *
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    @Override
    public synchronized void setPosition(double x, double y) {
        super.setPosition(x, y);
    }

}