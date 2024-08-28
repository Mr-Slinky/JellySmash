package com.slinky.jellysmash.model.physics.comps;

import com.slinky.jellysmash.model.physics.Entity;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kheagen Haskins
 */
public class ComponentManager {

    // ============================== Fields ================================ //
    private Map<Long, Map<Class<? extends Component>, Component>> components = new HashMap<>();

    // ============================ API Methods ============================= //
    public <T extends Component> void addComponent(Entity entity, T component) {
        Map<Class<? extends Component>, Component> entityComponents = components.computeIfAbsent(entity.id(), k -> new HashMap<>());
        entityComponents.put(component.getClass(), component);
    }

    public <T extends Component> T getComponent(Entity entity, Class<T> componentClass) {
        Map<Class<? extends Component>, Component> entityComponents = components.get(entity.id());
        if (entityComponents != null) {
            return componentClass.cast(entityComponents.get(componentClass));
        }
        return null;
    }

}