package com.slinky.jellysmash.model.physics;

import com.slinky.jellysmash.model.physics.base.Entities;
import com.slinky.jellysmash.model.physics.base.Entity;
import com.slinky.jellysmash.model.physics.comps.Vector2D;

import com.slinky.jellysmash.model.physics.systems.MotionSystem;
import com.slinky.jellysmash.model.physics.systems.CollisionSystem;
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
    private CollisionSystem collisionSystem;

    // =========================== Constructors ============================= //
    public PhysicsEngine(int width, int height) {
        createEntities();
        this.motionSystem = new MotionSystem();
        this.collisionSystem = new CollisionSystem(width, height);
    }

    // ============================ API Methods ============================= //
    /**
     * Should be called by the game loop
     *
     * @param deltaTime time since last frame, in seconds
     */
    public void update(double deltaTime) {
        deltaTime = Math.min(deltaTime, 0.5);
        // Loop through each subsystem in an appropriate order:
        motionSystem.applyMotionForces();
        motionSystem.calculateAccelerations(deltaTime);
        motionSystem.calculateVelocitiesAndPositions(IntegrationMethods.EULER);
        
        collisionSystem.update();
    }

    private void createEntities() {
        Entities.createSolidBall(50, 0, 5, -10, 2);
        Entities.createSolidBall(250, 10, 1, 0, 20);
    }

}