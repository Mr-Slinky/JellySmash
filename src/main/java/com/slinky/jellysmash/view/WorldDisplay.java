package com.slinky.jellysmash.view;

import com.slinky.jellysmash.model.physics.PhysicsEngine;
import com.slinky.jellysmash.model.physics.systems.RenderSystem;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


/**
 * Renders the data contained within the {@link PhysicsEngine}.
 *
 * @author Kheagen Haskins
 */
public class WorldDisplay extends Canvas {

    // ============================== Static ================================ //
    // ============================== Fields ================================ //
    private GraphicsContext gc;
    private RenderSystem renderer;
    
    // =========================== Constructors ============================= //
    public WorldDisplay(RenderSystem renderSystem, int width, int height) {
        super(width, height);
        this.gc = getGraphicsContext2D();
        this.renderer = renderSystem;
    }
    
    // ============================== Getters =============================== //
    // ============================== Setters =============================== //
    // ============================ API Methods ============================= //
    public void drawBallUpdate() {
        renderer.draw(gc, getWidth(), getHeight());
    }
    
    // ========================== Helper Methods ============================ //
    // ========================== Helper Classes ============================ //
}
