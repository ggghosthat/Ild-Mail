package com.Ild_Mail.unit_tests;

import com.Ild_Mail.models.input_processor.configuration.ConfigReader;
import com.Ild_Mail.models.input_processor.configuration.ConfigPOJO;

import java.io.IOException;

class ConfigReaderTest {

    private ConfigReader configReader = new ConfigReader(System.getProperty("user.dir") + "\\src\\com\\java\\com\\Ild_Mail\\default\\config.json");


    void parseNode() {
        try {
            configReader.parseConfigNode(ConfigPOJO.class );

        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigPOJO pojo = configReader.getConfigPOJO();
        System.out.println(pojo.getSND_HOST() + " " + pojo.getREC_ADDRESS());
    }
}