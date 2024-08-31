package com.slinky.jellysmash.model.physics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit tests for the Entity class.
 *
 * This test class is designed to validate the functionality of the Entity
 * class, specifically its constructor and getter method.
 *
 * @author: Kheagen Haskins
 */
public class EntityTest {

    // A reusable instance of the Entity class, used across multiple tests.
    private Entity resusableEntity;

    /**
     * Cleans up resources after each test case.
     *
     * This method is automatically called after each test case execution to
     * ensure that the resusableEntity is reset to null. This helps to avoid
     * unintended interactions between test cases.
     */
    @AfterEach
    public void cleanUp() {
        resusableEntity = null;
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
     * @param count The number of times to create and verify the Entity
     * instances.
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("Test Entity Constructor and Getter")
    public void testConstuctorAndGetter(int count) {
        for (int i = 0; i < count; i++) {
            int id = i + 1;
            assertDoesNotThrow(() -> resusableEntity = new Entity(id));
            assertEquals(id, resusableEntity.id());
        }
    }

}
