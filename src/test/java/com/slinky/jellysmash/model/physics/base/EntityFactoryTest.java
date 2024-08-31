package com.slinky.jellysmash.model.physics.base;

import com.slinky.jellysmash.model.physics.base.ComponentManager;
import com.slinky.jellysmash.model.physics.base.EntityManager;
import com.slinky.jellysmash.model.physics.base.Entity;
import com.slinky.jellysmash.model.physics.base.Component;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *
 * @author Kheagen Haskins
 */
public class EntityFactoryTest {

    private static class MockComponent1 implements Component {
    }

    private static class MockComponent2 implements Component {
    }

    private EntityManager factory;
    private ComponentManager componentManager;

    @BeforeEach
    public void setUp() {
        componentManager = new ComponentManager();
        factory = new EntityManager(componentManager);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Test EntityFactory Constructor Against null Input")
    public void testEntityFactoryConstructor_NullInput_Throws(ComponentManager nullCompMan) {
        assertThrows(IllegalArgumentException.class,
                () -> new EntityManager(nullCompMan)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 100})
    @DisplayName("Test Entity Creation With Entity Retrieval")
    public void testEntityCreation_NoComponents_CanRetrieveEntities(int count) {
        long[] ids = new long[count];
        Entity[] ents = new Entity[count];

        for (int i = 0; i < count; i++) {
            Entity e = factory.newEntity();
            ents[i] = e;
            ids[i] = e.id();
        }

        for (int i = 0; i < count; i++) {
            assertSame(ents[i], factory.getEntity(ids[i]));
        }
    }

    @Test
    @DisplayName("Test Entity Destruction With Entity Retrieval")
    public void testEntityDestruction_NoComponents_CantRetrieveEntities() {
        Entity e = factory.newEntity();
        assertAll(
                () -> assertTrue(factory.destroyEntity(factory.getEntity(e.id())), "First call to factory.destoryEntity returned false when true was expected"),
                () -> assertFalse(factory.destroyEntity(e), "Second call to factory.destoryEntity returned true when false expected"),
                () -> assertNull(factory.getEntity(e.id()), "Expected null return from factory.getEntity after destroyed")
        );
    }

    @Test
    @DisplayName("EntityFactory/ComponentManager Integration Test - getComponent()")
    public void testIntegrationWithComponentManager_getComponent_EqualityCheck() {
        Component comp1 = new MockComponent1();
        Component comp2 = new MockComponent2();
        Entity ent = factory.newEntity(comp1, comp2);

        assertAll(
                () -> assertSame(comp1, componentManager.getComponent(ent, comp1.getClass())),
                () -> assertNotSame(comp1, componentManager.getComponent(ent, comp2.getClass())),
                () -> assertSame(comp2, componentManager.getComponent(ent, comp2.getClass())),
                () -> assertNotSame(comp2, componentManager.getComponent(ent, comp1.getClass()))
        );
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("EntityFactory/ComponentManager Integration Test - Null Components")
    public void testIntegrationWithComponentManager(Component nullComp) {
        Component comp1 = new MockComponent1();
        Component comp2 = new MockComponent2();
        
        assertThrows(
                NullPointerException.class,
                () -> factory.newEntity(comp1, comp2, nullComp)
        );
    }

    @AfterEach
    public void cleanUp() {
        factory.clean();
    }

}