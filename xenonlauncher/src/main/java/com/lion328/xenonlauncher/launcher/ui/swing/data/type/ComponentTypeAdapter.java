package com.lion328.xenonlauncher.launcher.ui.swing.data.type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lion328.xenonlauncher.launcher.ui.swing.data.Component;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ComponentTypeAdapter implements JsonSerializer<Component>, JsonDeserializer<Component>
{

    private static Map<String, Class<? extends Component>> componentClasses;

    static
    {
        componentClasses = new HashMap<>();
    }

    public static void registerComponent(String type, Class<? extends Component> clazz)
    {
        componentClasses.put(type, clazz);
    }

    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext)
    {
        return jsonSerializationContext.serialize(component);
    }

    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        // I can use reflection but then obfuscation is not working
        // Maybe I just use annotation... If that idea come before

        Component component = jsonDeserializationContext.deserialize(jsonElement, Component.class);

        Class<? extends Component> componentClass = componentClasses.get(component.getType());

        if (componentClass == null)
        {
            return null;
        }

        return jsonDeserializationContext.deserialize(jsonElement, componentClass);
    }
}
