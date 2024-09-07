package com.slinky.physics.systems.util;

import java.util.stream.Stream;

import com.slinky.physics.base.Entities;
import com.slinky.physics.comps.PointMass;
import com.slinky.physics.comps.Vector2D;
import com.slinky.physics.systems.MotionSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;



/**
 * Tests the different aspects of the PhysicsEngine, which itself is just a
 * coordinator of different sub systems
 *
 * @author Kheagen Haskins
 */
public class PhysicsEngineTest {
    
    @BeforeEach
    public void setup() {
        
    }
    
    @AfterEach
    public void cleanup() {
        Entities.clean();
    }
    
    private static Stream<Arguments> provideExpectedPointAccelerations() {
        PointMass p1 = new PointMass(
                new Vector2D(2, 0), // position
                new Vector2D(3, 0), // velocity
                new Vector2D(0, 0), // acceleration
                2,
                0,
                0.9,
                false
        );

        PointMass p2 = new PointMass(
                new Vector2D( 6, 0), // position
                new Vector2D(-1, 0), // velocity
                new Vector2D( 0, 0), // acceleration
                3,
                0,
                0.9,
                false
        );
        
        return Stream.of(
                Arguments.of(p1, p2, 4.56, 3.04, 0.001)
        );
    } // Used below
    
    @ParameterizedTest
    @MethodSource("provideExpectedPointAccelerations")
    public void testPostCollision_CorrectAccelerationUpdate(PointMass p1, PointMass p2, double expectedA1Mag, double expectedA2Mag, double tolerance) {
        MotionSystem motionSystem = new MotionSystem(IntegrationMethods.EULER);
        motionSystem.add(p1);
        motionSystem.add(p2);
        
        CollisionPair cp = new CollisionPair(p1, p2, Vector2D.sub(p2.position(), p1.position()));
        cp.processCollision();
        
        motionSystem.updateAccelerations();
        double a1Mag = p1.acceleration().mag();
        double a2Mag = p2.acceleration().mag();
        
        
        assertAll(
                // Check that the magnitude of acceleration for Particle 1 is within the expected range
                () -> assertTrue(
                        Math.abs(a1Mag - expectedA1Mag) <= tolerance,
                        "Particle 1 acceleration magnitude should be within tolerance of " + expectedA1Mag + ", but was " + a1Mag
                ),
                // Check that the x-component of Particle 1's acceleration is within the expected range
                () -> assertTrue(
                        Math.abs(p1.acceleration().x() + expectedA1Mag) <= tolerance,
                        "Particle 1 x-acceleration should approximate at " + -expectedA1Mag + ", but was " + p1.acceleration().x()
                ),
                // Check that the magnitude of acceleration for Particle 2 is within the expected range
                () -> assertTrue(
                        Math.abs(a2Mag - expectedA2Mag) <= tolerance,
                        "Particle 2 acceleration magnitude should be within tolerance of " + expectedA2Mag + ", but was " + a2Mag
                ),
                // Check that the x-component of Particle 2's acceleration is within the expected range
                () -> assertTrue(
                        Math.abs(p2.acceleration().x() - expectedA2Mag) <= tolerance,
                        "Particle 2 x-acceleration should approximate at " + expectedA2Mag + ", but was " + p2.acceleration().x()
                )
        );
    }
    
    private static Stream providePointsWithExpectedVelocitiesAfterCollision() {
         PointMass p1 = new PointMass(
                new Vector2D(2, 0), // position
                new Vector2D(3, 0), // velocity
                2,
                0,
                0.9,
                false
        );

        PointMass p2 = new PointMass(
                new Vector2D( 6, 0), // position
                new Vector2D(-1, 0), // velocity
                3,
                0,
                0.9,
                false
        );

        return Stream.of(
                Arguments.of(
                        p1, p2, 
                        new Vector2D(-1.56, 0), new Vector2D(2.04, 0), 
                        0.001, 1
                )
        );
    }
    
    @ParameterizedTest
    @MethodSource("providePointsWithExpectedVelocitiesAfterCollision")
    public void testPostCollision_CorrectVelocityUpdate(
            PointMass p1, PointMass p2, Vector2D expectedV1, Vector2D expectedV2, double tolerance, double deltaTime
    ) {
        MotionSystem motionSystem = new MotionSystem(IntegrationMethods.EULER);
        motionSystem.add(p1);
        motionSystem.add(p2);
        
        CollisionPair cp = new CollisionPair(p1, p2, Vector2D.sub(p2.position(), p1.position()));
        cp.processCollision();
        
        motionSystem.updateAccelerations();
        motionSystem.updateVelocitiesAndPositions(deltaTime);
        
                assertAll(
                // Check that the x-component of Particle 1's velocity is within the expected range
                () -> assertTrue(
                        Math.abs(p1.velocity().x() - expectedV1.x()) <= tolerance,
                        "Particle 1 velocity x component should be within tolerance of " + expectedV1.x() + ", but was " + p1.velocity().x()
                ),
                // Check that the y-component of Particle 1's velocity is within the expected range
                () -> assertTrue(
                        Math.abs(p1.velocity().y() - expectedV1.y()) <= tolerance,
                        "Particle 1 velocity y component should be within tolerance of " + expectedV1.y() + ", but was " + p1.velocity().y()
                ),
                // Check that the x-component of Particle 2's velocity is within the expected range
                () -> assertTrue(
                        Math.abs(p2.velocity().x() - expectedV2.x()) <= tolerance,
                        "Particle 2 velocity x component should be within tolerance of " + expectedV2.x() + ", but was " + p2.velocity().x()
                ),
                // Check that the y-component of Particle 2's velocity is within the expected range
                () -> assertTrue(
                        Math.abs(p2.velocity().y() - expectedV2.y()) <= tolerance,
                        "Particle 2 velocity y component should be within tolerance of " + expectedV2.y() + ", but was " + p2.velocity().y()
                )
        );
    }
    
    private static Stream providePointsWithExpectedPositionsAfterCollision() {
         PointMass p1 = new PointMass(
                new Vector2D(2, 0), // position
                new Vector2D(3, 0), // velocity
                2,
                0,
                0.9,
                false
        );

        PointMass p2 = new PointMass(
                new Vector2D( 6, 0), // position
                new Vector2D(-1, 0), // velocity
                3,
                0,
                0.9,
                false
        );

        return Stream.of(
                Arguments.of(
                        p1, p2, 
                        new Vector2D(0.44, 0), new Vector2D(8.04, 0), 
                        0.001, 1
                )
        );
    }
    
    @ParameterizedTest
    @MethodSource("providePointsWithExpectedPositionsAfterCollision")
    public void testPostCollision_CorrectPositionUpdate (
            PointMass p1, PointMass p2, Vector2D expectedX1, Vector2D expectedX2, double tolerance, double deltaTime
    ) {
        MotionSystem motionSystem = new MotionSystem(IntegrationMethods.EULER);
        motionSystem.add(p1);
        motionSystem.add(p2);
        
        CollisionPair cp = new CollisionPair(p1, p2, Vector2D.sub(p2.position(), p1.position()));
        cp.processCollision();
        
        motionSystem.updateAccelerations();
        motionSystem.updateVelocitiesAndPositions(deltaTime);
        
                assertAll(
                // Check that the x-component of Particle 1's velocity is within the expected range
                () -> assertTrue(
                        Math.abs(p1.position().x() - expectedX1.x()) <= tolerance,
                        "Particle 1 velocity x component should be within tolerance of " + expectedX1.x() + ", but was " + p1.position().x()
                ),
                // Check that the y-component of Particle 1's velocity is within the expected range
                () -> assertTrue(
                        Math.abs(p1.position().y() - expectedX1.y()) <= tolerance,
                        "Particle 1 velocity y component should be within tolerance of " + expectedX1.y() + ", but was " + p1.position().y()
                ),
                // Check that the x-component of Particle 2's velocity is within the expected range
                () -> assertTrue(
                        Math.abs(p2.position().x() - expectedX2.x()) <= tolerance,
                        "Particle 2 velocity x component should be within tolerance of " + expectedX2.x() + ", but was " + p2.position().x()
                ),
                // Check that the y-component of Particle 2's velocity is within the expected range
                () -> assertTrue(
                        Math.abs(p2.position().y() - expectedX2.y()) <= tolerance,
                        "Particle 2 velocity y component should be within tolerance of " + expectedX2.y() + ", but was " + p2.position().y()
                )
        );
    }

}