package io.airlift.airline.help;

import io.airlift.airline.Arguments;
import io.airlift.airline.Command;
import io.airlift.airline.help.cli.CliCommandUsageGenerator;
import io.airlift.airline.help.cli.CliGlobalUsageGenerator;
import io.airlift.airline.help.cli.CliGlobalUsageSummaryGenerator;
import io.airlift.airline.help.cli.CliCommandGroupUsageGenerator;
import io.airlift.airline.model.CommandGroupMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.GlobalMetadata;

import javax.annotation.Nullable;
import javax.inject.Inject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Callable;

import static com.google.common.collect.Lists.newArrayList;

@Command(name = "help", description = "Display help information")
public class Help implements Runnable, Callable<Void> {
    @Inject
    @Nullable
    public GlobalMetadata global;

    @Arguments
    public List<String> command = newArrayList();

    @Override
    public void run() {
        try {
            help(global, command);
        } catch (IOException e) {
            throw new RuntimeException("Error generating usage documentation", e);
        }
    }

    @Override
    public Void call() {
        run();
        return null;
    }

    public static void help(CommandMetadata command) throws IOException {
        help(command, System.out);
    }

    public static void help(CommandMetadata command, OutputStream out) throws IOException {
        new CliCommandUsageGenerator().usage(null, null, command.getName(), command, out);
    }

    public static void help(GlobalMetadata global, List<String> commandNames) throws IOException {
        help(global, commandNames, System.out);
    }

    public static void help(GlobalMetadata global, List<String> commandNames, OutputStream out) throws IOException {
        if (commandNames.isEmpty()) {
            new CliGlobalUsageSummaryGenerator().usage(global, out);
            return;
        }

        String name = commandNames.get(0);

        // main program?
        if (name.equals(global.getName())) {
            new CliGlobalUsageGenerator().usage(global, out);
            return;
        }

        // command in the default group?
        for (CommandMetadata command : global.getDefaultGroupCommands()) {
            if (name.equals(command.getName())) {
                new CliCommandUsageGenerator().usage(global.getName(), null, command.getName(), command, out);
                return;
            }
        }

        // command in a group?
        for (CommandGroupMetadata group : global.getCommandGroups()) {
            if (name.endsWith(group.getName())) {
                // general group help or specific command help?
                if (commandNames.size() == 1) {
                    new CliCommandGroupUsageGenerator().usage(global, group, out);
                    return;
                } else {
                    String commandName = commandNames.get(1);
                    for (CommandMetadata command : group.getCommands()) {
                        if (commandName.equals(command.getName())) {
                            new CliCommandUsageGenerator()
                                    .usage(global.getName(), group.getName(), command.getName(), command, out);

                            return;
                        }
                    }
                    System.out.println("Unknown command " + name + " " + commandName);
                }
            }
        }

        System.out.println("Unknown command " + name);
    }
}
