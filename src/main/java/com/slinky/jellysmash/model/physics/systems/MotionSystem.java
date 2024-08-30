package com.slinky.jellysmash.model.physics.systems;

import com.slinky.jellysmash.model.physics.comps.Particle2D;
import com.slinky.jellysmash.model.physics.comps.Vector2D;
import com.slinky.jellysmash.model.physics.systems.util.IntegrationMethod;

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
 * motion of {@code Particle2D} components by applying the Earth's gravitational
 * force, represented by the constant {@link #GRAVITY_EARTH}, and updating their
 * positions and velocities over time. The system operates under the assumption
 * that the acceleration and velocity of particles are the final steps in their
 * state updates, meaning any external forces should be applied before invoking
 * this system to ensure accurate and realistic motion calculations.</p>
 *
 * <p>
 * This class is designed to be flexible and extendable, allowing for the
 * integration of additional forces beyond gravity by external systems or
 * classes. The class is structured to handle both static and dynamic particles,
 * with static particles being ignored in the motion calculations.</p>
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
 * of scenarios within the game.</p>
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
 * the integrity of the physics simulation.</p>
 *
 *
 * @version 1.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see Particle2D
 * @see Vector2D
 * @see IntegrationMethod
 */
public class MotionSystem extends VectorSystem2D {

    // ============================== Static ================================ //
    /**
     * Represents the acceleration due to Earth's gravity in metres per second
     * squared (m/s²). This constant is used to apply a gravitational force to
     * particles within the {@code MotionSystem}. The value is set to 9.81,
     * which is the standard gravitational acceleration on Earth.
     */
    public static final double GRAVITY_EARTH = 9.81;

    // ============================== Fields ================================ //
    /**
     * The gravitational force vector applied to particles within the
     * {@code MotionSystem}. This vector is initialized with a downward force
     * equivalent to Earth's gravity, where the x-component is 0 and the
     * y-component is {@code GRAVITY_EARTH}.
     *
     * <p>
     * This vector is scaled based on the time step when applying motion forces
     * to particles.</p>
     */
    private final Vector2D FG = new Vector2D(0, GRAVITY_EARTH);

    // =========================== Constructors ============================= //
    public MotionSystem() {
        // hmm?
    }

    // ============================ API Methods ============================= //
    /**
     * Applies all motion forces, including gravity, to the specified list of
     * particles. This method updates each particle's acting force by adding the
     * gravitational force vector ({@link #FG}) and any other applicable forces.
     *
     * <p>
     * <b>Note:</b> Static particles are ignored as they do not undergo
     * motion.</p>
     *
     * @param particles The list of {@code Particle2D} objects to which the
     * forces will be applied.
     * @param deltaTime The time in seconds used to scale the forces
     * appropriately, ensuring that the force application is consistent with the
     * simulation's time step.
     */
    public void applyMotionForces(List<Particle2D> particles) {
        // Avoid recalculating gravity vector if delta time is consistent
        for (Particle2D p : particles) {
            if (p.isStatic()) {
                continue;
            }

            addTarget(p.getActingForce(), FG);
            // Other possible forces
        }
    }

    /**
     * Calculates and updates the acceleration of each particle in the specified
     * list using the formula {@code a = F/m}, where {@code F} is the total
     * acting force on the particle, and {@code m} is the particle's mass.
     *
     * <p>
     * This method should only be invoked after all forces have been applied to
     * the particles, as it relies on the total force acting on each particle to
     * compute the acceleration.</p>
     *
     * <p>
     * <b>Important:</b> This method assumes that the acting force has already
     * been computed and stored in each particle before it is called.</p>
     *
     * @param particles The list of {@code Particle2D} objects whose
     * accelerations will be calculated.
     */
    public void calculateAccelerations(List<Particle2D> particles) {
        for (Particle2D p : particles) {
            if (p.isStatic()) {
                continue;
            }

            Vector2D force = p.getActingForce();
            double mass = p.getMass();

            // a = F / m
            p.getAcceleration().setComponents(force.getX() / mass, force.getY() / mass);
        }
    }

    /**
     * Calculates and updates the velocities and positions of the specified
     * particles based on their current accelerations and velocities, using the
     * provided integration method.
     *
     * <p>
     * This method utilizes the injected {@link IntegrationMethod} to perform
     * the calculations, allowing for different numerical integration techniques
     * (e.g., Euler, Verlet) to be used depending on the specific needs of the
     * simulation.</p>
     *
     * <p>
     * The {@code deltaTime} parameter is crucial for scaling the velocity and
     * position updates to maintain consistency with the simulation's time
     * step.</p>
     *
     * @param particles The list of {@code Particle2D} objects whose velocities
     * and positions will be updated.
     * @param iFunc The {@link IntegrationMethod} to be used for calculating the
     * velocity and position updates.
     * @param deltaTime The time in seconds used to scale the velocity and
     * position updates appropriately.
     */
    public void calculateVelocitiesAndPositions(
            List<Particle2D> particles,
            IntegrationMethod iFunc,
            double deltaTime
    ) {
        for (Particle2D p : particles) {
            if (p.isStatic()) {
                continue;
            }

            iFunc.updateVelocity(p.getVelocity(), p.getAcceleration(), deltaTime);
            iFunc.updatePosition(p.getPosition(), p.getVelocity(), deltaTime);
        }
    }

}