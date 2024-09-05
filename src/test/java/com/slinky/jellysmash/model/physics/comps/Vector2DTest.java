package com.slinky.jellysmash.model.physics.comps;

import static java.lang.Math.abs;
import static java.lang.Math.round;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Kheagen Haskins
 */
public class Vector2DTest {

    // ============================= Providers ============================== //
    private static final double PRECISION_THRESHOLD = 0.01;

    static Stream<Arguments> provideNonZeroVectorArgs() {
        return Stream.of(
                Arguments.of(0.0001, 0.0001),
                Arguments.of(-0.001, 0.001),
                Arguments.of(0.01, -0.01),
                Arguments.of(-0.1, -0.1),
                Arguments.of(1.5, 2.5),
                Arguments.of(-1, -2),
                Arguments.of(3.14159, 2.71828),
                Arguments.of(0.14159, 0.71828),
                Arguments.of(5.21516, 515642.71828)
        );
    }

    static Stream<Arguments> provideScalarArgs() {
        return Stream.of(
                Arguments.of(1, 1, 2),
                Arguments.of(0.5, 0.5, 4),
                Arguments.of(-1, -1, 3),
                Arguments.of(1.5, 2.5, 0.5),
                Arguments.of(1000, 2000, 0.001)
        );
    }

    // =================== distanceBetween Static Method ==================== //
    @Test
    public void testDistanceBetween_Superficial() {
        Vector2D v1 = Vector2D.zero();
        Vector2D v2 = new Vector2D(3, 4);

        assertEquals(5, Vector2D.distanceBetween(v1, v2));
    }

    // =================== Constructor & Getter & Setter ==================== //
    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testConstructorAndGetters(double x, double y) {
        // Arrange & Act
        Vector2D vector = new Vector2D(x, y);

        // Assert
        assertAll(
                () -> assertEquals(x, vector.x(), "X coordinate should match the value passed to the constructor."),
                () -> assertEquals(y, vector.y(), "Y coordinate should match the value passed to the constructor.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    void testSetters(double x, double y) {
        // Arrange
        Vector2D v1 = new Vector2D(0, 0);
        Vector2D v2 = new Vector2D(0, 0);

        // Act
        v1.setX(x);
        v1.setY(y);
        v2.setComponents(x, y);

        // Assert
        assertAll("Testing Vector2D setters",
                () -> assertEquals(x, v1.x()),
                () -> assertEquals(y, v1.y()),
                () -> assertEquals(x, v2.x()),
                () -> assertEquals(y, v2.y())
        );
    }

    // ========================= Vector Equals Test ========================= //
    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testEquals(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);
        Vector2D v2 = new Vector2D(x, y);
        Vector2D v3 = new Vector2D(x + 10, y / 10);

        assertAll(
                () -> assertTrue(v1.matches(v2)),
                () -> assertFalse(v1.matches(v3)),
                () -> assertFalse(v2.matches(v3))
        );
    }

    // ========================== Vector Zero Tests ========================= //
    @Test
    public void testZeroVectorConstant_ZeroValues() {
        assertAll(
                () -> assertEquals(0, Vector2D.ZERO.x),
                () -> assertEquals(0, Vector2D.ZERO.y)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testZeroVectorConstant_ScalarFunctions(double x, double y) {
        assertAll(
                () -> assertEquals(0, Vector2D.ZERO.mag()),
                () -> assertEquals(0, abs(Vector2D.ZERO.dot(new Vector2D(x, y)))),
                () -> assertEquals(0, abs(Vector2D.ZERO.cross(new Vector2D(x, y))))
        );
    }

    @Test
    public void testZeroVectorConstant_Immutability() {
        assertAll(
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.setX(1)),
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.setY(1)),
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.setComponents(1, 1)),
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.add(new Vector2D(10, 10))),
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.sub(new Vector2D(10, 10))),
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.mult(new Vector2D(10, 10))),
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.div(new Vector2D(10, 10))),
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.copy(new Vector2D(10, 10))),
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.scale(10)),
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.div(10)),
                () -> assertThrows(UnsupportedOperationException.class, () -> Vector2D.ZERO.rotate(10))
        );
    }

    @Test
    public void testZeroVectorMethod() {
        assertAll(
                () -> assertEquals(0, Vector2D.zero().x),
                () -> assertEquals(0, Vector2D.zero().y)
        );
    }

    // ======================= Vector Normalize Tests ======================= //
    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testNormalize_MagEqualsOne(double x, double y) {
        double mag = new Vector2D(x, y).normalize().mag();
        double tolerance = 0.0001;
        assertTrue(mag <= 1 + tolerance && mag >= 1 - tolerance);
    }

    // ========================== Vector Add Tests ========================== //
    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testAdd_ToZero(double x, double y) {
        Vector2D v1 = Vector2D.zero();
        Vector2D v2 = new Vector2D(x, y);

        v1.add(v2);
        v1.add(Vector2D.ZERO);
        v1.add(Vector2D.zero());

        assertAll(
                () -> assertEquals(x, v1.x),
                () -> assertEquals(y, v1.y)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testAdd_ToReverseParams(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);
        Vector2D v2 = new Vector2D(y, x);

        v1.add(v2);

        assertAll(
                () -> assertEquals(x + y, v1.x),
                () -> assertEquals(y + x, v1.y)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testAdd_SelfAddition(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);

        v1.add(v1);

        assertAll(
                () -> assertEquals(x + x, v1.x),
                () -> assertEquals(y + y, v1.y)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testAdd_ChainAddition(double x, double y) {
        Vector2D v1 = Vector2D.zero();
        Vector2D v2 = new Vector2D(x, y);

        v1.add(v2)
                .add(v2);

        assertAll(
                () -> assertEquals(x + x, v1.x),
                () -> assertEquals(y + y, v1.y)
        );
    }

    // ========================== Vector Sub Tests ========================== //
    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testSub_FromZero(double x, double y) {
        Vector2D v1 = Vector2D.zero();
        Vector2D v2 = new Vector2D(x, y);

        v1.sub(v2);
        v1.sub(Vector2D.ZERO);
        v1.sub(Vector2D.zero());

        assertAll(
                () -> assertEquals(0 - x, v1.x(), "X coordinate should be the negative of the initial value."),
                () -> assertEquals(0 - y, v1.y(), "Y coordinate should be the negative of the initial value.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testSub_FromReverseParams(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);
        Vector2D v2 = new Vector2D(y, x);

        v1.sub(v2);

        assertAll(
                () -> assertEquals(x - y, v1.x(), "X coordinate should be the difference between x and y."),
                () -> assertEquals(y - x, v1.y(), "Y coordinate should be the difference between y and x.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testSub_SelfSubtraction(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);

        v1.sub(v1);

        assertAll(
                () -> assertEquals(0, v1.x(), "X coordinate should be 0 after self-subtraction."),
                () -> assertEquals(0, v1.y(), "Y coordinate should be 0 after self-subtraction.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testSub_ChainSubtraction(double x, double y) {
        Vector2D v1 = new Vector2D(x + x, y + y);
        Vector2D v2 = new Vector2D(x, y);

        v1.sub(v2)
                .sub(v2);

        assertAll(
                () -> assertEquals(0, v1.x(), "X coordinate should be 0 after chain subtraction."),
                () -> assertEquals(0, v1.y(), "Y coordinate should be 0 after chain subtraction.")
        );
    }

    // ========================= Vector Mult Tests ========================== //
    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testMult_WithOne(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);
        Vector2D v2 = new Vector2D(1, 1);

        v1.mult(v2);
        v1.mult(Vector2D.ZERO);
        v1.mult(Vector2D.zero());

        assertAll(
                () -> assertEquals(0, abs(v1.x()), "X coordinate should be 0 after multiplying by zero."),
                () -> assertEquals(0, abs(v1.y()), "Y coordinate should be 0 after multiplying by zero.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testMult_WithReverseParams(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);
        Vector2D v2 = new Vector2D(y, x);

        v1.mult(v2);

        assertAll(
                () -> assertEquals(x * y, v1.x(), "X coordinate should be the product of x and y."),
                () -> assertEquals(y * x, v1.y(), "Y coordinate should be the product of y and x.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testMult_SelfMultiplication(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);

        v1.mult(v1);

        assertAll(
                () -> assertEquals(x * x, v1.x(), "X coordinate should be x squared after self-multiplication."),
                () -> assertEquals(y * y, v1.y(), "Y coordinate should be y squared after self-multiplication.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testMult_ChainMultiplication(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);
        Vector2D v2 = new Vector2D(x, y);

        v1.mult(v2)
                .mult(v2);

        assertAll(
                () -> assertEquals(x * x * x, v1.x(), "X coordinate should be x cubed after chain multiplication."),
                () -> assertEquals(y * y * y, v1.y(), "Y coordinate should be y cubed after chain multiplication.")
        );
    }

    // ========================== Vector Div Tests ========================== //
    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testDiv_ByZero_ReturnsZero(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);
        Vector2D v2 = new Vector2D(1, 1);

        v1.div(v2)
                .div(Vector2D.ZERO);

        assertAll(
                () -> assertEquals(0, v1.x(), "X coordinate should be 0 when divided by 0."),
                () -> assertEquals(0, v1.y(), "Y coordinate should be 0 when divided by 0.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testDiv_ByOne(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);
        Vector2D v2 = new Vector2D(1, 1);

        v1.div(v2);

        assertAll(
                () -> assertEquals(x, v1.x(), "X coordinate should remain the same when divided by 1."),
                () -> assertEquals(y, v1.y(), "Y coordinate should remain the same when divided by 1.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testDiv_ByReverseParams(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);
        Vector2D v2 = new Vector2D(y, x);

        v1.div(v2);

        assertAll(
                () -> assertEquals(x / y, v1.x(), "X coordinate should be x divided by y."),
                () -> assertEquals(y / x, v1.y(), "Y coordinate should be y divided by x.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testDiv_SelfDivision(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);

        v1.div(v1);

        assertAll(
                () -> assertEquals(1, v1.x(), "X coordinate should be 1 after self-division."),
                () -> assertEquals(1, v1.y(), "Y coordinate should be 1 after self-division.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testDiv_ChainDivision(double x, double y) {
        Vector2D v1 = new Vector2D(x * 100, y * 100);
        Vector2D v2 = new Vector2D(10, 10);

        v1.div(v2)
                .div(v2);

        assertAll(
                () -> assertEquals(round(x * 100) / 100.0, round(v1.x * 100) / 100.0),
                () -> assertEquals(round(y * 100) / 100.0, round(v1.y * 100) / 100.0)
        );
    }

    // ========================= Vector Scale Tests ========================== //
    @ParameterizedTest
    @MethodSource("provideScalarArgs")
    public void testScaleUp_ByPositiveScalar(double x, double y, double scalar) {
        Vector2D v1 = new Vector2D(x, y);

        v1.scale(scalar);

        assertAll(
                () -> assertEquals(x * scalar, v1.x(), "X coordinate should be scaled by the scalar."),
                () -> assertEquals(y * scalar, v1.y(), "Y coordinate should be scaled by the scalar.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideScalarArgs")
    public void testScaleUp_ByNegativeScalar(double x, double y, double scalar) {
        Vector2D v1 = new Vector2D(x, y);

        v1.scale(-scalar);

        assertAll(
                () -> assertEquals(x * -scalar, v1.x(), "X coordinate should be scaled by the negative scalar."),
                () -> assertEquals(y * -scalar, v1.y(), "Y coordinate should be scaled by the negative scalar.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    public void testScaleUp_ByZero(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);

        v1.scale(0);

        assertAll(
                () -> assertEquals(0, abs(v1.x()), "X coordinate should be 0 after scaling by zero."),
                () -> assertEquals(0, abs(v1.y()), "Y coordinate should be 0 after scaling by zero.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideScalarArgs")
    public void testScaleUp_ChainScaling(double x, double y, double scalar) {
        Vector2D v1 = new Vector2D(x, y);

        v1.scale(scalar)
                .scale(scalar);

        assertAll(
                () -> assertEquals(x * scalar * scalar, v1.x(), "X coordinate should be scaled by the scalar twice."),
                () -> assertEquals(y * scalar * scalar, v1.y(), "Y coordinate should be scaled by the scalar twice.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideScalarArgs")
    public void testScaleDown_ByPositiveScalar(double x, double y, double scalar) {
        Vector2D v1 = new Vector2D(x, y);

        v1.div(scalar);

        assertAll(
                () -> assertEquals(x / scalar, v1.x(), "X coordinate should be scaled down by the scalar."),
                () -> assertEquals(y / scalar, v1.y(), "Y coordinate should be scaled down by the scalar.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideScalarArgs")
    public void testScaleDown_ByNegativeScalar(double x, double y, double scalar) {
        Vector2D v1 = new Vector2D(x, y);

        v1.div(-scalar);

        assertAll(
                () -> assertEquals(x / -scalar, v1.x(), "X coordinate should be scaled down by the negative scalar."),
                () -> assertEquals(y / -scalar, v1.y(), "Y coordinate should be scaled down by the negative scalar.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideScalarArgs")
    public void testScaleDown_ByZero(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);

        v1.div(0);

        assertAll(
                () -> assertEquals(x, v1.x(), "X coordinate should remain unchanged when scaled down by zero."),
                () -> assertEquals(y, v1.y(), "Y coordinate should remain unchanged when scaled down by zero.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideScalarArgs")
    public void testScaleDown_ChainScaling(double x, double y, double scalar) {
        Vector2D v1 = new Vector2D(x * scalar * scalar, y * scalar * scalar);

        v1.div(scalar)
                .div(scalar);

        assertAll(
                () -> assertEquals(x, v1.x(), "X coordinate should return to the original x after chain scaling down."),
                () -> assertEquals(y, v1.y(), "Y coordinate should return to the original y after chain scaling down.")
        );
    }

    // ===================== Vector Mag Function Test ======================= //
    @ParameterizedTest
    @MethodSource("provideNonZeroVectorArgs")
    void testMag_NonZeroVectors(double x, double y) {
        Vector2D v1 = new Vector2D(x, y);

        double expectedMagnitude = Math.sqrt(x * x + y * y);

        assertEquals(expectedMagnitude, v1.mag(), 1e-9,
                () -> String.format("Magnitude should be %f for vector (%f, %f)", expectedMagnitude, x, y));
    }

    @Test
    void testMag_ZeroVector() {
        Vector2D v1 = Vector2D.zero();

        assertEquals(0, v1.mag(), "Magnitude of the zero vector should be 0.");
    }

    @Test
    void testMag_UnitVectors() {
        Vector2D v1 = new Vector2D(1, 0);
        Vector2D v2 = new Vector2D(0, 1);

        assertEquals(1, v1.mag(), "Magnitude of vector (1, 0) should be 1.");
        assertEquals(1, v2.mag(), "Magnitude of vector (0, 1) should be 1.");
    }

    @ParameterizedTest
    @MethodSource("provideScalarArgs")
    public void testSetMag(double x, double y, double scalar) {
        Vector2D v = new Vector2D(x, y);

        v.setMag(scalar);
        double mag = v.mag();
        double lowerThreshold = scalar - PRECISION_THRESHOLD;
        double upperThreshold = scalar + PRECISION_THRESHOLD;

        assertTrue(mag >= lowerThreshold && mag <= upperThreshold, mag + " outside of accepted tolerance values: " + lowerThreshold + " - " + upperThreshold);
    }

}