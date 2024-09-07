package com.slinky.physics.systems.util;

import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;

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