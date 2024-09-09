package com.slinky.physics.comps;

import static java.lang.Math.abs;
import java.util.stream.Stream;



import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static java.lang.Math.pow;

/**
 * Large amounts of redundancy in mutation methods to ensure consistent state.
 *
 * @author Kheagen Haskins
 */
public class PointMassTest {
    
    // ============================= Providers ============================== //
    static Stream<Arguments> provideParticleParams() {
        return Stream.of(
                Arguments.of(
                        Vector2D.of(10, 10),
                        Vector2D.of(0, 0), // at rest
                        5,    // mass
                        0,    // bounce
                        false // static
                ),
                Arguments.of(
                        Vector2D.of(0.0001, 0.0001),
                        Vector2D.of(-10, 0),
                        50,  // mass
                        1,   // bounce
                        true // static
                ),
                Arguments.of(
                        Vector2D.of(10, 10),
                        Vector2D.of(0, 0), // at rest
                        0.0000001, // mass
                        0.9,       // bounce
                        false      // static
                ),
                Arguments.of(
                    new Vector2D(1, 1),
                    new Vector2D(20, 20), // high velocity
                    2,    // mass
                    0.5,  // restitution
                    false // dynamic
                )
        );
    }

    static Stream<Arguments> provideIllegalParticleParams() {
        return Stream.of(
                Arguments.of(
                        Vector2D.of(0, 0),
                        Vector2D.of(0, 0),
                        -0.1, // Should not allow negative mass
                        0,    // bounce
                        false // static
                ),
                Arguments.of(
                        null, // Invalid null position
                        Vector2D.of(0, 0),
                        1,    // valid mass
                        0.5,  // valid restitution
                        false // static
                ),
                Arguments.of(
                        Vector2D.zero(), // Invalid null position
                        null,
                        1,    // valid mass
                        0.5,  // valid restitution
                        false // static
                )
        );
    }
    
    // ========================= Constructor Tests ========================== //
    @ParameterizedTest
    @MethodSource("provideParticleParams")
    public void testConstructor_ValidInput_NoThrow(Vector2D position, Vector2D velocity, double mass, double restitution, boolean isStatic) {
        assertDoesNotThrow(() -> PointMass.of(position, velocity, mass, restitution, isStatic));
    }

    @ParameterizedTest
    @MethodSource("provideIllegalParticleParams")
    public void testConstructor_InvalidInput_ThrowsIllegalArgs(Vector2D position, Vector2D velocity, double mass, double restitution, boolean isStatic) {
        assertThrows(
                IllegalArgumentException.class,
                () -> PointMass.of(position, velocity, mass, restitution, isStatic)
        );
    }
    
    // ========================== Clamping Tests ============================ //
    @ParameterizedTest
    @CsvSource({
        " -0",
        " -0.1",
        " -0.000001",
        " -13",
        " -1300"
    })
    public void testValueClamping_NegativeValues_SetToZero(double restitution) {
        PointMass p = PointMass.of(Vector2D.zero(), Vector2D.zero(), 10, restitution, false);
        assertAll (
                () -> assertEquals(0, p.restitution())
        );
    }

    @ParameterizedTest
    @CsvSource({
        "1",
        "1.1",
        "1.000001",
        "1300"
    })
    public void testValueClamping_PositiveValues_SetToZero(double restitution) {
        PointMass p = PointMass.of(Vector2D.zero(), Vector2D.zero(), 10, restitution, false);
        assertAll (
                () -> assertEquals(1, p.restitution())
        );
    }
    
    // ============================ Getter Tests ============================ //
    @ParameterizedTest
    @MethodSource("provideParticleParams")
    public void testGettersWithConstructor_ValidInput_ExpectedState(Vector2D position, Vector2D velocity, double mass, double restitution, boolean isStatic) {
        PointMass p = PointMass.of(position, velocity, mass, restitution, isStatic);
        
        double ke       = 0.5 * p.mass() * pow(velocity.mag(), 2);
        double momentum = Vector2D.scale(velocity, mass).mag();
        
        assertAll(
                () -> assertTrue(p.position().matches(position)),
                () -> assertTrue(p.velocity().matches(velocity)),
                () -> assertEquals(mass,        p.mass()),
                () -> assertEquals(restitution, p.restitution()),
                () -> assertEquals(isStatic,    p.isStatic()),
                () -> assertEquals(ke,          p.kineticEnergy()),
                () -> assertEquals(momentum,    p.momentum().mag()),
                
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p.force()))
        );
    }
    
    // ============================ Setter Tests ============================ //
    @ParameterizedTest
    @ValueSource(doubles = {10, 1, 0.1, 0.01, 1000, 2000, -2000, -0.002, -0.00001, -1})
    public void testSetX_ValidInput_ExpectedState(double x) {
        PointMass p = PointMass.of(0, 0, PointMass.DEFAULT_MASS);
        p.setX(x);
        
        
        double ke       = 0.5 * p.mass() * pow(p.velocity().mag(), 2);
        double momentum = Vector2D.scale(p.velocity(), p.mass()).mag();
        
        assertAll(
                () -> assertTrue(p.position().matches(Vector2D.of(x, 0))),
                () -> assertTrue(p.velocity().matches(Vector2D.ZERO)),
                
                () -> assertEquals(p.position().x, x),
                () -> assertEquals(p.x(), x),
                
                () -> assertEquals(PointMass.DEFAULT_MASS, p.mass()),
                () -> assertEquals(PointMass.DEFAULT_REST, p.restitution()),
                () -> assertEquals(PointMass.DEFAULT_STAT, p.isStatic()),
                () -> assertEquals(ke,                     p.kineticEnergy(),  "Unexpected kinetic energy."),
                () -> assertEquals(momentum,               p.momentum().mag(), "Unexpected momentum."),
                
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p.force()))
        );
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {10, 1, 0.1, 0.01, 1000, 2000, -2000, -0.002, -0.00001, -1})
    public void testSetVx_ValidInput_ExpectedState(double x) {
        PointMass p = PointMass.of(0, 0, PointMass.DEFAULT_MASS);
        p.setVx(x);
        
        double ke       = 0.5 * p.mass() * pow(p.velocity().mag(), 2);
        double momentum = Vector2D.scale(p.velocity(), p.mass()).mag();
        
        assertAll(
                () -> assertTrue(p.position().matches(Vector2D.ZERO)),
                () -> assertTrue(p.velocity().matches(Vector2D.of(x, 0))),
                
                () -> assertEquals(p.velocity().x, x),
                () -> assertEquals(p.velocity().x(), x),
                
                () -> assertEquals(PointMass.DEFAULT_MASS, p.mass()),
                () -> assertEquals(PointMass.DEFAULT_REST, p.restitution()),
                () -> assertEquals(PointMass.DEFAULT_STAT, p.isStatic()),
                () -> assertEquals(ke,                     p.kineticEnergy(),  "Unexpected kinetic energy."),
                () -> assertEquals(momentum,               p.momentum().mag(), "Unexpected momentum."),
                
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p.force()))
        );
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {10, 1, 0.1, 0.01, 1000, 2000, -2000, -0.002, -0.00001, -1})
    public void testSetY_ValidInput_ExpectedState(double y) {
        PointMass p = PointMass.of(0, 0, PointMass.DEFAULT_MASS);
        p.setY(y);
        
        
        double ke       = 0.5 * p.mass() * pow(p.velocity().mag(), 2);
        double momentum = Vector2D.scale(p.velocity(), p.mass()).mag();
        
        assertAll(
                () -> assertTrue(p.position().matches(Vector2D.of(0, y))),
                () -> assertTrue(p.velocity().matches(Vector2D.ZERO)),
                
                () -> assertEquals(p.position().y, y),
                () -> assertEquals(p.y(), y),
                
                () -> assertEquals(PointMass.DEFAULT_MASS, p.mass()),
                () -> assertEquals(PointMass.DEFAULT_REST, p.restitution()),
                () -> assertEquals(PointMass.DEFAULT_STAT, p.isStatic()),
                () -> assertEquals(ke,                     p.kineticEnergy(),  "Unexpected kinetic energy."),
                () -> assertEquals(momentum,               p.momentum().mag(), "Unexpected momentum."),
                
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p.force()))
        );
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {10, 1, 0.1, 0.01, 1000, 2000, -2000, -0.002, -0.00001, -1})
    public void testSetVy_ValidInput_ExpectedState(double y) {
        PointMass p = PointMass.of(0, 0, PointMass.DEFAULT_MASS);
        p.setVy(y);
        
        double ke       = 0.5 * p.mass() * pow(p.velocity().mag(), 2);
        double momentum = Vector2D.scale(p.velocity(), p.mass()).mag();
        
        assertAll(
                () -> assertTrue(p.position().matches(Vector2D.ZERO)),
                () -> assertTrue(p.velocity().matches(Vector2D.of(0, y))),
                
                () -> assertEquals(y, p.velocity().y),
                () -> assertEquals(y, p.velocity().y()),
                
                () -> assertEquals(PointMass.DEFAULT_MASS, p.mass()),
                () -> assertEquals(PointMass.DEFAULT_REST, p.restitution()),
                () -> assertEquals(PointMass.DEFAULT_STAT, p.isStatic()),
                () -> assertEquals(ke,                     p.kineticEnergy(),  "Unexpected kinetic energy."),
                () -> assertEquals(momentum,               p.momentum().mag(), "Unexpected momentum."),
                
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p.force()))
        );
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {10, 1, 0.1, 0.01, 3.14159, 0.19547, 2000, 50000, Integer.MAX_VALUE})
    public void testSetMass_ValidInput_ExpectedState(double mass) {
        PointMass p = PointMass.of(0, 0, PointMass.DEFAULT_MASS);
        p.setMass(mass);
        
        
        double ke       = 0.5 * p.mass() * pow(p.velocity().mag(), 2);
        double momentum = Vector2D.scale(p.velocity(), p.mass()).mag();
        
        assertAll(
                () -> assertTrue(p.position().matches(Vector2D.of(0, 0))),
                () -> assertTrue(p.velocity().matches(Vector2D.ZERO)),
                () -> assertEquals(mass,                   p.mass()),
                () -> assertEquals(PointMass.DEFAULT_REST, p.restitution()),
                () -> assertEquals(PointMass.DEFAULT_STAT, p.isStatic()),
                () -> assertEquals(ke,                     p.kineticEnergy(),  "Unexpected kinetic energy."),
                () -> assertEquals(momentum,               p.momentum().mag(), "Unexpected momentum."),
                
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p.force()))
        );
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {0.001, 1, 0, 0.999, 0.01, 0.19547, 0.9525, 0.9999999999999999999999999, 0.1111111111111111})
    public void testSetRestitution_ValidInput_ExpectedState(double restitution) {
        PointMass p = PointMass.of(0, 0, PointMass.DEFAULT_MASS);
        p.setRestitution(restitution);
        
        double ke       = 0.5 * p.mass() * pow(p.velocity().mag(), 2);
        double momentum = Vector2D.scale(p.velocity(), p.mass()).mag();
        
        assertAll(
                () -> assertTrue(p.position().matches(Vector2D.zero())),
                () -> assertTrue(p.velocity().matches(Vector2D.ZERO)),
                () -> assertEquals(PointMass.DEFAULT_MASS, p.mass()),
                () -> assertEquals(restitution,            p.restitution()),
                () -> assertEquals(PointMass.DEFAULT_STAT, p.isStatic()),
                () -> assertEquals(ke,                     p.kineticEnergy(),  "Unexpected kinetic energy."),
                () -> assertEquals(momentum,               p.momentum().mag(), "Unexpected momentum."),
                
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p.force()))
        );
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {1.1, 1.00000000000000000000001, 2, 5000, Double.MAX_VALUE, Integer.MAX_VALUE})
    public void testSetRestitution_InvalidInput_ClampsValueDown(double restitution) {
        PointMass p = PointMass.of(0, 0, PointMass.DEFAULT_MASS);
        p.setRestitution(restitution);
        
        assertAll (
                () -> assertEquals(1, p.restitution())
        );
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {-1.1, -0.00000000000000000000001, -2, -5000, -Double.MAX_VALUE, -Double.MIN_VALUE, Integer.MIN_VALUE})
    public void testSetRestitution_InvalidInput_ClampsValueUp(double restitution) {
        PointMass p = PointMass.of(0, 0, PointMass.DEFAULT_MASS);
        p.setRestitution(restitution);
        
        assertAll (
                () -> assertEquals(0, p.restitution())
        );
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {1.1, 1.00000000000000000000001, 2, 5000, Double.MAX_VALUE, Integer.MAX_VALUE})
    public void testSetTerminalVelocity_ValidInput_Enforced(double tv) {
        PointMass p = PointMass.of(0, 0, tv, tv, PointMass.DEFAULT_MASS, PointMass.DEFAULT_REST, PointMass.DEFAULT_STAT);
        p.setTerminalVelocity(tv);
        
        double ke       = 0.5 * p.mass() * pow(p.velocity().mag(), 2);
        double momentum = Vector2D.scale(p.velocity(), p.mass()).mag();
        
        assertAll(
                () -> assertTrue(p.velocity().mag() <= tv),

                () -> assertTrue(p.position().matches(Vector2D.zero())),
                () -> assertEquals(PointMass.DEFAULT_MASS, p.mass()),
                () -> assertEquals(PointMass.DEFAULT_REST, p.restitution()),
                () -> assertEquals(PointMass.DEFAULT_STAT, p.isStatic()),
                () -> assertEquals(ke,                     p.kineticEnergy(),  "Unexpected kinetic energy."),
                () -> assertEquals(momentum,               p.momentum().mag(), "Unexpected momentum."),
                
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p.force()))
        );
    }

    // ========================== API Method Tests ========================== //
    @ParameterizedTest
    @CsvSource({
        " 0,    0,     10,      10",
        " 0,    0,     0.1,     0.1",
        " 0,    0,     0.00001, 0.0001",
        "-0,   -0,     10,      10",
        " 600,  1000, -500,    -250",
        "-600, -1000, -500,    -250",
        " 600,  1000,  500,     250"
    })
    public void testMove_ValidInput_ExpectedState(double x, double y, double dx, double dy) {
        PointMass p = PointMass.of(x, y, PointMass.DEFAULT_MASS);
        p.move(dx, dy);
        
        
        double ke       = 0.5 * p.mass() * pow(p.velocity().mag(), 2);
        double momentum = Vector2D.scale(p.velocity(), p.mass()).mag();
        
        assertAll(
                () -> assertTrue(p.position().matches(Vector2D.of(x + dx, y + dy))),
                () -> assertEquals(x + dx, p.position().x),
                () -> assertEquals(y + dy, p.position().y),
                () -> assertEquals(x + dx, p.x()),
                () -> assertEquals(y + dy, p.y()),
                
                () -> assertTrue(p.velocity().matches(Vector2D.ZERO)),
                () -> assertEquals(PointMass.DEFAULT_MASS, p.mass()),
                () -> assertEquals(PointMass.DEFAULT_REST, p.restitution()),
                () -> assertEquals(PointMass.DEFAULT_STAT, p.isStatic()),
                () -> assertEquals(ke,                     p.kineticEnergy(),  "Unexpected kinetic energy."),
                () -> assertEquals(momentum,               p.momentum().mag(), "Unexpected momentum."),
                
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p.force()))
        );
    }
    
    
    @ParameterizedTest
    @CsvSource({
        "   0,      0",
        "   1,      1",
        " 100,    100",
        "2000,   2000",
        "-100,   -100",
        "-950,   -100",
        "-950,   -100"
    })
    public void testAddForce_ValidInput_ExpectedState(double fx, double fy) {
        fx /= 10;
        fy /= 10;
        
        double ffx = fx;
        double ffy = fy;
        
        PointMass p1 = PointMass.of(PointMass.DEFAULT_MASS);
        PointMass p2 = PointMass.at(20, 20);
        
        p1.addForce(Vector2D.of(fx, fy));
        p2.addForce(fx, fy);
        
        double ke1       = 0.5 * p1.mass() * pow(p1.velocity().mag(), 2);
        double momentum1 = Vector2D.scale(p1.velocity(), p1.mass()).mag();
        double ke2       = 0.5 * p2.mass() * pow(p2.velocity().mag(), 2);
        double momentum2 = Vector2D.scale(p2.velocity(), p2.mass()).mag();
        
        assertAll(
                () -> assertTrue(p1.position().matches(Vector2D.ZERO)),
                () -> assertTrue(p2.position().matches(Vector2D.of(20, 20))),
                () -> assertTrue(p1.velocity().matches(Vector2D.ZERO)),
                () -> assertTrue(p2.velocity().matches(Vector2D.ZERO)),
                () -> assertEquals(PointMass.DEFAULT_MASS, p1.mass()),
                () -> assertEquals(PointMass.DEFAULT_MASS, p2.mass()),
                () -> assertEquals(PointMass.DEFAULT_REST, p1.restitution()),
                () -> assertEquals(PointMass.DEFAULT_REST, p2.restitution()),
                () -> assertEquals(PointMass.DEFAULT_STAT, p1.isStatic()),
                () -> assertEquals(PointMass.DEFAULT_STAT, p2.isStatic()),
                () -> assertEquals(ke1,                    p1.kineticEnergy(),  "Unexpected kinetic energy."),
                () -> assertEquals(ke2,                    p2.kineticEnergy(),  "Unexpected kinetic energy."),
                () -> assertEquals(momentum1,              p1.momentum().mag(), "Unexpected momentum."),
                () -> assertEquals(momentum2,              p2.momentum().mag(), "Unexpected momentum."),
                // Actual force checks
                () -> assertEquals(ffx, p1.force().x),
                () -> assertEquals(ffx, p2.force().x),
                () -> assertEquals(ffy, p1.force().y),
                () -> assertEquals(ffy, p2.force().y)
        );
    }
    
    @ParameterizedTest
    @MethodSource("provideParticleParams")
    public void testIncreaseSpeed_RespectsTerminalVelocity(Vector2D position, Vector2D velocity, double mass, double restitution, boolean isStatic) {
        PointMass p1 = PointMass.of(position, velocity, mass, restitution, isStatic);
        PointMass p2 = PointMass.of(position, velocity, mass, restitution, isStatic);

        double speedIncrease = 1.4e6;
        double tolerance     = 0.001;
        
        p1.increaseSpeed(speedIncrease, speedIncrease);
        p2.increaseSpeed(Vector2D.DOWN.scale(speedIncrease));
        
        double ke       = 0.5 * p1.mass() * pow(velocity.mag(), 2);
        double momentum = Vector2D.scale(velocity, mass).mag();
        
         assertAll(
                () -> assertTrue(p1.position().matches(position)),
                () -> assertEquals(mass,        p1.mass()),
                () -> assertEquals(restitution, p1.restitution()),
                () -> assertEquals(isStatic,    p1.isStatic()),
                () -> assertEquals(ke,          p1.kineticEnergy()),
                () -> assertEquals(momentum,    p1.momentum().mag()),
                
                () -> assertTrue(p2.position().matches(position)),
                () -> assertEquals(mass,        p2.mass()),
                () -> assertEquals(restitution, p2.restitution()),
                () -> assertEquals(isStatic,    p2.isStatic()),
                () -> assertEquals(ke,          p2.kineticEnergy()),
                () -> assertEquals(momentum,    p2.momentum().mag()),
                
                () -> assertTrue(
                        abs(p1.velocity().mag() - p1.terminalVelocity()) <= tolerance, 
                        "Particle has a terminal velocity of " + p1.terminalVelocity() + ", but its speed is " + p1.velocity().mag()
                ),
                () -> assertTrue(
                        abs(p2.velocity().mag() - p2.terminalVelocity()) <= tolerance, 
                        "Particle has a terminal velocity of " + p2.terminalVelocity() + ", but its speed is " + p2.velocity().mag()
                ),
                
                // Dependent on Vector2D#matches(Vector2D otherVector) working correctly
                () -> assertTrue(Vector2D.ZERO.matches(p1.force())),
                () -> assertTrue(Vector2D.ZERO.matches(p2.force()))
        );
    }
    
}