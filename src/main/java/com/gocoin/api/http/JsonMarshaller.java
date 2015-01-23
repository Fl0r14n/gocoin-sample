package com.gocoin.api.http;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonMarshaller {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonFactory factory = new JsonFactory();
    
    public static <T> T read(String json, Class<T> t) throws IOException {        
        return mapper.readValue(json, t);
    }
    
    public static <T> Collection<T> readArray(String json, Class<T> t) throws IOException {
        Collection<T> result = new ArrayList<>();
        JsonParser jp = factory.createParser(json);
        jp.nextToken();
        while (JsonToken.START_OBJECT == jp.nextToken()) {
            T node = mapper.readValue(jp, t);
            result.add(node);
        }
        return result;
    }
    
    public static LinkedHashMap read(String json) throws IOException {
        return mapper.readValue(json, LinkedHashMap.class);
    }
    
    public static String write(Map map) throws JsonProcessingException {
        return mapper.writeValueAsString(map);
    }
    
    public static <T> String write(T t) throws JsonProcessingException {
        return mapper.writeValueAsString(t);
    }
}
