package com.slinky.jellysmash.model.physics.comps;

/**
 * The {@code Vector} interface serves as a marker interface, extending the
 * {@link Position} interface within the Entity-Component-System (ECS)
 * architecture of the JellySmash physics engine.
 * <p>
 * This interface does not introduce any new methods or fields beyond those
 * inherited from {@link Position}. Its primary purpose is to provide a semantic
 * distinction between components that represent different types of spatial data
 * within the physics engine, specifically identifying components that function
 * as vectors rather than just positions.
 * <p>
 * In the context of physics simulations, a vector often represents both a
 * direction and a magnitude, such as <strong>velocity</strong> or
 * <strong>acceleration</strong>. By using the {@code Vector} interface as a
 * marker, the design facilitates the organisation of vector-related data into
 * contiguous blocks of memory, which is a core principle of Data-Oriented
 * Programming (DOP). This approach enhances cache efficiency and overall
 * performance, particularly in computation-heavy scenarios typical in physics
 * simulations.
 * <p>
 * The separation of vectors from positions using this marker interface allows
 * the ECS architecture to store and process vector data independently,
 * optimising access patterns and reducing cache misses. This is critical for
 * the performance of the physics engine, as it enables the system to handle
 * large volumes of vector data (such as forces or velocities) efficiently,
 * adhering to the DOP paradigm where data layout is optimised for processing
 * speed.
 * <p>
 * Furthermore, this design decision aligns with the ECS principles by allowing
 * components to be easily identified and managed based on their roles within
 * the system, without compromising type safety. The ability to distinguish
 * between vector and position components through this interface also promotes
 * clarity and extensibility, ensuring that the codebase remains maintainable
 * and scalable as new spatial components are introduced.
 *
 * @version 1.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see Component
 * @see Position
 */
public interface Vector extends Position {}