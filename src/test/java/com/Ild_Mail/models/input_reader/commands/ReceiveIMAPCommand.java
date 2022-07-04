package com.Ild_Mail.models.input_reader.commands;

import com.Ild_Mail.models.input_reader.ConfigReader;
import com.Ild_Mail.models.input_reader.POJO.ConfigPOJO;
import com.Ild_Mail.models.input_reader.pico_converters.PairTupleConverter;
import com.Ild_Mail.models.recieve.ReceiverIMAP;
import org.javatuples.Pair;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.Scanner;
import java.util.concurrent.Callable;

@Command(name="imap",
        mixinStandardHelpOptions = true,
        description = "receive messages with imap proto")
public class ReceiveIMAPCommand implements Callable {

    //region Fields
    //Represent config file
    private static ConfigPOJO configPOJO;

    //imap receiver to interact with mail
    private static ReceiverIMAP reciever;
    //endregion



    //region ClI_Inputs
    @Parameters(index = "0",
            arity = "1",
            description = "Path to your config.json",
            defaultValue = "com\\Ild_Mail\\default\\config.json"
    )
    private String config_path;

    @Option(names = {"-a", "--all"},
            description = "extract all messages from the box")
    private boolean all;

    @Option(names = {"-un", "--unread"},
            description = "extract unread messages from the box")
    private boolean unread;

    @Option(names = {"-rg", "--range"},
            description = "extract messages range from the box \n\tExample: (startPos, endPos) ",
            converter = PairTupleConverter.class)
    private Pair<Integer, Integer> range;


    @Option(names = {"-l", "--last"},
            description = "extract range of last messages from the box")
    private int last;
    //endregion

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
            reciever = configReader.EnableReciever(password);

            if(all)
                System.out.println("all_option");
            if (unread)
                System.out.println("unread_option");
            if (range != null)
                System.out.println(range);
            if (last != 0)
                System.out.println(last);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
