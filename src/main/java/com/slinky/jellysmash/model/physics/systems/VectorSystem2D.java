package com.slinky.jellysmash.model.physics.systems;

import com.slinky.jellysmash.model.physics.comps.Vector;
import com.slinky.jellysmash.model.physics.comps.Vector2D;

/**
 * The {@code VectorSystem2D} abstract class provides a foundational framework
 * for any system that aims to work with {@link Vector} objects within a 2D
 * space in an Entity-Component-System (ECS) architecture.
 *
 * <p>
 * This class offers a comprehensive set of vector operations, including
 * addition, subtraction, scaling, normalisation, dot product, cross product,
 * and more. These operations are essential for manipulating vectors in physics
 * simulations, computer graphics, and other applications that require precise
 * control over 2D vector data. The class ensures that vector operations are
 * performed safely and consistently by checking type compatibility between
 * vectors before executing operations, throwing an
 * {@link IllegalArgumentException} in case of a mismatch.
 * </p>
 *
 * <p>
 * The design pattern employed in this class provides dual versions of certain
 * vector operations:
 * <ul>
 * <li><b>Target-modifying methods:</b> These methods, such as
 * {@code addTarget(Vector v1, Vector v2)} and
 * {@code subTarget(Vector v1, Vector v2)}, modify the components of the first
 * vector ({@code v1}) directly by adding or subtracting the corresponding
 * components of the second vector ({@code v2}).</li>
 * <li><b>Non-mutating methods:</b> These methods, such as
 * {@code add(Vector2D v1, Vector2D v2)} and
 * {@code sub(Vector2D v1, Vector2D v2)}, return a new {@code Vector2D} instance
 * that represents the result of the operation, leaving the original vectors
 * unchanged.</li>
 * </ul>
 * This pattern allows users to choose between directly modifying vectors or
 * working with immutable results, depending on the needs of the application.
 * </p>
 *
 * <p>
 * <b>Example Usage:</b>
 * <pre><code>
 *     Vector2D v1 = new Vector2D(1.0, 2.0);
 *     Vector2D v2 = new Vector2D(3.0, 4.0);
 *
 *     // Modifying the target vector directly
 *     vectorSystem.addTarget(v1, v2);  // v1 is now (4.0, 6.0)
 *
 *     // Creating a new vector with the result of the addition
 *     Vector2D v3 = vectorSystem.add(v1, v2);  // v3 is (7.0, 10.0), v1 remains (4.0, 6.0)
 * </code></pre>
 * </p>
 *
 * @author Kheagen Haskins
 *
 * @see System
 * @see Vector
 * @see Vector2D
 */
public abstract class VectorSystem2D implements System {

    // ============================ API Methods ============================= //
    /**
     * Creates a new {@code Vector2D} representing the sum of the components of
     * two {@code Vector2D} instances.
     *
     * <p>
     * This operation does not modify the original vectors but returns a new
     * {@code Vector2D} instance with components equal to the sum of the
     * corresponding components of the input vectors. If the provided vectors
     * are not compatible (i.e., not instances of {@code Vector2D}), an
     * {@code IllegalArgumentException} is thrown.
     * </p>
     *
     * @param v1 the first vector to add.
     * @param v2 the second vector to add.
     * @return a new {@code Vector2D} that is the result of adding the
     * components of {@code v1} and {@code v2}.
     * @throws IllegalArgumentException if the vectors are not of type
     * {@code Vector2D}.
     */
    public Vector2D add(Vector2D v1, Vector2D v2) {
        throwMismatch(v1, v2);
        return new Vector2D(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    /**
     * Subtracts the components of one {@code Vector} ({@code v2}) from another
     * {@code Vector} ({@code v1}).
     *
     * <p>
     * This operation modifies the target vector {@code v1} by subtracting the
     * corresponding components of the {@code v2} vector from it. If the
     * provided vectors are not compatible (i.e., not instances of the same
     * vector class), an {@code IllegalArgumentException} is thrown.
     * </p>
     *
     * @param v1 the vector from which the other vector's components will be
     * subtracted.
     * @param v2 the vector whose components will be subtracted from the target
     * vector.
     * @throws IllegalArgumentException if the vectors are not of the same type.
     */
    public void subTarget(Vector v1, Vector v2) {
        throwMismatch(v1, v2);
        v1.setComponents(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    }

    /**
     * Creates a new {@code Vector2D} representing the difference between the
     * components of two {@code Vector2D} instances.
     *
     * <p>
     * This operation does not modify the original vectors but returns a new
     * {@code Vector2D} instance with components equal to the difference between
     * the corresponding components of the input vectors. If the provided
     * vectors are not compatible (i.e., not instances of {@code Vector2D}), an
     * {@code IllegalArgumentException} is thrown.
     * </p>
     *
     * @param v1 the vector from which the other vector's components will be
     * subtracted.
     * @param v2 the vector whose components will be subtracted from the first
     * vector.
     * @return a new {@code Vector2D} that is the result of subtracting the
     * components of {@code v2} from {@code v1}.
     * @throws IllegalArgumentException if the vectors are not of type
     * {@code Vector2D}.
     */
    public Vector2D sub(Vector2D v1, Vector2D v2) {
        throwMismatch(v1, v2);
        return new Vector2D(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    }

    /**
     * Adds the components of one {@code Vector} ({@code v2}) to another
     * {@code Vector} ({@code v1}).
     *
     * <p>
     * This operation modifies the target vector {@code v1} by adding the
     * corresponding components of the {@code v2} vector to it. If the provided
     * vectors are not compatible (i.e., not instances of the same vector
     * class), an {@code IllegalArgumentException} is thrown.
     * </p>
     *
     * @param v1 the vector to which the other vector's components will be
     * added.
     * @param v2 the vector whose components will be added to the target vector.
     * @throws IllegalArgumentException if the vectors are not of the same type.
     */
    public void addTarget(Vector v1, Vector v2) {
        throwMismatch(v1, v2);
        v1.setComponents(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    /**
     * Creates a new {@code Vector2D} that is the result of scaling a
     * {@code Vector2D} by a given scalar value.
     *
     * <p>
     * This operation multiplies both the x and y components of the vector by
     * the specified scalar, returning a new {@code Vector2D} with the scaled
     * components. The original vector remains unchanged.
     * </p>
     *
     * @param v the vector to be scaled.
     * @param scalar the value by which to scale the vector.
     * @return a new {@code Vector2D} with the scaled components.
     */
    public Vector2D scale(Vector2D v, double scalar) {
        return new Vector2D(v.getX() * scalar, v.getY() * scalar);
    }

    /**
     * Scales a {@code Vector} by a given scalar value, modifying the original
     * vector.
     *
     * <p>
     * This operation multiplies both the x and y components of the target
     * vector by the specified scalar, effectively changing its magnitude while
     * preserving its direction. The original vector is modified and returned.
     * </p>
     *
     * @param v the vector to be scaled.
     * @param scalar the value by which to scale the vector.
     * @return the modified vector after scaling.
     */
    public Vector scaleTarget(Vector v, double scalar) {
        v.setComponents(v.getX() * scalar, v.getY() * scalar);
        return v;
    }

    /**
     * Divides the components of the given {@code Vector} by a scalar value and
     * returns a new {@code Vector2D} with the resulting components.
     *
     * <p>
     * This method does not modify the original vector but returns a new
     * {@code Vector2D} instance with components equal to the original
     * components divided by the scalar value. This operation can be used when
     * you need a new vector that represents the result of a division operation,
     * while keeping the original vector unchanged.
     * </p>
     *
     * @param v the vector whose components will be divided.
     * @param scalar the double value by which to divide the vector's
     * components.
     * @return a new {@code Vector2D} with the components of {@code v} divided
     * by the scalar.
     * @throws ArithmeticException if the scalar is zero, as division by zero is
     * undefined.
     */
    public Vector2D divTarget(Vector v, double scalar) {
        return new Vector2D(
                v.getX() / scalar,
                v.getY() / scalar
        );
    }

    /**
     * Divides the components of the given {@code Vector} by a scalar value and
     * modifies the original vector with the resulting components.
     *
     * <p>
     * This method modifies the original vector {@code v} by dividing its
     * components by the specified scalar value. The method then returns the
     * modified vector. This operation is useful when you want to directly
     * update the vector with the result of the division operation, without
     * creating a new vector instance.
     * </p>
     *
     * @param v the vector whose components will be divided.
     * @param scalar the double value by which to divide the vector's
     * components.
     * @return the modified vector {@code v} with its components divided by the
     * scalar.
     * @throws ArithmeticException if the scalar is zero, as division by zero is
     * undefined.
     */
    public Vector div(Vector v, double scalar) {
        v.setComponents(
                v.getX() / scalar,
                v.getY() / scalar
        );

        return v;
    }

    /**
     * Returns the magnitude (length) of a {@code Vector}.
     * <p>
     * The magnitude is calculated as the Euclidean norm, defined as
     * {@code sqrt(x^2 + y^2)}. It represents the distanceBetween of the point
     * (x, y) from the origin in the vector space.
     * </p>
     *
     * @param v the vector whose magnitude is to be calculated.
     * @return the magnitude of the vector.
     */
    public double magnitude(Vector v) {
        return Math.sqrt((v.getX() * v.getX()) + (v.getY() * v.getY()));
    }

    /**
     * Computes the dot product of two vectors.
     * <p>
     * The dot product is calculated as {@code (v1.x * v2.x) + (v1.y * v2.y)}.
     * This operation is useful for determining the angle between two vectors or
     * for projecting one vector onto another.
     * </p>
     *
     * @param v1 the first vector.
     * @param v2 the second vector.
     * @return the dot product of the two vectors.
     * @throws IllegalArgumentException if the vectors are not of the same type.
     */
    public double dotProduct(Vector v1, Vector v2) {
        throwMismatch(v1, v2);
        return (v1.getX() * v2.getX()) + (v1.getY() * v2.getY());
    }

    /**
     * Computes the 2D cross product of two vectors.
     * <p>
     * The 2D cross product is computed as
     * {@code (v1.x * v2.y) - (v1.y * v2.x)}. It represents the signed area of
     * the parallelogram formed by the two vectors. This value is useful for
     * determining the relative orientation of the vectors.
     * </p>
     *
     * @param v1 the first vector.
     * @param v2 the second vector.
     * @return the cross product of the two vectors.
     * @throws IllegalArgumentException if the vectors are not of the same type.
     */
    public double crossProduct(Vector v1, Vector v2) {
        throwMismatch(v1, v2);
        return (v1.getX() * v2.getY()) - (v1.getY() * v2.getX());
    }

    /**
     * Calculates the angle between two vectors in radians.
     * <p>
     * The angle is calculated using the dot product and magnitudes of the
     * vectors. The result is in radians, ranging from 0 to π.
     * </p>
     *
     * @param v1 the first vector.
     * @param v2 the second vector.
     * @return the angle between the two vectors in radians.
     * @throws IllegalArgumentException if the vectors are not of the same type.
     */
    public double angleBetween(Vector v1, Vector v2) {
        throwMismatch(v1, v2);
        double dot = dotProduct(v1, v2);
        double mags = magnitude(v1) * magnitude(v2);
        return Math.acos(dot / mags);
    }

    /**
     * Reflects a vector across another vector.
     * <p>
     * The reflection of a vector is calculated using the formula:
     * {@code R = V - 2 * (V . N) * N}, where {@code V} is the original vector
     * and {@code N} is the normal vector.
     * </p>
     *
     * @param v the vector to be reflected.
     * @param normal the vector to reflect across.
     * @return a new vector representing the reflection.
     * @throws IllegalArgumentException if the vectors are not of the same type.
     */
    public Vector reflect(Vector v, Vector normal) {
        throwMismatch(v, normal);
        double dot = dotProduct(v, normal);
        return new Vector2D(
                v.getX() - 2 * dot * normal.getX(),
                v.getY() - 2 * dot * normal.getY()
        );
    }

    /**
     * Rotates a vector by a given angle around the origin.
     * <p>
     * The rotation is performed using the 2D rotation matrix:<br/>
     * <code> x' = x * cos(theta) - y * sin(theta)</code>,<br/>
     * <code> y' = x * sin(theta) + y * cos(theta)</code>.
     * </p>
     *
     * @param v the vector to rotate.
     * @param angle the angle in radians by which to rotate the vector.
     * @return a new vector representing the rotated vector.
     */
    public Vector rotate(Vector v, double angle) {
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        return new Vector2D(
                v.getX() * cosTheta - v.getY() * sinTheta,
                v.getX() * sinTheta + v.getY() * cosTheta
        );
    }

    /**
     * Projects one vector onto another vector.
     * <p>
     * The projection is calculated using the formula:
     * {@code proj_v1_on_v2 = (v1 . v2) / |v2|^2 * v2}.
     * </p>
     *
     * @param v1 the vector to be projected.
     * @param v2 the vector onto which {@code v1} is projected.
     * @return a new vector representing the projection of {@code v1} onto
     * {@code v2}.
     * @throws IllegalArgumentException if the vectors are not of the same type.
     */
    public Vector project(Vector v1, Vector v2) {
        throwMismatch(v1, v2);
        double dot = dotProduct(v1, v2);
        double magSq = magnitude(v2) * magnitude(v2);
        return new Vector2D(
                (dot / magSq) * v2.getX(),
                (dot / magSq) * v2.getY()
        );
    }

    /**
     * Calculates the distance between two vectors.
     * <p>
     * The distanceBetween is calculated as the Euclidean distance between the
     * two points represented by the vectors.
     * </p>
     *
     * @param v1 the first vector.
     * @param v2 the second vector.
     * @return the distanceBetween between the two vectors.
     * @throws IllegalArgumentException if the vectors are not of the same type.
     */
    public double distanceBetween(Vector v1, Vector v2) {
        throwMismatch(v1, v2);
        return Math.sqrt(Math.pow(v2.getX() - v1.getX(), 2) + Math.pow(v2.getY() - v1.getY(), 2));
    }

    /**
     * Linearly interpolates between two vectors.
     * <p>
     * The interpolation is computed as {@code v1 + t * (v2 - v1)} where
     * {@code t} is a value between 0 and 1 that determines the blend factor
     * between the two vectors.
     * </p>
     *
     * @param v1 the starting vector.
     * @param v2 the ending vector.
     * @param t the interpolation factor, which must be between 0 inclusive and
     * 1 exclusive.
     * @return a new vector representing the interpolated result.
     * @throws IllegalArgumentException if the vectors are not of the same type.
     */
    public Vector lerp(Vector v1, Vector v2, double t) {
        throwMismatch(v1, v2);
        return new Vector2D(
                v1.getX() + t * (v2.getX() - v1.getX()),
                v1.getY() + t * (v2.getY() - v1.getY())
        );
    }

    /**
     * Normalises a {@code Vector}, making its magnitude equal to 1.
     * <p>
     * Normalising a vector involves scaling it so that its magnitude becomes 1,
     * while maintaining its direction. If the vector is a zero vector
     * (magnitude of 0), this method does nothing.
     * </p>
     *
     * @param v the vector to be normalised.
     */
    public void normalize(Vector v) {
        double mag = magnitude(v);

        if (mag != 0) {
            v.setComponents(v.getX() / mag, v.getY() / mag);
        }
    }

    // ========================== Helper Methods ============================ //
    /**
     * Throws an {@link IllegalArgumentException} if the provided vectors are
     * not of the same type.
     * <p>
     * This method checks whether the two provided {@code Vector} instances are
     * of the same class. If they are not, it throws an
     * {@code IllegalArgumentException} with a message indicating a vector type
     * mismatch. This utility method is useful for ensuring that operations
     * involving two vectors are only performed when the vectors are compatible.
     * </p>
     *
     * @param v1 the first vector to check.
     * @param v2 the second vector to check.
     * @throws IllegalArgumentException if the vectors are not of the same type.
     */
    private static void throwMismatch(Vector v1, Vector v2) {
        if (!v1.getClass().equals(v2.getClass())) {
            throw new IllegalArgumentException("Vector type mismatch.");
        }
    }

}