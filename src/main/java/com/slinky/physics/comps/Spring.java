package com.slinky.physics.comps;

import static java.lang.Math.max;
import javafx.scene.canvas.GraphicsContext;

/**
 * Models a physical spring, connecting two <code>PointMass</code> objects in a
 * physics simulation. It simulates behaviour following Hooke's Law, where the
 * force exerted by the spring is proportional to the displacement from its rest
 * length. The class supports methods to calculate spring forces, potential
 * energy, and dynamic updates to the rest length, simulating realistic spring
 * behaviour.
 * 
 * <br><b>Key Concepts</b>
 * <p>
 * <ul>
 * <li><b>Hooke's Law:</b> The force exerted by the spring is defined by:
 * <pre> F = -k * (x - L_0) </pre> where <code>k</code> is the spring constant,
 * <code>x</code> is the current length of the spring, and <code>L_0</code> is
 * the rest length.
 * </li>
 * <li><b>Rest Length:</b> The length at which the spring is neither stretched
 * nor compressed. The spring will try to return to this length by applying a
 * force on the connected <code>PointMass</code> objects.
 * </li>
 * <li><b>Spring Constant:</b> Also referred to as stiffness, the spring
 * constant <code>k</code> defines the stiffness of the spring. A higher value
 * results in a stiffer spring that applies more force for the same
 * displacement.
 * </li>
 * </ul>
 * 
 * </p>
 * 
 * <br><b>Usage</b>
 * <p>
 * To create a <code>Spring</code> object between two <code>PointMass</code>
 * objects, use the static factory methods:
 * </p>
 * <pre><code>
 *     PointMass p1 = new PointMass(...);
 *     PointMass p2 = new PointMass(...);
 *     Spring spring = Spring.between(p1, p2, 0.8);   // With custom  <b>spring constant</b>
 *     Spring defaultSpring = Spring.between(p1, p2); // Uses default <b>spring constant</b>
 * </code></pre>
 * <p>
 * Once instantiated, the spring can be used to calculate forces, energy, or to
 * update its state dynamically.
 * </p>
 *
 * <br><b>Important Methods</b>
 * <ul>
 * <li><b><code>force</code>:</b> Calculates the force exerted by the spring based on the
 * displacement from the rest length. This force will pull the
 * <code>PointMass</code> objects towards or push them away from each other.
 * </li>
 * <li><b><code>relax</code>:</b> Dynamically sets the rest length of the spring to its
 * current length, effectively neutralising the force it exerts at that moment.
 * Useful for adapting the spring during runtime.
 * </li>
 * <li><b><code>potentialEnergy</code>:</b> Calculates the potential energy stored in the
 * spring based on its current stretch or compression. This energy is
 * proportional to the square of the displacement.
 * </li>
 * </ul>
 *
 * <br><b>Example</b>
 * <p>
 * This class can be integrated into a physics simulation system, where
 * <code>Spring</code> objects apply forces on their connected points. For
 * instance:
 * </p>
 * <pre><code>
 *     PointMass p1  = new PointMass(Vector2D.zero(),     Vector2D.zero(), 10, 0.5, 1, false);
 *     PointMass p2  = new PointMass(Vector2D.of(10, 10), Vector2D.zero(), 10, 0.5, 1, false);
 *     Spring spring = Spring.between(p1, p2, 0.8);
 *
 *     Vector2D force = spring.force();          // Calculate the spring force
 *     double energy = spring.potentialEnergy(); // Get potential energy of the spring
 *     spring.relax();                           // Update rest length to current displacement
 * </code></pre>
 *
 * <br><b>Fluid API Methods</b>
 * <p>
 * The <code>Spring</code> class incorporates a fluid API design, where several 
 * methods return the <code>Spring</code> object itself, allowing method calls 
 * to be chained together. The methods that support this fluid API are:
 * </p>
 * <ul>
 *     <li><code>{@link #setActive(boolean)}</code>: Sets the active state of 
 *     the spring and returns the updated <code>Spring</code> object.</li>
 *     <li><code>{@link #setRestLength(double)}</code>: Sets the rest length 
 *     and returns the updated <code>Spring</code> object.</li>
 *     <li><code>{@link #scaleRestLength(double)}</code>: Scales the current 
 *     rest length by a given scalar and returns the updated <code>Spring</code> object.</li>
 *     <li><code>{@link #relax()}</code>: Adjusts the rest length to the current 
 *     displacement and returns the updated <code>Spring</code> object.</li>
 * </ul>
 * <p>
 * The fluid API allows users to chain these method calls in a concise and readable 
 * manner, making it easy to configure or modify a <code>Spring</code> object 
 * in a single statement. For example:
 * </p>
 * <pre><code>
 *     spring.setActive(false)
 *           .scaleRestLength(1.5)
 *           .relax();
 * </code></pre>
 * 
 * @version 1.0
 * @since   0.1.0
 * 
 * @author  Kheagen Haskins
 * 
 * @see     PointMass
 * @see     Vector2D
 * @see     Component
 */
public class Spring implements Component {

    // ============================== Static ================================ //
    /**
     * The default spring constant, <code>k</code>, used in spring creation when
     * no custom spring constant is provided. This value represents the
     * stiffness of the spring, with <code>0.5</code> being a moderate
     * stiffness.
     */
    public static final double DEFAULT_K = 0.5;

    /**
     * Creates a new <code>Spring</code> object between two
     * <code>PointMass</code> objects with a specified spring constant. This
     * static factory method is a convenient way to instantiate a spring while
     * explicitly defining the stiffness of the spring.
     *
     * @param p1 the first <code>PointMass</code> connected by the spring
     * @param p2 the second <code>PointMass</code> connected by the spring
     * @param k the spring constant (stiffness), must be greater than or equal
     * to 0.01
     * 
     * @return a new <code>Spring</code> instance connecting <code>p1</code> and
     * <code>p2</code>
     * 
     * @throws IllegalArgumentException if either <code>p1</code> or
     * <code>p2</code> is <code>null</code>
     */
    public static Spring between(PointMass p1, PointMass p2, double k) {
        return new Spring(p1, p2, k);
    }

    /**
     * Creates a new <code>Spring</code> object between two
     * <code>PointMass</code> objects using the default spring constant
     * <code>{@link #DEFAULT_K}</code>. This method provides a quick way to
     * create a spring with predefined stiffness.
     *
     * @param p1 the first <code>PointMass</code> connected by the spring
     * @param p2 the second <code>PointMass</code> connected by the spring
     * 
     * @return a new <code>Spring</code> instance connecting <code>p1</code> and
     * <code>p2</code>
     * 
     * @throws IllegalArgumentException if either <code>p1</code> or
     * <code>p2</code> is <code>null</code>
     */
    public static Spring between(PointMass p1, PointMass p2) {
        return Spring.between(p1, p2, DEFAULT_K);
    }

    // ============================== Fields ================================ //
    /**
     * The first <code>PointMass</code> connected by the spring. This is one of
     * the two entities between which the spring applies its force.
     */
    private PointMass pointA;

    /**
     * The second <code>PointMass</code> connected by the spring. This is one of
     * the two entities between which the spring applies its force.
     */
    private PointMass pointB;

    /**
     * The rest length of the spring, representing the natural length at which
     * the spring neither stretches nor compresses. When the distance between
     * <code>pointA</code> and <code>pointB</code> is equal to
     * <code>restLength</code>, the spring exerts no force.
     */
    private double restLength;

    /**
     * The spring constant, or stiffness, of the spring. This value determines
     * how much force the spring applies based on the displacement from its rest
     * length. A higher value indicates a stiffer spring.
     */
    private double k;

    /**
     * Indicates whether the spring is active or not. An inactive spring does
     * not apply any force on the connected <code>PointMass</code> objects.
     */
    private boolean active;

    // =========================== Constructors ============================= //
    /**
     * Constructs a new <code>Spring</code> object between two
     * <code>PointMass</code> objects with a specified spring constant. The rest
     * length is set to the initial distance between <code>pointA</code> and
     * <code>pointB</code>, and the spring is marked as active by default.
     *
     * @param a the first <code>PointMass</code> connected by the spring
     * @param b the second <code>PointMass</code> connected by the spring
     * @param springConstant the spring constant, which must be at least
     * <code>0.01</code> to avoid a virtually non-existent spring
     * 
     * @throws IllegalArgumentException if either <code>a</code> or
     * <code>b</code> is <code>null</code>
     */
    private Spring(PointMass a, PointMass b, double springConstant) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Cannot instantiate a Spring object with null arguments");
        } else if (a == b) {
            throw new IllegalArgumentException("Anchor and Bob cannot be the same PointMass instance");
        }

        this.pointA = a;
        this.pointB = b;
        this.k      = max(0.001, springConstant);  // Ensure spring constant is non-zero

        this.restLength   = Vector2D.sub(b.position(), a.position()).mag(); // this.displacement.mag()  // Set rest length to initial displacement
        this.active       = true;                 // Default active state
    }

    
   // ============================== Getters =============================== //
    /**
     * Returns the current rest length of the spring. The rest length is the
     * natural length of the spring at which it exerts no force. When the
     * distance between <code>pointA</code> and <code>pointB</code> is equal to
     * this rest length, the spring is in equilibrium.
     *
     * @return the rest length of the spring
     */
    public double restLength() {
        return restLength;
    }

    /**
     * Returns the first <code>PointMass</code> object connected by the spring.
     * This is one of the two entities between which the spring applies force.
     *
     * @return the <code>PointMass</code> object referred to as
     * <code>pointA</code>
     */
    public PointMass pointA() {
        return pointA;
    }

    /**
     * Returns the second <code>PointMass</code> object connected by the spring.
     * This is one of the two entities between which the spring applies force.
     *
     * @return the <code>PointMass</code> object referred to as
     * <code>pointB</code>
     */
    public PointMass pointB() {
        return pointB;
    }

    /**
     * Returns the active state of the spring. If the spring is active, it will
     * apply forces on the connected <code>PointMass</code> objects. If
     * inactive, the spring will not exert any force.
     *
     * @return <code>true</code> if the spring is active, <code>false</code>
     * otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the spring constant, or stiffness, of the spring. The spring
     * constant <code>k</code> defines how much force the spring exerts per unit
     * of displacement from its rest length. A higher spring constant results in
     * a stiffer spring.
     *
     * @return the spring constant <code>k</code>
     */
    public double constant() {
        return k;
    }

    /**
     * Calculates and returns the current displacement vector between
     * <code>pointA</code> and <code>pointB</code>. The displacement is the
     * difference in position between the two points, representing the direction
     * and distance that the spring is stretched or compressed.
     * <p>
     * This method recalculates the displacement each time it is called,
     * updating the cached displacement vector and returning a copy.
     * </p>
     *
     * @return a new <code>Vector2D</code> object representing the current
     * displacement
     */
    public Vector2D calculateDisplacement() {
//        displacement.copy(pointB.position()).sub(pointA.position());
        return Vector2D.sub(pointB.position(), pointA.position());
    }

    
// ============================== Setters =============================== //
    /**
     * Sets the active state of the spring. When the spring is active, it will
     * apply forces on the connected <code>PointMass</code> objects during
     * simulation. When inactive, the spring does not exert any force.
     * <p>
     * This method is part of the fluid API, returning the <code>Spring</code>
     * object itself to allow method chaining.
     * </p>
     *
     * @param active the new active state of the spring (<code>true</code> to
     * activate the spring, <code>false</code> to deactivate it)
     * 
     * @return the updated <code>Spring</code> object, enabling method chaining
     */
    public Spring setActive(boolean active) {
        this.active = active;
        
        return this;
    }

    /**
     * Sets a new rest length for the spring. The rest length is the natural
     * length of the spring at which it applies no force. Changing this value
     * allows the spring to adjust its equilibrium point dynamically during the
     * simulation.
     * <p>
     * This method is part of the fluid API, returning the <code>Spring</code>
     * object itself to allow method chaining.
     * </p>
     *
     * @param restLength the new rest length of the spring
     * 
     * @return the updated <code>Spring</code> object, enabling method chaining
     */
    public Spring setRestLength(double restLength) {
        this.restLength = restLength;
        
        return this;
    }

    
    // ============================ API Methods ============================= //
    /**
     * Scales the rest length of the spring by a given scalar factor. This
     * allows the rest length to be increased or decreased by multiplying it by
     * the <code>scalar</code> value.
     * <p>
     * The <code>scalar</code> must be non-negative. Attempting to scale by a
     * negative value will result in an <code>IllegalArgumentException</code>.
     * </p>
     * <p>
     * This method is part of the fluid API, returning the <code>Spring</code>
     * object itself to allow method chaining.
     * </p>
     *
     * @param scalar the factor by which to scale the current rest length, must
     * be non-negative
     * 
     * @return the updated <code>Spring</code> object, enabling method chaining
     * 
     * @throws IllegalArgumentException if <code>scalar</code> is negative
     */
    public Spring scaleRestLength(double scalar) {
        if (scalar < 0) {
            throw new IllegalArgumentException("Cannot scale spring rest length to a negative value");
        }

        restLength *= scalar;
        return this;
    }

    /**
     * Sets the rest length of the spring to its current displacement,
     * effectively "relaxing" the spring. After calling this method, the spring
     * will exert no force in its current state, as the rest length is adjusted
     * to match the current distance between <code>pointA</code> and
     * <code>pointB</code>.
     * 
     * <p>
     * This method is part of the fluid API, returning the <code>Spring</code>
     * object itself to allow method chaining.
     * </p>
     *
     * @return the updated <code>Spring</code> object, enabling method chaining
     */
    public Spring relax() {
        this.restLength   = Vector2D.sub(pointB.position(), pointA.position()).mag();

        return this;
    }

    /**
     * Calculates and returns the potential energy stored in the spring based on
     * its current displacement from the rest length. The potential energy is
     * proportional to the square of the displacement, following the formula:
     * <pre> E = 0.5 * k * (stretch)^2 </pre> where <code>k</code> is the spring
     * constant and <code>stretch</code> is the difference between the current
     * length and the rest length.
     *
     * @return the potential energy stored in the spring
     */
    public double potentialEnergy() {
        double stretch = calculateDisplacement().mag() - restLength;
        
        return 0.5 * k * stretch * stretch;
    }

    /**
     * Calculates the force exerted by the spring on the connected
     * <code>PointMass</code> objects based on the displacement from the rest
     * length. The force follows Hooke's Law:
     * <pre> F = -k * (x - L_0) </pre> where <code>k</code> is the spring
     * constant, <code>x</code> is the current length of the spring, and
     * <code>L_0</code> is the rest length. The force is restorative, meaning it
     * pulls the points back towards the rest length.
     *
     * @return a <code>Vector2D</code> representing the force exerted by the
     * spring
     */
    public Vector2D force() {
        Vector2D d = calculateDisplacement();
        double stretch
                = Vector2D.sub(pointB.position(), pointA.position())
                          .mag() - restLength;
        return d.normalize().scale(-k * (stretch));
    }
    
    /**
     * Applies the spring force to both <code>PointMass</code> objects connected
     * by the spring. The force is calculated using Hooke's Law, and the spring
     * applies equal and opposite forces to <code>pointA</code> and
     * <code>pointB</code>.
     * <p>
     * The force is calculated using the <code>{@link #force()}</code> method,
     * which returns the force vector. This force is added to
     * <code>pointA</code>, and the negated version of the force is applied to
     * <code>pointB</code>, ensuring that the spring follows Newton's third law
     * (equal and opposite forces).
     * </p>
     *
     * <p>
     * The method modifies the internal state of both <code>PointMass</code>
     * objects, applying the spring forces to them during each physics update.
     * </p>
     */
    public void applySpringForce() {
        Vector2D force = force();
        pointB.addForce(force);
        pointA.addForce(force.copy().negate());
    }
    
    
    // FOR DEBUG, REMOVE
    public void draw(GraphicsContext gc) {
        gc.strokeLine(pointA.x(), pointA.y(), pointB.x(), pointB.y());
    }
    
}