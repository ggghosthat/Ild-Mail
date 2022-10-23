package com.Ild_Mail.models.input_processor.commands;

import com.Ild_Mail.models.input_processor.POJO.ConfigReader;
import com.Ild_Mail.models.input_processor.POJO.ConfigPOJO;
import com.Ild_Mail.models.input_processor.ini_processor.SendPOJO;
import com.Ild_Mail.models.smtp_send.Sender;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

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

    @Parameters(index = "1",
            arity = "1",
            description = "Path to your note.json",
            defaultValue = "com\\Ild_Mail\\default\\note.json"
    )
    private String send_note;

    @Option(names = {"-t", "--target"},
            description = "specify destination address")
    private String send_target;

    @Option(names = {"-s", "--subject"},
            description = "specify send-mail subject.\nWork with only <-s>, <--send>, <--smtp> options")
    private String send_subject = "default-subject";

    @Option(names = {"-l","--letter"},
            description = "define your sending letter (type your text in cli).\nUse this option just with send-mode option")
    private String send_text = "";

    @Option(names = {"-f", "--isFile"},
            description = "specify type of <letter (-l)> option : \n\tis it plain text or text-file")
    private boolean is_file = true;

    @Option(names = {"-a", "--attach"},
            description = "specify path 2 your attach file")
    private String attach_file = "";

    @Override
    public Object call() {
        try {
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
            ConfigReader configReader = new ConfigReader(config_path, send_note);
            configReader.parseConfigNode(ConfigPOJO.class);
            configReader.parseSendPOJO(SendPOJO.class);
            Sender sender = configReader.EnableSender(password);
            sender.SendMessage();
        }
        catch (AddressException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }

    }
}
