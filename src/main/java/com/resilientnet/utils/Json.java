package com.resilientnet.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Json {
    public static Map<String, Object> toMap (String jsonObj ) throws Exception{
           return new ObjectMapper().readValue(jsonObj, HashMap.class);
    }
}
