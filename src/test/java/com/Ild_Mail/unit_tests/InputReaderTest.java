package com.Ild_Mail.unit_tests;

import com.Ild_Mail.models.input_reader.InputReader;
import com.Ild_Mail.models.input_reader.POJO.ConfigPOJO;

import java.io.IOException;

class InputReaderTest {

    private InputReader inputReader = new InputReader(System.getProperty("user.dir") + "\\src\\test\\java\\com\\Ild_Mail\\models\\input_reader\\test.json");

    @org.junit.jupiter.api.Test
    void parseNode() {
        try {
            inputReader.parseNode(ConfigPOJO.class );

        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigPOJO pojo = inputReader.getConfigPOJO();
        System.out.println(pojo.getSMTP_HOST() + " " + pojo.getIMAP_ADDRESS() + " " + pojo.getSMTP_PASSWORD());
    }
}