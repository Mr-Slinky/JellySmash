package com.slinky.jellysmash.model.physics.comps;

/**
 * The {@code Position} interface is a marker interface representing a specific
 * type of {@link Vector} that is used to denote positional data in the
 * JellySmash soft-body physics engine. While it does not add any new behaviour
 * or properties beyond what is provided by the {@link Vector} interface, it
 * allows instances of classes implementing {@code Position} to be easily
 * organised into groups, following a data-oriented design approach.
 * 
 * <p>
 * This design choice facilitates efficient processing of positional data,
 * enabling the engine to treat positional vectors separately from other types
 * of vectors such as velocities or accelerations, even though they share the
 * same underlying structure and behaviour.
 * </p>
 * 
 * <p>
 * By categorising vectors as positions, the physics engine can optimise certain
 * operations, such as collision detection or spatial partitioning, by focusing
 * specifically on positional data. This also aids in code readability and
 * maintainability by clearly indicating the intent of a vector's use within the
 * physics system.
 * </p>
 *
 * @author Kheagen
 * @see    Vector
 * @see    Component
 */
public interface Position extends Vector {}