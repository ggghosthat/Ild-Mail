package com.Ild_Mail;

import com.Ild_Mail.models.input_processor.commands.ReceivePOPCommand;
import picocli.CommandLine;
import com.Ild_Mail.models.input_processor.commands.ReceiveIMAPCommand;
import com.Ild_Mail.models.input_processor.commands.SendSMTPCommand;

import static picocli.CommandLine.*;

@Command(name="ild_mail",
        mixinStandardHelpOptions = true,
        version = "ild-mail 0.1",
        description = "tiny cli e-mail client",
        subcommands = {SendSMTPCommand.class, ReceiveIMAPCommand.class, ReceivePOPCommand.class})
public class Core {

//    public static ConfigReader configReader;

    public static void main(String[] args) {
        new CommandLine(new Core()).execute(args);
    }
}
