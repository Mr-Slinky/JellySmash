package com.slinky.jellysmash.model.physics;

import com.slinky.jellysmash.GameLoop;
import com.slinky.jellysmash.model.physics.base.Entities;

import com.slinky.jellysmash.model.physics.systems.MotionSystem;
import com.slinky.jellysmash.model.physics.systems.CollisionSystem;
import com.slinky.jellysmash.model.physics.systems.ForceSystem;
import com.slinky.jellysmash.model.physics.systems.util.IntegrationMethods;

/**
 * The main controller for all physics-based systems. Currently a work in
 * progress
 *
 * @author Kheagen Haskins
 *
 * @see    GameLoop
 */
public final class PhysicsEngine {

    // ============================== Fields ================================ //
    private MotionSystem     motionSystem;
    private CollisionSystem  collisionSystem;
    private ForceSystem      forceSystem;

    // =========================== Constructors ============================= //
    public PhysicsEngine(int width, int height) {
        createEntities();
        this.forceSystem     = new ForceSystem();
        this.motionSystem    = new MotionSystem(IntegrationMethods.EULER);
        this.collisionSystem = new CollisionSystem(width, height);
    }
    
    // ============================ API Methods ============================= //
    /**
     * Should be called by {@link GameLoop}
     *
     * @param deltaTime time since last frame, in seconds
     */
    public void update(double deltaTime) {
        forceSystem.update();
        motionSystem.update(1 + (deltaTime - 0.016));
        collisionSystem.update();
    }

    private void createEntities() {
        Entities.createSolidBall(100, 50,  3, 5, 1);
        Entities.createSolidBall(10,  50,  3, 5, 5);
        Entities.createSolidBall(400, 50, -2, 5, 5);
        Entities.createSolidBall(200, 50, -2, 5, 15);
    }

}