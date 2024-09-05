package com.slinky.jellysmash.debug;

import com.slinky.jellysmash.GameLoop;
import com.slinky.physics.PhysicsEngine2D;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.SPACE;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Kheagen Haskisn
 */
public class FXDebugScene extends Scene {

    // ============================== Static ================================ //
    private static final int WIDTH  = 500;
    private static final int HEIGHT = 500;
    
    /**
     * The {@link PhysicsEngine2D} that manages the physics simulation in the
     * game.
     */
    private static final PhysicsEngine2D engine;

    /**
     * The {@link FXWorldDisplay} responsible for rendering the game world.
     */
    private static final FXWorldDisplay worldDisplay;

    /**
     * The {@link GameLoop} that handles the timing and updates for the game,
     * coordinating the physics engine and rendering system.
     */
    private static final GameLoop gameLoop;

    static {
        engine = new PhysicsEngine2D(WIDTH, HEIGHT);
        worldDisplay = new FXWorldDisplay(new DebugRenderSystem(), WIDTH, HEIGHT);
        gameLoop = new GameLoop(engine, worldDisplay);
        
    }
    
    // =========================== Constructors ============================= //
    public FXDebugScene() {
        super(new StackPane(worldDisplay));
        setOnScroll(ev -> {
            engine.update(0.016);
            worldDisplay.drawWorld();
        });

        setOnKeyPressed((KeyEvent ev) -> {
            switch (ev.getCode()) {
                case SPACE:
                    gameLoop.stop();
                    break;
            }
        });
        
        gameLoop.start();
    }
    // ============================ API Methods ============================= //
    // ========================== Helper Methods ============================ //
    // ========================== Helper Classes ============================ //
}
