package com.palash.healthspringfoapp.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class JsonObjectMapper {

    // Define the String type variable which hold the class name for error
    // logging
    private static final String TAG = JsonObjectMapper.class.getSimpleName();
    // Define the ObjectMappper class object
    ObjectMapper mapper = null;
    // Define the ByteArrayOututStream class object
    ByteArrayOutputStream outStream = null;
    // Define the String type variable which hold the value
    String value = null;

    /**
     * This method is intended to map JSON object into respective objects
     *
     * @param data
     * @param clazz
     * @return
     * @author Pratik Karanje
     * @date jan 7,2015
     */
    public <T> T map(String data, Class<?> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, mapper.getTypeFactory().constructCollectionType(List.class, Class.forName(clazz.getName())));
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method is intended to convert object into JSON Object
     *
     * @param data
     * @return
     * @author Pratik Karanje
     * @date jan 7,2015
     */
    public String unMap(Object data) {
        try {
            mapper = new ObjectMapper();
            outStream = new ByteArrayOutputStream();
            mapper.writeValue(outStream, data);
            value = new String(outStream.toByteArray());
        } catch (JsonParseException e) {
            e.printStackTrace();
            value = null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            value = null;
        } catch (IOException e) {
            e.printStackTrace();
            value = null;
        } catch (Exception e) {
            e.printStackTrace();
            value = null;
        }
        return value;
    }
}
