package com.Ild_Mail.models.input_reader.commands;

import com.Ild_Mail.Core;
import com.Ild_Mail.models.input_reader.ConfigReader;
import com.Ild_Mail.models.input_reader.POJO.ConfigPOJO;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name="imap",
        mixinStandardHelpOptions = true,
        description = "receive messages with imap proto")
public class ReceiveIMAPCommand implements Callable {

    //Represent config file
    private ConfigPOJO configPOJO;

    @Parameters(index = "0",
            arity = "1",
            description = "Path to your config.json",
            defaultValue = "com\\Ild_Mail\\default\\config.json"
    )
    private String config_path;

    @Override
    public Object call() {
        try{
            Core.configReader = new ConfigReader(config_path);
            Core.configReader.parseNode(ConfigPOJO.class);
            configPOJO = Core.configReader.getConfigPOJO();
            System.out.println(configPOJO.getSMTP_HOST());
            return 0;
        }
        catch(Exception ex){
            return -1;
        }
    }
}
