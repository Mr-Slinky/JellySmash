package com.slinky.jellysmash.debug;

import com.slinky.jellysmash.GameLoop;
import com.slinky.physics.PhysicsEngine2D;
import com.slinky.physics.base.Entities;
import com.slinky.physics.base.Entity;
import com.slinky.physics.base.EntityType;
import com.slinky.physics.comps.PointMass;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.KeyEvent;

import static javafx.scene.input.KeyCode.SPACE;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Kheagen Haskisn
 */
public class FXDebugScene extends Scene {

    // ============================== Static ================================ //
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private static final LineChart<Number, Number> lineChart;
    private static final XYChart.Series<Number, Number> energySeries;

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
    
    private static final BorderPane borderPane;
    
    private static final InformationPanel infoPanel;
    
    static {
        borderPane = new BorderPane();
        engine = new PhysicsEngine2D(WIDTH, HEIGHT);
        engine.setGravityOn(false);
        worldDisplay = new FXWorldDisplay(new DebugRenderSystem(), WIDTH, HEIGHT);
        infoPanel = new InformationPanel();
        
        // Axes setup
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Collisions Count or Time");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total System Kinetic Energy");
        // Creating the line chart with the axes
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Energy Monitoring Over Collisions");
        // Series to hold data
        energySeries = new XYChart.Series<>();
        energySeries.setName("Energy Loss");
        // Adding series to chart
        lineChart.getData().add(energySeries);
        
         // Setting the size of the plot points directly in Java
        for (XYChart.Data<Number, Number> data : energySeries.getData()) {
            data.getNode().setStyle("-fx-padding: 1px; -fx-background-radius: 1px;");
        }
        
        gameLoop = new GameLoop(engine, worldDisplay, energySeries, infoPanel);
        
        for (Entity e : Entities.getEntitiesOfType(EntityType.SOLID_BALL)) {
            infoPanel.add(e.getComponent(PointMass.class));
        }
        
        borderPane.setCenter(worldDisplay);
        borderPane.setBottom(infoPanel);
    }
    
    
    
    // =========================== Constructors ============================= //
    public FXDebugScene() {
        super(borderPane);
        
        setOnScroll(ev -> {
            gameLoop.update(1);
        });

        setOnKeyPressed((KeyEvent ev) -> {
            switch (ev.getCode()) {
                case SPACE:
                    if (gameLoop.isRunning()) {
                        gameLoop.stop();
                    } else {
                        gameLoop.start();
                    }
                    break;
            }
        });
        
        worldDisplay.drawWorld();
    }
    
    
    // ============================ API Methods ============================= //
    // ========================== Helper Methods ============================ //
    // ========================== Helper Classes ============================ //

    public static LineChart<Number, Number> getLineChart() {
        return lineChart;
    }
}
