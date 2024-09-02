package com.slinky.jellysmash;

import com.slinky.jellysmash.model.physics.PhysicsEngine;
import com.slinky.jellysmash.model.physics.systems.RenderSystem;
import com.slinky.jellysmash.view.WorldDisplay;
import javafx.animation.AnimationTimer;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;

public class App extends Application {

    private PhysicsEngine engine;
    private RenderSystem renderer;
    private WorldDisplay wordDisplay;
    private AnimationTimer tick;

    @Override
    public void init() throws Exception {
        super.init();
        engine = new PhysicsEngine();
        renderer = new RenderSystem();
        wordDisplay = new WorldDisplay(renderer, 500, 800);
        tick = configAnimationTimer();
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new StackPane(wordDisplay)));
        stage.show();
        tick.start();
    }

    private AnimationTimer configAnimationTimer() {

        return new AnimationTimer() {
            private long previousTime = 0;

            @Override
            public void handle(long currentTime) {
                if (previousTime > 0) {
                    engine.update((currentTime - previousTime) / 1_000_000_000.0);
                    wordDisplay.drawBallUpdate();
                }

                previousTime = currentTime;
            }
        };
    }

    // =========================== MAIN Method ============================== //
    public static void main(String[] args) {
        launch();
    }

}