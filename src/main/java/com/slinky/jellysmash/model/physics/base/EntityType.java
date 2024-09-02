package com.slinky.jellysmash.model.physics.base;

import com.slinky.jellysmash.model.physics.comps.Circle;
import com.slinky.jellysmash.model.physics.comps.Component;
import com.slinky.jellysmash.model.physics.comps.PointMass;

/**
 * The {@code EntityType} enum defines and categorises different types of
 * entities within the JellySmash game, associating each entity type with a set
 * of component classes that define its behaviour and properties.
 *
 * <p>
 * Each {@code EntityType} instance is linked with specific {@link Component}
 * classes, which act as the key to identifying and grouping entities based on
 * their characteristics and the components they possess. This design allows for
 * efficient retrieval and management of entities within the ECS
 * (Entity-Component-System) architecture by grouping them based on their
 * associated components.
 * </p>
 *
 * <p>
 * For example, the {@code BALL} entity type is associated with both the
 * {@link PointMass} and {@link Circle} components, making it easy to filter and
 * manage all entities representing balls within the game.
 * </p>
 *
 * @version 2.0
 * @since 0.1.0
 *
 * @author Kheagen Haskins
 *
 * @see Entity
 * @see Entities
 * @see Component
 *
 */
public enum EntityType {

    /**
     * Represents the ball in the game.
     * <p>
     * This entity type is associated with the {@link PointMass} and
     * {@link Circle} components, which together define the behaviour and
     * properties of a ball within the JellySmash game. The {@code BALL} entity
     * type can be used to easily filter and manage all ball-related entities in
     * the game.
     * </p>
     */
    BALL(new Class[]{PointMass.class, Circle.class}),
    BRICK;

    /**
     * An array of {@link Class} objects representing the component classes
     * associated with this {@code EntityType}.
     */
    private final Class<? extends Component>[] componentClasses;

    /**
     * Constructs an {@code EntityType} with the specified component classes.
     * <p>
     * This constructor associates the given component classes with the
     * {@code EntityType}, allowing entities of this type to be identified and
     * managed based on their components.
     * </p>
     *
     * @param componentClasses the classes of components associated with this
     * {@code EntityType}
     * @param <T> the type of components extending {@link Component}
     */
    @SafeVarargs
    private <T extends Component> EntityType(Class<T>... componentClasses) {
        this.componentClasses = componentClasses;
    }

    /**
     * Retrieves the component classes associated with this {@code EntityType}.
     * <p>
     * This method returns an array of component classes that are linked to the
     * {@code EntityType}. These classes define the behaviour and properties of
     * entities that fall under this type.
     * </p>
     *
     * @return an array of {@link Class} objects representing the component
     * classes associated with this {@code EntityType}.
     */
    public Class[] getComponentClasses() {
        return componentClasses;
    }

}