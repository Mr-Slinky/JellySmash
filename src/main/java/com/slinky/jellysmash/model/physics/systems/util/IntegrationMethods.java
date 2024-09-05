package com.slinky.jellysmash.model.physics.systems.util;

import com.slinky.jellysmash.model.physics.comps.Vector2D;

/**
 * The {@code IntegrationMethods} class provides a collection of numerical
 * integration functions for use in physics simulations. These methods update
 * the state of vectors and positions based on discrete time steps, enabling the
 * approximation of continuous motion within the simulation.
 *
 * <p>
 * Currently, the class provides a single integration method, {@code EULER},
 * which implements the Euler method of integration. This implementation Updates
 * the velocity and position of the provided {@link PointMass} particle using
 * the Euler integration method based on the given time step {@code deltaTime}.
 * </p>
 * 
 * <p>
 * The velocity update equation used is:
 * <br>
 * <code>v<sub>new</sub> = v<sub>old</sub> + a &middot; &Delta;t</code>
 * </p>
 *
 * <p>
 * where <code>v<sub>old</sub></code> is the initial velocity of the particle,
 * <code>a</code> is the acceleration, and <code>&Delta;t</code> is the time
 * step.
 * </p>
 *
 * <p>
 * The position update equation is:
 * <br>
 * <code>x<sub>new</sub> = x<sub>old</sub> + v<sub>old</sub> &middot; &Delta;t +
 * 1/2 a &middot; &Delta;t<sup>2</sup></code>
 * </p>
 *
 * <p>
 * where <code>x<sub>old</sub></code> is the initial position of the particle.
 * This equation accounts for the linear displacement due to the initial
 * velocity and the additional displacement due to the acceleration over the
 * time interval.
 * </p>
 *
 * @version 1.1
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 *
 * @see     IntegrationMethod
 * @see     Vector2D
 */
public class IntegrationMethods {

    // ========================= Static Constants ========================== //
    /**
     * The {@code EULER} constant provides an instance of the Euler integration
     * method for updating velocity and position. This method is simple and
     * effective for many real-time simulations, though it may not be as
     * accurate as higher-order methods for all scenarios.
     * 
     * <p>
     * This implementation Updates the velocity and position of the provided
     * {@link PointMass} particle using the Euler integration method based on
     * the given time step {@code deltaTime}.
     * </p>
     *
     * <p>
     * The velocity update equation used is:
     * <br>
     * <code>v<sub>new</sub> = v<sub>old</sub> + a &middot; &Delta;t</code>
     * </p>
     *
     * <p>
     * where <code>v<sub>old</sub></code> is the initial velocity of the
     * particle, <code>a</code> is the acceleration, and <code>&Delta;t</code>
     * is the time step.
     * </p>
     *
     * <p>
     * The position update equation is:
     * <br>
     * <code>x<sub>new</sub> = x<sub>old</sub> + v<sub>old</sub> &middot;
     * &Delta;t + 1/2 a &middot; &Delta;t<sup>2</sup></code>
     * </p>
     *
     * <p>
     * where <code>x<sub>old</sub></code> is the initial position of the
     * particle. This equation accounts for the linear displacement due to the
     * initial velocity and the additional displacement due to the acceleration
     * over the time interval.
     * </p>
     */
    public static final IntegrationMethod EULER = new EulerIntegrationFunction();

}