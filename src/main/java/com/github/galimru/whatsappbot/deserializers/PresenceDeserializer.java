package com.github.galimru.whatsappbot.deserializers;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PresenceDeserializer<T> extends StdDeserializer<T> {

    private final Map<String, Class<?>> classMap;

    public PresenceDeserializer(Class<T> vc) {
        super(vc);
        this.classMap = Arrays.stream(vc.getAnnotation(JsonSubTypes.class).value())
                .collect(Collectors.toMap(JsonSubTypes.Type::name, JsonSubTypes.Type::value,
                        (key, value) -> key, LinkedHashMap::new));
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext context) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper) p.getCodec();
        ObjectNode object = objectMapper.readTree(p);
        for (String propertyName : classMap.keySet()) {
            if (object.has(propertyName)) {
                return deserialize(objectMapper, propertyName, object);
            }
        }
        throw new IllegalArgumentException("Cannot infer to which class to deserialize " + object);
    }

    @SuppressWarnings("unchecked")
    private T deserialize(ObjectMapper objectMapper,
                          String propertyName,
                          ObjectNode object) throws IOException {
        return (T) objectMapper.treeToValue(object, classMap.get(propertyName));
    }
}
