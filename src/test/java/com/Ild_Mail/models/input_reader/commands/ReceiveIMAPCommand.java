package com.Ild_Mail.models.input_reader.commands;

import com.Ild_Mail.models.input_reader.ConfigReader;
import com.Ild_Mail.models.input_reader.POJO.ConfigPOJO;
import com.Ild_Mail.models.recieve.ReceiverIMAP;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.Scanner;
import java.util.concurrent.Callable;

@Command(name="imap",
        mixinStandardHelpOptions = true,
        description = "receive messages with imap proto")
public class ReceiveIMAPCommand implements Callable {

    //Represent config file
    private ConfigPOJO configPOJO;

    @Parameters(index = "0",
            arity = "1",
            description = "Path to your config.json",
            defaultValue = "com\\Ild_Mail\\default\\config.json"
    )
    private String config_path;


    @Override
    public Object call() {
        try{
            String password = new Scanner(System.in).nextLine();
            ReceiveMail(password);
            return 0;
        }
        catch(Exception ex){
            return -1;
        }
    }

    private void ReceiveMail(String password){
        try {
            ConfigReader configReader = new ConfigReader(config_path);
            configReader.parseNode(ConfigPOJO.class);
            ReceiverIMAP reciever = configReader.EnableReciever(password);
            reciever.LookIntoBox();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
