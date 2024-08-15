package org.zarate.chatserver.Helper.Formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Formatter {
    public static <T> T Deserialize(byte[] data, Class<T> clss){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String decodedString = new String(data, StandardCharsets.UTF_8);
            //System.err.println(decodedString);
            return objectMapper.readValue(decodedString, clss);
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] Serialize(Object data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(data);
            return jsonString.getBytes("UTF-8");
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
