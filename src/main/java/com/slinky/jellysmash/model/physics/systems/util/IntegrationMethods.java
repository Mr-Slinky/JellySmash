package com.slinky.jellysmash.model.physics.systems.util;

import com.slinky.jellysmash.model.physics.systems.VectorSystem2D;
import com.slinky.jellysmash.model.physics.comps.Position2D;
import com.slinky.jellysmash.model.physics.comps.Vector2D;

/**
 * The {@code IntegrationMethods} class provides a collection of numerical
 * integration functions for use in physics simulations. These methods update
 * the state of vectors and positions based on discrete time steps, enabling the
 * approximation of continuous motion within the simulation.
 *
 * <p>
 * All methods in this class operate by directly modifying the values of the
 * first argument passed to the function and returning the modified object. This
 * design ensures that no new vector instances are created during the operation,
 * making the methods efficient for use in high-performance simulations where
 * minimizing object creation is crucial.
 * </p>
 *
 * <p>
 * The class also encapsulates constants representing specific integration
 * methods. Each constant is implemented as a private static class that adheres
 * to the {@link IntegrationMethod} interface, ensuring a consistent API for
 * integration across the simulation framework.
 * </p>
 *
 * <p>
 * Currently, the class provides a single integration method, {@code EULER},
 * which implements the Euler method of integration. This method updates
 * velocity and position by calculating and using the average velocity over a
 * time step, providing a straightforward and commonly used approach to
 * numerical integration in physics simulations.
 * </p>
 *
 * @version 1.0
 * @since   0.1.0
 * 
 * @author  Kheagen Haskins
 * 
 * @see     IntegrationMethod
 * @see     Vector2D
 * @see     Position2D
 * @see     VectorSystem2D
 */
public class IntegrationMethods {

    // ========================= Static Constants ========================== //
    /**
     * The {@code EULER} constant provides an instance of the Euler integration
     * method for updating velocity and position. This method is simple and
     * effective for many real-time simulations, though it may not be as
     * accurate as higher-order methods for all scenarios.
     */
    public final IntegrationMethod EULER = new EulerIntegrationFunction();

    // ======================= Static Implementation ======================= //
    /**
     * The {@code EulerIntegrationFunction} class implements the Euler method of
     * numerical integration, extending the {@link VectorSystem2D} to gain
     * access to vector operations necessary for the integration calculations.
     *
     * <p>
     * This implementation uses a utility vector to cache the old velocity and
     * compute the average velocity needed for updating the particle's position.
     * </p>
     */
    private static class EulerIntegrationFunction
            extends VectorSystem2D // Get access to vector operations
            implements IntegrationMethod {

        /**
         * A utility vector used to cache the old velocity and compute the
         * average velocity for position updates.
         */
        private Vector2D utilVector = new Vector2D(0, 0);

        /**
         * Updates the velocity of a particle using the Euler integration
         * method.
         *
         * <p>
         * This method calculates the new velocity by adding the product of the
         * acceleration and the time step to the current velocity.
         * </p>
         *
         * @param v the current velocity vector of the particle.
         * @param a the current acceleration vector acting on the particle.
         * @param deltaTime the time step over which to update the velocity.
         * @return the updated velocity vector.
         */
        @Override
        public Vector2D updateVelocity(Vector2D v, Vector2D a, double deltaTime) {
            // Implements vNew = vOld + (a * deltaTime)

            // a * deltaTime
            scaleTarget(a, deltaTime);

            // Cache vOld in utility vector for position update
            utilVector.setComponents(v.getX(), v.getY());

            // vNew = vOld + scaled acceleration
            addTarget(v, a);
            return v;
        }

        /**
         * Updates the position of a particle using the Euler integration
         * method.
         *
         * <p>
         * This method calculates the new position by adding the product of the
         * average velocity (calculated as the average of the old and new
         * velocities) and the time step to the current position.
         * </p>
         *
         * @param p the current position of the particle.
         * @param vNew the updated velocity vector of the particle.
         * @param deltaTime the time step over which to update the position.
         * @return the updated position.
         */
        @Override
        public Position2D updatePosition(Position2D p, Vector2D vNew, double deltaTime) {
            // Implements pNew = pOld + (vAverage * deltaTime)

            // Add old and new vectors, then half them (turns utilVector from vOld to vAverage)
            addTarget(utilVector, vNew); // utilVector becomes sum of vOld and vNew
            divTarget(utilVector, 2); // Average the two velocities
            scaleTarget(utilVector, deltaTime); // Scale average velocity

            addTarget(p, utilVector); // Add pOld with average velocity to get pNew

            return p;
        }
    }

}