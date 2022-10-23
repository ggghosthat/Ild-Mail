package com.Ild_Mail.models.input_processor.POJO;


import com.Ild_Mail.models.recieve.ReceiverPOP;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Ild_Mail.models.smtp_send.Sender;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.Ild_Mail.models.recieve.ReceiverIMAP;

import javax.mail.internet.AddressException;
import java.io.File;
import java.io.IOException;


public class ConfigReader {
    //Jackson JSON handler
    private static ObjectMapper objectMapper = defaultObjectMapper();

    //Config handler & it's stored path
    private static String path_config;
    private static ConfigPOJO configPOJO;

    private static Sender smtp_sender;
    private static ReceiverIMAP imap_reciever;
    private static ReceiverPOP pop_receiver;


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
            smtp_sender = new Sender(configPOJO.getSND_SOURCE(),
                    password,
                    target,
                    configPOJO.getSND_HOST());
        }
        else{
            smtp_sender = new Sender(configPOJO.getSND_SOURCE(),
                    password,
                    target,
                    configPOJO.getSND_HOST(),
                    configPOJO.getMailProxy().get_host(),
                    configPOJO.getMailProxy().get_port(),
                    configPOJO.getMailProxy().get_user(),
                    configPOJO.getMailProxy().get_password());
        }
        return smtp_sender;
    }

    //Reciever(IMAP) initialization
    public Object EnableReciever(String password) throws Exception {
        if (configPOJO.getREC_PROTO().equals("imap")) {
            if (configPOJO.getMailProxy() == null) {
                imap_reciever = new ReceiverIMAP(configPOJO.getREC_HOST(),
                        configPOJO.getREC_ADDRESS(),
                        password,
                        configPOJO.getREC_ALLOC());
            } else {
                imap_reciever = new ReceiverIMAP(configPOJO.getREC_HOST(),
                        configPOJO.getREC_ADDRESS(),
                        password,
                        configPOJO.getREC_ALLOC(),
                        configPOJO.getMailProxy().get_host(),
                        configPOJO.getMailProxy().get_port(),
                        configPOJO.getMailProxy().get_user(),
                        configPOJO.getMailProxy().get_password());
            }

            System.out.println(configPOJO.getREC_PROTO());
            return imap_reciever;
        }
        else if (configPOJO.getREC_PROTO().equals("pop")){
            if (configPOJO.getMailProxy() == null) {
                pop_receiver= new ReceiverPOP(configPOJO.getREC_HOST(),
                        configPOJO.getREC_ADDRESS(),
                        password,
                        configPOJO.getREC_ALLOC());
            }
            else{
                pop_receiver= new ReceiverPOP(configPOJO.getREC_HOST(),
                        configPOJO.getREC_ADDRESS(),
                        password,
                        configPOJO.getREC_ALLOC(),
                        configPOJO.getMailProxy().get_host(),
                        configPOJO.getMailProxy().get_port(),
                        configPOJO.getMailProxy().get_user(),
                        configPOJO.getMailProxy().get_password());
            }

            return pop_receiver;
        }
        else {
            throw  new Exception(String.format("Could find correct receiver for protocol: %s", configPOJO.getREC_PROTO()));
        }
    }

    public ReceiverPOP EnablePOPReceiver(String password) throws Exception {
        if (configPOJO.getMailProxy() == null) {
            imap_reciever= new ReceiverIMAP(configPOJO.getREC_HOST(),
                    configPOJO.getREC_ADDRESS(),
                    password,
                    configPOJO.getREC_ALLOC());
        }
        else{
            imap_reciever= new ReceiverIMAP(configPOJO.getREC_HOST(),
                    configPOJO.getREC_ADDRESS(),
                    password,
                    configPOJO.getREC_ALLOC(),
                    configPOJO.getMailProxy().get_host(),
                    configPOJO.getMailProxy().get_port(),
                    configPOJO.getMailProxy().get_user(),
                    configPOJO.getMailProxy().get_password());
        }

        return pop_receiver;
    }
}
