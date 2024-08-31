package com.slinky.jellysmash.model.physics;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Kheagen Haskins
 */
public class EntityFactoryTest {

    private EntityFactory factory;
    
    
    
    @BeforeEach
    public void setUp() {
        factory = new EntityFactory(null);
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

}
