package com.Ild_Mail.models.input_processor.commands;

import com.Ild_Mail.models.input_processor.POJO.ConfigReader;
import com.Ild_Mail.models.input_processor.POJO.ConfigPOJO;
import com.Ild_Mail.models.input_processor.pico_converters.PairTupleConverter;
import com.Ild_Mail.models.recieve.ReceiverPOP;
import org.javatuples.Pair;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.Scanner;
import java.util.concurrent.Callable;

@Command(name="pop",
        mixinStandardHelpOptions = true,
        description = "receive messages with pop3 proto")
public class ReceivePOPCommand  implements Callable{
    //region Fields
    //Represent config file
    private static ConfigPOJO configPOJO;

    //imap receiver to interact with mail
    private static ReceiverPOP receiver;
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
            configReader.parseConfigNode(ConfigPOJO.class);
            receiver = (ReceiverPOP) configReader.EnableReciever(password);

            if(all)
                receiver.ExtractAll();
            if (range != null)
                receiver.ExtractRange(range.getValue0(), range.getValue1());
            if (last != 0)
                receiver.ExtractLast(last);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
