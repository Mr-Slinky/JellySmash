package com.slinky.jellysmash.model.physics.systems;

import java.util.stream.Stream;

import com.slinky.jellysmash.model.physics.comps.Vector2D;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 *
 * @author Kheagen Haskins
 */
public class VectorSystem2DTest {

    private class TestVectorSystem extends VectorSystem2D {
    }
    private TestVectorSystem vOps;

    // Both x and y vars are setup to be 3-4-5 triangles
    private final double x1 = 30;
    private final double y1 = 40;

    private final double x2 = 40;
    private final double y2 = 30;

    private final double xSum = x1 + x2;
    private final double ySum = x1 + x2;
    private final double xSub = x1 - x2;
    private final double ySub = y1 - y2;

    private final double mag1 = Math.sqrt(x1 * x1 + y1 * y1);
    private final double mag2 = Math.sqrt(x2 * x2 + y2 * y2);

    private Vector2D v1;
    private Vector2D v2;
    private Vector2D v1Copy;

    @BeforeEach
    public void setup() {
        v1 = new Vector2D(x1, y1);
        v1Copy = new Vector2D(x1, y1);
        v2 = new Vector2D(x2, y2);
        vOps = new TestVectorSystem();
    }

    // ============================ Copy Tests ============================== //
    @Test
    public void testCopy_GlobalObjects_CorrectCopy() {
        Vector2D testCopy = vOps.copy(v1);
        assertAll(
                () -> assertEquals(v1.x(), testCopy.x()),
                () -> assertEquals(v1.y(), testCopy.y()),
                () -> assertEquals(v1Copy.x(), testCopy.x()),
                () -> assertEquals(v1Copy.y(), testCopy.y())
        );
    }

    @Test
    public void testCopy_GlobalObjects_OriginalsRemainSeparate() {
        Vector2D testCopy = vOps.copy(v1);

        v1.setComponents(0, 0);
        v1Copy.setComponents(0, 0);

        assertAll(
                () -> assertNotEquals(v1.x(), testCopy.x()),
                () -> assertNotEquals(v1.y(), testCopy.y()),
                () -> assertNotEquals(v1Copy.x(), testCopy.x()),
                () -> assertNotEquals(v1Copy.y(), testCopy.y())
        );
    }

    // ============================= Add Tests ============================== //
    @Test
    @DisplayName("VectorSystem2D Addition Test")
    public void testAdd_GlobalObjects_CorrectVectorProduced() {
        Vector2D v3 = vOps.add(v1, v2);
        /*
            Makes sure that the expected arithmetic still works, 
            which shows that the underlying v1 and v2 objects were 
            not changed by the operation.
        
            Also checks that v3 is the expected value of 70 as well 
            for both components
         */
        assertAll(
                () -> assertEquals(v1.x() + v2.x(), v3.x()),
                () -> assertEquals(xSum, v3.x()),
                () -> assertEquals(v1.y() + v2.y(), v3.y()),
                () -> assertEquals(ySum, v3.y())
        );
    }

    @Test
    @DisplayName("VectorSystem2D Addition To Target Test")
    public void testAddTarget_GlobalObjects_TargetVectorChanged() {
        vOps.addTarget(v1, v2);

        assertAll(
                () -> assertEquals(v1Copy.x() + v2.x(), v1.x()),
                () -> assertEquals(xSum, v1.x()),
                () -> assertEquals(v1Copy.y() + v2.y(), v1.y()),
                () -> assertEquals(ySum, v1.y())
        );
    }

    @ParameterizedTest
    @MethodSource("provideTwoVectorComponents")
    public void testAdd_WithParams_CorrectVectorProduced(double x1, double x2, double y1, double y2) {
        double sumX = x1 + x2;
        double sumY = y1 + y2;
        Vector2D vC = new Vector2D(sumX, sumY);

        Vector2D vA = new Vector2D(x1, y1);
        Vector2D vB = new Vector2D(x2, y2);
        Vector2D vR = vOps.add(vA, vB);

        assertAll(
                () -> assertEquals(vA.x() + vB.x(), vR.x()),
                () -> assertEquals(sumX, vR.x()),
                () -> assertEquals(vA.y() + vB.y(), vR.y()),
                () -> assertEquals(sumY, vR.y()),
                () -> assertEquals(vC.x(), vR.x()),
                () -> assertEquals(vC.y(), vR.y())
        );
    }

    @ParameterizedTest
    @MethodSource("provideTwoVectorComponents")
    public void testAddTarget_WithParams_TargetVectorChanged(double x1, double x2, double y1, double y2) {
        double sumX = x1 + x2;
        double sumY = y1 + y2;
        Vector2D vC = new Vector2D(sumX, sumY);

        Vector2D targetVector = new Vector2D(x1, y1);
        Vector2D vB = new Vector2D(x2, y2);
        vOps.addTarget(targetVector, vB);

        assertAll(
                () -> assertEquals(vC.x(), targetVector.x()),
                () -> assertEquals(sumX, targetVector.x()),
                () -> assertEquals(vC.y(), targetVector.y()),
                () -> assertEquals(sumY, targetVector.y())
        );
    }

    // ============================= Sub Tests ============================== //
    @Test
    @DisplayName("VectorSystem2D Subtraction Test")
    public void testSub_GlobalObjects_CorrectVectorProduced() {
        Vector2D v3 = vOps.sub(v1, v2);
        /*
            Ensures that the subtraction works as expected without 
            modifying the original vectors v1 and v2.
        
            Also checks that v3 has the correct values after the 
            subtraction, where xSub and ySub are the expected differences.
         */
        assertAll(
                () -> assertEquals(v1.x() - v2.x(), v3.x()),
                () -> assertEquals(xSub, v3.x()),
                () -> assertEquals(v1.y() - v2.y(), v3.y()),
                () -> assertEquals(ySub, v3.y())
        );
    }

    @Test
    @DisplayName("VectorSystem2D Subtraction To Target Test")
    public void testSubTarget_GlobalObjects_TargetVectorChanged() {
        vOps.subTarget(v1, v2);

        /*
            Ensures that the subtraction modifies the target vector v1 
            correctly, while also checking that the original v2 vector 
            remains unchanged. 
         */
        assertAll(
                () -> assertEquals(v1Copy.x() - v2.x(), v1.x()),
                () -> assertEquals(xSub, v1.x()),
                () -> assertEquals(v1Copy.y() - v2.y(), v1.y()),
                () -> assertEquals(ySub, v1.y())
        );
    }

    @ParameterizedTest
    @MethodSource("provideTwoVectorComponents")
    public void testSub_WithParams_CorrectVectorProduced(double x1, double x2, double y1, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        Vector2D vC = new Vector2D(diffX, diffY);

        Vector2D vA = new Vector2D(x1, y1);
        Vector2D vB = new Vector2D(x2, y2);
        Vector2D vR = vOps.sub(vA, vB);

        assertAll(
                () -> assertEquals(vA.x() - vB.x(), vR.x()),
                () -> assertEquals(diffX, vR.x()),
                () -> assertEquals(vA.y() - vB.y(), vR.y()),
                () -> assertEquals(diffY, vR.y()),
                () -> assertEquals(vC.x(), vR.x()),
                () -> assertEquals(vC.y(), vR.y())
        );
    }

    @ParameterizedTest
    @MethodSource("provideTwoVectorComponents")
    public void testSubTarget_WithParams_TargetVectorChanged(double x1, double x2, double y1, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        Vector2D vC = new Vector2D(diffX, diffY);

        Vector2D targetVector = new Vector2D(x1, y1);
        Vector2D vB = new Vector2D(x2, y2);
        vOps.subTarget(targetVector, vB);

        assertAll(
                () -> assertEquals(vC.x(), targetVector.x()),
                () -> assertEquals(diffX, targetVector.x()),
                () -> assertEquals(vC.y(), targetVector.y()),
                () -> assertEquals(diffY, targetVector.y())
        );
    }

    // ============================ Scale Tests ============================= //
    @ParameterizedTest
    @MethodSource("provideVectorComponentsWithScalar")
    public void testScale_WithParams_CorrectVectorProduced(double x, double y, double scalar) {
        double scaleX = x * scalar;
        double scaleY = y * scalar;
        Vector2D autoScaledVector = new Vector2D(scaleX, scaleY);

        Vector2D vA = new Vector2D(x, y);
        Vector2D scaledVector = vOps.scale(vA, scalar);

        assertAll(
                () -> assertEquals(autoScaledVector.x(), scaledVector.x()),
                () -> assertEquals(scaleX, scaledVector.x()),
                () -> assertEquals(autoScaledVector.y(), scaledVector.y()),
                () -> assertEquals(scaleY, scaledVector.y())
        );
    }

    // ============================= Div Tests ============================== //
    @Test
    public void testDivTarget_GlobalTestObjects_CorrectVectorProduced() {
        vOps.divTarget(v1, 10);
        vOps.divTarget(v2, 10);
        assertAll(
                () -> assertEquals(x1 / 10, v1.x()),
                () -> assertEquals(y1 / 10, v1.y()),
                () -> assertEquals(x2 / 10, v2.x()),
                () -> assertEquals(y2 / 10, v2.y())
        );
    }

    @ParameterizedTest
    @MethodSource("provideVectorComponentsWithScalar")
    public void testDiv_WithParams_CorrectVectorProduced(double x, double y, double scalar) {
        double divX = x / scalar;
        double divY = y / scalar;
        Vector2D autoScaledVector = new Vector2D(divX, divY);

        Vector2D vA = new Vector2D(x, y);
        Vector2D scaledVector = vOps.div(vA, scalar);

        assertAll(
                () -> assertEquals(autoScaledVector.x(), scaledVector.x()),
                () -> assertEquals(divX, scaledVector.x()),
                () -> assertEquals(autoScaledVector.y(), scaledVector.y()),
                () -> assertEquals(divY, scaledVector.y())
        );
    }

    // ============================= Mag Tests ============================== //
    @Test
    public void testMag_GlobalTestObjects_CorrectMagnitutdeGiven() {
        assertAll(
                () -> assertEquals(mag1, vOps.mag(v1)),
                () -> assertEquals(Math.sqrt(v1.x() * v1.x() + v1.y() * v1.y()), vOps.mag(v1)),
                () -> assertEquals(mag2, vOps.mag(v2)),
                () -> assertEquals(Math.sqrt(v2.x() * v2.x() + v2.y() * v2.y()), vOps.mag(v2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTwoVectorComponents")
    public void testMag_WithParams_CorrectMagnitutdeGiven(double x1, double y1, double x2, double y2) {
        double magA = Math.sqrt(x1 * x1 + y1 * y1);
        double magB = Math.sqrt(x2 * x2 + y2 * y2);

        Vector2D vA = new Vector2D(x1, y1);
        Vector2D vB = new Vector2D(x2, y2);

        assertAll(
                () -> assertEquals(magA, vOps.mag(vA)),
                () -> assertEquals(magB, vOps.mag(vB))
        );
    }

    // =========================== Distance Tests =========================== //
    // ========================== Normalise Tests =========================== //
    // ========================= Provider Methods =========================== //
    private static Stream<Arguments> provideTwoVectorComponents() {
        return Stream.of(
                Arguments.of(-1.0, -1.0, -1.0, -1.0),
                Arguments.of(1, 1, 1, 1),
                Arguments.of(1.0, 1.0, -1.0, -1.0),
                Arguments.of(10, -1, -1, 10),
                Arguments.of(1000, -1500, 6101, 451),
                Arguments.of(3.14159, -3.14159, 3.14159, -3.14159)
        );
    }

    private static Stream<Arguments> provideVectorComponentsWithScalar() {
        return Stream.of(
                Arguments.of(1.0, 1.0, 1.0),
                Arguments.of(2, 2, 2),
                Arguments.of(-1.0, -1.0, -1.0),
                Arguments.of(-1.0, -1.0, 1.0),
                Arguments.of(-1.0, 1.0, -1.0),
                Arguments.of(-1.0, 1.0, 1.0),
                Arguments.of(-3.0, -3.0, -3.0),
                Arguments.of(-3.0, -3.0, 3.0),
                Arguments.of(-3.0, 3.0, -3.0),
                Arguments.of(-3.0, 3.0, 3.0),
                Arguments.of(-3.0, 5.0, 7.0),
                Arguments.of(3.0, -5.0, 7.0),
                Arguments.of(100, 100, 10)
        );
    }
}
