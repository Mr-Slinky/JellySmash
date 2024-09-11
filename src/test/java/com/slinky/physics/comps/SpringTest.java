package com.slinky.physics.comps;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 *
 * @author Kheagen Haskins
 */
public class SpringTest {

    // ========================= Provider Methods =========================== //
    private static Stream<Arguments> providePointMasses() {
        return Stream.of(
                Arguments.of(
                        PointMass.of(Vector2D.zero(),     Vector2D.zero(), 10, 1, false),
                        PointMass.of(Vector2D.of(10, 10), Vector2D.zero(), 10, 1, false)
                ),
                
                Arguments.of(
                        PointMass.of(Vector2D.zero(),       Vector2D.zero(), 1, 1, false),
                        PointMass.of(Vector2D.of(-25, 5.2), Vector2D.zero(), 1, 1, false)
                ),
                
                Arguments.of(
                        PointMass.of(Vector2D.of(20, -5), Vector2D.zero(), 5,  1, true),
                        PointMass.of(Vector2D.of(10, 10), Vector2D.zero(), 10, 1, false)
                )
        );
    }
    
    private static Stream<Arguments> provideForceTestParams() {
        return Stream.of(
                // No relative velocity, simple spring calculateSpringForce
                Arguments.of(
                        3,    // restLength
                        5,    // deltaX
                        0,    // deltaY
                        50,   // k
                        2,    // damping
                        3,    // vx
                        0,    // vy
                        -106, // expectedForceX
                        0     // expectedForceY
                ),
                // Pull back
                Arguments.of(
                        5,       // restLength
                        6,       // deltaX
                        8,       // deltaY
                        100,     // k
                        3,       // damping
                        4,       // vx
                        3,       // vy
                        -308.64, // expectedForceX
                        -411.52  // expectedForceY
                ),
                // No movement - No Force
                Arguments.of(
                        5,  // restLength
                        5,  // deltaX
                        0,  // deltaY
                        50, // k
                        3,  // damping
                        0,  // vx
                        0,  // vy
                        0,  // expectedForceX
                        0   // expectedForceY
                ),
                // Compression - basic
                Arguments.of(
                        7,  // restLength
                        4,  // deltaX
                        3,  // deltaY
                        80, // k
                        4,  // damping
                       -2,  // vx
                       -2,  // vy
                   136.96,  // expectedForceX
                   102.72   // expectedForceY
                ),
                // Compression - faster
                Arguments.of(
                       10,  // restLength
                        3,  // deltaX
                        4,  // deltaY
                      150, // k
                        6,  // damping
                       -5,  // vx
                       -4,  // vy
                   472.32,  // expectedForceX
                   629.76   // expectedForceY
                ),
                // Compression - large displacement
                Arguments.of(
                       10,  // restLength
                       12,  // deltaX
                       16,  // deltaY
                      120,  // k
                        5,  // damping
                        6,  // vx
                        8,  // vy
                     -750,  // expectedForceX
                    -1000   // expectedForceY
                )
        );
    }

    // ========================= Constructor Tests ========================== //
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testConstructor_NoErrorWithDifferentPararms(PointMass p1, PointMass p2) {
        assertAll(
                () -> assertDoesNotThrow(() -> Spring.between(p1, p2)),
                () -> assertThrows(IllegalArgumentException.class, () -> Spring.between(null, p2)),
                () -> assertThrows(IllegalArgumentException.class, () -> Spring.between(p1, null))
        );
    }
    
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testConstructor_InitialState(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        assertAll(
                () -> assertTrue ( spring.isActive()),
                () -> assertFalse(!spring.isActive())
        );
    }

    // =========================== Active Tests ============================= //
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testConstructor_ActiveMutates(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        spring.setActive(false);
        assertAll(
                () -> assertFalse ( spring.isActive()),
                () -> assertTrue  (!spring.isActive())
        );
    }
    
    // ======================== Points Fetch Test =========================== //
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testGetPointA_and_B(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        assertAll(
                () -> assertSame   (spring.pointA(), p1),
                () -> assertSame   (spring.pointB(), p2),
                () -> assertNotSame(spring.pointA(), p2),
                () -> assertNotSame(spring.pointB(), p1)
        );
    }
    
    // ======================== Displacement Tests ========================== //
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testDisplacement_AtRest(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        double expectedDisplacementMag1 = Vector2D.sub(p2.position(), p1.position()).mag();

        double x = p2.x() - p1.x();
        double y = p2.y() - p1.y();
        double expectedDisplacementMag2 = sqrt(x * x + y * y);

        spring.calculateDisplacement();
        assertAll(
                () -> assertEquals(expectedDisplacementMag1, spring.displacement().mag()),
                () -> assertEquals(expectedDisplacementMag2, spring.displacement().mag())
        );
    }
    
    @Test
    public void testDisplacement_EdgeCase_Zero() {
        PointMass p1 = PointMass.of(Vector2D.of(10, 10), Vector2D.zero(), 10, 1, false);
        PointMass p2 = PointMass.of(Vector2D.of(10, 10), Vector2D.zero(), 10, 1, false);
        PointMass p3 = PointMass.of(Vector2D.zero(),     Vector2D.zero(), 10, 1, false);
        PointMass p4 = PointMass.of(Vector2D.zero(),     Vector2D.zero(), 10, 1, false);
        
        Spring spring1 = Spring.between(p1, p2);
        Spring spring2 = Spring.between(p3, p4);
        
        spring1.calculateDisplacement();
        spring2.calculateDisplacement();

        assertAll(
                () -> assertEquals(0, spring1.displacement().mag()),
                () -> assertEquals(0, spring2.displacement().mag()),
                () -> assertTrue(Vector2D.ZERO.matches(spring1.displacement())),
                () -> assertTrue(Vector2D.ZERO.matches(spring2.displacement()))
        );
    }
 
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testDisplacement_AfterMoved(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        double initialDisplacement1 = Vector2D.sub(p2.position(), p1.position()).mag();
        double x = p2.x() - p1.x();
        double y = p2.y() - p1.y();
        double initialDisplacement2 = sqrt(x * x + y * y);

        p1.setX(p1.x() - 10);
        p1.setY(p1.y() - 10);
        
        p2.setX(p2.x() + 10);
        p2.setY(p2.y() + 10);

        double newDisplacement1 = Vector2D.sub(p2.position(), p1.position()).mag();
        x = p2.x() - p1.x();
        y = p2.y() - p1.y();
        double newDisplacement2 = sqrt(x * x + y * y); 
        spring.calculateDisplacement();

        assertAll(
                () -> assertNotEquals(
                        initialDisplacement1, spring.displacement().mag(),
                        "Displacement magnitude should change after pointB moves (initialDisplacement1 comparison failed)"),
                
                () -> assertNotEquals(
                        initialDisplacement1, spring.displacement().mag(),
                        "Displacement vector magnitude should change after pointB moves (initialDisplacement1 comparison failed)"),
                
                () -> assertNotEquals(
                        initialDisplacement2, spring.displacement().mag(),
                        "Displacement magnitude should change after pointB moves (initialDisplacement2 comparison failed)"),
                
                () -> assertNotEquals(
                        initialDisplacement2, spring.displacement().mag(),
                        "Displacement vector magnitude should change after pointB moves (initialDisplacement2 comparison failed)"),
                
                () -> assertEquals(
                        newDisplacement1, spring.displacement().mag(),
                        "Displacement magnitude should match the newly calculated displacement after pointB moves (newDisplacement1 comparison failed)"),
                
                () -> assertEquals(
                        newDisplacement1, spring.displacement().mag(),
                        "Displacement vector magnitude should match the newly calculated displacement after pointB moves (newDisplacement1 comparison failed)"),
                
                () -> assertEquals(
                        newDisplacement2, spring.displacement().mag(),
                        "Displacement magnitude should match the manually calculated displacement after pointB moves (newDisplacement2 comparison failed)"),
             
                () -> assertEquals(
                        newDisplacement2, spring.displacement().mag(),
                        "Displacement vector magnitude should match the manually calculated displacement after pointB moves (newDisplacement2 comparison failed)")
        );

    }

    
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testDisplacementCache_InvalidationOnPointMove(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        spring.calculateDisplacement();
        
        // Get initial displacement and cache it
        Vector2D initialDisplacement          = spring.displacement();
        double   initialDisplacementMagnitude = initialDisplacement.mag();

        // Move pointB to invalidate the cached displacement
        p2.move(35, 35);
        spring.calculateDisplacement();
        // Get the new displacement and ensure it has changed
        Vector2D newDisplacement          = spring.displacement();
        double   newDisplacementMagnitude = newDisplacement.mag();
        
        assertAll(
                // Ensure the cached displacement is invalidated and recalculated
                () -> assertNotEquals(
                        initialDisplacementMagnitude, newDisplacementMagnitude,
                        newDisplacementMagnitude + " should not be equal to " + initialDisplacement
                )
        );
    }

    // ========================= Rest Length Tests ========================== //
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testRestLength_MatchesIntialDisplacement(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        double initialDisplacement1 = Vector2D.sub(p2.position(), p1.position()).mag();
        
        assertEquals(initialDisplacement1, spring.restLength());
    }
    
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testRestLength_NoAccidentalStateChange(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        double initialDisplacement1 = Vector2D.sub(p2.position(), p1.position()).mag();
        
        p1.setX(27);
        p1.setY(21);
        p2.setX(30);
        p2.setY(20);
        
        assertEquals(initialDisplacement1, spring.restLength());
    }
    
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testRestLength_ScalesCorrectly(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        double initialDisplacement1 = Vector2D.sub(p2.position(), p1.position()).mag();
        double initialRestLength    = spring.restLength();
        
        double scalar = initialRestLength + 5;
        spring.scaleRestLength(scalar);
        
        assertAll(
                () -> assertNotEquals(initialDisplacement1,       spring.restLength()),
                () -> assertEquals   (initialDisplacement1,       initialRestLength),
                () -> assertEquals   (initialRestLength * scalar, spring.restLength())

        );
    }
    
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testRestLength_Mutates(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        double initialDisplacement1 = Vector2D.sub(p2.position(), p1.position()).mag();
        double initialRestLength    = spring.restLength();
        
        double newRest = initialRestLength + 5;
        spring.setRestLength(newRest);
        
        assertAll(
                () -> assertNotEquals(initialDisplacement1, spring.restLength()),
                () -> assertEquals   (initialDisplacement1, initialRestLength),
                () -> assertEquals   (newRest,              spring.restLength())

        );
    }

    // ============================ Force Tests ============================= //
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testForce_AtRestLength(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        // Ensure no calculateSpringForce is applied when the spring is at rest length
        Vector2D force = spring.calculateSpringForce();

        assertAll(
                () -> assertEquals(0, abs(force.x()),   "Force X should be 0 at rest length, but was " + force.x()),
                () -> assertEquals(0, abs(force.y()),   "Force Y should be 0 at rest length, but was " + force.y()),
                () -> assertEquals(0, Double.compare(0, force.mag()), "Force magnitude should be 0 at rest length, but was " + force.mag())
        );
    }
    
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testForce_AtRestLength_AfterMove(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);
        
        p1.move(10, 10);
        p2.move(10, 10);
        // Ensure no calculateSpringForce is applied when the spring is at rest length
        Vector2D force = spring.calculateSpringForce();

        assertAll(
                () -> assertEquals(0, abs(force.x()),   "Force X should be 0 at rest length, but was " + force.x()),
                () -> assertEquals(0, abs(force.y()),   "Force Y should be 0 at rest length, but was " + force.y()),
                () -> assertEquals(0, Double.compare(0, force.mag()), "Force magnitude should be 0 at rest length, but was " + force.mag())
        );
    }

    @Test
    public void testForce_PositiveStretch_NegativeForce() {
        PointMass p1 = PointMass.of(Vector2D.zero(),     Vector2D.zero(), 10, 1, false);
        PointMass p2 = PointMass.of(Vector2D.of(10, 10), Vector2D.zero(), 10, 1, false);
        Spring spring = Spring.between(p1, p2);

        // Stretch the spring beyond its rest length
        p2.move(20, 20);
        
        Vector2D force = spring.calculateSpringForce();

        assertAll(
                () -> assertTrue(force.x() < 0, "Force X should be negative, pulling towards p1"),
                () -> assertTrue(force.y() < 0, "Force Y should be negative, pulling towards p1")
        );
    }
    
    @Test
    public void testForce_NegativeStretch_PositiveForce() {
        PointMass p1 = PointMass.of(Vector2D.zero(),     Vector2D.zero(), 10, 1, false);
        PointMass p2 = PointMass.of(Vector2D.of(10, 10), Vector2D.zero(), 10, 1, false);
        Spring spring = Spring.between(p1, p2);

        p2.move(-5, -5); // compress points
        
        Vector2D force = spring.calculateSpringForce();
        assertAll(
                () -> assertTrue(force.x() > 0, "Force X should be negative, pulling towards p1"),
                () -> assertTrue(force.y() > 0, "Force Y should be negative, pulling towards p1")
        );
    }

    @Test
    public void testForce_MagnitudeConsistency() {
        PointMass p1 = PointMass.of(Vector2D.zero(),     Vector2D.zero(), 10, 1, false);
        PointMass p2 = PointMass.of(Vector2D.of(50, 50), Vector2D.zero(), 10, 1, false);
        Spring spring = Spring.between(p1, p2);
        double initialForceMagnitude = spring.calculateSpringForce().mag();

        // Move p2 further to increase the stretch
        p2.move(10, 10);
        double forceAfterStretch = spring.calculateSpringForce().mag();
        
        assertAll(
                () -> assertEquals(0, initialForceMagnitude,                 "Force magnitude should begin at 0"),
                () -> assertTrue(forceAfterStretch  > initialForceMagnitude, "Force magnitude should increase with stretch ")
        );
    }
    
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testForce_VerySmallStretch(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);

        // Slightly move p2
        p2.move(0.001, 0.001);

        Vector2D force = spring.calculateSpringForce();

        assertAll(
                () -> assertTrue(force.mag() > 0, "Force magnitude should be small but non-zero for very small stretch")
        );
    }

    @ParameterizedTest
    @MethodSource("provideForceTestParams")
    public void testForce_WithRelativeVelocity(double restLength, double deltaX, double deltaY, double k, double damping, double vx, double vy, double expectedForceX, double expectedForceY) {
        PointMass p1 = PointMass.at(0, 0); // Anchor
        PointMass p2 = PointMass.at(0, 0); // Bob


        // Set relative velocity for velocity-based damping
        p1.velocity().setComponents(0, 0);   // Stationary anchor
        p2.velocity().setComponents(vx, vy); // Moving point

        Spring spring = Spring.between(p1, p2, k, damping);
        spring.setRestLength(restLength);
        // Simulate movement
        p2.move(deltaX, deltaY);
        // Calculate expected magnitude of the spring calculateSpringForce
        double expectedMagnitude = Math.hypot(expectedForceX, expectedForceY);
        Vector2D force = spring.calculateSpringForce();
        
        assertAll (
                // Check the magnitude of the combined spring and damping calculateSpringForce
                () -> assertEquals(expectedMagnitude, force.mag(), 0.001),
                // Verify the X component of the calculateSpringForce
                () -> assertEquals(expectedForceX,    force.x(),   0.001),
                // Verify the Y component of the calculateSpringForce
                () -> assertEquals(expectedForceY,    force.y(),   0.001)
        );
    }




    // ============================ Relax Tests ============================= //
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testRelax_UpdatesRestLength(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);

        // Move p2 to create a different displacement
        p2.setX(p2.x() + 5);
        p2.setY(p2.y() + 5);

        // Capture the new displacement before calling relax
        double expectedRestLength = Vector2D.sub(p2.position(), p1.position()).mag();

        // Call relax
        spring.relax();

        // Check if the restLength is updated to the current displacement
        assertEquals(expectedRestLength, spring.restLength(),
                "restLength should be updated to the current displacement after relax() is called");
    }
    
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testRelax_NoChangeIfAtRest(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);

        // Store the initial rest length (which should be the distance between p1 and p2)
        double initialRestLength = spring.restLength();

        // Call relax without moving the points
        spring.relax();

        // Check that the rest length hasn't changed
        assertEquals(initialRestLength, spring.restLength(),
                "restLength should remain unchanged when relax() is called and the spring is at rest");
    }

    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testRelax_RecalculatesDisplacement(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);

        // Move p1 and p2
        p1.move(-10, -10);
        p2.move( 10,  10);
        
        // Capture the expected displacement before calling relax
        Vector2D expectedDisplacement = Vector2D.sub(p2.position(), p1.position());

        // Call relax
        spring.relax();
        spring.calculateDisplacement();
        
        // Ensure that displacement is updated correctly
        assertTrue(expectedDisplacement.matches(spring.displacement()),
                "Displacement should be recalculated when relax() is called after moving the points");
    }

}