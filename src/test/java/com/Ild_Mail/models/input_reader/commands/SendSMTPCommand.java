package com.Ild_Mail.models.input_reader.commands;

import com.Ild_Mail.Core;
import com.Ild_Mail.models.input_reader.ConfigReader;
import com.Ild_Mail.models.input_reader.POJO.ConfigPOJO;
import com.Ild_Mail.models.smtp_send.Sender;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.Console;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Callable;


@Command(name="smtp",
        mixinStandardHelpOptions = true,
        description = "send smpt messages")
public class SendSMTPCommand implements Callable {

    //Represent config file
    private ConfigPOJO configPOJO;



    @Parameters(index = "0",
            arity = "1",
            description = "Path to your config.json",
            defaultValue = "com\\Ild_Mail\\default\\config.json"
    )
    private String config_path;

    @Option(names = {"-t", "--target"},
            description = "specify destination address",
            required = true)
    private String send_target;

    @Option(names = {"-s", "--subject"},
            description = "specify send-mail subject.\nWork with only <-s>, <--send>, <--smtp> options")
    private String send_subject = "default-subject";

    @Option(names = {"-l","--letter"},
            description = "define your sending letter (path to your text file) or use a letter-pad.\nUse this option just with send-mode option")
    private String send_text = "";



    @Override
    public Object call() {
        try {
//            Core.configReader = new ConfigReader(config_path);
//            Core.configReader.parseNode(ConfigPOJO.class);
//            configPOJO = Core.configReader.getConfigPOJO();
            String password = new Scanner(System.in).nextLine();
            SendMail(config_path,password, send_text, send_subject);
            return 0;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }



    private void SendMail(String config_path, String password, String path2letter, String subject) {
        try {
            ConfigReader configReader = new ConfigReader(config_path);
            configReader.parseNode(ConfigPOJO.class);
            Sender sender = configReader.EnableSender(password, send_target);
            sender.PrepareTextMessage(subject, path2letter);
            sender.SendMessage();
        }
        catch (AddressException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }

    }
}
