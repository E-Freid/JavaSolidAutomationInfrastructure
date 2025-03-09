package org.automation.elifreid.serializers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonSerializer<T> implements ISerializer<T, String> {
    private final ObjectMapper objectMapper;
    private final TypeReference<T> typeRef;

    public JacksonJsonSerializer(TypeReference<T> typeRef) {
        this.typeRef = typeRef;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    @Override
    public String serialize(T obj) throws SerializationException {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new SerializationException(
                    String.format("Failed to serialize object of type : >> %s to JSON", obj.getClass().getSimpleName()), e);
        }
    }

    @Override
    public T deserialize(String jsonData) throws SerializationException {
        try {
            return objectMapper.readValue(jsonData, typeRef);
        } catch (Exception e) {
            throw new SerializationException(
                    String.format("Failed to deserialize JSON to object of type : >> %s", typeRef.getType().getTypeName()), e);
        }
    }
}
