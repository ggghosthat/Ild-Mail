package com.Ild_Mail;

import com.Ild_Mail.models.input_reader.ConfigReader;
import com.Ild_Mail.models.input_reader.POJO.ConfigPOJO;
import com.Ild_Mail.models.smtp_send.Sender;
import picocli.CommandLine;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;

import static picocli.CommandLine.*;

@Command(name="ild_mail",
        mixinStandardHelpOptions = true,
        version = "ild-mail 0.1",
        description = "Send & receive e-mail")
public class Core implements Runnable {

    //These command parameter define path 2 config.json which specify basic settings
    @Parameters(index = "0",
                arity = "1",
                description = "Path to your config.json",
                defaultValue = "com\\Ild_Mail\\default\\config.json"
    )
    private String config_path;

    //These command options define mode of actions (send or receive)
    @Option(names = {"-s","--smtp", "--send"},
            description = "send your letter \n(input path 2 your .txt file)")
    private boolean send_letter;

    @Option(names = {"-ri", "--imap"},
            description = "obtain mail-staff rom your imap-server.\nBefore using this function make sure that your mail provider allow using imap-protocol")
    private boolean recieve_mail;

    //These command options define sending letter's subject & body(letter_raw)
    @Option(names = {"-sub", "--subject"},
            description = "specify send-mail subject.\nWork with only <-s>, <--send>, <--smtp> options")
    private String send_subject;

    @Option(names = {"-l","--letter"},
            description = "define your sending letter (path to your text file) or use a letter-pad.\nUse this option just with send-mode option")
    private String letter_row;





    public static void main(String[] args) throws AddressException {
        new CommandLine(new Core()).execute(args);
    }


    @Override
    public void run() {
//        ConfigReader configReader = new ConfigReader(config_path);
//        try {
//            configReader.parseNode(ConfigPOJO.class);
//            ConfigPOJO pojo = configReader.getConfigPOJO();
//            System.out.println(pojo.getSMTP_HOST());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println(recieve_mail);

    }



    private void SendMail(String config_path, String path2letter, String subject) {
        try {
            ConfigReader configReader = new ConfigReader(config_path);
            configReader.parseNode(ConfigPOJO.class);
            Sender sender = configReader.EnableSender();
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

    private void RecieveMail(String config_path){
        try {
            ConfigReader configReader = new ConfigReader(config_path);
            configReader.parseNode(ConfigPOJO.class);
            configReader.EnableReciever().LookIntoBox();
        }
        catch (AddressException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
