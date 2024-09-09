package com.slinky.physics.comps;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Represents a 2-dimensional point mass within the physics ECS, providing a
 * fluid API for flexible interaction.
 *
 * <p>
 * The {@code PointMass} class models a particle in a 2D physics simulation,
 * encapsulating key physical properties such as mass, position, velocity,
 * acceleration, damping, and restitution. Designed according to the Entity
 * Component System (ECS) pattern, it serves as a versatile and reusable
 * component for a physics engine, supporting simulations that involve motion,
 * force accumulation, and collision handling.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *     <li><b>Fluent API:</b> Supports method chaining for configuring and
 *     manipulating the particle’s state, allowing for streamlined interactions
 *     (e.g., <code>setMass()</code>, <code>move()</code>,
 *     <code>addForce()</code>).</li>
 *     <li><b>Momentum and Kinetic Energy:</b> Provides methods to calculate the
 *     particle's momentum and kinetic energy based on its mass and velocity, useful
 *     for dynamic simulations.</li>
 *     <li><b>Restitution:</b> Allows configuration of elasticity and friction to
 *     simulate how the particle bounces and slows down after collisions, enhancing
 *     realism.</li>
 *     <li><b>Terminal Velocity:</b> Implements limits on the particle’s velocity,
 *     ensuring realistic behaviour when exposed to forces like gravity and air
 *     resistance.</li>
 * </ul>
 *
 * <p>
 * The class provides a set of static factory methods using the {@code of()}
 * pattern, offering multiple pathways to create {@code PointMass} objects with
 * varying configurations. These factory methods simplify object creation by
 * allowing users to specify only the necessary properties, while default values
 * are applied where appropriate.
 * </p>
 *
 * <h3>Constructor Patterns:</h3>
 * <ul>
 *     <li><b>Full Parameterisation:</b> Users can define position, velocity, mass,
 *     restitution, and whether the particle is static.
 *     <br>Example:
 *     {@code PointMass.of(position, velocity, mass, restitution, isStatic);}</li>
 *     <li><b>Partial Parameterisation:</b> Simplified constructors with default
 *     values for omitted parameters.
 *     <br>Example: {@code PointMass.of(x, y, mass);}</li>
 *     <li><b>Mass-Only:</b> A minimal constructor requiring only mass, with all
 *     other properties set to defaults (e.g., zero position and velocity,
 *     restitution of 1).
 *     <br>Example: {@code PointMass.of(mass);}</li>
 * </ul>
 *
 * <p>
 * These patterns make object creation more intuitive, reducing the need for
 * multiple constructors and improving code readability by minimising clutter
 * from overloaded constructors.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre><code>
 *    PointMass pm1 = PointMass.of(Vector2D.of(5, 5), Vector2D.of(2, 2), 10, 0.8, false); // Full configuration
 *    PointMass pm2 = PointMass.of(5, 5, 10); // Partial configuration with default values
 *    PointMass pm3 = PointMass.of(15); // Minimal setup with mass only
 * </code></pre>
 *
 * <p>
 * This flexible approach enhances code clarity and allows users to control the
 * level of detail they require when creating instances of {@code PointMass}.
 * </p>
 *
 * <h3>Additional Features:</h3>
 * <p>
 * The {@code PointMass} class includes several utility methods for interacting
 * with the particle in a physics simulation. Users can dynamically update
 * properties such as position, velocity, and mass, while the class handles
 * force accumulation and collision responses efficiently.
 * </p>
 *
 * <h3>Important Notes:</h3>
 * <ul>
 *     <li><b>Validation:</b> All properties are validated to ensure they are within
 *     acceptable ranges. For example, <code>setMass()</code> will throw an
 *     {@code IllegalArgumentException} if an invalid value (e.g., negative mass) is
 *     supplied.</li>
 * </ul>
 * 
 * @version 3.1
 * @since   0.1.0
 * 
 * @author  Kheagen Haskins
 * 
 * @see     Vector2D
 * @see     Component
 */
public class PointMass implements Component {

    // ============================== Static ================================ //
    /**
     * The squared terminal velocity of the particle. This value is squared to
     * optimise performance by avoiding the need to calculate the square root
     * during velocity comparisons or calculations.
     *
     * <p>
     * The terminal velocity is set to 20 units, resulting in a squared value of
     * 400.
     * </p>
     */
    public static final double TERMINAL_VELOCITY_SQ = 20 * 20;
    
    public static final double  DEFAULT_MASS = 1;
    public static final double  DEFAULT_REST = 1;
    public static final boolean DEFAULT_STAT = false;

    /**
     * Creates a new {@link PointMass} object with the given position, velocity,
     * mass, restitution, and static state.
     *
     * @param position The initial {@link Vector2D} position of the
     * {@link PointMass}. This value must not be {@code null}.
     * @param velocity The initial {@link Vector2D} velocity of the
     * {@link PointMass}. This value must not be {@code null}.
     * @param mass The mass of the {@link PointMass}. This value must be greater
     * than zero.
     * @param restitution The restitution coefficient, a value between 0 and 1
     * representing the elasticity of collisions for this {@link PointMass}. A
     * value of 1 means perfectly elastic, while 0 means inelastic.
     * @param isStatic A boolean indicating whether this {@link PointMass} is
     * static. Static point masses do not move in response to forces.
     * @return A new {@link PointMass} object initialised with the specified
     * attributes.
     * @throws IllegalArgumentException if {@code mass} is less than or equal to
     * zero.
     */
    public static PointMass of(Vector2D position, Vector2D velocity, double mass, double restitution, boolean isStatic) {
        return new PointMass(position, velocity, mass, restitution, isStatic);
    }

    /**
     * Creates a new {@link PointMass} object with the specified position,
     * velocity, mass, restitution, and static state, using individual x and y
     * coordinates for position and velocity.
     *
     * @param x The x-coordinate of the initial position.
     * @param y The y-coordinate of the initial position.
     * @param vx The x-component of the initial velocity.
     * @param vy The y-component of the initial velocity.
     * @param mass The mass of the {@link PointMass}. This value must be greater
     * than zero.
     * @param restitution The restitution coefficient, a value between 0 and 1.
     * @param isStatic A boolean indicating whether this {@link PointMass} is
     * static.
     * @return A new {@link PointMass} object initialised with the given values.
     * @throws IllegalArgumentException if {@code mass} is less than or equal to
     * zero.
     */
    public static PointMass of(double x, double y, double vx, double vy, double mass, double restitution, boolean isStatic) {
        return new PointMass(Vector2D.of(x, y), Vector2D.of(vx, vy), mass, restitution, isStatic);
    }

    /**
     * Creates a new {@link PointMass} object with the specified position, mass,
     * restitution, and static state, with the initial velocity set to zero.
     *
     * @param x The x-coordinate of the initial position.
     * @param y The y-coordinate of the initial position.
     * @param mass The mass of the {@link PointMass}. This value must be greater
     * than zero.
     * @param restitution The restitution coefficient, a value between 0 and 1.
     * @param isStatic A boolean indicating whether this {@link PointMass} is
     * static.
     * @return A new {@link PointMass} object initialised with the given values.
     * @throws IllegalArgumentException if {@code mass} is less than or equal to
     * zero.
     */
    public static PointMass of(double x, double y, double mass, double restitution, boolean isStatic) {
        return new PointMass(Vector2D.of(x, y), Vector2D.zero(), mass, restitution, isStatic);
    }

    /**
     * Creates a new {@link PointMass} object with the specified position,
     * velocity, mass, and restitution, with the static state set to false by
     * default.
     *
     * @param position The initial {@link Vector2D} position of the
     * {@link PointMass}. This value must not be {@code null}.
     * @param velocity The initial {@link Vector2D} velocity of the
     * {@link PointMass}. This value must not be {@code null}.
     * @param mass The mass of the {@link PointMass}. This value must be greater
     * than zero.
     * @param restitution The restitution coefficient, a value between 0 and 1.
     * @return A new {@link PointMass} object initialised with the specified
     * attributes.
     * @throws IllegalArgumentException if {@code mass} is less than or equal to
     * zero.
     */
    public static PointMass of(Vector2D position, Vector2D velocity, double mass, double restitution) {
        return new PointMass(position, velocity, mass, restitution, DEFAULT_STAT);
    }

    /**
     * Creates a new {@link PointMass} object with the specified position, mass,
     * and restitution, with velocity set to zero and static state set to false.
     *
     * @param position The initial {@link Vector2D} position of the
     * {@link PointMass}. This value must not be {@code null}.
     * @param mass The mass of the {@link PointMass}. This value must be greater
     * than zero.
     * @param restitution The restitution coefficient, a value between 0 and 1.
     * @return A new {@link PointMass} object initialised with the given values.
     * @throws IllegalArgumentException if {@code mass} is less than or equal to
     * zero.
     */
    public static PointMass of(Vector2D position, double mass, double restitution) {
        return new PointMass(position, Vector2D.zero(), mass, restitution, DEFAULT_STAT);
    }

    /**
     * Creates a new {@link PointMass} object with the specified position and
     * mass, with velocity set to zero, restitution set to 1, and static state
     * set to false.
     *
     * @param position The initial {@link Vector2D} position of the
     * {@link PointMass}. This value must not be {@code null}.
     * @param mass The mass of the {@link PointMass}. This value must be greater
     * than zero.
     * @return A new {@link PointMass} object initialised with the given values.
     * @throws IllegalArgumentException if {@code mass} is less than or equal to
     * zero.
     */
    public static PointMass of(Vector2D position, double mass) {
        return new PointMass(position, Vector2D.zero(), mass, DEFAULT_REST, DEFAULT_STAT);
    }

    /**
     * Creates a new {@link PointMass} object with the specified position and
     * mass, with velocity set to zero, restitution set to 1, and static state
     * set to false, using individual x and y coordinates for the position.
     *
     * @param x The x-coordinate of the initial position.
     * @param y The y-coordinate of the initial position.
     * @param mass The mass of the {@link PointMass}. This value must be greater
     * than zero.
     * @return A new {@link PointMass} object initialised with the given values.
     * @throws IllegalArgumentException if {@code mass} is less than or equal to
     * zero.
     */
    public static PointMass of(double x, double y, double mass) {
        return new PointMass(Vector2D.of(x, y), Vector2D.zero(), mass, DEFAULT_REST, DEFAULT_STAT);
    }

    /**
     * Creates a new {@link PointMass} object with the specified mass, with
     * position and velocity set to zero, restitution set to 1, and static state
     * set to true. 
     *
     * @param mass The mass of the {@link PointMass}. This value must be greater
     * than zero.
     * 
     * @return A new static {@link PointMass} object initialised with the given
     * mass.
     * 
     * @throws IllegalArgumentException if {@code mass} is less than or equal to
     * zero.
     */
    public static PointMass of(double mass) {
        return new PointMass(Vector2D.zero(), Vector2D.zero(), mass, DEFAULT_REST, DEFAULT_STAT);
    }
    
    /**
     * Creates a new {@link PointMass} object at the specified coordinates, with
     * position and velocity set to zero, restitution and mass set to 1, and
     * static state set to true.
     *
     * @param x The x-coordinate of the initial position.
     * @param y The y-coordinate of the initial position.
     * 
     * @return A new static {@link PointMass} object.
     */
    public static PointMass at(double x, double y) {
        return new PointMass(Vector2D.of(x, y), Vector2D.zero(), DEFAULT_MASS, DEFAULT_REST, DEFAULT_STAT);
    }
    
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
    private Vector2D force;
    
    /**
     * The squared terminal velocity of the particle. This value is squared to
     * optimise performance by avoiding the need to calculate the square root
     * during velocity comparisons or calculations.
     */
    private double terminalVelocitySq = TERMINAL_VELOCITY_SQ;
    
    // =========================== Constructors ============================= //
    /**
     * Constructs a new {@code Particle2D} with specified position, velocity,
     * mass, restitution, and static flag.
     *
     * <p>
     * This constructor allows for full customisation of the particle's physical
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
     * @param mass the mass of the particle, which must be a non-negative value.
     * @param restitution the restitution (bounciness) of the particle,
     * determining how much kinetic energy is conserved in collisions, which
     * must be a non-negative value.
     * @param isStatic a flag indicating whether the particle is static
     * (immovable). If {@code true}, the particle will not respond to forces or
     * acceleration.
     *
     * @throws IllegalArgumentException if mass is negative or vector arguments
     * are {@code null}.
     */
    private PointMass(
            Vector2D position, Vector2D velocity, double mass, double restitution, boolean isStatic
    ) {
        throwIfNonPositive(mass,     "mass");
        throwIfNull       (position, "Position Vector");
        throwIfNull       (velocity, "Velocity Vector");
        
        // Vectors
        this.position     = position;
        this.velocity     = velocity;
        this.force        = Vector2D.zero();

        // Physics properties
        this.mass        = mass;
        this.restitution = Math.max(0, Math.min(1, restitution));
        this.isStatic    = isStatic;
    }

    // ============================== Getters =============================== //
    /**
     * Retrieves the mass of the particle.
     * 
     * <p>
     * The mass is a fundamental property of the particle, determining its
     * resistance to acceleration when a force is applied. It is typically
     * measured in kilograms (kg) and is a non-negative value.
     * </p>
     *
     * @return The mass of the particle.
     */
    public double mass() {
        return mass;
    }

    /**
     * Retrieves the current position of the particle in 2D space.
     * 
     * <p>
     * The position is represented as a {@link Vector2D}, which contains the x
     * and y coordinates of the particle. This determines the particle's
     * location in the simulation or environment.
     * </p>
     *
     * @return The current position of the particle as a {@link Vector2D}.
     */
    public Vector2D position() {
        return position;
    }

    /**
     * Retrieves the x-coordinate of the particle's position in 2D space.
     * 
     * <p>
     * This method returns the horisontal position (x) of the particle, which is
     * a fundamental aspect of its location in the simulation space. The
     * x-coordinate is typically used to determine the particle's interactions
     * with other entities along the horisontal axis.
     * </p>
     *
     * @return The x-coordinate of the particle's position as a {@code double}.
     */
    public double x() {
        return position.x();
    }

    /**
     * Retrieves the y-coordinate of the particle's position in 2D space.
     * 
     * <p>
     * This method returns the vertical position (y) of the particle, which is a
     * fundamental aspect of its location in the simulation space. The
     * y-coordinate is typically used to determine the particle's interactions
     * with other entities along the vertical axis.
     * </p>
     *
     * @return The y-coordinate of the particle's position as a {@code double}.
     */
    public double y() {
        return position.y();
    }

    /**
     * Retrieves the current velocity of the particle.
     * 
     * <p>
     * The velocity is represented as a {@link Vector2D}, indicating the speed
     * and direction of the particle's motion. It is the rate of change of the
     * particle's position with respect to time.
     * </p>
     *
     * @return The current velocity of the particle as a {@link Vector2D}.
     */
    public Vector2D velocity() {
        return velocity;
    }

    /**
     * Retrieves the net force currently acting on the particle.
     * 
     * <p>
     * The acting force is represented as a {@link Vector2D} and is the sum of
     * all forces applied to the particle. According to Newton's Second Law of
     * Motion, this force determines the particle's acceleration based on its
     * mass.
     * </p>
     *
     * @return The net force acting on the particle as a {@link Vector2D}.
     */
    public Vector2D force() {
        return force;
    }
    
    /**
     * <b>Calculates</b> the momentum of an object based on its velocity and
     * mass. Momentum is defined as the product of an object's mass and its
     * velocity. This method returns a <b>new</b> {@code Vector2D} object
     * representing the momentum vector.
     *
     * @return a {@code Vector2D} representing the momentum of the object.
     */
    public Vector2D momentum() {
        return Vector2D.scale(velocity, mass);
    }

    /**
     * Retrieves the restitution coefficient of the particle.
     * 
     * <p>
     * The restitution coefficient, also known as the coefficient of restitution
     * (CoR), is a scalar value that determines the elasticity of collisions. A
     * value of 1.0 represents a perfectly elastic collision, while a value of
     * 0.0 represents a perfectly inelastic collision.
     * </p>
     *
     * @return The restitution coefficient of the particle.
     */
    public double restitution() {
        return restitution;
    }
    
    /**
     * Calculates the kinetic energy of an object based on its mass and
     * velocity. Kinetic energy is given by the formula KE = 0.5 * m * v^2,
     * where m is the mass of the object and v is the magnitude of its velocity.
     *
     * @return the kinetic energy of the object as a double.
     */
    public double kineticEnergy() {
        return (mass * pow(velocity.mag(), 2)) / 2;
    }
    
    /**
     * Checks if the particle is static.
     * 
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
   
    /**
     * Retrieves the square of the terminal velocity for this PointMass object.
     * 
     * <p>
     * This method is more efficient than {@link #terminalVelocity()} because it
     * simply returns the precomputed value of the terminal velocity squared,
     * avoiding the computational cost of calculating a square root.
     * </p>
     *
     * @return the square of the terminal velocity.
     */
    public double terminalVelocitySquared() {
        return terminalVelocitySq;
    }

    /**
     * Retrieves the terminal velocity for this PointMass object.
     * 
     * <p>
     * This method calculates the terminal velocity by taking the square root of
     * the precomputed terminal velocity squared. Since calculating the square
     * root is computationally more expensive, use this method only when the
     * actual terminal velocity value (as opposed to the squared value) is
     * required.
     * </p>
     *
     * @return the terminal velocity.
     */
    public double terminalVelocity() {
        return sqrt(terminalVelocitySq);
    }

    
    // ============================== Setters =============================== //
    /**
     * Sets the x-coordinate of the particle's position in 2D space.
     *
     * <p>
     * This method updates the horisontal position (x) of the particle within
     * the simulation space. Adjusting the x-coordinate can be used to move the
     * particle along the horisontal axis, influencing its interactions with
     * other entities and boundaries in the simulation.
     * </p>
     *
     * <p>
     * <strong>Fluent API Example:</strong></p>
     * <pre><code>
     *     PointMass particle = PointMass.of(1).setX(10.5);
     * </code></pre>
     *
     * <p>
     * Chaining example:</p>
     * <pre><code>
     *     PointMass particle = PointMass.of(10)
     *                                   .setX(10.5)
     *                                   .setY(20.3);
     * </code></pre>
     *
     * @param x The new x-coordinate for the particle's position.
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass setX(double x) {
        position.setX(x);
        return this;
    }

    /**
     * Sets the y-coordinate of the particle's position in 2D space.
     *
     * <p>
     * This method updates the vertical position (y) of the particle within the
     * simulation space. Adjusting the y-coordinate can be used to move the
     * particle along the vertical axis, influencing its interactions with other
     * entities and boundaries in the simulation.
     * </p>
     *
     * <p>
     * <strong>Fluent API Example:</strong></p>
     * <pre><code>
     *     PointMass particle = PointMass.of(1).setY(20.3);
     * </code></pre>
     *
     * <p>
     * Chaining example:</p>
     * <pre><code>
     *     PointMass particle = PointMass.of(1)
     *                                   .setX(10.5)
     *                                   .setY(20.3);
     * </code></pre>
     *
     * @param y The new y-coordinate for the particle's position.
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass setY(double y) {
        position.y = y;
        return this;
    }

    /**
     * Sets the x-component of the particle's velocity in 2D space.
     *
     * <p>
     * This method updates the horizontal velocity (vx) of the particle within
     * the simulation space. Adjusting the x-component of the velocity affects
     * how the particle moves horizontally and can influence its interactions
     * with other objects, forces, and boundaries in the simulation.
     * </p>
     *
     * <p>
     * <strong>Fluent API Example:</strong></p>
     * <pre><code>
     *     PointMass particle = PointMass.of(1).setVx(15.7);
     * </code></pre>
     *
     * <p>
     * Chaining example:</p>
     * <pre><code>
     *     PointMass particle = PointMass.of(1)
     *                                   .setX(10.5)
     *                                   .setY(20.3)
     *                                   <b>.setVx(15.7)</b>
     *                                   .setVy(-5.2);
     * </code></pre>
     *
     * @param vx The new x-component of the particle's velocity.
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass setVx(double vx) {
        velocity.x = vx;
        return this;
    }

    /**
     * Sets the y-component of the particle's velocity in 2D space.
     *
     * <p>
     * This method updates the vertical velocity (vy) of the particle within the
     * simulation space. Adjusting the y-component of the velocity impacts the
     * particle's movement along the vertical axis and can affect interactions
     * with external forces such as gravity and other particles or boundaries.
     * </p>
     *
     * <p>
     * <strong>Fluent API Example:</strong></p>
     * <pre><code>
     *     PointMass particle = PointMass.of(1).setVy(-5.2);
     * </code></pre>
     *
     * <p>
     * Chaining example:
     * </p>
     * <pre><code>
     *     PointMass particle = PointMass.of(1)
     *                                   .setX(10.5)
     *                                   .setY(20.3)
     *                                   .setVx(15.7)
     *                                   <b>.setVy(-5.2)</b>;
     * </code></pre>
     *
     * @param vy The new y-component of the particle's velocity.
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass setVy(double vy) {
        velocity.y = vy;
        return this;
    }


    /**
     * Sets the mass of the particle.
     *
     * <p>
     * The mass is a fundamental property of the particle that determines its
     * resistance to acceleration when a force is applied. It is typically
     * measured in kilograms (kg) and should be a non-negative value. Setting
     * the mass to zero or a very small value could result in unrealistic
     * behaviour in the simulation.
     * </p>
     *
     * <p>
     * <strong>Fluent API Example:</strong></p>
     * <pre><code>
     *     PointMass particle = PointMass.at(10, -5).setMass(5.0);
     * </code></pre>
     *
     * <p>
     * Chaining example:
     * </p>
     * <pre><code>
     *     PointMass particle = PointMass.at(0, 0)
     *                                   .setMass(5.0);
     * </code></pre>
     *
     * @param mass The new mass of the particle. Must be non-negative.
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     * @throws IllegalArgumentException if {@code mass} is zero or negative.
     */
    public PointMass setMass(double mass) {
        throwIfNonPositive(mass, "mass");
        this.mass = mass;
        return this;
    }

    /**
     * Sets the restitution coefficient of the particle.
     *
     * <p>
     * The restitution coefficient determines the elasticity of collisions. A
     * value of 1.0 indicates a perfectly elastic collision where no kinetic
     * energy is lost, while a value of 0.0 indicates a perfectly inelastic
     * collision where all kinetic energy is lost. This coefficient influences
     * how the particle bounces off other objects in the simulation.
     * </p>
     *
     * <p>
     * <strong>Fluent API Example:</strong></p>
     * <pre><code>
     *     PointMass particle = PointMass.of(1).setRestitution(0.6);
     * </code></pre>
     *
     * <p>
     * Chaining example:
     * </p>
     * <pre><code>
     *     PointMass particle = PointMass.of(10)
     *                                   .setX(10.5)
     *                                   .setY(20.3)
     *                                   .setRestitution(0.6);
     * </code></pre>
     *
     * @param restitution The new restitution coefficient for the particle.
     * Typically a value between 0 and 1.
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass setRestitution(double restitution) {
        this.restitution = Math.max(0, Math.min(1, restitution));
        return this;
    }

    /**
     * Sets the terminal velocity for an entity and updates the square of this
     * value.
     *
     * <p>
     * This method takes the terminal velocity as input and computes its square,
     * storing it in the {@code TERMINAL_VELOCITY_SQ} field. The square of the terminal
     * velocity is useful in various calculations, such as drag force
     * simulations, without requiring frequent use of square root operations.
     * </p>
     *
     * <p>
     * <strong>Fluent API Example:</strong></p>
     * <pre><code>
     *     PointMass particle = PointMass.of(1).setTerminalVelocity(50.0);
     * </code></pre>
     *
     * <p>
     * Chaining example:
     * </p>
     * <pre><code>
     *     PointMass particle = PointMass.of(10)
     *                                   .setX(10.5)
     *                                   .setY(20.3)
     *                                   .setTerminalVelocity(50.0);
     * </code></pre>
     *
     * @param terminalVelocity The terminal velocity to set for the entity.
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass setTerminalVelocity(double terminalVelocity) {
        this.terminalVelocitySq = terminalVelocity * terminalVelocity;
        if (velocity.mag() > terminalVelocity) {
            this.velocity.setMag(terminalVelocity);
        }
        return this;
    }

    /**
     * Sets the static state of the particle.
     * 
     * <p>
     * A static particle is one that does not move, regardless of any forces
     * acting on it. This can be used to represent fixed objects or boundaries
     * within the simulation. Once set to static, the particle's position will
     * not change unless explicitly modified.
     * </p>
     * 
     * <p>
     * <b>Not part of the fluent API; should call at the end of method chains</b>
     * </p>
     *
     * @param isStatic {@code true} to make the particle static and immovable,
     * {@code false} to allow it to move freely.
     */
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    // ============================ API Methods ============================= //
    /**
     * Moves the {@code PointMass} by the specified x and y distances.
     *
     * <p>
     * This method modifies the current position of the {@code PointMass} by
     * adding the specified x and y values to the current x and y coordinates,
     * respectively. It allows for moving the particle incrementally by applying
     * small or large displacements over time.
     * </p>
     *
     * <p>
     * <strong>Fluent API Example:</strong></p>
     * <pre><code>
     *     PointMass pm = new PointMass(Vector2D.zero(), Vector2D.of(1, 1), 5.0);
     *     pm.move(2, 3);  // Moves the point mass by (2, 3)
     * </code></pre>
     *
     * <p>
     * This method allows chaining for multiple operations:</p>
     * <pre><code>
     *     pm.move(2, 3)
     *       .move(-1, -1);  // Moves the point mass cumulatively
     * </code></pre>
     *
     * @param x The distance to move along the x-axis.
     * @param y The distance to move along the y-axis.
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass move(double x, double y) {
        position.x += x;
        position.y += y;

        return this;
    }

    /**
     * Adds the given force vector to the current force acting on this
     * {@code PointMass}.
     *
     * <p>
     * This method modifies the current force vector by adding the x and y
     * components of the provided {@code force} vector to the respective
     * components of the current force. It allows for applying multiple forces
     * to the {@code PointMass} in a cumulative manner.
     * </p>
     *
     * <p>
     * <strong>Fluent API Example:</strong></p>
     * <pre><code>
     *     PointMass pm = new PointMass(new Vector2D(0, 0), new Vector2D(1, 1), 5.0);
     *     pm.addForce(new Vector2D(2, 3));  // Adds (2, 3) to the current force
     * </code></pre>
     *
     * <p>
     * This method updates the force in place and allows cumulative forces to be
     * applied over time:</p>
     * <pre><code>
     *     pm.addForce(new Vector2D(2, 3))
     *       .addForce(new Vector2D(-1, -1));  // Cumulative force on the point mass
     * </code></pre>
     *
     * @param force The {@code Vector2D} representing the force to be added to
     * this {@code PointMass}.
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass addForce(Vector2D force) {
        this.force.x += force.x;
        this.force.y += force.y;

        return this;
    }

    /**
     * Adds the specified x and y components to the current force acting on this
     * {@code PointMass}.
     *
     * <p>
     * This method allows for directly adding force to the x and y components of
     * the current force vector. It is useful for applying multiple forces
     * incrementally to the {@code PointMass} without the need to create a new
     * {@code Vector2D} object.
     * </p>
     *
     * <p>
     * <strong>Fluent API Example:</strong></p>
     * <pre><code>
     *     PointMass pm = new PointMass(new Vector2D(0, 0), new Vector2D(1, 1), 5.0);
     *     pm.addForce(2, 3);  // Adds (2, 3) to the current force
     * </code></pre>
     *
     * <p>
     * This method modifies the force in place, allowing for multiple forces to
     * be accumulated over time:</p>
     * <pre><code>
     *     pm.addForce(2, 3)
     *       .addForce(-1, -1);  // Cumulative force of (1, 2) applied to the point mass
     * </code></pre>
     *
     * @param x The value to be added to the x-component of the current force.
     * @param y The value to be added to the y-component of the current force.
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass addForce(double x, double y) {
        this.force.x += x;
        this.force.y += y;

        return this;
    }



    /**
     * Increases the velocity of the {@code PointMass} by the specified x and y
     * increments while enforcing terminal velocity.
     * <p>
     * This method updates the velocity by adding the provided {@code vx} and
     * {@code vy} increments to the x and y components of the current velocity.
     * After updating the velocity, it checks if the squared magnitude of the
     * velocity exceeds the squared terminal velocity. If the limit is exceeded,
     * the velocity is scaled down to match the terminal velocity.
     * </p>
     *
     * @param vx The amount by which to increase the x-component of the
     * velocity.
     * @param vy The amount by which to increase the y-component of the
     * velocity.
     *
     * <p>
     * Example usage:</p>
     * <pre><code>
     *     PointMass pm = new PointMass(new Vector2D(0, 0), new Vector2D(1, 1), 5.0, 10.0);  // 10.0 is terminal velocity
     *     pm.increaseSpeed(3, 4);  // Increases velocity by (3, 4), but enforces the terminal velocity limit
     * </code></pre>
     *
     * <p>
     * This method modifies the velocity in place and ensures that the terminal
     * velocity is not exceeded, which is useful for simulating friction or air
     * resistance:</p>
     * <pre><code>
     *     pm.increaseSpeed(5, 6).increaseSpeed(1, 2);  // Terminal velocity constraint applied after each increase
     * </code></pre>
     *
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass increaseSpeed(double vx, double vy) {
        velocity.add(vx, vy);

        // Check if the squared magnitude of the velocity exceeds the squared terminal velocity
        double velocitySquared = vx * vx + vy * vy;

        if (velocitySquared > terminalVelocitySq) {
            // Scale the velocity to match the terminal velocity
            double scaleFactor = Math.sqrt(terminalVelocitySq / velocitySquared);
            velocity = velocity.scale(scaleFactor);
        }

        return this;
    }

    /**
     * Increases the velocity of the {@code PointMass} by a specified speed
     * vector while enforcing terminal velocity.
     * <p>
     * This method provides an overload of {@code increaseSpeed} that accepts a
     * {@code Vector2D} for speed increment. The x and y components of the
     * {@code speedIncrease} vector are added to the respective components of
     * the velocity, and the terminal velocity limit is enforced in the same way
     * as the scalar version.
     * </p>
     *
     *
     * <p>
     * Example usage:</p>
     * <pre><code>
     *     PointMass pm = PointMass.of(0, 0, 0, 0, 5.0, 10.0);  // 10.0 is terminal velocity
     *     pm.increaseSpeed(new Vector2D(3, 4));  // Increases velocity by (3, 4), but enforces terminal velocity
     * </code></pre>
     *
     * <p>
     * This method modifies the velocity in place, allowing for speed increases
     * via vector arithmetic while ensuring the terminal velocity is not
     * exceeded:</p>
     * <pre><code>
     *     pm.increaseSpeed(new Vector2D(5, 6))
     *       .increaseSpeed(new Vector2D(1, 2));  // Terminal velocity constraint applied after each increase
     * </code></pre>
     *
     * @param speedIncrease The {@code Vector2D} representing the amount to
     * increase the velocity in the x and y directions.
     * 
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass increaseSpeed(Vector2D speedIncrease) {
        increaseSpeed(speedIncrease.x, speedIncrease.y);
        return this;
    }

    /**
     * Reverses the x-component of the velocity vector of this object, applying
     * the restitution coefficient to simulate a bounce effect.
     *
     * <p>
     * This method is typically used when the object collides with a vertical
     * surface. The restitution coefficient controls how "bouncy" the collision
     * is, where a value of 1.0 represents a perfectly elastic collision (no
     * energy loss), and a value of 0.0 represents a perfectly inelastic
     * collision (no bounce).
     * </p>
     *
     * <p>
     * The x-component of the velocity is negated and scaled by the restitution
     * factor, effectively reversing the horisontal direction of movement and
     * reducing its magnitude according to the restitution value.
     * </p>
     * 
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass bounceX() {
        velocity.x *= restitution * -1;
        return this;
    }

    /**
     * Reverses the y-component of the velocity vector of this object, applying
     * the restitution coefficient to simulate a bounce effect.
     *
     * <p>
     * This method is typically used when the object collides with a horisontal
     * surface. The restitution coefficient controls how "bouncy" the collision
     * is, where a value of 1.0 represents a perfectly elastic collision (no
     * energy loss), and a value of 0.0 represents a perfectly inelastic
     * collision (no bounce).
     * </p>
     *
     * <p>
     * The y-component of the velocity is negated and scaled by the restitution
     * factor, effectively reversing the vertical direction of movement and
     * reducing its magnitude according to the restitution value.
     * </p>
     * @return The current instance of {@code PointMass} to allow for method
     * chaining.
     */
    public PointMass bounceY() {
        velocity.y *= restitution * -1;
        return this;
    }

    /**
     * Returns a detailed string representation of this {@code PointMass}
     * object, including its position, velocity, acceleration, and mass.
     *
     * <p>
     * This method generates a multi-line string that provides an overview of
     * the current state of the point mass. Each physical property (position,
     * velocity, acceleration) is listed with its associated vector values,
     * followed by the mass value. This detailed output is particularly useful
     * for debugging and logging the state of a point mass in simulations.
     * </p>
     *
     * @return a string representation of this {@code PointMass}, including its
     * position, velocity, acceleration, and mass.
     */
    @Override
    public String toString() {
        StringBuilder outp = new StringBuilder();
        
        outp.append("Position_").append(position).append("\n");
        outp.append("Velocity_").append(velocity).append("\n");
        outp.append("Force_")   .append(force)   .append("\n");
        outp.append("Mass:\t\t").append(mass)    .append("\n");
        
        return outp.toString();
        
    }


    // ========================== Helper Methods ============================ //
    private void throwIfNonPositive(double field, String varName) {
        if (field <= 0) {
            throw new IllegalArgumentException(varName + " must be positive (" + field + ")");
        }
    }
    
    private void throwIfNull(Object o, String fieldName) {
        if (o == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
    }

}