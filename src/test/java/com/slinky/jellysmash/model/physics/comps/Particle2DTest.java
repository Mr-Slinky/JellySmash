package com.slinky.jellysmash.model.physics.comps;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * 
 * @author Kheagen Haskins
 */
public class Particle2DTest {

    @ParameterizedTest
    @MethodSource("provideParticleParams")
    public void testConstructor_ValidInput_NoThrow(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double damping, double restitution, boolean isStatic) {
        assertDoesNotThrow(() -> new Particle2D(position, velocity, acceleration, mass, damping, restitution, isStatic));
    }
    
    @ParameterizedTest
    @MethodSource("provideIllegalParticleParams")
    public void testConstructor_InvalidInput_ThrowsIllegalArgs(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double damping, double restitution, boolean isStatic) {
        assertThrows (
                IllegalArgumentException.class,
                () -> new Particle2D(position, velocity, acceleration, mass, damping, restitution, isStatic)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParticleParams")
    public void testConstructor_ValidInput_ExpectedState(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double damping, double restitution, boolean isStatic) {
        Particle2D p = new Particle2D(position, velocity, acceleration, mass, damping, restitution, isStatic);

        assertAll (
                () -> assertEquals(position,      p.getPosition()),
                () -> assertEquals(velocity,      p.getVelocity()),
                () -> assertEquals(acceleration,  p.getAcceleration()),
                () -> assertEquals(mass,          p.getMass()),
                () -> assertEquals(damping,       p.getDampingCoefficient()),
                () -> assertEquals(restitution,   p.getRestitution()),
                () -> assertEquals(isStatic,      p.isStatic()),
                
                // Dependent on Vector2D#equals(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.equals(p.getActingForce()))
        );
    }
    
    // ============================= Providers ============================== //
    static Stream<Arguments> provideParticleParams() {
        return Stream.of (
                Arguments.of (
                        new Vector2D(10, 10),
                        new Vector2D(0, 0),    // at rest
                        new Vector2D(0, 9.81), // earth grav
                        5,    // mass
                        0,    // damping
                        0,    // bounce
                        false // static
                ),
                Arguments.of (
                        new Vector2D(0.0001, 0.0001),
                        new Vector2D(-10, 0), 
                        new Vector2D(0, 15.78), 
                        50,  // mass
                        1,   // damping
                        1,   // bounce
                        true // static
                ),
                Arguments.of (
                        new Vector2D(10, 10),
                        new Vector2D(0, 0), // at rest
                        new Vector2D(-10, 9.81),
                        0.0000001, // mass
                        0.5,       // damping
                        0.9,       // bounce
                        false      // static
                )
        );
    }
    
    static Stream<Arguments> provideIllegalParticleParams() {
        return Stream.of(
                Arguments.of(
                        new Vector2D(0, 0), 
                        new Vector2D(0, 0), 
                        new Vector2D(0, 0), 
                        -5,   // Should not allow negative mass
                        0,    // damping
                        0,    // bounce
                        false // static
                ),
                
                Arguments.of(
                        new Vector2D(0, 0), 
                        new Vector2D(0, 0), 
                        new Vector2D(0, 0), 
                        5,    // mass
                        -4,   // Should not allow negative damping
                        0,    // bounce
                        false // static
                ),
                
                Arguments.of(
                        new Vector2D(0, 0), 
                        new Vector2D(0, 0), 
                        new Vector2D(0, 0), 
                        5,    // mass
                        4,    // damping
                        -1,   // Should not allow negative bounce
                        false // static
                ),
                
                Arguments.of(
                        new Vector2D(0, 0), 
                        new Vector2D(0, 0), 
                        new Vector2D(0, 9.81), 
                        1,    // mass
                        1.1,  // Should not allow damping above 1
                        0,    // bounce
                        false // static
                ),
                
                Arguments.of(
                        new Vector2D(0, 0), 
                        new Vector2D(0, 0), 
                        new Vector2D(0, 9.81), 
                        1,    // mass
                        0.5,  // Should not allow damping above 1
                        1.1,  // bounce
                        false // static
                )
        );
    }

}