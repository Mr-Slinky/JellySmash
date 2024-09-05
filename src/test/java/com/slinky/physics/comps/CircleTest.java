package com.slinky.physics.comps;

import com.slinky.physics.comps.Circle;
import static java.lang.Math.PI;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author Kheagen
 */
public class CircleTest {

    @ParameterizedTest
    @ValueSource(doubles = {3.14, 6.28, 10.22, 1, 15, 0.1, 0})
    public void testSetRadius(double r) {
        Circle circle = new Circle(0);
        circle.setRadius(r);
        
        double d = r * 2;
        double c = 2 * PI * r;
        double a = PI * r * r;

        assertAll("Circle properties",
                () -> assertEquals(r, circle.radius(), "The radius should be " + r),
                () -> assertEquals(d, circle.diameter(), "The diameter should be " + d),
                () -> assertEquals(c, circle.circumference(), "The circumference should be " + c),
                () -> assertEquals(a, circle.area(), "The area should be " + a)
        );
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {3.14, 6.28, 10.22, 1, 15, 0.1, 0})
    public void testCircleProperties(double r) {
        Circle circle = new Circle(r);

        double d = r * 2;
        double c = 2 * PI * r;
        double a = PI * r * r;

        assertAll("Circle properties",
                () -> assertEquals(r, circle.radius(), "The radius should be " + r),
                () -> assertEquals(d, circle.diameter(), "The diameter should be " + d),
                () -> assertEquals(c, circle.circumference(), "The circumference should be " + c),
                () -> assertEquals(a, circle.area(), "The area should be " + a)
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, -10, -0.001, -200})
    public void testCircleConstructor_NegativeValues(double neg) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Circle(neg)
        );
    }

}