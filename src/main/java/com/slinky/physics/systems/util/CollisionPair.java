package com.slinky.physics.systems.util;

import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;
import java.util.Objects;

/**
 * Represents a pair of {@link PointMass} objects involved in a collision. This
 * class is designed to manage and process collisions symmetrically, ensuring
 * that the pairs (first, second) and (second, first) are treated as equivalent.
 * This is useful where the order of collision participants does not affect the
 * outcome.
 *
 * <p>
 * The class not only checks for equality between pairs but also handles the
 * resolution of collisions by correcting positions to avoid overlap and
 * applying impulse forces to simulate realistic collisions. It encapsulates the
 * collision handling logic within the outer class and is marked private to
 * prevent its use outside this context.
 * </p>
 *
 * <p>
 * The collision resolution includes adjustments to the positions of the
 * {@link PointMass} objects to correct any overlap, and it calculates and
 * applies impulse forces based on their relative velocities and masses to
 * simulate an elastic collision.
 * </p>
 *
 * <p>
 * This class is designed to work efficiently within collections, such as
 * {@link java.util.HashSet}, due to its symmetric handling of hash codes,
 * ensuring that the pair (A, B) produces the same hash code as (B, A).
 * </p>
 * 
 * @version 1.2
 * @since   0.1.0
 * 
 * @author  Kheagen Haskins
 * 
 * @see     PointMass
 */
public class CollisionPair {

    /**
     * The first {@link PointMass} involved in the collision.
     */
    private final PointMass p1;

    /**
     * The second {@link PointMass} involved in the collision.
     */
    private final PointMass p2;

    /**
     * The normal vector representing the line of impact between the two
     * {@link PointMass} objects.
     */
    private Vector2D normal;

    /**
     * Constructs a new {@code CollisionPair} with the specified
     * {@link PointMass} objects, normal vector, distance between them, and the
     * sum of their radii. The order of the {@link PointMass} objects does not
     * matter, as (first, second) is treated as equivalent to (second, first).
     *
     * @param first the first {@link PointMass} in the pair
     * @param second the second {@link PointMass} in the pair
     * @param normal the normal vector representing the line of impact
     */
    public CollisionPair(PointMass first, PointMass second, Vector2D normal) {
        this.p1 = first;
        this.p2 = second;
        this.normal = normal;
    }

    /**
     * Checks whether this {@code CollisionPair} is equal to another object.
     * Equality is based on the symmetric nature of the pair: (first, second) is
     * considered equal to (second, first).
     *
     * @param obj the object to compare with this {@code CollisionPair} for
     * equality
     * @return true if the specified object is also a {@code CollisionPair} and
     * represents the same pair of {@link PointMass} objects, regardless of
     * order
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CollisionPair other = (CollisionPair) obj;
        return (Objects.equals(p1, other.p1) && Objects.equals(p2, other.p2))
                || (Objects.equals(p1, other.p2) && Objects.equals(p2, other.p1));
    }

    /**
     * Returns a hash code value for this {@code CollisionPair}. The hash code
     * is designed to be symmetrical, meaning that the hash code for (first,
     * second) will be the same as for (second, first), supporting the use of
     * this class in collections like {@link java.util.HashSet}.
     *
     * @return a hash code value for this {@code CollisionPair}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(p1, p2) + Objects.hash(p2, p1);
    }

    /**
     * Processes the collision between the two {@link PointMass} objects by
     * correcting their positions to eliminate overlap and applying impulse
     * forces to simulate an elastic collision. The positions are adjusted by
     * moving each object away from the other along the line of impact, and the
     * velocities are updated based on the relative velocities and masses of the
     * objects.
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
     * @return the magnitude of the impulse force
     */
    public double processCollision() {
        normal.normalize(); // the line of impact

        Vector2D vRel = Vector2D.sub(p1.velocity(), p2.velocity());
        double vNormal = vRel.dot(normal);
        if (vNormal < 0) {
            return 0;
        }

        double e = (p1.restitution() + p2.restitution()) / 2;
        double impulseMagnitude = (-(1 + e) * vNormal) / (1 / p1.mass() + 1 / p2.mass());
        Vector2D impulseForce = normal.copy().scale(impulseMagnitude);

        p1.addForce(impulseForce);
        p2.addForce(impulseForce.copy().negate());
        
        return impulseMagnitude;
    }
    
}