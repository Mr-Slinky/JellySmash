package com.slinky.physics.systems.util;

import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;

/**
 * Implements the Euler method for numerical integration to update the state
 * (velocity and position) of a {@link PointMass} object over time. This class
 * is part of the physics systems utilities that provide the fundamental
 * mechanics to simulate motion in the JellySmash game environment.
 * 
 * <p>
 * The Euler Integration method is a straightforward numerical procedure for
 * solving ordinary differential equations (ODEs) with a given initial value. It
 * is particularly known for its simplicity and serves as the basis for more
 * complex methods.
 * </p>
 * 
 * <p>
 * This class is implemented as a singleton, meaning only one instance of this
 * class can exist within the application context. This instance is managed and
 * provided by the {@link IntegrationMethods} utility class, which also holds
 * references to all implementations of the {@link IntegrationMethod} interface.
 * </p>
 *
 * <p>
 * The EulerIntegrationFunction can be accessed via the
 * {@code IntegrationMethods.EULER} reference, ensuring that the same instance
 * is reused wherever needed. This approach helps maintain consistency and
 * reduces overhead in managing multiple instances of the same integration
 * method.
 * </p>
 * 
 * <p>
 * Currently, the {@code MotionSystem} class is the only class dependent on this
 * interface, and it fetches the Euler integration method instance as follows:
 * <br>
 * <code>MotionSystem motionSystem = new MotionSystem(IntegrationMethods.EULER);</code>
 * </p>
 * 
 * <p>
 * This design ensures that all parts of the system using the Euler method are
 * synchronized in terms of the integration technique used, making the system
 * easier to maintain and extend if necessary.
 * </p>
 * 
 * @version 1.0
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 * 
 * @see     IntegrationMethod
 * @see     IntegrationMethods
 * @see     PointMass
 * @see     Vector2D
 */
public class EulerIntegrationFunction implements IntegrationMethod {

    // ============================ API Methods ============================= //
    /**
     * Updates the velocity and position of the provided {@link PointMass}
     * particle using the Euler integration method based on the given time step
     * {@code deltaTime}.
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
     * <code>x<sub>new</sub> = x<sub>old</sub> + v<sub>old</sub> &middot; &Delta;t + 1/2 a &middot; &Delta;t<sup>2</sup></code>
     * </p>
     * 
     * <p>
     * where <code>x<sub>old</sub></code> is the initial position of the
     * particle. This equation accounts for the linear displacement due to the
     * initial velocity and the additional displacement due to the acceleration
     * over the time interval.
     * </p>
     *
     * @param p          The {@link PointMass} object whose state is to be updated.
     *                   This object must not be {@code null}.
     * @param deltaTime  The time step to be used for updating the state, representing
     *                   how much time passes in the simulation for this update cycle.
     *                   This value should be positive and represent a small fraction of time.
     */
    @Override
    public void updateParticle(PointMass p, double deltaTime) {
        // Update Velocity
        Vector2D vOld = p.velocity().copy(); // Make a copy of the current velocity to use in position update
        // call increase speed instead of add() to enforce terminal velocity
        p.velocity().add(p.acceleration().copy().scale(deltaTime)); // Scale acceleration by deltaTime and add to current velocity
//        p.increaseSpeed(p.acceleration().copy().scale(deltaTime)); // Scale acceleration by deltaTime and add to current velocity

        // Update Position
        vOld.scale(deltaTime); // Scale the old velocity by deltaTime to get displacement due to initial velocity
        Vector2D a = p.acceleration().copy(); // Copy the acceleration to use in position update
        a.div(2)                        // Divide the acceleration by 2 for the 1/2 factor in the displacement equation
         .scale(deltaTime * deltaTime); // Scale by deltaTime squared to get displacement due to acceleration
        p.position().add(vOld).add(a);  // Add both displacements to the current position to get the new position
    }

}