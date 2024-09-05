package com.slinky.physics;

import com.slinky.jellysmash.GameLoop;
import com.slinky.physics.base.Entities;

import com.slinky.physics.systems.MotionSystem;
import com.slinky.physics.systems.CollisionSystem;
import com.slinky.physics.systems.ForceSystem;
import com.slinky.physics.systems.util.IntegrationMethods;

/**
 * The main controller for all physics-based systems. Currently a work in
 * progress
 *
 * @author Kheagen Haskins
 *
 * @see GameLoop
 */
public final class PhysicsEngine2D {

    // ============================== Fields ================================ //
    private MotionSystem motionSystem;
    private CollisionSystem collisionSystem;
    private ForceSystem forceSystem;
    private final double totalKineticEnergyAtStart;

    // =========================== Constructors ============================= //
    public PhysicsEngine2D(int width, int height) {
        createEntities();
        this.forceSystem = new ForceSystem();
        this.motionSystem = new MotionSystem(IntegrationMethods.EULER);
        this.collisionSystem = new CollisionSystem(width, height);
        this.totalKineticEnergyAtStart = Entities.getTotalSystemMomentum();
    }

    // ============================ API Methods ============================= //
    /**
     * Should be called by {@link GameLoop}
     *
     * @param deltaTime time since last frame, in seconds
     */
    public void update(double deltaTime) {
//        forceSystem.update();
        double m1 = Entities.getTotalSystemMomentum();  // DEBUG
        motionSystem.update(1 + (deltaTime - 0.016));
        double m2 = Entities.getTotalSystemMomentum();  // DEBUG
        if (m1 - m2 != 0) {
            System.out.println(totalKineticEnergyAtStart - m2); // DEBUG
        }
        collisionSystem.update();
    }
    
    private void createEntities() {
        for (int i = 2; i < 4; i++) {
            double r = Math.sqrt(i + 1) * 10;
            Entities.createSolidBall(r * 1.5 * (i + 1), r * 1.5 * (i + 1), Math.random() * 10, Math.random() * 10, (i + 1));
        }
//        Entities.createSolidBall(100, 50,  3, 5, 1);
//        Entities.createSolidBall(10,  50,  3, 5, 5);
//        Entities.createSolidBall(400, 50, -2, 5, 5);
//        Entities.createSolidBall(200, 50, -2, 5, 15);
    }

}