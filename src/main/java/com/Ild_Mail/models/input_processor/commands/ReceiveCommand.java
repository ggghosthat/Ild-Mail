package com.Ild_Mail.models.input_processor.commands;

import com.Ild_Mail.models.input_processor.configuration.ConfigReader;
import com.Ild_Mail.models.input_processor.configuration.ConfigPOJO;
import com.Ild_Mail.models.input_processor.pico_converters.PairTupleConverter;
import com.Ild_Mail.models.recieve.ReceiverIMAP;
import com.Ild_Mail.models.recieve.ReceiverPOP;
import org.javatuples.Pair;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.Scanner;
import java.util.concurrent.Callable;

@Command(name="rec",
        mixinStandardHelpOptions = true,
        description = "receive messages with imap proto")
public class ReceiveCommand implements Callable {

    //region Fields
    //Represent config file
    private static ConfigPOJO configPOJO;

    //imap receiver to interact with mail
    private static Object receiver;
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
            System.out.print("Enter password :  ");
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
            configReader.parseConfigNode(ConfigPOJO.class);
            receiver = configReader.EnableReciever(password);

            if (receiver instanceof ReceiverIMAP) {
                if (all)
                    ((ReceiverIMAP)receiver).ExtractAll();
                if (unread)
                    ((ReceiverIMAP)receiver).ExtractUnread();
                if (range != null)
                    ((ReceiverIMAP)receiver).ExtractRange(range.getValue0(), range.getValue1());
                if (last != 0)
                    ((ReceiverIMAP)receiver).ExtractLast(last);

                ((ReceiverIMAP)receiver).Unwrapping();
            }
            if (receiver instanceof ReceiverPOP) {
                if(all)
                    ((ReceiverPOP)receiver).ExtractAll();
                if (range != null)
                    ((ReceiverPOP)receiver).ExtractRange(range.getValue0(), range.getValue1());
                if (last != 0)
                    ((ReceiverPOP)receiver).ExtractLast(last);

                ((ReceiverPOP)receiver).Unwrapping();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
