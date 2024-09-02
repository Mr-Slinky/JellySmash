package com.slinky.jellysmash.model.physics.systems;

import com.slinky.jellysmash.model.physics.base.Entities;
import com.slinky.jellysmash.model.physics.base.Entity;
import com.slinky.jellysmash.model.physics.base.EntityType;
import com.slinky.jellysmash.model.physics.comps.Circle;
import com.slinky.jellysmash.model.physics.comps.PointMass;
import com.slinky.jellysmash.model.physics.comps.Vector2D;
import static java.lang.Math.round;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kheagen Haskins
 */
public class CollisionSystem {

    // ============================== Fields ================================ //
    private int width;
    private int height;
    private List<Circle> circles = new ArrayList<>();
    private List<PointMass> positions = new ArrayList<>();
    private int componentCount;
    /**
     * The threshold for velocity. x and y values equal to or lower than this
     * will have their values set to 0.
     */
    private final double nullifyThreshold_Y = 1;
    private final double nullifyThreshold_X = 0.2;

    // =========================== Constructors ============================= //
    public CollisionSystem(int width, int height) {
        this.width = width;
        this.height = height;

        List<Entity> ents = Entities.getEntitiesOfType(EntityType.BALL);
        componentCount = ents.size();
        for (Entity e : ents) {
            circles.add(e.getComponent(Circle.class));
            positions.add(e.getComponent(PointMass.class));
        }
    }

    // ============================ API Methods ============================= //
    public void update() {
        PointMass p;
        Circle c;
        for (int i = 0; i < componentCount; i++) {
            p = positions.get(i);
            c = circles.get(i);
            handleBoundCollision(p, c);
            handleObjectCollision(p, c);
        }
    }

    // ========================== Helper Methods ============================ //
    private void handleBoundCollision(PointMass p, Circle c) {
        double r = round(c.radius());
        double px = p.x(); // + r;
        double py = p.y(); // + r;
        boolean bounce = false;
        if (px < r) {
            p.bounceX();
            p.setX(r);
            bounce = true;
        } else if (px > width - r) {
            p.bounceX();
            p.setX(width - r);
            bounce = true;
        }

        if (py < r) {
            p.bounceY();
            p.setY(r);
            bounce = true;
        } else if (py > height - r) {
            p.bounceY();
            p.setY(height - r);
            bounce = true;
        }

        if (bounce) {
            handleSmallVelocity(p.velocity());
        }
    }

    private void handleObjectCollision(PointMass p, Circle c) {
        // TODO
    }

    private void handleSmallVelocity(Vector2D velocity) {
        if (velocity.y() <= nullifyThreshold_Y && velocity.y() >= -nullifyThreshold_Y) {
            velocity.setY(0);
        }

        if (velocity.x() <= nullifyThreshold_X && velocity.x() >= -nullifyThreshold_X) {
            velocity.setX(0);
        }

    }

}
