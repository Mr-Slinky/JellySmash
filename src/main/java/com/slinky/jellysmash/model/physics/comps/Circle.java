package com.slinky.jellysmash.model.physics.comps;

import static java.lang.Math.PI;

/**
 * The {@code Circle} class represents the basic geometric and mathematical
 * properties of a circle. This component class is used to store and manage the
 * essential attributes of a circle, such as its radius, diameter,
 * circumference, and area. This data can be used in various physics
 * simulations, rendering engines, or any system that requires the manipulation
 * or analysis of circular shapes.
 * <p>
 * <b>Note:</b> The class automatically recalculates dependent properties
 * (diameter, circumference, and area) when the radius is updated. For
 * simplicity, the mutability of these properties are all tied to the
 * manipulation of the {@code radius}.
 * </p>
 * <p>
 * This class is a part of the JellySmash physics model and adheres to the
 * {@code Component} interface, making it a versatile component within an Entity
 * Component System (ECS).
 * </p>
 *
 * <p>
 * <strong>Example Usage:</strong></p>
 * <pre><code>
 * // Creating a Circle object with a specific radius
 * Circle circle = new Circle(5.0);
 *
 * // Accessing the circle's properties
 * double radius = circle.radius();               // 5.0
 * double diameter = circle.diameter();           // 10.0
 * double circumference = circle.dircumference(); // 31.41592653589793
 * double area = circle.area();                   // 78.53981633974483
 *
 * // Modifying the circle's radius
 * circle.setRadius(10.0);
 * double newRadius = circle.radius();               // 10.0
 * double newDiameter = circle.diameter();           // 20.0
 * double newCircumference = circle.circumference(); // 62.83185307179586
 * double newArea = circle.area();                   // 314.1592653589793
 * </code></pre>
 *
 * @version 1.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 */
public class Circle implements Component {

    // ============================== Fields ================================ //
    /**
     * The radius of the circle, representing the distance from the centre to
     * any point on its circumference.
     */
    private double radius;

    /**
     * The diameter of the circle, representing the distance across the circle
     * through its centre. This value is always twice the radius.
     */
    private double diameter;

    /**
     * The circumference of the circle, representing the perimeter or the length
     * around the circle. This value is calculated as {@code 2 * PI * radius}.
     */
    private double circumference;

    /**
     * The area of the circle, representing the space enclosed within the
     * circle. This value is calculated as {@code PI * radius^2}.
     */
    private double area;

    // =========================== Constructors ============================= //
    /**
     * Constructs a {@code Circle} with a specified radius.
     * <p>
     * This constructor initialises the radius and calculates the corresponding
     * diameter, circumference, and area based on the provided radius.
     * </p>
     *
     * @param r the radius of the circle.
     * @param position the position of the circle's centre.
     */
    public Circle(/*Vector2D position, */double r) {
        if (r < 0) {
            throw new IllegalArgumentException("Radius cannot be negative");
        }

        this.radius = r;
        this.diameter = r * 2;
        this.circumference = 2 * PI * r;
        this.area = PI * r * r;
    }

    // ============================== Getters =============================== //
    /**
     * Returns the area of the circle.
     *
     * @return the area of the circle.
     */
    public double area() {
        return area;
    }

    /**
     * Returns the circumference of the circle.
     *
     * @return the circumference of the circle.
     */
    public double circumference() {
        return circumference;
    }

    /**
     * Returns the diameter of the circle.
     *
     * @return the diameter of the circle.
     */
    public double diameter() {
        return diameter;
    }

    /**
     * Returns the radius of the circle.
     *
     * @return the radius of the circle.
     */
    public double radius() {
        return radius;
    }

    // ============================== Setters =============================== //
    /**
     * Sets a new radius for the circle and recalculates the dependent
     * properties: diameter, circumference, and area.
     * <p>
     * This method allows for dynamically changing the size of the circle while
     * ensuring that all related properties remain consistent.
     * </p>
     *
     * @param radius the new radius of the circle.
     */
    public void setRadius(double radius) {
        this.radius = radius;
        this.diameter = radius * 2;
        this.circumference = 2 * PI * radius;
        this.area = PI * radius * radius;
    }

    /**
     * Returns a string representation of this {@code Circle} object, including
     * its radius and diameter.
     *
     * <p>
     * This method provides a summary of the circle's key properties, formatted
     * in a clear and concise manner. The output includes the radius (r) and
     * diameter (d) of the circle, making it easy to understand the circle's
     * size at a glance. This is particularly useful for debugging and logging
     * purposes.
     * </p>
     *
     * @return a string representation of this {@code Circle}, including its
     * radius and diameter.
     */
    @Override
    public String toString() {
        return "Circle:\n\t" + "r:\t" + radius + "\n\t" + "d:\t" + diameter;
    }


}