package com.Ild_Mail;

import picocli.CommandLine;
import com.Ild_Mail.models.input_processor.commands.ReceiveIMAPCommand;
import com.Ild_Mail.models.input_processor.commands.SendSMTPCommand;

import static picocli.CommandLine.*;

@Command(name="ild_mail",
        mixinStandardHelpOptions = true,
        version = "ild-mail 0.1",
        description = "tiny cli e-mail client",
        subcommands = {SendSMTPCommand.class, ReceiveIMAPCommand.class})
public class Core implements Runnable {

//    public static ConfigReader configReader;

    public static void main(String[] args) {
        new CommandLine(new Core()).execute(args);
    }

    @Override
    public void run() {

//        ConfigReader configReader = new ConfigReader(config_path);
//        try {
//            configReader.parseNode(ConfigPOJO.class);
//            configPOJO = configReader.getConfigPOJO();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Hello from Main");
    }

}
