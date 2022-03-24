package com.Ild_Mail.models.input_reader;


import com.Ild_Mail.models.input_reader.POJO.ConfigPOJO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class InputReader {
    private static ObjectMapper objectMapper = defaultObjectMapper();
    private String path_config;

    public InputReader(String path_config){
        this.path_config = path_config;
    }

    private static ObjectMapper defaultObjectMapper(){
        ObjectMapper defaultMapper = new ObjectMapper();
        defaultMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        return defaultMapper;
    }

    public static ConfigPOJO parseNode(File jsonFile, Class<ConfigPOJO> config_pojo_clazz) throws IOException {
        JsonNode node = objectMapper.readTree(jsonFile);
        return objectMapper.treeToValue(node, config_pojo_clazz);
    }
}
