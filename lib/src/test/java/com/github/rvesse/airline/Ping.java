package com.github.rvesse.airline;

import javax.inject.Inject;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.HelpOption;
import com.github.rvesse.airline.Option;
import com.github.rvesse.airline.SingleCommand;

@Command(name = "ping", description = "network test utility")
public class Ping
{
    @Inject
    public HelpOption helpOption;

    @Option(name = {"-c", "--count"}, description = "Send count packets")
    public int count = 1;

    public static void main(String... args)
    {
        Ping ping = SingleCommand.singleCommand(Ping.class).parse(args);

        if (ping.helpOption.showHelpIfRequested()) {
            return;
        }

        ping.run();
    }

    public void run()
    {
        System.out.println("Ping count: " + count);
    }
}
