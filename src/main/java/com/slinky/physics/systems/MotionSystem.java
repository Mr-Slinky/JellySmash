package com.slinky.physics.systems;

import com.slinky.physics.base.Entities;
import com.slinky.physics.base.Entity;

import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;

import com.slinky.physics.systems.util.IntegrationMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * A core component of the JellySmash physics engine, responsible for
 * calculating and applying
 * <a href="https://en.wikipedia.org/wiki/Kinematics">kinematic</a> equations to
 * simulate realistic motion of entities within the game.
 *
 * <p>
 * This system primarily manages the motion of objects represented as
 * {@link PointMass} components. Upon construction, the {@code MotionSystem}
 * automatically fetches the relevant entities through the {@link Entities}
 * utility class, which are created earlier during the initialisation process.
 * </p>
 *
 * <p>
 * A {@link #add(PointMass...)} method is available to dynamically add new
 * particles to the system. Its primary role is convenience, allowing particles
 * to be added during runtime if needed. However, the system's primary operation
 * relies on the pre-fetched entities, making it immediately ready to update the
 * physics state of these entities after construction.
 * </p>
 *
 * <p>
 * The {@code MotionSystem} is integrated into the broader physics engine, and
 * its update sequence is carefully controlled by the engine's
 * {@link PhysicsEngine#update(double)} method. In this sequence, the
 * {@code MotionSystem} is invoked after force calculations are performed by
 * other systems, ensuring that all forces are correctly applied before motion
 * updates occur. This structured approach guarantees the integrity and
 * consistency of the physics simulation, preventing issues such as unstable or
 * incorrect updates.
 * </p>
 *
 * <p>
 * The {@code MotionSystem} uses an injected {@link IntegrationMethod} to
 * calculate the motion of particles. This allows the system to employ different
 * numerical integration techniques, such as Euler or Verlet, depending on the
 * needs of the simulation. This flexibility provides precise control over how
 * motion is calculated, allowing the system to adapt to various scenarios and
 * requirements within the game.
 * </p>
 *
 * <p>
 * A typical usage pattern of the {@code MotionSystem} involves constructing the
 * system with a chosen {@link IntegrationMethod} and then allowing the physics
 * engine to manage its update process. The system will automatically manage the
 * motion of all relevant entities, updating their state based on the forces
 * applied during each update cycle.
 * </p>
 *
 * <p>
 * <b>Example Usage:</b>
 * <pre><code>
 *     MotionSystem motionSystem = new MotionSystem(IntegrationMethod.EULER);
 * </code></pre>
 * </p>
 *
 * @version 2.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see PointMass
 * @see Vector2D
 * @see IntegrationMethod
 */
public class MotionSystem {

    // ============================== Fields ================================ //
    /**
     * The integration method used for calculating the motion of particles. This
     * determines how the motion equations are applied during the update.
     */
    private IntegrationMethod iFunc;

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
    /**
     * Constructs a {@code MotionSystem} with the specified integration method.
     *
     * @param integrationMethod the integration method to be used for motion
     * calculations. This method is injected and determines how the system will
     * calculate the motion of particles.
     */
    public MotionSystem(IntegrationMethod integrationMethod) {
        this.iFunc = integrationMethod;
        List<Entity> ents = Entities.getEntitiesWith(PointMass.class);
        for (Entity ent : ents) {
            particles.add(ent.getComponent(PointMass.class));
        }
    }

    // ============================ API Methods ============================= //
    /**
     * Updates the state of the motion system based on the elapsed time since
     * the last update. This method applies forces, calculates accelerations,
     * and updates velocities and positions for all particles in the system.
     *
     * <p>
     * <b>Note:</b> Static particles are ignored as they do not undergo motion.
     * </p>
     *
     * @param deltaTime the amount of time elapsed since the last update, in
     * seconds
     */
    public void update(double deltaTime) {
//        Entities.debug_DisplayTotalKEandMomentum("Before kinematics: "); // DEBUG
        updateAccelerations();
        updateVelocitiesAndPositions(deltaTime);
//        Entities.debug_DisplayTotalKEandMomentum("After kinematics: ");  // DEBGU
    }
    
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

    /**
     * Removes all particles from the motion system.
     *
     * <p>
     * This method clears the list of particles, effectively resetting the
     * motion system. After this call, the system will contain no particles
     * until new ones are added.
     * </p>
     */
    public void removeAll() {
        particles.clear();
    }

    // ========================== Helper Methods ============================ //
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
     */
    private void updateAccelerations() {
        for (PointMass p : particles) {
            if (p.isStatic()) {
                continue;
            }

            Vector2D force = p.force();
            double mass = p.mass();

            // a = F / m
            p.acceleration().setComponents(force.x() / mass, force.y() / mass);
            // nullify force so that it cannot contribute to any future acceleration calculations
            // Might result in unintended side effect if force is needed elsewhere!
            p.force().setComponents(0, 0); 
        }
    }

    /**
     * Calculates and updates the velocities and positions of the specified
     * particles based on their current accelerations and velocities using the
     * internal {@link IntegrationMethod} instance.
     */
    private void updateVelocitiesAndPositions(double deltaTime) {
        for (PointMass p : particles) {
            if (p.isStatic()) {
                continue;
            }

            iFunc.updateParticle(p, deltaTime);
        }
    }

}