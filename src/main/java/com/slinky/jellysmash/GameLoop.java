package com.slinky.jellysmash;

import com.slinky.jellysmash.model.physics.PhysicsEngine;
import com.slinky.jellysmash.view.WorldDisplay;
import javafx.animation.AnimationTimer;

/**
 * The {@code GameLoop} class is responsible for managing the core loop of the
 * JellySmash game, handling the timing and update cycles necessary for
 * simulating physics and rendering the game world.
 *
 * <p>
 * This class ties together the physics engine and rendering system, ensuring
 * that the game updates at a consistent frame rate. By leveraging the
 * {@link AnimationTimer}, it efficiently manages the update-render cycle,
 * calling the {@link PhysicsEngine#update(double)} method to simulate physics
 * and the {@link WorldDisplay#drawWorld()} method to render the updated game
 * state.
 * </p>
 *
 * <p>
 * The {@code GameLoop} class is designed to be simple yet flexible, allowing
 * the game to start and stop its main loop via the {@link #start()} and
 * {@link #stop()} methods, respectively. This class encapsulates the logic
 * necessary to calculate the time elapsed between frames and use that to drive
 * the simulation, ensuring smooth and consistent gameplay.
 * </p>
 *
 * <p>
 * <b>Example Usage:</b></p>
 * <pre><code>
 * // Create a physics engine and world display
 * PhysicsEngine engine = new PhysicsEngine();
 * WorldDisplay display = new WorldDisplay(new RenderSystem(), 500, 800);
 *
 * // Create the game loop
 * GameLoop gameLoop = new GameLoop(engine, display);
 *
 * // Start the game loop
 * gameLoop.start();
 *
 * // Stop the game loop when needed
 * gameLoop.stop();
 * </code></pre>
 *
 * @version 1.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see PhysicsEngine
 * @see WorldDisplay
 * @see javafx.animation.AnimationTimer
 */
public class GameLoop {

    // ============================== Fields ================================ //
    /**
     * The {@link PhysicsEngine} responsible for simulating the physics of the
     * game.
     * <p>
     * This engine handles the calculations related to motion, collisions,
     * forces, and other physics-related aspects of the entities within the
     * game.
     * </p>
     */
    private PhysicsEngine engine;

    /**
     * The {@link WorldDisplay} responsible for rendering the visual
     * representation of the game world.
     * <p>
     * This component draws the game entities and background on the screen,
     * reflecting the current state of the simulation managed by the
     * {@link PhysicsEngine}.
     * </p>
     */
    private WorldDisplay worldDisplay;

    /**
     * The {@link AnimationTimer} that drives the game loop, calling the
     * {@link #handle(long)} method on every frame.
     * <p>
     * This timer controls the timing of the game loop, ensuring that the game
     * updates and renders at a consistent rate. The {@link #handle(long)}
     * method is overridden to implement the main game loop logic, including
     * updating the physics engine and rendering the game world.
     * </p>
     */
    private AnimationTimer tick;

    // =========================== Constructors ============================= //
    /**
     * Constructs a new {@code GameLoop} with the specified
     * {@link PhysicsEngine} and {@link WorldDisplay}.
     *
     * <p>
     * This constructor initializes the game loop with the necessary components
     * to simulate and render the game world. It also configures the
     * {@link AnimationTimer} to manage the update-render cycle.
     * </p>
     *
     * @param engine the physics engine responsible for updating the game state
     * @param display the world display responsible for rendering the game world
     *
     * @see PhysicsEngine
     * @see WorldDisplay
     */
    public GameLoop(PhysicsEngine engine, WorldDisplay display) {
        this.engine = engine;
        this.worldDisplay = display;
        this.tick = configAnimationTimer();
    }
    
    // ============================ API Methods ============================= //
    /**
     * Starts the game loop, beginning the update-render cycle.
     *
     * <p>
     * This method starts the {@link AnimationTimer}, which continuously calls
     * the {@link #handle(long)} method to update the physics engine and render
     * the game world. The game loop will continue running until {@link #stop()}
     * is called.
     * </p>
     *
     * @see #stop()
     */
    public void start() {
      tick.start();
    }

    /**
     * Stops the game loop, halting the update-render cycle.
     *
     * <p>
     * This method stops the {@link AnimationTimer}, effectively pausing the
     * game loop. Once stopped, the game will no longer update or render until
     * {@link #start()} is called again.
     * </p>
     *
     * @see #start()
     */
    public void stop() {
        tick.stop();
    }

    // ========================== Helper Methods ============================ //
    /**
     * Configures the {@link AnimationTimer} that drives the game loop.
     *
     * <p>
     * This method sets up an {@link AnimationTimer} that calculates the time
     * elapsed between frames and uses that to update the physics engine and
     * render the game world. The {@link #handle(long)} method is overridden to
     * provide the core game loop logic.
     * </p>
     *
     * @return the configured {@link AnimationTimer} for the game loop
     *
     * @see AnimationTimer
     */
    private AnimationTimer configAnimationTimer() {
        return new AnimationTimer() {
            private long previousTime = 0;

            /**
             * The main loop that is called on every frame.
             *
             * <p>
             * This method is called by the {@link AnimationTimer} on every
             * frame and is responsible for updating the physics engine and
             * rendering the game world. It calculates the time elapsed since
             * the last frame, updates the physics engine with this delta time,
             * and then triggers the rendering of the game world. The
             * {@code previousTime} variable is used to store the timestamp of
             * the last frame, allowing accurate calculation of the time
             * difference between frames.
             * </p>
             *
             * @param currentTime the timestamp of the current frame in
             * nanoseconds
             */
            @Override
            public void handle(long currentTime) {
                if (previousTime > 0) {
                    worldDisplay.drawWorld();
                    engine.update((currentTime - previousTime) / 1_000_000_000.0);
                }

                previousTime = currentTime;
            }
        };
    }
}
