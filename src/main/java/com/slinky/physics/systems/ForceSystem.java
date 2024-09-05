package com.slinky.physics.systems;

import com.slinky.physics.base.Entities;
import com.slinky.physics.base.Entity;

import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for applying all continuous forces to the appropriate components,
 * although currently only applies gravity. This system in invoked before the
 * kinematics calculations.
 *
 * @author Kheagen Haskins
 */
public class ForceSystem implements ISystem {

    // ============================== Static ================================ //
    /**
     * Represents the acceleration due to Earth's gravity in metres per second
     * squared ( m/s² ). This constant is used to apply a gravitational force to
     * particles within the {@code MotionSystem}. The value is set to 9.81,
     * which is the standard gravitational acceleration on Earth. Arbitrarily
     * scaled to provide accuracy
     */
    public static final double GRAVITY_EARTH = 9.81 / 50;

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
     * All particles that will have forces applied to them
     */
    private final List<PointMass> particles = new ArrayList<>();

    // =========================== Constructors ============================= //
    public ForceSystem() {
        List<Entity> ents = Entities.getEntitiesWith(PointMass.class);
        for (Entity ent : ents) {
            particles.add(ent.getComponent(PointMass.class));
        }
    }

    // ============================ API Methods ============================= //
    /**
     * Applies all motion forces, including gravity, to the internal list of
     * particles. This method updates each particle's acting force by adding the
     * gravitational force vector ({@link #FG}) and any other applicable forces.
     *
     *
     */
    public void update() {
        for (PointMass p : particles) {
            if (!p.isStatic()) {
                applyGravity(p);
                // Might calculate more forces such as drag, friction etc here...
            }
        }
    }

    // ========================== Helper Methods ============================ //
    private void applyGravity(PointMass p) {
        // Fg = mg
        p.addForce(FG.copy().scale(p.mass()));
    }

}