package org.cunxin.app.util;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonSerDe {
    protected static ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings({"unchecked"})
    public static <T> T deserialize(String json, Class<T> valueType) {
        T t = null;
        try {
            t = (T) mapper.readValue(json, valueType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    static public String serialize(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
