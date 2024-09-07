package com.slinky.physics;

import com.slinky.jellysmash.GameLoop;
import com.slinky.physics.base.Entities;
import com.slinky.physics.systems.MotionSystem;
import com.slinky.physics.systems.CollisionSystem;
import com.slinky.physics.systems.ForceSystem;
import com.slinky.physics.systems.util.IntegrationMethods;

/**
 * The {@code PhysicsEngine2D} class is the primary controller for managing all
 * physics-based systems in a 2D environment. This class integrates various
 * subsystems, such as motion, collisions, and forces, and orchestrates their
 * updates in each frame of the simulation.
 *
 * <p>
 * It is designed to work within a game loop structure, allowing continuous
 * simulation of object motion, collision detection, and force application. The
 * engine also provides a simple API to enable or disable gravity during the
 * simulation. Currently, the system is under development, and entity creation
 * is hardcoded for testing purposes.
 * </p>
 *
 * <h2>Key Responsibilities:</h2>
 *     <ul>
 *     <li>Manage the updating of physics systems such as motion, collisions, and
 *     forces.</li>
 *     <li>Support toggling of gravity for simulations that require it.</li>
 *     <li>Provide basic entity creation for testing physics systems.</li>
 * </ul>
 *
 * <p>
 * This class is part of the {@code com.slinky.physics} package, and it
 * interacts with the game loop and various physics subsystems to handle
 * real-time simulations.
 * </p>
 * 
 * @version 1.0
 * @since 0.1.0
 * 
 * @author Kheagen Haskins
 * 
 * @see GameLoop
 * @see MotionSystem
 * @see CollisionSystem
 * @see ForceSystem
 * @see Entities
 */
public final class PhysicsEngine2D {

    // ============================== Fields ================================ //
    /**
     * Manages and updates the motion of all entities in the simulation.
     */
    private MotionSystem motionSystem;

    /**
     * Manages and updates the collision detection and response between
     * entities.
     */
    private CollisionSystem collisionSystem;

    /**
     * Manages and updates the forces applied to entities, including gravity and
     * custom forces.
     */
    private ForceSystem forceSystem;

    /**
     * Indicates whether gravity is currently enabled in the simulation.
     */
    private boolean gravityOn;

    // =========================== Constructors ============================= //
    /**
     * Constructs a new {@code PhysicsEngine2D} with the specified simulation
     * area dimensions.
     *
     * <p>
     * This constructor initialises the main physics systems and configures the
     * default integration method for the motion system to use Euler's
     * method.</p>
     *
     * @param width the width of the simulation area, used by the collision
     * system
     * @param height the height of the simulation area, used by the collision
     * system
     */
    public PhysicsEngine2D(int width, int height) {
        createEntities();  // Creates predefined test entities
        this.forceSystem     = new ForceSystem();  // Initialises the force system
        this.motionSystem    = new MotionSystem(IntegrationMethods.EULER);  // Motion system using Euler integration
        this.collisionSystem = new CollisionSystem(width, height);  // Initialises the collision system with bounds
        this.gravityOn       = true;  // Gravity is enabled by default
    }

    // ============================ API Methods ============================= //
    /**
     * Updates all physics systems for the current frame.
     *
     * <p>
     * This method is typically called by the {@link GameLoop} and is
     * responsible for updating the motion, collision, and force systems,
     * ensuring a realistic simulation of physical interactions in
     * real-time.</p>
     *
     * <p>
     * If gravity is enabled, the force system will apply gravitational forces
     * to all entities before updating the motion system. The motion system
     * updates the position and velocity of entities based on the elapsed time,
     * and the collision system detects and resolves collisions.</p>
     *
     * @param deltaTime the time elapsed since the last frame, in seconds
     * @see GameLoop
     */
    public void update(double deltaTime) {
        if (gravityOn) {
            forceSystem.update();  // Apply forces, including gravity
        }
        motionSystem.update(1 + (deltaTime - 0.016));  // Update motion with modified deltaTime
        collisionSystem.update();  // Update collision detection and response
    }

    /**
     * Enables or disables gravity in the simulation.
     *
     * <p>
     * This method can be used to toggle the gravitational forces applied to all
     * entities in the simulation. If gravity is disabled, the force system will
     * not apply gravity during the update loop, allowing for zero-gravity
     * simulations.</p>
     *
     * @param gravityOn {@code true} to enable gravity, {@code false} to disable
     * it
     */
    public void setGravityOn(boolean gravityOn) {
        this.gravityOn = gravityOn;
    }

    // ============================ Private Methods ============================= //
    /**
     * Creates test entities for the simulation.
     *
     * <p>
     * This method is currently used for testing purposes and creates two solid
     * ball entities with predefined properties, including position, velocity,
     * mass, and size.</p>
     *
     * <p>
     * In future iterations, this method may be replaced with dynamic entity
     * creation as the physics engine progresses in development.</p>
     *
     * @see Entities#createSolidBall
     */
    private void createEntities() {
        Entities.createSolidBall(60, 440, 2, -5.5, 5, 0, 1, 50, false);  // Create first ball entity
        Entities.createSolidBall(440, 440, -2, -5.5, 10, 0, 1, 50, false);  // Create second ball entity
    }

}