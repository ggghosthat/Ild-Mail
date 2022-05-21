package com.Ild_Mail.models.input_reader.commands;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name="find",
         mixinStandardHelpOptions = true,
         description = "find text in mail-staff")
public class MailFinderCommand implements Callable {

    @Parameters(index = "0",
                arity = "1",
                description = "Text to search")
    private String text;

    @Option(names={"-m", "--method"},
            description = "Define search type method")
    private String search_type;

    @Option(names={"-st", "--staff"},
            description = "Define staff allocation")
    private String staff;

    @Option(names={"-crd", "--credentials"},
            description="Your mail credentials for searching in your mailbox\n\tTemplate: [-host- -address- -password-]"
    )
    private String credentials;

    @Override
    public Object call() throws Exception {
        return null;
    }
}
