package com.slinky.jellysmash.debug;

import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * The {@code PointMassView} class is a JavaFX component that extends
 * {@code VBox}. It visually displays key properties of a {@link PointMass}
 * object, such as position, velocity, acceleration, and force, in the form of
 * labels.
 *
 * This class does not observe the {@code PointMass} directly using JavaFX
 * observable mechanisms, but instead updates its view via an explicit call to
 * the {@link #update()} method, ensuring that the physics module remains
 * decoupled from JavaFX-specific classes.
 *
 * <p>
 * This approach allows the {@code PointMassView} to render information without
 * introducing unnecessary dependencies on JavaFX observable properties.
 * </p>
 * 
 * @version 1.0
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 * 
 * @see     PointMass
 * @see     Vector2D
 * @see     VBox
 */
public class PointMassView extends VBox {

    // ============================== Fields ================================ //
    /**
     * Label to display the current position of the {@link PointMass}.
     */
    private Label lblPosition;

    /**
     * Label to display the current velocity of the {@link PointMass}.
     */
    private Label lblVelociity;

    /**
     * Label to display the current force acting on the {@link PointMass}.
     */
    private Label lblForce;

    /**
     * The {@link PointMass} object whose properties are being displayed.
     */
    private PointMass p;

    // =========================== Constructors ============================= //
    /**
     * Constructs a {@code PointMassView} for the specified {@code PointMass}
     * object. This constructor initializes the labels for displaying position,
     * velocity, acceleration, and force, and adds them as children of the
     * {@code VBox}.
     *
     * @param particle the {@code PointMass} object to display.
     */
    public PointMassView(PointMass particle) {
        this.p = particle;

        // Initialize labels for position, velocity, acceleration, and force
        lblPosition = createLabel("Position", particle.position());
        lblVelociity = createLabel("Velocity", particle.velocity());
        lblForce = createLabel("Force", particle.force());

        // Add labels to the VBox's children
        var children = getChildren();
        children.add(lblPosition);
        children.add(lblVelociity);
        children.add(lblForce);
    }

    // ============================ API Methods ============================= //
    /**
     * Updates the text of each label to reflect the current state of the
     * {@code PointMass} object. This method should be called whenever the
     * properties of the {@code PointMass} are modified and the view needs to be
     * refreshed.
     *
     * <p>
     * Each label is updated with the most recent x and y components for
     * position, velocity, acceleration, and force.</p>
     */
    public void update() {
        lblPosition .setText(format(p.position(), "Position"));
        lblVelociity.setText(format(p.velocity(), "Velocity"));
        lblForce    .setText(format(p.force(),    "Force"));
    }

    // ========================== Helper Methods ============================ //
    /**
     * Creates a {@link Label} for a given vector property of the
     * {@code PointMass} and formats its text to show the name of the property
     * and its x and y components.
     *
     * @param vectorName the name of the vector property (e.g., "Position",
     * "Velocity").
     * @param v the {@code Vector2D} object representing the vector to be
     * displayed.
     * @return a formatted {@link Label} showing the x and y components of the
     * vector.
     */
    private Label createLabel(String vectorName, Vector2D v) {
        
        Label lbl = new Label(format(v, vectorName));

        // Additional label configuration can be added here in the future
        return lbl;
    }
    
    private String format(Vector2D v, String vectorName) {
        String x = String.format("%.2f", v.x());  // Format x-component to 2 decimal places
        String y = String.format("%.2f", v.y());  // Format y-component to 2 decimal places
        
        return vectorName + " (" + x + ", " + y + ")";
    }
}