package com.slinky.jellysmash.model.physics.systems;

import com.slinky.jellysmash.model.physics.base.Entities;
import com.slinky.jellysmash.model.physics.base.Entity;

import com.slinky.jellysmash.model.physics.comps.PointMass;
import com.slinky.jellysmash.model.physics.comps.Vector2D;

import com.slinky.jellysmash.model.physics.systems.util.IntegrationMethod;

import java.util.ArrayList;

import java.util.List;

/**
 * The {@code MotionSystem} class is a specialised physics system within the
 * JellySmash game that handles the calculation and application of motion forces
 * on particles, particularly focusing on updating their acceleration, velocity,
 * and position. This class is a core component of the game's physics engine,
 * simulating realistic movement by integrating gravitational forces and other
 * potential forces acting on the particles.
 *
 * <p>
 * The primary responsibility of the {@code MotionSystem} is to manage the
 * motion of {@code PointMass} components by applying the Earth's gravitational
 * force, represented by the constant {@link #GRAVITY_EARTH}, and updating their
 * positions and velocities over time. The system operates under the assumption
 * that the acceleration and velocity of particles are the final steps in their
 * state updates, meaning any external forces should be applied before invoking
 * this system to ensure accurate and realistic motion calculations.
 * </p>
 *
 * <p>
 * This class is designed to be flexible and extendable, allowing for the
 * integration of additional forces beyond gravity by external systems or
 * classes. The class is structured to handle both static and dynamic particles,
 * with static particles being ignored in the motion calculations.
 * </p>
 *
 * <p>
 * One of the key features of the {@code MotionSystem} is its reliance on an
 * {@code IntegrationMethod} for updating velocities and positions. This method
 * is injected as a dependency during the calculation process, allowing for
 * different integration techniques to be applied depending on the specific
 * needs of the simulation. For example, you can choose to use the Euler method
 * by selecting {@code IntegrationMethod integrator = IntegrationMethod.EULER;}
 * and passing this integrator to the {@link #calculateVelocitiesAndPositions}
 * method. This approach provides a high degree of flexibility and precision in
 * the way the physics system handles updates, making it adaptable to a variety
 * of scenarios within the game.
 * </p>
 *
 * <p>
 * Usage of this system typically follows a three-step process:
 * <ol>
 * <li>Apply motion forces (e.g., gravity) using the {@link #applyMotionForces}
 * method.</li>
 * <li>Calculate accelerations based on the accumulated forces using
 * {@link #calculateAccelerations}.</li>
 * <li>Update velocities and positions using the specified
 * {@link IntegrationMethod} through
 * {@link #calculateVelocitiesAndPositions}.</li>
 * </ol>
 * This ensures a clear and consistent update sequence, crucial for maintaining
 * the integrity of the physics simulation.
 * </p>
 *
 *
 * @version 1.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see PointMass
 * @see Vector2D
 * @see IntegrationMethod
 */
public class MotionSystem extends VectorSystem2D {

    // ============================== Static ================================ //
    /**
     * Represents the acceleration due to Earth's gravity in metres per second
     * squared ( m/s² ). This constant is used to apply a gravitational force to
     * particles within the {@code MotionSystem}. The value is set to 9.81,
     * which is the standard gravitational acceleration on Earth.
     */
    public static final double GRAVITY_EARTH = 9.81; 

    // ============================== Fields ================================ //
    /**
     * The gravitational force vector applied to particles within the
     * {@code MotionSystem}. This vector is initialised with a downward force
     * equivalent to Earth's gravity, where the x-component is 0 and the
     * y-component is {@code GRAVITY_EARTH}.
     *
     * <p>
     * This vector is scaled based on the time step when applying motion forces
     * to particles.
     * </p>
     */
    private final Vector2D FG = new Vector2D(0, GRAVITY_EARTH);

    /**
     * A list of {@code PointMass} objects managed by the {@code MotionSystem}.
     * <p>
     * This list stores all the particles that the motion system needs to manage
     * and update during the simulation. Each {@code PointMass} in the list
     * represents an individual point mass with properties such as position,
     * velocity, and acceleration, which the motion system will process to
     * simulate movement and other physical interactions.
     * </p>
     * <p>
     * The list is initialised during the construction of the
     * {@code MotionSystem} and is populated through the {@code add} method.
     * </p>
     */
    private final List<PointMass> particles = new ArrayList<>();

    // =========================== Constructors ============================= //
    public MotionSystem() {
        List<Entity> ents = Entities.getEntitiesWith(PointMass.class);
        for (Entity ent : ents) {
            particles.add(ent.getComponent(PointMass.class));
        }
    }

    // ============================ API Methods ============================= //
    /**
     * Adds one or more {@code PointMass} objects to the motion system for
     * management.
     *
     * <p>
     * This method allows for adding multiple {@code PointMass} objects to the
     * {@code MotionSystem}'s internal list in a single call. The method is
     * variadic, accepting any number of {@code PointMass} objects. Each
     * non-null particle provided will be added to the system, enabling the
     * motion system to manage its movement and interactions during the
     * simulation.
     * </p>
     *
     * <p>
     * The method includes null checks to ensure that only valid, non-null
     * particles are added to the system. If the input array is {@code null} or
     * empty, or if any of the particles in the array are {@code null}, those
     * invalid particles are ignored, and the method proceeds without adding
     * them.
     * </p>
     *
     *
     * @param particles an array of {@code PointMass} objects to be added to the
     * motion system. Any {@code null} elements in the array are ignored.
     */
    public void add(PointMass... particles) {
        if (particles == null || particles.length == 0) {
            return;
        }

        for (int i = 0; i < particles.length; i++) {
            if (particles[i] == null) {
                continue;
            }

            this.particles.add(particles[i]);
        }
    }

    public void removeAll() {
        particles.clear();
    }

    /**
     * Applies all motion forces, including gravity, to the internal list of
     * particles. This method updates each particle's acting force by adding the
     * gravitational force vector ({@link #FG}) and any other applicable forces.
     *
     * <p>
     * <b>Note:</b> Static particles are ignored as they do not undergo motion.
     * </p>
     */
    public void applyMotionForces() {
        for (PointMass p : particles) {
            if (p.isStatic()) {
                continue;
            }

            // Fg = mg
            p.force().setComponents(
                    FG.x() * p.mass(),
                    FG.y() * p.mass()
            );
            // Other possible forces
        }
    }

    /**
     * Calculates and updates the acceleration of each particle in the specified
     * list based on the formula {@code a = F/m}, where {@code F} is the total
     * force acting on the particle, and {@code m} is the particle's mass.
     *
     * <p>
     * This method should be called only after all relevant forces have been
     * applied to the particles, as it depends on the total force acting on each
     * particle to accurately compute the resulting acceleration. If no force is
     * acting on a particle, its acceleration will be 0, in accordance with
     * Newton's First Law of Motion (Law of Inertia).
     * </p>
     *
     * <p>
     * <b>Note:</b> It is crucial that all forces acting on the particles have
     * been calculated and stored within each particle object before this method
     * is invoked. Failure to do so will result in incorrect acceleration
     * values.
     * </p>
     *
     * @param deltaTime the amount of time that has elapsed since the last
     * update, in seconds. Used to scale the acceleration.
     */
    public void calculateAccelerations(double deltaTime) {
        for (PointMass p : particles) {
            if (p.isStatic()) {
                continue;
            }

            Vector2D force = p.force();
            double mass = p.mass();

            // a = F / m
            p.acceleration().setComponents(force.x() / mass, force.y() / mass);
            scaleTarget(p.acceleration(), deltaTime);
        }
    }

    /**
     * Calculates and updates the velocities and positions of the specified
     * particles based on their current accelerations and velocities, using the
     * provided integration method.
     *
     * <p>
     * This method uses the injected {@link IntegrationMethod} to perform the
     * calculations, allowing for different numerical integration techniques
     * (e.g., Euler, Verlet) to be used depending on the specific needs of the
     * simulation.</p>
     *
     * <p>
     * The {@code deltaTime} parameter is crucial for scaling the velocity and
     * position updates to maintain consistency with the simulation's time step.
     * </p>
     *
     * @param iFunc The {@link IntegrationMethod} to be used for calculating the
     * velocity and position updates.
     */
    public void calculateVelocitiesAndPositions(IntegrationMethod iFunc) {
        for (PointMass p : particles) {
            if (p.isStatic()) {
                continue;
            }

            iFunc.updateVelocity(p.velocity(), p.acceleration());
            iFunc.updatePosition(p.position(), p.velocity());
        }
    }

}
