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
                        new PointMass(Vector2D.zero(),     Vector2D.zero(), 10, 0.5, 1, false),
                        new PointMass(Vector2D.of(10, 10), Vector2D.zero(), 10, 0.5, 1, false)
                ),
                
                Arguments.of(
                        new PointMass(Vector2D.zero(),       Vector2D.zero(), 1, 0.5, 1, false),
                        new PointMass(Vector2D.of(-25, 5.2), Vector2D.zero(), 1, 0.5, 1, false)
                ),
                
                Arguments.of(
                        new PointMass(Vector2D.of(20, -5), Vector2D.zero(), 5,  0.2, 1, true),
                        new PointMass(Vector2D.of(10, 10), Vector2D.zero(), 10, 0.5, 1, false)
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

        assertAll(
                () -> assertEquals(expectedDisplacementMag1, spring.calculateDisplacement().mag()),
                () -> assertEquals(expectedDisplacementMag2, spring.calculateDisplacement().mag())
        );
    }
    
    @Test
    public void testDisplacement_EdgeCase_Zero() {
        PointMass p1 = new PointMass(Vector2D.of(10, 10), Vector2D.zero(), 10, 0.5, 1, false);
        PointMass p2 = new PointMass(Vector2D.of(10, 10), Vector2D.zero(), 10, 0.5, 1, false);
        PointMass p3 = new PointMass(Vector2D.zero(),     Vector2D.zero(), 10, 0.5, 1, false);
        PointMass p4 = new PointMass(Vector2D.zero(),     Vector2D.zero(), 10, 0.5, 1, false);
        
        Spring spring1 = Spring.between(p1, p2);
        Spring spring2 = Spring.between(p3, p4);

        assertAll(
                () -> assertEquals(0, spring1.calculateDisplacement().mag()),
                () -> assertEquals(0, spring2.calculateDisplacement().mag()),
                () -> assertTrue(Vector2D.ZERO.matches(spring1.calculateDisplacement())),
                () -> assertTrue(Vector2D.ZERO.matches(spring2.calculateDisplacement()))
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
        
        assertAll(
                () -> assertNotEquals(
                        initialDisplacement1, spring.calculateDisplacement().mag(),
                        "Displacement magnitude should change after pointB moves (initialDisplacement1 comparison failed)"),
                
                () -> assertNotEquals(
                        initialDisplacement1, spring.calculateDisplacement().mag(),
                        "Displacement vector magnitude should change after pointB moves (initialDisplacement1 comparison failed)"),
                
                () -> assertNotEquals(
                        initialDisplacement2, spring.calculateDisplacement().mag(),
                        "Displacement magnitude should change after pointB moves (initialDisplacement2 comparison failed)"),
                
                () -> assertNotEquals(
                        initialDisplacement2, spring.calculateDisplacement().mag(),
                        "Displacement vector magnitude should change after pointB moves (initialDisplacement2 comparison failed)"),
                
                () -> assertEquals(
                        newDisplacement1, spring.calculateDisplacement().mag(),
                        "Displacement magnitude should match the newly calculated displacement after pointB moves (newDisplacement1 comparison failed)"),
                
                () -> assertEquals(
                        newDisplacement1, spring.calculateDisplacement().mag(),
                        "Displacement vector magnitude should match the newly calculated displacement after pointB moves (newDisplacement1 comparison failed)"),
                
                () -> assertEquals(
                        newDisplacement2, spring.calculateDisplacement().mag(),
                        "Displacement magnitude should match the manually calculated displacement after pointB moves (newDisplacement2 comparison failed)"),
             
                () -> assertEquals(
                        newDisplacement2, spring.calculateDisplacement().mag(),
                        "Displacement vector magnitude should match the manually calculated displacement after pointB moves (newDisplacement2 comparison failed)")
        );

    }

    
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testDisplacementCache_InvalidationOnPointMove(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);

        // Get initial calculateDisplacement and cache it
        Vector2D initialDisplacement          = spring.calculateDisplacement();
        double   initialDisplacementMagnitude = spring.calculateDisplacement().mag();

        // Move pointB to invalidate the cached calculateDisplacement
        p2.setX(p2.x() + 10);
        p2.setY(p2.y() + 10);

        // Get the new calculateDisplacement and ensure it has changed
        Vector2D newDisplacement          = spring.calculateDisplacement();
        double   newDisplacementMagnitude = spring.calculateDisplacement().mag();
        
        assertAll(
                () -> assertNotSame(
                        initialDisplacement, newDisplacement,
                        "Initial displacement and new displacement return the same instance"
                ),
                // Ensure the cached calculateDisplacement is invalidated and recalculated
                () -> assertNotEquals(
                        initialDisplacementMagnitude, newDisplacementMagnitude, 
                        newDisplacementMagnitude + " should not be equal to " + initialDisplacement
                ),
                () -> assertNotEquals(
                        initialDisplacement.mag(),    newDisplacement.mag(), 
                        initialDisplacement.mag() + " should not be equal to " + newDisplacement.mag()
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
        // Ensure no force is applied when the spring is at rest length
        Vector2D force = spring.force();

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
        // Ensure no force is applied when the spring is at rest length
        Vector2D force = spring.force();

        assertAll(
                () -> assertEquals(0, abs(force.x()),   "Force X should be 0 at rest length, but was " + force.x()),
                () -> assertEquals(0, abs(force.y()),   "Force Y should be 0 at rest length, but was " + force.y()),
                () -> assertEquals(0, Double.compare(0, force.mag()), "Force magnitude should be 0 at rest length, but was " + force.mag())
        );
    }

    @Test
    public void testForce_PositiveStretch_NegativeForce() {
        PointMass p1 = new PointMass(Vector2D.zero(),     Vector2D.zero(), 10, 0.5, 1, false);
        PointMass p2 = new PointMass(Vector2D.of(10, 10), Vector2D.zero(), 10, 0.5, 1, false);
        Spring spring = Spring.between(p1, p2);

        // Stretch the spring beyond its rest length
        p2.move(20, 20);
        
        Vector2D force = spring.force();

        assertAll(
                () -> assertTrue(force.x() < 0, "Force X should be negative, pulling towards p1"),
                () -> assertTrue(force.y() < 0, "Force Y should be negative, pulling towards p1")
        );
    }
    
    @Test
    public void testForce_NegativeStretch_PositiveForce() {
        PointMass p1 = new PointMass(Vector2D.zero(),     Vector2D.zero(), 10, 0.5, 1, false);
        PointMass p2 = new PointMass(Vector2D.of(10, 10), Vector2D.zero(), 10, 0.5, 1, false);
        Spring spring = Spring.between(p1, p2);

        p2.move(-5, -5); // compress points
        
        Vector2D force = spring.force();
        assertAll(
                () -> assertTrue(force.x() > 0, "Force X should be negative, pulling towards p1"),
                () -> assertTrue(force.y() > 0, "Force Y should be negative, pulling towards p1")
        );
    }

    @Test
    public void testForce_MagnitudeConsistency() {
        PointMass p1 = new PointMass(Vector2D.zero(),     Vector2D.zero(), 10, 0.5, 1, false);
        PointMass p2 = new PointMass(Vector2D.of(50, 50), Vector2D.zero(), 10, 0.5, 1, false);
        Spring spring = Spring.between(p1, p2);
        double initialForceMagnitude = spring.force().mag();

        // Move p2 further to increase the stretch
        p2.move(10, 10);
        double forceAfterStretch = spring.force().mag();
        
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
        p2.setX(p2.x() + 0.01);
        p2.setY(p2.y() + 0.01);

        Vector2D force = spring.force();

        assertAll(
                () -> assertTrue(force.mag() > 0, "Force magnitude should be small but non-zero for very small stretch")
        );
    }

    // ============================ Relax Tests ============================= //
    @ParameterizedTest
    @MethodSource("providePointMasses")
    public void testRelax_UpdatesRestLength(PointMass p1, PointMass p2) {
        Spring spring = Spring.between(p1, p2);

        // Move p2 to create a different calculateDisplacement
        p2.setX(p2.x() + 5);
        p2.setY(p2.y() + 5);

        // Capture the new calculateDisplacement before calling relax
        double expectedRestLength = Vector2D.sub(p2.position(), p1.position()).mag();

        // Call relax
        spring.relax();

        // Check if the restLength is updated to the current calculateDisplacement
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
        
        // Capture the expected calculateDisplacement before calling relax
        Vector2D expectedDisplacement = Vector2D.sub(p2.position(), p1.position());

        // Call relax
        spring.relax();

        // Ensure that calculateDisplacement is updated correctly
        assertTrue(expectedDisplacement.matches(spring.calculateDisplacement()),
                "Displacement should be recalculated when relax() is called after moving the points");
    }

}