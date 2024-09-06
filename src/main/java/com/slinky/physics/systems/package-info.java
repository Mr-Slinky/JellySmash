/**
 * <p>
 * Contains the core System classes within the Physics Entity Component System (ECS).
 * It currently includes three essential systems responsible for handling collisions, force application,
 * and object motion, ensuring that physical simulations run smoothly and accurately.
 * </p>
 *
 * <h2>Core Systems</h2>
 * <ul>
 *   <li>
 *     <b>Collision System</b> - 
 *     This system is responsible for detecting and resolving collisions between entities, particularly those 
 *     represented by {@link PointMass} and {@link Circle} components. It applies impulse forces to simulate 
 *     the physical interaction between objects, making sure collisions are realistic, consistent, and 
 *     computationally efficient.
 *     <p>
 *     The system handles two main types of collisions:
 *     <ul>
 *       <li><b>Boundary Collisions</b> - Occur when a {@link PointMass} exceeds the simulation's defined boundary. The system 
 *       applies corrective forces to keep the object within the simulation's limits.</li>
 *       <li><b>Inter-object Collisions</b> - These are detected when two {@link PointMass} objects come into contact. The system 
 *       adjusts their positions and velocities to simulate elastic impacts, ensuring energy conservation and 
 *       realistic interactions between entities.</li>
 *     </ul>
 *     </p>
 *   </li>
 *   <li>
 *     <b>Force System</b> - 
 *     This system applies continuous forces to the relevant components, currently limited to gravity. It 
 *     operates on all entities before kinematics calculations are performed, ensuring that forces such as gravity 
 *     are factored into the simulation in a physically accurate manner.
 *   </li>
 *   <li>
 *     <b>Motion System</b> - 
 *     This system primarily manages the movement and velocity of objects represented by {@link PointMass} 
 *     components. Upon its construction, the {@code MotionSystem} automatically retrieves the necessary 
 *     entities using the {@link Entities} utility class. These entities are typically created during the 
 *     initialisation phase and are later updated by the motion system to reflect changes in their velocity 
 *     and position.
 *   </li>
 * </ul>
 *
 * <h2>Interaction with Utility Classes</h2>
 * <p>
 * These systems rely heavily on the utility classes provided in the 
 * {@link com.slinky.physics.utils} package. For instance, the collision system utilises the {@link CollisionPair} class 
 * from the utils package to manage and process collisions between entities, ensuring efficient and symmetric handling 
 * of collisions. Additionally, numerical integration methods provided by the {@link IntegrationMethods} class are used 
 * within the motion system to calculate updates in position and velocity over discrete time steps, simulating continuous 
 * motion.
 * </p>
 *
 * <h2>System Coordination</h2>
 * <p>
 * The systems in this package are designed to work in a coordinated manner. The force system is invoked first, applying
 * gravity or other continuous forces. Then, the motion system calculates the new positions and velocities of the entities.
 * Finally, the collision system checks for any collisions and resolves them, ensuring the simulation remains stable and 
 * consistent.
 * </p>
 */
package com.slinky.physics.systems;