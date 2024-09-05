package com.slinky.jellysmash.debug;

import com.slinky.physics.PhysicsEngine2D;
import com.slinky.physics.base.Entities;
import com.slinky.physics.base.Entity;
import com.slinky.physics.base.EntityType;
import com.slinky.physics.comps.Circle;
import com.slinky.physics.comps.PointMass;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A system responsible for drawing various components. This is not an internal
 * sub-system to the {@link PhysicsEngine2D} class like most other systems,
 although this class does work together with the PhysicsEngine2D.

 This class links the internal components of the ECS to the JavaFX framework,
 allowing entities to be rendered or represented visually.
 *
 * @author Kheagen Haskins
 */
public class DebugRenderSystem {

    // ============================== Fields ================================ //
    private List<PointMass> ballPoints = new ArrayList<>();
    private List<Circle> ballBounds = new ArrayList<>();
    private int componentCount;
    // Other render components will be placed here

    // =========================== Constructors ============================= //
    public DebugRenderSystem() {
        List<Entity> ents = Entities.getEntitiesOfType(EntityType.SOLID_BALL);
        componentCount = ents.size();
        for (Entity ent : ents) {
            ballPoints.add(ent.getComponent(PointMass.class));
            ballBounds.add(ent.getComponent(Circle.class));
        }
    }

    // ============================ API Methods ============================= //
    Color b1 = Color.web("0xFFFF00");
    Color b2 = Color.web("0x00FFFF");
    Color b3 = Color.web("0xFF00FF");
    Color b4 = Color.PURPLE;
    Color[] cs = {b1, b2};
    int c = 0;
    public void draw(GraphicsContext gc, double gcWidth, double gcHeight) {
        // Refresh canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gcWidth, gcHeight);
        gc.setLineWidth(2);
        
        double x, y, d, r;
        for (int i = 0; i < componentCount; i++) {
            r = ballBounds.get(i).radius();
            d = ballBounds.get(i).diameter();
            x = ballPoints.get(i).x() - r; // must move to top left for rendering
            y = ballPoints.get(i).y() - r; // must move to top left for rendering

            gc.setFill(cs[c = ++c % cs.length]);
            gc.fillOval(x, y, d, d);
            gc.setStroke(Color.YELLOW);
            gc.strokeLine(
                    x + r,
                    y + r,
                    x + r + ballPoints.get(i).velocity().x() * 10,
                    y + r + ballPoints.get(i).velocity().y() * 10
            );

            gc.setStroke(Color.WHITE);
            gc.strokeLine(
                    x + r,
                    y + r,
                    x + r + ballPoints.get(i).acceleration().x() * 10,
                    y + r + ballPoints.get(i).acceleration().y() * 10
            );
            
            gc.setStroke(Color.web("0x00FF00"));
            gc.strokeLine(
                    x + r,
                    y + r,
                    x + r + ballPoints.get(i).force().x() * 60,
                    y + r + ballPoints.get(i).force().y() * 60
            );
        }

    }

}
