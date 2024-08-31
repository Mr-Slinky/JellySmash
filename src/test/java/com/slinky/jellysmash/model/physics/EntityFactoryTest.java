package com.slinky.jellysmash.model.physics;

import com.slinky.jellysmash.model.physics.comps.ComponentManager;

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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *
 * @author Kheagen Haskins
 */
public class EntityFactoryTest {

    private EntityFactory factory;
    private ComponentManager componentManager;

    @BeforeEach
    public void setUp() {
        componentManager = new ComponentManager();
        factory = new EntityFactory(componentManager);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Test EntityFactory Constructor Against null Input")
    public void testEntityFactoryConstructor_NullInput_Throws(ComponentManager nullCompMan) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EntityFactory(nullCompMan)
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
    @DisplayName("Test Entity Deletion With Entity Retrieval")
    public void testEntityDeletion_NoComponents_CantRetrieveEntities() {
            Entity e = factory.newEntity();
            assertAll(
                    () -> assertTrue(factory.destroyEntity(factory.getEntity(e.id())), "First call to factory.destoryEntity returned false when true was expected"),
                    () -> assertFalse(factory.destroyEntity(e), "Second call to factory.destoryEntity returned true when false expected"),
                    () -> assertNull(factory.getEntity(e.id()), "Expected null return from factory.getEntity after destroyed")
            );
    }
    
    

    @AfterEach
    public void cleanUp() {
        factory.clean();
    }

}