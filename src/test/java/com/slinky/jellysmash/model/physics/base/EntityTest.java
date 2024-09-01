package com.slinky.jellysmash.model.physics.base;

import com.slinky.jellysmash.model.physics.comps.Component;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the Entity class.
 *
 * This test class is designed to validate the functionality of the Entity
 * class, specifically its constructor and getter method.
 *
 * @author: Kheagen Haskins
 */
public class EntityTest {

    private class ComponentMock1 implements Component {}
    private class ComponentMock2 implements Component {}
    private class ComponentMock3 implements Component {}

    private ComponentMock1 comp1A;
    private ComponentMock1 comp1B;
    private ComponentMock2 comp2A;
    private ComponentMock2 comp2B;
    private ComponentMock3 comp3A;
    private ComponentMock3 comp3B;

    @BeforeEach
    public void setup() {
        comp1A = new ComponentMock1();
        comp1B = new ComponentMock1();
        comp2A = new ComponentMock2();
        comp2B = new ComponentMock2();
        comp3A = new ComponentMock3();
        comp3B = new ComponentMock3();
    }

    /**
     * Tests the Entity class constructor and the id() getter method.
     *
     * This parameterized test runs multiple times, each with a different input
     * integer value from the ValueSource. For each iteration, it creates a new
     * Entity instance with the given ID, and then checks that the instance is
     * created without throwing any exceptions. It also verifies that the ID
     * returned by the id() method matches the input value.
     *
     * @param id The number of times to create and verify the Entity
     * instances.
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 100, 1000, -10})
    @DisplayName("Test Entity Constructor and Getter")
    public void testConstuctorAndGetter(int id) {
        Entity ent = new Entity(id);
        assertEquals(id, ent.id());
    }

    @Test
    @DisplayName("Test Add Mutliple Components of Same Type")
    public void testAddComponent_AddMultiple_PreventsOverride() {
        Entity ent = new Entity(1);
        ent.addComponent(comp1A);
        ent.addComponent(comp2A);
        ent.addComponent(comp3A);

        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> ent.addComponent(comp1B)),
                () -> assertThrows(IllegalStateException.class, () -> ent.addComponent(comp2B)),
                () -> assertThrows(IllegalStateException.class, () -> ent.addComponent(comp3B)),
                () -> assertEquals(3, ent.getAllComponents().size())
        );

    }

    @Test
    @DisplayName("Test Add And Get Mutliple Components of Different Type")
    public void testAddAndGetComponent_AddMultipleOfDifferent_RetrievalBehaviour() {
        Entity ent = new Entity(1);
        ent.addComponent(comp1A);
        ent.addComponent(comp2A);
        ent.addComponent(comp3A);

        assertAll(
                () -> assertEquals(3, ent.getAllComponents().size()),
                () -> assertSame(comp1A, ent.getComponent(comp1A.getClass())),
                () -> assertSame(comp1A, ent.getComponent(comp1B.getClass())),
                () -> assertSame(comp2A, ent.getComponent(comp2A.getClass())),
                () -> assertSame(comp2A, ent.getComponent(comp2B.getClass())),
                () -> assertSame(comp3A, ent.getComponent(comp3A.getClass())),
                () -> assertSame(comp3A, ent.getComponent(comp3B.getClass())),
                () -> assertNotSame(comp1A, ent.getComponent(comp2A.getClass())),
                () -> assertNotSame(comp2A, ent.getComponent(comp3A.getClass())),
                () -> assertNotSame(comp3A, ent.getComponent(comp1A.getClass()))
        );

    }

    @Test
    public void testHasComponent_AddMultipleOfDifferent() {
        Entity ent = new Entity(1);
        ent.addComponent(comp1A);
        ent.addComponent(comp2A);
        ent.addComponent(comp3A);

        assertAll(
                () -> assertTrue(ent.hasComponent(comp1A.getClass())),
                () -> assertTrue(ent.hasComponent(comp2A.getClass())),
                () -> assertTrue(ent.hasComponent(comp3A.getClass())),
                () -> assertTrue(ent.hasComponent(comp1B.getClass())),
                () -> assertTrue(ent.hasComponent(comp2B.getClass())),
                () -> assertTrue(ent.hasComponent(ComponentMock3.class))
        );
    }

    @Test
    public void testRemoveComponent_MultipleOfDifferent() {
        Entity ent = new Entity(1);
        ent.addComponent(comp1A);
        ent.addComponent(comp2A);
        ent.addComponent(comp3A);

        ent.removeComponent(comp1A.getClass());
        ent.removeComponent(comp2B.getClass());
        ent.removeComponent(ComponentMock3.class);

        assertAll(
                () -> assertFalse(ent.hasComponent(ComponentMock1.class)),
                () -> assertFalse(ent.hasComponent(comp2A.getClass())),
                () -> assertFalse(ent.hasComponent(comp3A.getClass()))
        );
    }
    
    @Test
    public void testRemoveAllComponents() {
        Entity ent = new Entity(1);
        
        ent.addComponent(comp1A);
        ent.addComponent(comp2A);
        ent.addComponent(comp3A);
        
        ent.removeAllComponents();
        
         assertAll(
                () -> assertFalse(ent.hasComponent(ComponentMock1.class)),
                () -> assertFalse(ent.hasComponent(comp2A.getClass())),
                () -> assertFalse(ent.hasComponent(comp3A.getClass()))
        );
    }
    
    @AfterEach
    public void cleanUp() {
        Entities.clean();
    }

}