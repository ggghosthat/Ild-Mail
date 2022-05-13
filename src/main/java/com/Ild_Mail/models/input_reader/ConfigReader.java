package com.Ild_Mail.models.input_reader;


import com.Ild_Mail.models.input_reader.POJO.ConfigPOJO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Ild_Mail.models.smtp_send.Sender;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.Ild_Mail.models.recieve.RecieverIMAP;

import javax.mail.internet.AddressException;
import java.io.File;
import java.io.IOException;

enum LetterWriteMode{
    NoteMode,
    LoadMode
}

public class ConfigReader {
    //Jackson JSON handler
    private static ObjectMapper objectMapper = defaultObjectMapper();

    //Config handler & it's stored path
    private static String path_config;
    private static ConfigPOJO configPOJO;

    private static Sender smtp_sender;
    private static RecieverIMAP imap_reciever;


    //path to sending letter
    private String path;




    public ConfigReader(String path_config){
        this.path_config = path_config;
    }

    public ConfigReader(String path_config, String path){
        this.path_config = path_config;
        this.path = path;
    }





    private static ObjectMapper defaultObjectMapper(){
        ObjectMapper defaultMapper = new ObjectMapper();
        defaultMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        defaultMapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        return defaultMapper;
    }

    //Parse JSON staff to Config handler
    public static void parseNode( Class<ConfigPOJO> config_pojo_clazz) throws IOException {
        JsonNode node = objectMapper.readTree(new File(path_config));
        configPOJO = objectMapper.treeToValue(node, config_pojo_clazz);
    }

    public static ConfigPOJO getConfigPOJO() {
        return configPOJO;
    }



    //Sender(SMTP) initialization
    public Sender EnableSender(String password, String target) throws AddressException {
        if (configPOJO.getMailProxy() == null) {
            smtp_sender = new Sender(configPOJO.getSMTP_SOURCE(),
                    password,
                    target,
                    configPOJO.getSMTP_HOST());
        }
        else{
            smtp_sender = new Sender(configPOJO.getSMTP_SOURCE(),
                    password,
                    target,
                    configPOJO.getSMTP_HOST(),
                    configPOJO.getMailProxy().get_host(),
                    configPOJO.getMailProxy().get_port(),
                    configPOJO.getMailProxy().get_user(),
                    configPOJO.getMailProxy().get_password());
        }
        return smtp_sender;
    }

    //Reciever(IMAP) initialization
    public RecieverIMAP EnableReciever(String password) throws AddressException {
        if (configPOJO.getMailProxy() == null) {
            imap_reciever= new RecieverIMAP(configPOJO.getIMAP_HOST(),
                                            configPOJO.getIMAP_ADDRESS(),
                                            password);
        }
        else{
            imap_reciever= new RecieverIMAP(configPOJO.getIMAP_HOST(),
                                            configPOJO.getIMAP_ADDRESS(),
                                            password,
                                            configPOJO.getMailProxy().get_host(),
                                            configPOJO.getMailProxy().get_port(),
                                            configPOJO.getMailProxy().get_user(),
                                            configPOJO.getMailProxy().get_password());
        }
        return imap_reciever;
    }






}
