package com.slinky.jellysmash.debug;

import com.slinky.physics.comps.PointMass;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.HBox;

/**
 * The {@code InformationPanel} class is a JavaFX component that extends
 * {@code HBox}. It serves as a container for multiple {@link PointMassView}
 * instances, allowing the visual display and management of information about
 * several {@link PointMass} objects.
 *
 * <p>
 * This class provides methods to update all displayed {@code PointMass}
 * information and dynamically add new {@code PointMass} objects to the
 * panel.
 * </p>
 *
 * <p>
 * The panel aggregates a list of {@code PointMassView} objects and provides a
 * convenient way to refresh their displayed data through the {@link #update()}
 * method.
 * </p>
 *
 * <p>
 * New {@code PointMass} objects can be added to the panel dynamically via the
 * {@link #add(PointMass)} method, making the panel flexible and expandable for
 * real-time simulations or debugging purposes.
 * </p>
 * 
 * @version 1.0
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 * 
 * @see     PointMass
 * @see     HBox
 */
public class InformationPanel extends HBox {

    // ============================== Fields ================================ //
    /**
     * A list of {@link PointMassView} objects that are displayed in this panel.
     * Each {@code PointMassView} represents the visual information of a single
     * {@link PointMass}.
     */
    private List<PointMassView> particleViews = new ArrayList<>();

    // =========================== Constructors ============================= //
    /**
     * Constructs an {@code InformationPanel} containing the specified
     * {@link PointMass} objects. Each {@code PointMass} is wrapped in a
     * {@link PointMassView} for visual display and added to the panel.
     *
     * @param points the {@code PointMass} objects to display in the panel.
     */
    public InformationPanel(PointMass... points) {
        super(50);
        
        PointMassView view;
        var children = getChildren();

        // Loop through each provided PointMass and create a PointMassView for it
        for (int i = 0; i < points.length; i++) {
            view = new PointMassView(points[i]);
            particleViews.add(view);  // Add the view to the internal list
            children.add(view);       // Add the view to the panel's visual children
        }
    }

    // ============================ API Methods ============================= //
    /**
     * Updates all {@link PointMassView} instances within the panel. This method
     * should be called to refresh the displayed information for each
     * {@link PointMass}, typically after their properties have changed.
     */
    public void update() {
        for (PointMassView view : particleViews) {
            view.update();
        }
    }

    /**
     * Adds a new {@link PointMass} to the panel. A {@link PointMassView} is
     * created for the specified {@code PointMass}, added to the internal list
     * of views, and displayed in the panel.
     *
     * @param p the {@code PointMass} object to add to the panel.
     */
    public void add(PointMass p) {
        var view = new PointMassView(p);

        particleViews.add(view);  // Add the new view to the internal list
        getChildren().add(view);  // Add the new view to the panel's visual children
    }
}