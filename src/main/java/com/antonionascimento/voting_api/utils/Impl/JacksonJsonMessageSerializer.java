package com.antonionascimento.voting_api.utils.Impl;

import org.springframework.stereotype.Component;

import com.antonionascimento.voting_api.utils.MessageSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JacksonJsonMessageSerializer implements MessageSerializer{

    private ObjectMapper objectMapper;

    public JacksonJsonMessageSerializer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    
    @Override
    public String serialize(Object message) {
        try{
            return objectMapper.writeValueAsString(message);
        }
        catch(JsonProcessingException e){
            throw new RuntimeException("Failed to serialize message to JSON ", e);
        }
    }

    @Override
    public <T> T deserialize(String message, Class<T> targetType) {
        try{
            return objectMapper.readValue(message, targetType);
        } catch(JsonProcessingException e){
            throw new RuntimeException("Failed to deserialize message to JSON ", e);
        }
    }
}
