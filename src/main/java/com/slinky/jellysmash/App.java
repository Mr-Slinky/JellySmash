package com.slinky.jellysmash;

import com.slinky.jellysmash.model.physics.PhysicsEngine;
import com.slinky.jellysmash.model.physics.systems.RenderSystem;
import com.slinky.jellysmash.view.WorldDisplay;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;

/**
 * The main entry point for the JellySmash application.
 *
 * <p>
 * This method serves as the launching point for the JavaFX application by
 * calling the {@link #launch()} method, which in turn triggers the JavaFX
 * runtime to start the application and subsequently invoke the {@link #init()}
 * and {@link #start(Stage)} methods.
 * </p>
 *
 * <p>
 * The `main` method is the first method called when the application starts,
 * responsible for initialising the JavaFX environment. It prepares the
 * necessary infrastructure to begin the execution of the game by delegating to
 * the {@code launch()} method, which takes care of setting up the JavaFX
 * application lifecycle.
 * </p>
 *
 * <p>
 * Once the JavaFX environment is ready, the {@link #init()} method is called to
 * initialise the core components of the game, such as the
 * {@link PhysicsEngine}, {@link RenderSystem}, and {@link WorldDisplay}. These
 * components are crucial for the game's physics calculations and rendering.
 * </p>
 *
 * <p>
 * The {@link #start(Stage)} method then sets up the primary stage (window) of
 * the application, defining the main scene and displaying it on the screen. The
 * game loop, encapsulated within the {@link GameLoop} class, is started here to
 * continuously update the physics and render the game world.
 * </p>
 *
 * <p>
 * This method ensures that the JellySmash game starts smoothly, handing off
 * control to the JavaFX runtime to manage the user interface, event handling,
 * and rendering loop.
 * </p>
 *
 * @version 1.1
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see Application
 * @see PhysicsEngine
 * @see WorldDisplay
 * @see GameLoop
 * @see RenderSystem
 * @see javafx.application.Application#launch(String...)
 * @see javafx.application.Application#init()
 * @see javafx.application.Application#start(Stage)
 */
public class App extends Application {

    /**
     * The {@link PhysicsEngine} that manages the physics simulation in the
     * game.
     */
    private PhysicsEngine engine;

    /**
     * The {@link WorldDisplay} responsible for rendering the game world.
     */
    private WorldDisplay worldDisplay;

    /**
     * The {@link GameLoop} that handles the timing and updates for the game,
     * coordinating the physics engine and rendering system.
     */
    private GameLoop gameLoop;

    private Scene scene;

    /**
     * Initialises the core components of the game, including the physics
     * engine, rendering system, and game loop.
     *
     * <p>
     * This method is called automatically by the JavaFX framework before the
     * {@link #start(Stage)} method. It sets up the game environment by creating
     * instances of {@link PhysicsEngine},
     * {@link WorldDisplay}, and {@link GameLoop}. These components are
     * essential for running the game and are used to manage the game's physics,
     * rendering, and update loop.
     * </p>
     *
     * @throws Exception if an error occurs during initialisation
     *
     * @see PhysicsEngine
     * @see WorldDisplay
     * @see GameLoop
     */
    @Override
    public void init() throws Exception {
        super.init();
        int width = 500;
        int height = 800;

        engine = new PhysicsEngine(width, height);
        worldDisplay = new WorldDisplay(new RenderSystem(), width, height);
        gameLoop = new GameLoop(engine, worldDisplay);

        scene = new Scene(new StackPane(worldDisplay));

        scene.setOnScroll(ev -> {
            engine.update(0.016);
            worldDisplay.drawWorld();
        });

        scene.setOnKeyPressed((KeyEvent ev) -> {
            switch (ev.getCode()) {
                case SPACE:
                    gameLoop.stop();
                    break;
                case P:
                    gameLoop.start();
            }
        });

    }

    /**
     * Sets up the primary stage of the application and starts the game loop.
     *
     * <p>
     * This method is called after {@link #init()} to configure the main window
     * of the game. It sets the scene to display the {@link WorldDisplay}, shows
     * the stage, and starts the {@link GameLoop} to begin the game's
     * update-render cycle.
     * </p>
     *
     * @param stage the primary stage for this application, provided by the
     * JavaFX runtime
     *
     * @see Stage
     * @see Scene
     * @see StackPane
     * @see WorldDisplay
     * @see GameLoop
     */
    @Override
    public void start(Stage stage) {
        stage.setScene(scene);
        stage.show();
//        gameLoop.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        gameLoop.stop();
    }

    /**
     * The main entry point for the application.
     *
     * <p>
     * This method is called when the application is launched from the command
     * line or other entry points. It triggers the JavaFX application lifecycle,
     * starting with the {@link #init()} method, followed by
     * {@link #start(Stage)}, and finally {@link #stop()} when the application
     * is closed.
     * </p>
     *
     * @param args command-line arguments passed to the application
     *
     * @see Application#launch(String...)
     */
    public static void main(String[] args) {
        launch();
//        System.out.println("Square Root increase: ");
//        for (int i = 1; i <= 10; i++) {
//            System.out.println(1 / Math.sqrt(i));
//        }
//
//        System.out.println("\nCube Root increase: ");
//        for (int i = 1; i <= 10; i++) {
//            System.out.println(1 / Math.cbrt(i));
//        }
//
//        System.exit(0);
    }

}
