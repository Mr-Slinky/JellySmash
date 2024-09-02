package com.slinky.jellysmash.model.physics;

import com.slinky.jellysmash.model.physics.base.Entities;

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
        this.motionSystem = new MotionSystem(IntegrationMethods.EULER);
        this.collisionSystem = new CollisionSystem(width, height);
    }

    // ============================ API Methods ============================= //
    /**
     * Should be called by the game loop
     *
     * @param deltaTime time since last frame, in seconds
     */
    public void update(double deltaTime) {
        // Max delta time to half a second to prevent large, unstable updateas
        motionSystem.update(Math.min(deltaTime, 0.5));
        collisionSystem.update();
    }

    private void createEntities() {
        Entities.createSolidBall(50, 0, 5, -10, 2);
        Entities.createSolidBall(250, 10, 1, 0, 20);
    }

}