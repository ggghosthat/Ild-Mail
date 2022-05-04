package com.Ild_Mail.unit_tests;

import com.Ild_Mail.models.input_reader.ConfigReader;
import com.Ild_Mail.models.input_reader.POJO.ConfigPOJO;

import java.io.IOException;

class ConfigReaderTest {

    private ConfigReader configReader = new ConfigReader(System.getProperty("user.dir") + "\\src\\test\\java\\com\\Ild_Mail\\models\\input_reader\\test.json");

    @org.junit.jupiter.api.Test
    void parseNode() {
        try {
            configReader.parseNode(ConfigPOJO.class );

        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigPOJO pojo = configReader.getConfigPOJO();
        System.out.println(pojo.getSMTP_HOST() + " " + pojo.getIMAP_ADDRESS() + " " + pojo.getSMTP_PASSWORD());
    }
}