package com.slinky.physics.systems.util;

import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;
import java.util.Vector;

/**
 * The {@code IntegrationMethod} interface defines the contract for numerical
 * integration methods used in physics simulations to update the velocity and
 * position of particles.
 *
 * <p>
 * Numerical integration is a fundamental technique in physics engines, enabling
 * the approximation of the continuous equations of motion by discrete updates.
 * This interface provides methods for updating both velocity and position,
 * given the current state of a particle. Implementing classes can apply
 * different integration methods, such as Euler, Verlet, or Runge-Kutta,
 * depending on the specific needs of the simulation.
 * </p>
 *
 * <p>
 * The {@code updateVelocity} method updates the velocity of a particle based on
 * its current acceleration and the elapsed time step. The
 * {@code updatePosition} method updates the position of a particle based on its
 * current velocity and the elapsed time step. Both methods are designed to work
 * within a 2D vector space, represented by the {@link Vector} and
 * {@link Position} types.
 * </p>
 *
 * <p>
 * This interface is intended to be implemented by classes that encapsulate
 * specific numerical integration algorithms. By providing a standard API for
 * these operations, the {@code IntegrationMethod} interface allows for
 * interchangeable integration strategies within a physics engine or simulation
 * framework.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre><code>
 *     IntegrationMethod integrator = IntegrationMethod<b>s</b>.EULER;
 *     Vector   newVelocity = integrator.updateVelocity(currentVelocity, currentAcceleration, deltaTime);
 *     Position newPosition = integrator.updatePosition(currentPosition, newVelocity, currentAcceleration, deltaTime);
 * </code></pre>
 * </p>
 *
 * @version 2.0
 * 
 * @since   0.1.0
 * 
 * @author  Kheagen Haskins
 *
 * @see     Vector
 * @see     Vector2D
 */
public interface IntegrationMethod {

    // ============================ API Methods ============================= //
    void updateParticle(PointMass particle, double deltaTime);
}

//    /**
//     * Updates the velocity of a particle based on its current acceleration.
//     *
//     * <p>
//     * This method applies the selected numerical integration technique to
//     * compute the new velocity vector for the particle. The integration
//     * considers the current velocity, the acceleration acting on the particle,
//     * and the time step over which the update is applied.
//     * </p>
//     *
//     * <p>
//     * Implementing classes may use different strategies for this update, such
//     * as the simple Euler method or more sophisticated techniques like Verlet
//     * integration or Runge-Kutta methods.
//     * </p>
//     *
//     * @param v         the current velocity vector of the particle.
//     * @param a         the current acceleration vector acting on the particle.
//     * @param deltaTime the time elapsed since the last update, in seconds.
//     * @return a new {@code Vector2D} representing the updated velocity of the
//     * particle.
//     */
//    Vector2D updateVelocity(Vector2D v, Vector2D a, double deltaTime);
//
//    /**
//     * Updates the position of a particle based on its current velocity.
//     *
//     * <p>
//     * This method applies the selected numerical integration technique to
//     * compute the new position of the particle. The integration considers the
//     * current position and the velocity of the particle.
//     * </p>
//     *
//     * <p>
//     * Implementing classes may use different strategies for this update,
//     * depending on the integration method being implemented, to achieve varying
//     * levels of accuracy and stability in the simulation.
//     * </p>
//     *
//     * @param p         the current position of the particle.
//     * @param v         the current velocity vector of the particle.
//     * @param deltaTime the time elapsed since the last update, in seconds.
//     * @param acc       the current acceleration of the particle
//     * @return a new {@code Position2D} representing the updated position of the
//     * particle.
//     */
//    Vector2D updatePosition(Vector2D p, Vector2D v, Vector2D acc, double deltaTime);