package com.slinky.jellysmash.model.physics.base;

import com.slinky.jellysmash.model.physics.comps.Component;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author Kheagen Haskins
 */
public class EntitiesTest {

    private class ComponentMock1 implements Component {
    }

    private class ComponentMock2 implements Component {
    }

    private class ComponentMock3 implements Component {
    }

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

    @RepeatedTest(2)
    @DisplayName("Test Entity ID Increments")
    public void testCreateEntity_IDIncrements_MatchExpected() {
        Entity e1 = Entities.newEntity();
        Entity e2 = Entities.newEntity();
        Entity e3 = Entities.newEntity();

        assertAll(
                () -> assertEquals(1, e1.id()),
                () -> assertEquals(2, e2.id()),
                () -> assertEquals(3, e3.id()),
                () -> assertNotEquals(-1, e1.id()),
                () -> assertNotEquals(-1, e2.id())
        );

    }

    @Test
    public void testEntityRetrieval_CreateMultiple_Retrievable() {
        Entity e1 = Entities.newEntity();
        Entity e2 = Entities.newEntity();
        Entity e3 = Entities.newEntity();
        Entity e4 = new Entity(1000);

        var allEntities = Entities.getAllEntities();

        assertAll(
                () -> assertTrue(allEntities.containsValue(e1)),
                () -> assertTrue(allEntities.containsValue(e2)),
                () -> assertTrue(allEntities.containsValue(e3)),
                () -> assertFalse(allEntities.containsValue(e4)),
                () -> assertSame(e1, Entities.getEntity(1)),
                () -> assertSame(e2, Entities.getEntity(2)),
                () -> assertSame(e3, Entities.getEntity(3)),
                () -> assertNull(Entities.getEntity(4)),
                () -> assertEquals(3, allEntities.size())
        );
    }

    @Test
    public void testCreateEntity_WithComponents_AllDifferent() {
        Entity e1 = Entities.newEntity(comp1A, comp2A, comp3A);
        assertAll(
                () -> assertSame(comp1A, e1.getComponent(comp1A.getClass())),
                () -> assertSame(comp2A, e1.getComponent(comp2B.getClass())),
                () -> assertSame(comp3A, e1.getComponent(ComponentMock3.class))
        );
    }

    @Test
    public void testCreateEntity_WithNull_ThrowsError() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> Entities.newEntity(null, comp2A, comp3A)),
                () -> assertThrows(IllegalArgumentException.class, () -> Entities.newEntity(comp1A, null, comp3A)),
                () -> assertThrows(IllegalArgumentException.class, () -> Entities.newEntity(comp1A, comp2A, null))
        );
    }

    @Test
    public void testDestroyEntity() {
        Entity e1 = Entities.newEntity(comp1A, comp2A, comp3A);
        Entity e2 = Entities.newEntity(comp1B, comp2B, comp3B);

        Entities.destroyEntity(e1);

        assertAll(
                () -> assertNull(e1.getAllComponents()),
                () -> assertNull(Entities.getEntity(1)),
                () -> assertNotNull(Entities.getEntity(2))
        );
        
    }

    @AfterEach
    public void cleanUp() {
        Entities.clean();
    }

}
