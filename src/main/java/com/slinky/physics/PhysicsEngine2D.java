package com.slinky.physics;

import com.slinky.jellysmash.GameLoop;
import com.slinky.physics.base.Entities;
import com.slinky.physics.base.Entity;
import com.slinky.physics.comps.PointMass;

import com.slinky.physics.systems.MotionSystem;
import com.slinky.physics.systems.CollisionSystem;
import com.slinky.physics.systems.ForceSystem;
import com.slinky.physics.systems.util.IntegrationMethods;
import java.util.ArrayList;
import java.util.List;

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

    private static List<PointMass> points = new ArrayList<>();

    // =========================== Constructors ============================= //
    public PhysicsEngine2D(int width, int height) {
        createEntities();
        this.forceSystem     = new ForceSystem();
        this.motionSystem    = new MotionSystem(IntegrationMethods.EULER);
        this.collisionSystem = new CollisionSystem(width, height);

        this.totalKineticEnergyAtStart = getTotalSystemMomentum();
    }

    // ============================ API Methods ============================= //
    /**
     * Should be called by {@link GameLoop}
     *
     * @param deltaTime time since last frame, in seconds
     */
    public void update(double deltaTime) {
//        forceSystem.update();
        double m1 = getTotalSystemMomentum();  // DEBUG
        motionSystem.update(1 + (deltaTime - 0.016));
        double m2 = getTotalSystemMomentum();  // DEBUG
        if (m1 - m2 != 0) {
            System.out.println(totalKineticEnergyAtStart - m2); // DEBUG
        }
        collisionSystem.update();
    }

    private void createEntities() {
        for (int i = 2; i < 6; i++) {
            double r = Math.sqrt(i + 1) * 10;
            Entity e = Entities.createSolidBall(r * 1.5 * (i + 1), r * 1.5 * (i + 1), Math.random() * 10, Math.random() * 10, (i + 1));
            points.add(e.getComponent(PointMass.class));
        }
    }

    // =========================== Debug Methods ============================ //
    public static double getTotalSystemKE() {
        double totalKe = 0;

        for (PointMass point : points) {
            totalKe += point.kineticEnergy();
        }

        return totalKe;
    }

    public static double getTotalSystemMomentum() {
        double totalMomentum = 0;

        for (PointMass point : points) {
            totalMomentum += point.momentum().mag();
        }

        return totalMomentum;
    }

    public static void debug_DisplayTotalKEandMomentum(String preText) {
        System.out.println(preText);
        System.out.println("Total KE:\t" + getTotalSystemKE());
        System.out.println("Total P: \t" + getTotalSystemMomentum());
    }

}