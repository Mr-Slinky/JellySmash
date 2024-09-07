package com.slinky.physics.systems.util;

import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 *
 * @author Kheagen Haskins
 */
public class CollisionPairTest {
    
    @Test
    public void testCollision_CorrectImpulseForce() {
        PointMass p1 = new PointMass(
                new Vector2D( 2,  0), // position
                new Vector2D( 3,  0), // velocity
                new Vector2D( 0,  0), // acceleration
                2,
                0,
                0.9,
                false
        );
        
        PointMass p2 = new PointMass(
                new Vector2D( 6,  0), // position
                new Vector2D(-1,  0), // velocity
                new Vector2D( 0,  0), // acceleration
                3,
                0,
                0.9,
                false
        );
        
        Vector2D normal = Vector2D.sub(p2.position(), p1.position());
        Vector2D vRel = Vector2D.sub(p1.velocity(), p2.velocity());
        CollisionPair cp = new CollisionPair(p1, p2, normal);
        
        final double upperTol = 9.121;
        final double lowerTol = 9.119;
        assertAll(
                () -> assertEquals(4, normal.mag()),
                () -> assertEquals(4, vRel.x()),
                () -> assertEquals(0, vRel.y()),
                () -> assertDoesNotThrow(() -> cp.processCollision()),
                () -> assertTrue(p1.force().mag() <=  upperTol && p1.force().mag() >=  lowerTol, "Expected force of roughly  9.12 but was " + p1.force().mag()),
                () -> assertTrue(p2.force().mag() <=  upperTol && p2.force().mag() >=  lowerTol, "Expected force of roughly  9.12 but was " + p2.force().mag())
        );
    }
    
}
