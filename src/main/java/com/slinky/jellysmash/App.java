package com.slinky.jellysmash;

import com.slinky.physics.PhysicsEngine2D;
import com.slinky.jellysmash.debug.DebugRenderSystem;
import com.slinky.jellysmash.debug.FXDebugScene;
import com.slinky.jellysmash.debug.FXWorldDisplay;

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
 * {@link PhysicsEngine2D}, {@link DebugRenderSystem}, and {@link FXWorldDisplay}. These
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
 * @see PhysicsEngine2D
 * @see FXWorldDisplay
 * @see GameLoop
 * @see DebugRenderSystem
 * @see javafx.application.Application#launch(String...)
 * @see javafx.application.Application#init()
 * @see javafx.application.Application#start(Stage)
 */
public class App extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    /**
     * Sets up the primary stage of the application and starts the game loop.
     *
     * <p>
     * This method is called after {@link #init()} to configure the main window
     * of the game. It sets the scene to display the {@link FXWorldDisplay}, shows
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
     * @see FXWorldDisplay
     * @see GameLoop
     */
    @Override
    public void start(Stage stage) {
        stage.setScene(new FXDebugScene());
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
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
    }

}
