package com.slinky.jellysmash.model.physics.base;

import com.slinky.jellysmash.model.physics.base.ComponentManager;
import com.slinky.jellysmash.model.physics.base.Entity;
import com.slinky.jellysmash.model.physics.base.Component;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 *
 * @author Kheagen Haskins
 */
public class ComponentManagerTest {

    private static class MockComponent1 implements Component {}
    private static class MockComponent2 implements Component {}
    
    private ComponentManager compMan;
    private Component mockComponent1;
    private Component mockComponent2;

    @BeforeEach
    public void setUp() {
        compMan = new ComponentManager();
        mockComponent1 = new MockComponent1();
        mockComponent2 = new MockComponent2();
    }

    @Test
    public void testAddComponent_MockComponent_NoThrow() {
        assertDoesNotThrow(() -> compMan.addComponent(new Entity(1), mockComponent1));
    }

    @ParameterizedTest
    @NullSource
    public void testAddComponent_NullEntity_Throws(Entity nullE) {
        assertThrows(
                IllegalArgumentException.class,
                () -> compMan.addComponent(nullE, mockComponent1)
        );
    }

    @ParameterizedTest
    @NullSource
    public void testAddComponent_NullComponent_Throws(Component nullC) {
        assertThrows(
                IllegalArgumentException.class,
                () -> compMan.addComponent(new Entity(1), nullC)
        );
    }

    @Test
    public void testAddThenGetComponent_MockComponent_CorrectlyRetrieves() {
        Entity ent = new Entity(1);
        compMan.addComponent(ent, mockComponent1);
        assertAll(
                () -> assertSame (mockComponent1, compMan.getComponent(ent, mockComponent1.getClass())),
                () -> assertTrue (compMan.getComponent(ent, mockComponent1.getClass()) instanceof MockComponent1),
                () -> assertFalse(compMan.getComponent(ent, mockComponent1.getClass()) instanceof MockComponent2)
        );
    }

    @Test
    public void testAddThenGetComponent_MultipleMockComponents_RejectsOverride() {
        Entity ent = new Entity(1);
        MockComponent1 mockComp1 = new MockComponent1();
        MockComponent1 mockComp2 = new MockComponent1();

        compMan.addComponent(ent, mockComp1);

        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> compMan.addComponent(ent, mockComp2)),
                () -> assertSame(mockComp1, compMan.getComponent(ent, mockComp1.getClass())),
                () -> assertNotSame(mockComp2, compMan.getComponent(ent, mockComp2.getClass()))
        );
    }

    @Test
    @DisplayName("Test: Add Then Remove With hasComponent()")
    public void testAddThenRemoveComponent_SingleMockComponent_ComponentOverrides() {
        Entity ent = new Entity(1);

        compMan.addComponent(ent, mockComponent1);
        assertAll(
                () -> assertTrue(compMan.hasComponent(ent, mockComponent1.getClass())),
                () -> assertTrue(compMan.removeComponent(ent, mockComponent1.getClass())),
                
                () -> assertFalse(compMan.removeComponent(ent, mockComponent1.getClass())),
                () -> assertFalse(compMan.hasComponent(ent, mockComponent1.getClass())),
                
                () -> assertNull(compMan.getComponent(ent, mockComponent1.getClass()))
        );
    }
    
    @Test
    public void testGetAllComponents_MultipleComponents_AllRetrieved() {
        Entity ent = new Entity(1);
        Component mockComp1 = new MockComponent1();
        Component mockComp2 = new MockComponent2();
        
        compMan.addComponent(ent, mockComp1);
        compMan.addComponent(ent, mockComp2);
        
        Map<Class<? extends Component>, Component> componentMap = compMan.getAllComponents(ent);
        assertAll(
                () -> assertEquals(2, componentMap.size())
        );
    }
    
}