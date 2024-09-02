package com.slinky.jellysmash.model.physics;

import com.slinky.jellysmash.model.physics.base.Entities;
import com.slinky.jellysmash.model.physics.comps.Vector2D;

import com.slinky.jellysmash.model.physics.systems.MotionSystem;
import com.slinky.jellysmash.model.physics.systems.VectorSystem2D;
import com.slinky.jellysmash.model.physics.systems.util.IntegrationMethods;

/**
 * The main controller for all physics-based systems.
 *
 * @author Kheagen Haskins
 */
public final class PhysicsEngine extends VectorSystem2D {

    // ============================== Fields ================================ //
    private MotionSystem motionSystem;

    // =========================== Constructors ============================= //
    public PhysicsEngine() {
        createEntities();
        this.motionSystem = new MotionSystem();
    }

    // ============================ API Methods ============================= //
    /**
     * Should be called by the game loop
     *
     * @param deltaTime time since last frame, in seconds
     */
    public void update(double deltaTime) {
        // Loop through each subsystem in an appropriate order:
        motionSystem.applyMotionForces();
        motionSystem.calculateAccelerations();
        motionSystem.calculateVelocitiesAndPositions(IntegrationMethods.EULER, deltaTime);
    }

    private void createEntities() {
        Entities.createSolidBall(new Vector2D(10, 10), 5);
    }

}
