package com.slinky.jellysmash.model.physics.comps;

/**
 * The {@code Particle} interface represents a physics body component within the
 * Entity Component System (ECS) architecture. It encapsulates the fundamental
 * physical properties and behaviours of a particle, including its mass,
 * position, velocity, acceleration, and other key attributes relevant to
 * physics simulations.
 *
 * <p>
 * A {@code Particle} serves as a component that can be attached to an entity,
 * allowing the entity to interact with the physical world. This interface
 * provides methods for accessing and modifying the particle's physical state,
 * as well as applying forces and computing the resulting dynamics. It is
 * designed to be flexible and extensible, supporting a wide range of physical
 * simulations, from simple point masses to more complex bodies with varying
 * properties.
 * </p>
 *
 * <p>
 * The {@code Particle} interface also includes methods for handling damping and
 * restitution, which are essential for simulating realistic physical behaviours
 * such as friction and collisions. Additionally, the interface supports the
 * concept of static particles, which do not move or respond to forces, making
 * them useful for representing fixed objects in the simulation.
 * </p>
 *
 * <p>
 * This interface is intended to be implemented by classes that manage the
 * physical properties of entities in a simulation, providing a standard way to
 * interact with these properties across different systems within the ECS
 * framework.
 * </p>
 *
 * @version 1.0
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 *
 * @see     Component
 * @see     Position
 * @see     Vector
 */
public interface Particle extends Component {

    // ============================== Getters =============================== //
    /**
     * Returns the mass of this particle.
     *
     * <p>
     * The mass is a fundamental property of the particle, representing the
     * amount of matter contained within it. In a physics simulation, the mass
     * is used to calculate forces, acceleration, and other dynamic behaviours.
     * A higher mass typically means the particle is less affected by external
     * forces.
     * </p>
     *
     * @return the mass of the particle.
     */
    double getMass();

    /**
     * Returns the position of this particle as a {@link Position}.
     *
     * <p>
     * The position represents the current location of the particle in the
     * simulation space. It is typically expressed as a vector in a Cartesian
     * coordinate system. The position is essential for determining the
     * particle's location relative to other entities and for calculating its
     * movement over time.
     * </p>
     *
     * @return the position of the particle.
     */
    Position getPosition();

    /**
     * Returns the velocity of this particle as a {@link Vector}.
     *
     * <p>
     * The velocity represents the rate of change of the particle's position
     * with respect to time. It is a vector quantity, meaning it has both
     * magnitude and direction. The velocity is crucial for determining how fast
     * and in which direction the particle is moving within the simulation
     * space.
     * </p>
     *
     * @return the velocity of the particle.
     */
    Vector getVelocity();

    /**
     * Returns the acceleration of this particle as a {@link Vector}.
     *
     * <p>
     * The acceleration represents the rate of change of the particle's velocity
     * with respect to time. It is also a vector quantity and is typically
     * influenced by the forces acting on the particle, such as gravity or
     * applied forces. Acceleration is key to understanding how the particle's
     * velocity and subsequently its position will change over time.
     * </p>
     *
     * @return the acceleration of the particle.
     */
    Vector getAcceleration();

    /**
     * Retrieves the damping coefficient of this particle.
     * <p>
     * The damping coefficient is a measure of how forces such as friction and
     * air resistance affect the particle's motion. A higher damping coefficient
     * indicates that the particle will lose velocity more quickly, simulating
     * the effect of these resistive forces. The damping is typically applied as
     * a reduction to the particle's velocity proportionally to its current
     * velocity, mimicking natural deceleration in physical environments.
     * </p>
     * <p>
     * This coefficient is used in physics calculations to apply damping forces
     * each frame, influencing how the particle's velocity is updated.
     * </p>
     *
     * @return the damping coefficient, where 0 means no damping (no velocity
     * loss over time) and higher values indicate stronger damping effects.
     */
    double getDampingCoefficient();

    /**
     * Retrieves the restitution coefficient of this particle.
     * <p>
     * The restitution coefficient, often referred to as the "coefficient of
     * restitution" (COR), quantifies the elasticity of collisions involving
     * this particle. It is primarily used to calculate how the particle bounces
     * back after colliding with another particle or surface. A restitution
     * coefficient of 1 indicates a perfectly elastic collision where no kinetic
     * energy is lost, while a value of 0 represents a perfectly inelastic
     * collision where the particle does not bounce back at all.
     * </p>
     * <p>
     * This property is critical in determining the outcome of collision
     * responses in the physics engine, affecting how particles interact when
     * they come into contact.
     * </p>
     *
     * @return the restitution coefficient ranging from 0.0 (completely
     * inelastic) to 1.0 (perfectly elastic).
     */
    double getRestitution();

    /**
     * Determines whether this particle is static.
     * <p>
     * A static particle does not respond to forces or collisions in the usual
     * manner; it remains immovable regardless of the forces exerted on it. This
     * property is useful for creating environment elements that should not be
     * displaced by interactions in the simulation, such as walls, floors, or
     * other fixed obstacles.
     * </p>
     * <p>
     * In the physics simulation, static particles are often excluded from
     * certain calculations like force application and collision response,
     * optimizing performance by reducing the number of computations required.
     * </p>
     *
     * @return {@code true} if the particle is static and does not move,
     * {@code false} otherwise.
     */
    boolean isStatic();

    // ============================== Setters =============================== //
    /**
     * Sets the mass of this particle.
     *
     * <p>
     * The mass determines how much the particle resists acceleration when
     * forces are applied. A higher mass means the particle will be less
     * responsive to applied forces, reflecting its greater inertia. Setting the
     * mass is essential when initializing the particle to ensure that it
     * behaves correctly under the physical laws being simulated. It can also be
     * used to dynamically alter the particle's inertia during the simulation,
     * which may be necessary in scenarios like mass redistribution or scaling
     * effects.
     * </p>
     *
     * @param mass the new mass of the particle, typically in kilograms or a
     * similar unit depending on the simulation's context.
     */
    void setMass(double mass);

    /**
     * Sets the damping coefficient for this particle.
     *
     * <p>
     * The damping coefficient controls how quickly the particle's motion decays
     * over time, simulating effects like friction or air resistance. A higher
     * damping coefficient results in a quicker reduction of the particle's
     * velocity, eventually bringing it to a stop. Setting the damping
     * coefficient is crucial for accurately simulating environments where
     * resistance plays a significant role in motion, such as in fluids or on
     * rough surfaces.
     * </p>
     *
     * @param damping the new damping coefficient for the particle, where a
     * value closer to 1.0 results in more rapid damping.
     */
    void setDampingCoefficient(double damping);

    /**
     * Sets the restitution (bounciness) of this particle.
     *
     * <p>
     * The restitution determines how much kinetic energy is conserved in a
     * collision. A restitution value of 1.0 means the particle bounces back
     * without losing any energy, while a value of 0 means the particle does not
     * bounce at all and instead comes to a complete stop. Setting the
     * restitution is important for simulating realistic collisions, whether
     * they are perfectly elastic, perfectly inelastic, or somewhere in between.
     * This parameter directly affects how the particle interacts with other
     * entities upon impact.
     * </p>
     *
     * @param restitution the new restitution coefficient for the particle,
     * typically between 0.0 and 1.0.
     */
    void setRestitution(double restitution);

    /**
     * Sets whether this particle is static (immovable).
     *
     * <p>
     * A static particle does not respond to forces or acceleration and remains
     * fixed in place within the simulation. This is useful for representing
     * immovable objects like walls or anchors that other entities can interact
     * with but which do not move themselves. Setting a particle as static
     * ensures that it will not be affected by the physics simulation, making it
     * a key element in scenarios requiring fixed reference points or
     * boundaries.
     * </p>
     *
     * @param isStatic {@code true} if the particle should be static and
     * immovable, {@code false} if it should behave as a dynamic, movable
     * particle.
     */
    void setStatic(boolean isStatic);
}