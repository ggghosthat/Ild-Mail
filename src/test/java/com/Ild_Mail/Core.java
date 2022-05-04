package com.Ild_Mail;

import com.Ild_Mail.models.input_reader.ConfigReader;
import com.Ild_Mail.models.input_reader.POJO.ConfigPOJO;
import com.Ild_Mail.models.letter_notes_structures.Letter;
import com.Ild_Mail.models.smtp_send.Sender;
import picocli.CommandLine;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(name="ild_mail",
        mixinStandardHelpOptions = true,
        version = "ild-mail 0.1",
        description = "Send & receive e-mail")
public class Core implements Callable {

    @Parameters(index = "0",
                arity = "1",
                description = "Path to your config.json")
    private String config_path;

    @Option(names = {"-m","--mode"},
            description = "sen(send by SMTP proto ), rec(receive by IMAP proto)")
    private String act_mode;

    @Option(names = {"-l","--letter"},
            description = "define your sending letter (path to your text file) or use a letter-pad.\nUse this option just with send-mode option")
    private String letter_row;

    @Option(names = {"-s","--smtp", "--send"},
            description = "send your letter \n(input path 2 your .txt file)")
    private String send_letter;

    @Option(names = {"-sub", "--subject"},
            description = "specify send-mail subject.\nWork with only <-s>, <--send>, <--smtp> options")
    private String send_subject;


    @Option(names = {"-ri", "--imap"},
            description = "obtain mail-staff rom your imap-server.\nBefore using this function make sure that your mail provider allow using imap-protocol")
    private String recieve_mail;

    public static void main(String[] args) throws AddressException {
    }

    @Override
    public Object call() throws Exception {
        return null;
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
