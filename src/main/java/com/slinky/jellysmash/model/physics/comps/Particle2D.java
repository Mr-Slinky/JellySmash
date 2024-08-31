package com.slinky.jellysmash.model.physics.comps;

import com.slinky.jellysmash.model.physics.Component;

/**
 * Represents a 2-dimensional particle in the physics engine.
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
 * adhering to the {@link Particle2D} interface, {@code Particle2D} ensures
 * compatibility and interoperability with other components and systems within
 * the ECS framework.
 * </p>
 *
 * @version 1.0
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 *
 * @see     Particle2D
 * @see     Vector2D
 * @see     Component
 */
public class Particle2D implements Component {

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
    private double restitution;

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
    private Vector2D position;

    /**
     * The velocity of the particle, represented as a {@link Vector2D}.
     *
     * <p>
     * The velocity indicates the speed and direction of the particle's movement
     * within the simulation space. It is crucial for determining the particle's
     * future position and interaction with other entities. The velocity is
     * typically set during initialisation or dynamically adjusted during the
     * simulation as forces are applied.
     * </p>
     */
    private Vector2D velocity;

    /**
     * The acceleration of the particle, represented as a {@link Vector2D}.
     *
     * <p>
     * The acceleration represents the rate of change of velocity over time and
     * is influenced by the forces acting on the particle. It is a key factor in
     * simulating realistic motion, as it dictates how quickly the particle
     * speeds up or slows down. The acceleration is typically influenced by
     * external forces like gravity or user-applied forces in the simulation.
     * </p>
     */
    private Vector2D acceleration;

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
    private Vector2D actingForce;

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
     * speed and direction of movement, represented as a {@link Vector2D}.
     * @param acceleration the initial acceleration of the particle,
     * representing the rate of change of velocity, represented as a
     * {@link Vector2D}.
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
    public Particle2D(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double damping, double restitution, boolean isStatic) {
        throwIfNonPositive("mass", mass);
        throwIfOutOfRange("dampining coefficient", damping, 0, 1);
        throwIfOutOfRange("restitution", restitution, 0, 1);

        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.actingForce = new Vector2D(0, 0);

        this.mass = mass;
        this.dampCoef = damping;
        this.restitution = restitution;
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
                new Vector2D(0, 0),
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
                new Vector2D(0, 0),
                new Vector2D(0, 0),
                new Vector2D(0, 0),
                mass, 0, 0, isStatic
        );
    }

    // ============================== Getters =============================== //
    /**
     * Retrieves the mass of the particle.
     * <p>
     * The mass is a fundamental property of the particle, determining its
     * resistance to acceleration when a force is applied. It is typically
     * measured in kilograms (kg) and is a non-negative value.
     * </p>
     *
     * @return The mass of the particle.
     */
    public double getMass() {
        return mass;
    }

    /**
     * Retrieves the current position of the particle in 2D space.
     * <p>
     * The position is represented as a {@link Vector2D}, which contains the x
     * and y coordinates of the particle. This determines the particle's
     * location in the simulation or environment.
     * </p>
     *
     * @return The current position of the particle as a {@link Vector2D}.
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * Retrieves the current velocity of the particle.
     * <p>
     * The velocity is represented as a {@link Vector2D}, indicating the speed
     * and direction of the particle's motion. It is the rate of change of the
     * particle's position with respect to time.
     * </p>
     *
     * @return The current velocity of the particle as a {@link Vector2D}.
     */
    public Vector2D getVelocity() {
        return velocity;
    }

    /**
     * Retrieves the current acceleration of the particle.
     * <p>
     * The acceleration is represented as a {@link Vector2D}, reflecting the
     * rate of change of the particle's velocity with respect to time. It is
     * directly influenced by the forces acting on the particle and its mass.
     * </p>
     *
     * @return The current acceleration of the particle as a {@link Vector2D}.
     */
    public Vector2D getAcceleration() {
        return acceleration;
    }

    /**
     * Retrieves the net force currently acting on the particle.
     * <p>
     * The acting force is represented as a {@link Vector2D} and is the sum of
     * all forces applied to the particle. According to Newton's Second Law of
     * Motion, this force determines the particle's acceleration based on its
     * mass.
     * </p>
     *
     * @return The net force acting on the particle as a {@link Vector2D}.
     */
    public Vector2D getActingForce() {
        return actingForce;
    }

    /**
     * Retrieves the damping coefficient of the particle.
     * <p>
     * The damping coefficient is a scalar value that represents the resistive
     * force acting against the particle's velocity. This is often used to
     * simulate friction or air resistance in a simulation.
     * </p>
     *
     * @return The damping coefficient of the particle.
     */
    public double getDampingCoefficient() {
        return dampCoef;
    }

    /**
     * Retrieves the restitution coefficient of the particle.
     * <p>
     * The restitution coefficient, also known as the coefficient of restitution
     * (CoR), is a scalar value that determines the elasticity of collisions. A
     * value of 1.0 represents a perfectly elastic collision, while a value of
     * 0.0 represents a perfectly inelastic collision.
     * </p>
     *
     * @return The restitution coefficient of the particle.
     */
    public double getRestitution() {
        return restitution;
    }

    /**
     * Checks if the particle is static.
     * <p>
     * A static particle is one that does not move, regardless of forces acting
     * upon it. This may be used to represent fixed objects or boundaries in the
     * simulation.
     * </p>
     *
     * @return {@code true} if the particle is static and immovable,
     * {@code false} otherwise.
     */
    public boolean isStatic() {
        return isStatic;
    }

    // ============================== Setters =============================== //
    /**
     * Sets the mass of the particle.
     * <p>
     * The mass is a fundamental property of the particle that determines its
     * resistance to acceleration when a force is applied. It is typically
     * measured in kilograms (kg) and should be a non-negative value. Setting
     * the mass to zero or a very small value could result in unrealistic
     * behaviour in the simulation.
     * </p>
     *
     * @param mass The new mass of the particle. Must be non-negative.
     */
    public void setMass(double mass) {
        throwIfNonPositive("mass", mass);
        this.mass = mass;
    }

    /**
     * Sets the damping coefficient of the particle.
     * <p>
     * The damping coefficient is a scalar value representing the resistive
     * force that acts against the particle's velocity. This is often used to
     * simulate friction or air resistance. Higher values of the damping
     * coefficient will result in greater resistance to motion, effectively
     * slowing the particle down more rapidly.
     * </p>
     *
     * @param damping The new damping coefficient for the particle. Typically a
     * value between 0 and 1.
     */
    public void setDampingCoefficient(double damping) {
        throwIfOutOfRange("dampining coefficient", damping, 0, 1);
        this.dampCoef = damping;
    }

    /**
     * Sets the restitution coefficient of the particle.
     * <p>
     * The restitution coefficient determines the elasticity of collisions. A
     * value of 1.0 indicates a perfectly elastic collision where no kinetic
     * energy is lost, while a value of 0.0 indicates a perfectly inelastic
     * collision where all kinetic energy is lost. This coefficient influences
     * how the particle bounces off other objects in the simulation.
     * </p>
     *
     * @param restitution The new restitution coefficient for the particle.
     * Typically a value between 0 and 1.
     */
    public void setRestitution(double restitution) {
        throwIfOutOfRange("restitution", restitution, 0, 1);
        this.restitution = restitution;
    }

    /**
     * Sets the static state of the particle.
     * <p>
     * A static particle is one that does not move, regardless of any forces
     * acting on it. This can be used to represent fixed objects or boundaries
     * within the simulation. Once set to static, the particle's position will
     * not change unless explicitly modified.
     * </p>
     *
     * @param isStatic {@code true} to make the particle static and immovable,
     * {@code false} to allow it to move freely.
     */
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    // ========================== Helper Methods ============================ //
    /**
     * Validates that a given numerical field is within a specified range, and
     * throws an {@code IllegalArgumentException} if the field is out of range.
     *
     * @param varName The name of the variable or field being validated. This is
     * used in the exception message to identify the offending variable if an
     * exception is thrown. Must not be {@code null} or empty.
     *
     * @param field The numerical value of the field to validate. This value is
     * compared against the specified minimum and maximum bounds (inclusive). It
     * is typically a double to accommodate a wide range of numerical inputs,
     * including those with fractional values.
     *
     * @param min The minimum allowable value (inclusive) for the {@code field}.
     * If {@code field} is less than this value, an
     * {@code IllegalArgumentException} will be thrown. Note that {@code min} is
     * an {@code int}, which implies that the minimum value is integral.
     *
     * @param max The maximum allowable value (inclusive) for the {@code field}.
     * If {@code field} exceeds this value, an {@code IllegalArgumentException}
     * will be thrown. Similar to {@code min}, {@code max} is an {@code int},
     * indicating an integral upper bound.
     *
     * @throws IllegalArgumentException If the {@code field} is less than
     * {@code min} or greater than {@code max}. The exception message will
     * contain {@code varName} to specify which field caused the validation
     * failure and the invalid {@code field} value that triggered the exception.
     *
     * @see IllegalArgumentException
     */
    private void throwIfOutOfRange(String varName, double field, int min, int max) {
        if (field < min || field > max) {
            throw new IllegalArgumentException(varName + " out of range (" + min + " >= " + field + " >= " + max + ")");
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