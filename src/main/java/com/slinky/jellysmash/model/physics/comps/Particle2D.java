package com.slinky.jellysmash.model.physics.comps;

/**
 * The {@code Particle2D} class provides a concrete implementation of the
 * {@link Particle} interface, representing a 2-dimensional particle in a
 * physics simulation.
 *
 * <p>
 * This class encapsulates the core physical properties of a particle, including
 * its mass, damping coefficient, restitution, position, velocity, acceleration,
 * and whether it is static. These properties are crucial for simulating
 * realistic physical behaviours, such as motion, collisions, and force
 * interactions, within a 2D space.
 * </p>
 *
 * <p>
 * The {@code Particle2D} class is designed to be flexible and extensible,
 * allowing it to be used in various types of physics simulations, from simple
 * point masses to more complex physical objects. The class ensures that all
 * physical properties are initialised with valid, non-negative values, throwing
 * an exception if any invalid values are provided.
 * </p>
 *
 * <p>
 * This class is essential for implementing a 2D physics engine using the Entity
 * Component System (ECS) pattern, as it provides the basic building blocks for
 * creating and manipulating physical entities within the simulation. By
 * adhering to the {@link Particle} interface, {@code Particle2D} ensures
 * compatibility and interoperability with other components and systems within
 * the ECS framework.
 * </p>
 *
 * @version 1.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see Particle
 * @see Position
 * @see Vector
 * @see Component
 */
public class Particle2D implements Particle {

    // ============================== Fields ================================ //
    /**
     * The mass of the particle, representing the amount of matter it contains.
     *
     * <p>
     * The mass is a fundamental property in physics, influencing how the
     * particle responds to forces. A greater mass results in greater inertia,
     * meaning the particle will be less affected by applied forces. The mass
     * must be a non-negative value, and it is validated upon initialisation to
     * ensure that it conforms to this requirement.
     * </p>
     */
    private double mass;

    /**
     * The damping coefficient of the particle, controlling the rate at which
     * its motion decreases over time.
     *
     * <p>
     * Damping simulates the effects of friction or resistance, gradually
     * reducing the particle's velocity and bringing it to rest. A higher
     * damping coefficient results in faster decay of motion. This value must be
     * non-negative and is validated during initialisation.
     * </p>
     */
    private double dampCoef;

    /**
     * The restitution (bounciness) of the particle, determining how much
     * kinetic energy is conserved during collisions.
     *
     * <p>
     * Restitution affects how the particle behaves upon impact with other
     * objects. A value of 1.0 indicates a perfectly elastic collision where no
     * energy is lost, while a value of 0 means the particle does not bounce and
     * all energy is absorbed. This value must be non-negative and is checked
     * during initialisation.
     * </p>
     */
    private double restt;

    /**
     * A flag indicating whether the particle is static (immovable).
     *
     * <p>
     * A static particle does not respond to forces or acceleration, remaining
     * fixed in place within the simulation. This is useful for representing
     * objects like walls or anchors that interact with other entities but do
     * not move themselves. If {@code true}, the particle is static and
     * immovable; if {@code false}, it behaves as a dynamic, movable particle.
     * </p>
     */
    private boolean isStatic;

    /**
     * The position of the particle in the simulation space, represented as a
     * {@link Position}.
     *
     * <p>
     * The position defines the current location of the particle in the 2D space
     * and is essential for determining its interactions with other entities and
     * boundaries within the simulation. The position is typically initialised
     * at the creation of the particle and can be updated during the simulation
     * as the particle moves.
     * </p>
     */
    private Position position;

    /**
     * The velocity of the particle, represented as a {@link Vector}.
     *
     * <p>
     * The velocity indicates the speed and direction of the particle's movement
     * within the simulation space. It is crucial for determining the particle's
     * future position and interaction with other entities. The velocity is
     * typically set during initialisation or dynamically adjusted during the
     * simulation as forces are applied.
     * </p>
     */
    private Vector velocity;

    /**
     * The acceleration of the particle, represented as a {@link Vector}.
     *
     * <p>
     * The acceleration represents the rate of change of velocity over time and
     * is influenced by the forces acting on the particle. It is a key factor in
     * simulating realistic motion, as it dictates how quickly the particle
     * speeds up or slows down. The acceleration is typically influenced by
     * external forces like gravity or user-applied forces in the simulation.
     * </p>
     */
    private Vector acceleration;

    /**
     * The resultant force vector representing the cumulative effect of all
     * forces acting on this particle within the current physics tick.
     *
     * <p>
     * This vector accumulates all forces applied to the particle during a
     * single simulation tick, including gravitational, applied, and interaction
     * forces. It is reset at the beginning of each tick to accurately reflect
     * the forces acting on the particle within that specific time frame.
     * </p>
     */
    private Vector actingForce;

    // =========================== Constructors ============================= //
    /**
     * Constructs a new {@code Particle2D} with specified position, velocity,
     * acceleration, mass, damping coefficient, restitution, and static flag.
     *
     * <p>
     * This constructor allows for full customization of the particle's physical
     * properties, making it ideal for scenarios where precise control over the
     * particle's behaviour is required. All physical properties are validated
     * to ensure they are non-negative. This constructor is particularly useful
     * when initialising particles with specific attributes in a physics
     * simulation.
     * </p>
     *
     * @param position the initial position of the particle in the simulation
     * space, represented as a {@link Position}.
     * @param velocity the initial velocity of the particle, determining its
     * speed and direction of movement, represented as a {@link Vector}.
     * @param acceleration the initial acceleration of the particle,
     * representing the rate of change of velocity, represented as a
     * {@link Vector}.
     * @param mass the mass of the particle, which must be a non-negative value.
     * @param damping the damping coefficient, controlling how quickly the
     * particle's motion decays, which must be a non-negative value.
     * @param restitution the restitution (bounciness) of the particle,
     * determining how much kinetic energy is conserved in collisions, which
     * must be a non-negative value.
     * @param isStatic a flag indicating whether the particle is static
     * (immovable). If {@code true}, the particle will not respond to forces or
     * acceleration.
     *
     * @throws IllegalArgumentException if mass, damping, or restitution are
     * negative.
     */
    public Particle2D(Position position, Vector velocity, Vector acceleration, double mass, double damping, double restitution, boolean isStatic) {
        throwIfNonPositive("mass", mass);
        throwIfNegative("dampining coefficient", damping);
        throwIfNegative("restitution", restitution);

        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.actingForce = new Vector2D(0, 0);

        this.mass = mass;
        this.dampCoef = damping;
        this.restt = restitution;
        this.isStatic = isStatic;
    }

    /**
     * Constructs a new {@code Particle2D} with a specified mass and default
     * values for other physical properties.
     *
     * <p>
     * This constructor initialises a static particle with the given mass and
     * default values for position, velocity, and acceleration. The particle's
     * damping coefficient and restitution are set to zero, indicating no decay
     * in motion and no bounciness, respectively. The position is initialised at
     * the origin (0, 0), and both the velocity and acceleration vectors are
     * initialised to zero, indicating no movement or change in movement. This
     * constructor is particularly useful for creating simple, static particles
     * or objects that do not interact dynamically within the simulation.
     * </p>
     *
     * @param mass the mass of the particle, which must be a non-negative value.
     *
     * @throws IllegalArgumentException if mass is negative.
     */
    public Particle2D(double mass) {
        this(
                new Position2D(0, 0),
                new Vector2D(0, 0),
                new Vector2D(0, 0),
                mass, 0, 0, false
        );
    }

    /**
     * Constructs a new {@code Particle2D} with a specified mass and static
     * flag, initialising the particle's position at the origin and setting its
     * velocity and acceleration to zero.
     *
     * <p>
     * This constructor provides a convenient way to create a particle with
     * default motion parameters, while allowing for the specification of mass
     * and whether the particle is static. The position is initialised at (0,
     * 0), and both the velocity and acceleration vectors are set to zero,
     * indicating no movement or change in movement. The damping coefficient and
     * restitution are set to zero, meaning the particle experiences no decay in
     * motion and does not bounce.
     * </p>
     *
     * <p>
     * This constructor is useful for quickly creating particles that are either
     * static or dynamic with a specified mass, but do not require initial
     * movement or complex interactions in the simulation.
     * </p>
     *
     * @param mass the mass of the particle, which must be a non-negative value.
     * @param isStatic a flag indicating whether the particle is static
     * (immovable). If {@code true}, the particle will not respond to forces or
     * acceleration.
     *
     * @throws IllegalArgumentException if mass is negative.
     */
    public Particle2D(double mass, boolean isStatic) {
        this(
                new Position2D(0, 0),
                new Vector2D(0, 0),
                new Vector2D(0, 0),
                mass, 0, 0, isStatic
        );
    }

    // ============================== Getters =============================== //
    /**
     * {@inheritDoc}
     */
    @Override
    public double getMass() {
        return mass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getVelocity() {
        return velocity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getAcceleration() {
        return acceleration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getActingForce() {
        return actingForce;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDampingCoefficient() {
        return dampCoef;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRestitution() {
        return restt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStatic() {
        return isStatic;
    }

// ============================== Setters =============================== //
    /**
     * {@inheritDoc}
     */
    @Override
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDampingCoefficient(double damping) {
        this.dampCoef = damping;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRestitution(double restitution) {
        this.restt = restitution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    // ========================== Helper Methods ============================ //
    /**
     * Throws an {@link IllegalArgumentException} if the specified field is
     * negative.
     *
     * @param varName the name of the variable to include in the exception
     * message
     * @param field the value to check; if this value is less than 0, an
     * exception is thrown
     * @throws IllegalArgumentException if the field is negative
     */
    private void throwIfNegative(String varName, double field) {
        if (field < 0) {
            throw new IllegalArgumentException(varName + " cannot be negative (" + field + ")");
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if the specified field is
     * non-positive.
     *
     * @param varName the name of the variable to include in the exception
     * message
     * @param field the value to check; if this value is less than or equal to
     * 0, an exception is thrown
     * @throws IllegalArgumentException if the field is non-positive
     */
    private void throwIfNonPositive(String varName, double field) {
        if (field <= 0) {
            throw new IllegalArgumentException(varName + " must be positive (" + field + ")");
        }
    }

}