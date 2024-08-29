package com.slinky.jellysmash.model.physics.comps;

/**
 * A thread-safe subclass of {@link Particle2D} that synchronises access to its
 * methods, ensuring safe use in multi-threaded environments.
 *
 * <p>
 * This class is useful in scenarios where particles are accessed or modified
 * concurrently, providing thread-safe operations for all particle attributes.
 * </p>
 *
 * @version 1.0
 * @since 0.1.0
 * 
 * @author Kheagen Haskins
 * 
 * @see Particle
 * @see Particle2D
 * @see Component
 * @see Position
 * @see Vector
 */
public class Particle2DSafe extends Particle2D {

    // =========================== Constructors ============================= //
    /**
     * {@inheritDoc}
     */
    public Particle2DSafe(Position position, Vector velocity, Vector acceleration, double mass, double damping, double restitution, boolean isStatic) {
        super(position, velocity, acceleration, mass, damping, restitution, isStatic);
    }

    /**
     * {@inheritDoc}
     */
    public Particle2DSafe(double mass) {
        super(mass);
    }

    /**
     * {@inheritDoc}
     */
    public Particle2DSafe(double mass, boolean isStatic) {
        super(mass, isStatic);
    }

    // ============================== Getters =============================== //
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized double getMass() {
        return super.getMass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Position getPosition() {
        return super.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Vector getVelocity() {
        return super.getVelocity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Vector getAcceleration() {
        return super.getAcceleration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized double getDampingCoefficient() {
        return super.getDampingCoefficient();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized double getRestitution() {
        return super.getRestitution();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean isStatic() {
        return super.isStatic();
    }

    // ============================== Setters =============================== //
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setMass(double mass) {
        super.setMass(mass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setDampingCoefficient(double damping) {
        super.setDampingCoefficient(damping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setRestitution(double restitution) {
        super.setRestitution(restitution);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setStatic(boolean isStatic) {
        super.setStatic(isStatic);
    }
}