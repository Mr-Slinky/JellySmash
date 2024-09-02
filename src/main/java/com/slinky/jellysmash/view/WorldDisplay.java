package com.slinky.jellysmash.view;

import com.slinky.jellysmash.model.physics.systems.RenderSystem;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The {@code WorldDisplay} class is a custom extension of the JavaFX
 * {@link Canvas} that serves as the primary rendering surface for the
 * JellySmash game world. This class is responsible for displaying the current
 * state of the game world by using a {@link RenderSystem} to draw all the
 * necessary visual components.
 *
 * <p>
 * The {@code WorldDisplay} class integrates with the ECS
 * (Entity-Component-System) architecture of the JellySmash physics engine. It
 * leverages a {@link RenderSystem} to manage and render all visual components
 * of the entities within the game world. The {@link GraphicsContext} obtained
 * from the {@code Canvas} is used by the {@code RenderSystem} to draw the game
 * elements.
 * </p>
 *
 * <p>
 * This class is designed to be used in conjunction with a game loop, where the
 * {@link #drawWorld()} method is called repeatedly to update and display the
 * current state of the game world.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre><code>
 *     RenderSystem renderSystem = new RenderSystem();
 *     WorldDisplay worldDisplay = new WorldDisplay(renderSystem, 800, 600);
 *
 *     // Add the WorldDisplay to a scene and stage
 *     StackPane root = new StackPane(worldDisplay);
 *     Scene scene = new Scene(root);
 *     stage.setScene(scene);
 *     stage.show();
 *
 *     // In the game loop, repeatedly call drawWorld() to render the game
 *     worldDisplay.drawWorld();
 * </code></pre>
 *
 * @version 1.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see Canvas
 * @see GraphicsContext
 * @see RenderSystem
 */
public class WorldDisplay extends Canvas {

    // ============================== Fields ================================ //
    /**
     * The {@link GraphicsContext} associated with this {@code Canvas} object,
     * used by the {@link RenderSystem} to draw visual components onto the
     * canvas.
     */
    private final GraphicsContext gc;

    /**
     * The {@link RenderSystem} responsible for rendering all the entities and
     * visual components within the game world.
     */
    private final RenderSystem renderer;

    // =========================== Constructors ============================= //
    /**
     * Constructs a new {@code WorldDisplay} with a specified
     * {@link RenderSystem}, and dimensions for the canvas.
     *
     * <p>
     * The {@code WorldDisplay} is initialized with the provided width and
     * height, and it sets up the {@link GraphicsContext} from the
     * {@code Canvas}. The {@link RenderSystem} is used to manage and draw the
     * visual components onto the canvas.
     * </p>
     *
     * @param renderSystem the {@link RenderSystem} that will handle the
     * rendering of visual components within the game world.
     * @param width the width of the {@code Canvas} in pixels.
     * @param height the height of the {@code Canvas} in pixels.
     */
    public WorldDisplay(RenderSystem renderSystem, int width, int height) {
        super(width, height);
        this.renderer = renderSystem;

        this.gc = getGraphicsContext2D();
        this.gc.setStroke(Color.web("FFFFFF"));
        this.gc.setLineWidth(2);
    }

    // ============================ API Methods ============================= //
    /**
     * Renders the current state of the game world onto the canvas.
     *
     * <p>
     * The {@link #drawWorld()} method is the primary rendering function that
     * clears the canvas and invokes the
     * {@link RenderSystem#draw(GraphicsContext, double, double)} method to draw
     * all entities and visual elements within the game world. This method
     * should be called repeatedly, typically within a game loop, to continually
     * update the visual representation of the game world.
     * </p>
     */
    public void drawWorld() {
        renderer.draw(gc, getWidth(), getHeight());
    }

}
