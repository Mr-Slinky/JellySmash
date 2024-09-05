package com.slinky.physics.comps;

import com.slinky.physics.comps.Vector2D;
import com.slinky.physics.comps.PointMass;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
public class PointMassTest {

    @ParameterizedTest
    @MethodSource("provideParticleParams")
    public void testConstructor_ValidInput_NoThrow(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double damping, double restitution, boolean isStatic) {
        assertDoesNotThrow(() -> new PointMass(position, velocity, acceleration, mass, damping, restitution, isStatic));
    }

    @ParameterizedTest
    @MethodSource("provideIllegalParticleParams")
    public void testConstructor_InvalidInput_ThrowsIllegalArgs(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double damping, double restitution, boolean isStatic) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PointMass(position, velocity, acceleration, mass, damping, restitution, isStatic)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParticleParams")
    public void testConstructor_ValidInput_ExpectedState(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double damping, double restitution, boolean isStatic) {
        PointMass p = new PointMass(position, velocity, acceleration, mass, damping, restitution, isStatic);

        assertAll(
                () -> assertEquals(position, p.position()),
                () -> assertEquals(velocity, p.velocity()),
                () -> assertEquals(acceleration, p.acceleration()),
                () -> assertEquals(mass, p.mass()),
                () -> assertEquals(damping, p.dampingCoefficient()),
//                () -> assertEquals(restitution, p.restitution()),
                () -> assertEquals(isStatic, p.isStatic()),
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p.force()))
        );
    }

    @ParameterizedTest
    @CsvSource({
        "-0, -0",
        "-0.1, -0.1",
        "-5, -13",
        "-5000, -1300"
    })
    public void testValueClamping_NegativeValues_SetToZero(double damping, double restitution) {
        PointMass p = new PointMass(new Vector2D(0, 0), new Vector2D(0, 0), new Vector2D(0, 0), 10, damping, restitution, false);
        assertAll (
                () -> assertEquals(0, p.dampingCoefficient()),
                () -> assertEquals(0, p.restitution())
        );
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1",
        "1.1, 1.1",
        "5000, 1300"
    })
    public void testValueClamping_PositiveValues_SetToZero(double damping, double restitution) {
        PointMass p = new PointMass(new Vector2D(0, 0), new Vector2D(0, 0), new Vector2D(0, 0), 10, damping, restitution, false);
        assertAll (
                () -> assertEquals(1, p.dampingCoefficient()),
                () -> assertEquals(1, p.restitution())
        );
    }

    // ============================= Providers ============================== //
    static Stream<Arguments> provideParticleParams() {
        return Stream.of(
                Arguments.of(
                        new Vector2D(10, 10),
                        new Vector2D(0, 0), // at rest
                        new Vector2D(0, 9.81), // earth grav
                        5, // mass
                        0, // damping
                        0, // bounce
                        false // static
                ),
                Arguments.of(
                        new Vector2D(0.0001, 0.0001),
                        new Vector2D(-10, 0),
                        new Vector2D(0, 15.78),
                        50, // mass
                        1, // damping
                        1, // bounce
                        true // static
                ),
                Arguments.of(
                        new Vector2D(10, 10),
                        new Vector2D(0, 0), // at rest
                        new Vector2D(-10, 9.81),
                        0.0000001, // mass
                        0.5, // damping
                        0.9, // bounce
                        false // static
                )
        );
    }

    static Stream<Arguments> provideIllegalParticleParams() {
        return Stream.of(
                Arguments.of(
                        new Vector2D(0, 0),
                        new Vector2D(0, 0),
                        new Vector2D(0, 0),
                        -0.1, // Should not allow negative mass
                        0, // damping
                        0, // bounce
                        false // static
                )
        );
    }

}