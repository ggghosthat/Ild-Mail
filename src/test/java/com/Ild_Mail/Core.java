package com.Ild_Mail;

import com.Ild_Mail.models.letter_notes_structures.Letter;
import com.Ild_Mail.models.smtp_send.Sender;
import picocli.CommandLine;

import javax.mail.internet.AddressException;
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

    public static void main(String[] args) throws AddressException {
    }

    @Override
    public Object call() throws Exception {
        return null;
    }
}
