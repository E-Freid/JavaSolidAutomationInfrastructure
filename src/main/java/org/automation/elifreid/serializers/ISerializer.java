package org.automation.elifreid.serializers;

public interface ISerializer<T, S> {
    S serialize(T obj) throws SerializationException;
    T deserialize(S data) throws SerializationException;
}
