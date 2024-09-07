package com.slinky.physics.systems;

import com.slinky.physics.base.Entities;
import com.slinky.physics.base.Entity;
import com.slinky.physics.base.EntityType;

import com.slinky.physics.comps.Circle;
import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;
import com.slinky.physics.systems.util.CollisionPair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A core component of the physics engine, responsible for detecting and
 * resolving collisions between various entities, specifically those represented
 * by {@link PointMass} and {@link Circle} components, by applying impulse forces
 * to the colliding entities. It plays a crucial role in ensuring that the
 * physical interactions within the simulation are realistic, consistent, and
 * computationally efficient.
 *
 * <p>
 * The system handles two main types of collisions: boundary collisions and
 * inter-object collisions. Boundary collisions occur when a {@link PointMass}
 * exceeds the defined simulation area, at which point the system applies
 * corrective forces to keep the object within bounds. Inter-object collisions
 * involve detecting when two {@link PointMass} objects come into contact and
 * resolving these collisions by adjusting their positions and velocities to
 * simulate elastic impacts.
 * </p>
 *
 * <p>
 * At the heart of the collision resolution process is the {@code CollisionPair}
 * class, which manages collision detection and resolution between pairs of
 * objects. The {@code CollisionSystem} ensures that collisions are handled
 * symmetrically, treating the pairs (A, B) and (B, A) as equivalent. This
 * approach guarantees that the order of the entities does not influence the
 * outcome, maintaining consistency across simulations.
 * </p>
 *
 * <p>
 * The system is designed to be easily extensible, allowing for the dynamic
 * addition of new entities and seamless updates to the simulation state. While
 * the class includes legacy methods for backward compatibility, these are
 * marked as deprecated and should be avoided in favour of more robust, modern
 * collision handling techniques.
 * </p>
 *
 * <p>
 * This class is essential for any simulation requiring accurate physical
 * interactions, making it suitable for games, simulations, or any application
 * where realistic physics is a priority.
 * </p>
 *
 * @version 2.0
 * @since   0.1.0
 *
 * @author  Kheagen Haskins
 * 
 * @see     Circle
 * @see     PointMass
 */
public class CollisionSystem {

    // ============================== Fields ================================ //
    /**
     * The width of the simulation area in which collisions are processed.
     */
    private int width;

    /**
     * The height of the simulation area in which collisions are processed.
     */
    private int height;

    /**
     * The number of entities (e.g., {@link PointMass} and {@link Circle}) being
     * handled by this {@code CollisionSystem}. This value is initialised based
     * on the number of entities retrieved during construction and is updated
     * accordingly.
     */
    private int componentCount;

    /**
     * A list of {@link Circle} components representing the boundaries of
     * entities in the system. Each circle corresponds to a {@link PointMass},
     * and this list is used to check for inter-object collisions.
     */
    private List<Circle> circles = new ArrayList<>();

    /**
     * A list of {@link PointMass} components representing the point mass
     * objects being simulated. These objects interact with one another and are
     * affected by both boundary and inter-object collisions.
     */
    private List<PointMass> pointMasses = new ArrayList<>();

    /**
     * A set of {@link CollisionPair} objects, each representing a unique pair
     * of {@link PointMass} objects involved in a collision during the current
     * simulation step. This set ensures that each collision is only processed
     * once per update cycle.
     */
    private Set<CollisionPair> collisionPairs;


    // =========================== Constructors ============================= //
    /**
     * Constructs a new {@code CollisionSystem} with the specified simulation
     * area dimensions. Initialises the system by retrieving all entities of
     * type {@link EntityType#SOLID_BALL} and populating the internal lists with
     * their corresponding {@link Circle} and {@link PointMass} components.
     *
     * @param width  the width of the simulation area
     * @param height the height of the simulation area
     */
    public CollisionSystem(int width, int height) {
        this.width = width;
        this.height = height;

        // Retrieve all entities of type SOLID_BALL and initialise circles and pointMasses lists
        List<Entity> ents = Entities.getEntitiesOfType(EntityType.SOLID_BALL);
        componentCount = ents.size();
        for (Entity e : ents) {
            circles.add(e.getComponent(Circle.class));
            pointMasses.add(e.getComponent(PointMass.class));
        }
    }

    // ============================ API Methods ============================= //
    /**
     * Updates the state of the {@code CollisionSystem} by detecting and
     * resolving collisions between entities. This method does not directly
     * update the velocity or acceleration of the entities, but applies forces
     * that may affect these properties. It ensures that overlapping collisions
     * are immediately corrected to avoid unrealistic behaviour, such as objects
     * passing through one another.
     * 
     * <p>
     * The impulse magnitude equation is calculated with:</p>
     * <p>
     * <strong>J</strong> =
     * <span>&#8722;</span>
     * <span>&#40;</span>
     * <sup>1</sup>/<sub>m<sub>1</sub></sub> +
     * <sup>1</sup>/<sub>m<sub>2</sub></sub>
     * <span>&#41;</span>
     * &#40;1 + <em>e</em>&#41; &#183; v<sub>normal</sub>
     * </p>
     *
     * <p>
     * The update process includes:
     * <ul>
     *     <li>Detecting collisions between each {@link PointMass} and the
     *     boundaries of the simulation area.</li>
     *     <li>Identifying inter-object collisions between {@link PointMass}
     *     objects.</li>
     *     <li>Adding all detected collisions to a set of {@link CollisionPair}
     *     objects.</li>
     *     <li>Processing each collision to apply the appropriate forces and
     *     adjustments.</li>
     *     </ul>
     * </p>
     *
     */
    public void update() {
        PointMass p;
        Circle c;

        collisionPairs = new HashSet<>();
        for (int i = 0; i < componentCount; i++) {
            p = pointMasses.get(i);
            c = circles.get(i);

            applyBoundCollisionForces(p, c);
            findCollisionsOf(p, c);
        }
        
        for (CollisionPair pair : collisionPairs) {
            pair.processCollision();
        }
    }

    /**
     * Adds a new entity to the {@code CollisionSystem}, allowing it to
     * participate in the collision detection and resolution process. The entity
     * must have both a {@link PointMass} and a {@link Circle} component, which
     * are used for simulating collisions.
     *
     * <p>
     * If the entity is null, an {@link IllegalArgumentException} is thrown, as
     * null entities cannot be added to the system.
     * </p>
     *
     * @param e the {@link Entity} to be added to the collision system
     * @throws IllegalArgumentException if the provided entity is {@code null}
     * or does not contain the required components
     */
    public void add(Entity e) {
        if (e == null) {
            throw new IllegalArgumentException("Cannot add null components to CollisionSystem");
        }
        circles.add(e.getComponent(Circle.class));
        pointMasses.add(e.getComponent(PointMass.class));
    }


    // ========================== Helper Methods ============================ //
    /**
     * Applies boundary collision forces to the specified {@link PointMass} and
     * its corresponding {@link Circle} component. This method ensures that the
     * {@code PointMass} stays within the bounds of the simulation area by
     * detecting if the point mass has crossed the boundaries and, if so,
     * reversing its velocity and repositioning it at the boundary edge.
     *
     * <p>
     * If the {@code PointMass} crosses the left or right boundaries, its
     * horizontal velocity is reversed using {@link PointMass#bounceX()}, and
     * its position is corrected to the boundary. Similarly, if the point mass
     * crosses the top or bottom boundaries, its vertical velocity is reversed
     * using {@link PointMass#bounceY()}, and its position is corrected
     * accordingly.
     * </p>
     *
     * @param p the {@link PointMass} whose boundary collision forces are to be
     * applied
     * @param c the {@link Circle} representing the boundary of the
     * {@code PointMass}
     */
    private void applyBoundCollisionForces(PointMass p, Circle c) {
        double r = c.radius();
        double px = p.x();
        double py = p.y();

        if (px < r) {
            p.bounceX();
            p.setX(r);
        } else if (px > width - r) {
            p.bounceX();
            p.setX(width - r);
        }

        if (py < r) {
            p.bounceY();
            p.setY(r);
        } else if (py > height - r) {
            p.bounceY();
            p.setY(height - r);
        }
    }

    /**
     * Detects potential collisions between the specified {@link PointMass} and
     * all other {@link PointMass} objects in the system. This method compares
     * the positions of the given point mass and circle with others in the
     * system to check if their distance is less than or equal to the sum of
     * their radii, indicating a collision.
     *
     * <p>
     * For each detected collision, a new {@link CollisionPair} is created and
     * added to the set of collision pairs to be processed during the update
     * cycle. The method ensures that a point mass does not check for collisions
     * with itself.
     * </p>
     *
     * @param p1 the {@link PointMass} whose collisions are to be detected
     * @param c1 the {@link Circle} representing the boundary of {@code p1}
     */
    private void findCollisionsOf(PointMass p1, Circle c1) {
        for (int i = 0; i < componentCount; i++) {
            PointMass p2 = pointMasses.get(i);
            Circle c2    = circles.get(i);
            if (p1 == p2 || c1 == c2) {
                continue;
            }

            double rSum = c1.radius() + c2.radius();
            // Unit vector pointing from p2 to p1
            Vector2D normal = Vector2D.sub(p2.position(), p1.position());
            double distance = normal.mag();

            if (distance <= rSum) {
                collisionPairs.add(new CollisionPair(p1, p2, normal));
            }
        }
    }

    // =========================== Deprecated =============================== //
    /**
     * Handles collisions between a given {@code PointMass} and {@code Circle}
     * with all other {@code PointMass} and {@code Circle} components in the
     * system.
     *
     * <p>
     * This method calculates and resolves collisions by adjusting positions and
     * velocities of the colliding entities based on their masses and the
     * overlap between them. The method assumes a simplified physics model where
     * the circles represent the boundaries of the {@code PointMass} objects.
     * </p>
     *
     * <p>
     * <strong>Note:</strong> This method is deprecated and should not be used
     * in future implementations. The preferred approach is to use the updated
     * collision handling method that accounts for more complex interactions.
     * </p>
     *
     * @param p1 the {@code PointMass} object to check for collisions
     * @param c1 the {@code Circle} boundary of {@code p1} to check for
     * collisions
     *
     * @deprecated This method is outdated and may not handle all edge cases
     * correctly. Consider using a more robust collision handling method.
     */
    @Deprecated
    private void handleRigidBodyCollisions(PointMass p1, Circle c1) {
        for (int i = 0; i < componentCount; i++) {
            PointMass p2 = pointMasses.get(i);
            Circle c2 = circles.get(i);
            if (p1 == p2 || c1 == c2) {
                continue;
            }
            // Note: Vector2D static methods produce new vectors without modifying their arguments
            double rSum = c1.radius() + c2.radius();
            // Also represents the line of impact
            Vector2D normal1 = Vector2D.sub(p2.position(), p1.position()); // p2 - p1
            double distance = normal1.mag();

            if (distance <= rSum) {
                double overlap = rSum - distance;
                Vector2D distanceCorrection = normal1.copy().setMag(overlap / 2);
                p1.position().sub(distanceCorrection);
                p2.position().add(distanceCorrection);

                double m1 = (2 * p2.mass()) / (p1.mass() + p2.mass());
                double m2 = (2 * p1.mass()) / (p1.mass() + p2.mass());

                Vector2D normal2 = Vector2D.sub(p1.position(), p2.position()); // p1 - p2 
                Vector2D v1 = Vector2D.sub(p2.velocity(), p1.velocity());      // v2 - v1
                Vector2D v2 = Vector2D.sub(p1.velocity(), p2.velocity());      // v1 - v2

                double dot1 = v1.dot(normal1);
                double dot2 = v2.dot(normal2);
                double distanceSquared = rSum * rSum;

                double middleTerm1 = dot1 / distanceSquared;
                double middleTerm2 = dot2 / distanceSquared;

                p1.velocity().add(normal1.scale(m1 * middleTerm1));
                p2.velocity().add(normal2.scale(m2 * middleTerm2));
            }
        }
    }

}