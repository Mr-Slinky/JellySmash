package com.slinky.jellysmash.model.physics.systems;

import com.slinky.jellysmash.model.physics.PhysicsEngine;
import com.slinky.jellysmash.model.physics.base.Entities;
import com.slinky.jellysmash.model.physics.base.Entity;
import com.slinky.jellysmash.model.physics.comps.Circle;
import com.slinky.jellysmash.model.physics.comps.PointMass;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A system responsible for drawing various components. This is not an internal
 * sub-system to the {@link PhysicsEngine} class like most other systems,
 * although this class does work together with the PhysicsEngine.
 *
 * This class links the internal components of the ECS to the JavaFX framework,
 * allowing entities to be rendered or represented visually.
 *
 * @author Kheagen Haskins
 */
public class RenderSystem implements ISystem {

    // ============================== Fields ================================ //
    private List<PointMass> ballPoints = new ArrayList<>();
    private List<Circle> ballBounds = new ArrayList<>();
    private int componentCount;
    // Other render components will be placed here

    // =========================== Constructors ============================= //
    public RenderSystem() {
        List<Entity> ents = Entities.getEntitiesWith(new Class[]{PointMass.class, Circle.class});
        componentCount = ents.size();
        for (Entity ent : ents) {
            ballPoints.add(ent.getComponent(PointMass.class));
            ballBounds.add(ent.getComponent(Circle.class));
        }
    }

    // ============================ API Methods ============================= //
    public void draw(GraphicsContext gc, double width, double height) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.RED);

        for (int i = 0; i < componentCount; i++) {
            gc.fillOval(ballPoints.get(i).x(), ballPoints.get(i).y(), ballBounds.get(i).diameter(), ballBounds.get(i).diameter());
        }

    }

}