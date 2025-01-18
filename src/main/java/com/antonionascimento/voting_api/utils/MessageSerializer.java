package com.antonionascimento.voting_api.utils;

public interface MessageSerializer {
    String serialize(Object message);
    <T> T deserialize(String message, Class<T> targetType);
}
