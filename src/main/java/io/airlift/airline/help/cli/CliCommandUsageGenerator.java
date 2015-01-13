package io.airlift.airline.help.cli;

import static com.google.common.collect.Lists.newArrayList;
import static io.airlift.airline.help.UsageHelper.DEFAULT_OPTION_COMPARATOR;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import io.airlift.airline.help.AbstractPrintedCommandUsageGenerator;
import io.airlift.airline.help.UsagePrinter;
import io.airlift.airline.model.ArgumentsMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class CliCommandUsageGenerator extends AbstractPrintedCommandUsageGenerator {

    public CliCommandUsageGenerator() {
        this(79, DEFAULT_OPTION_COMPARATOR);
    }

    public CliCommandUsageGenerator(int columnSize) {
        this(columnSize, DEFAULT_OPTION_COMPARATOR);
    }

    public CliCommandUsageGenerator(int columnSize, @Nullable Comparator<? super OptionMetadata> optionComparator) {
        super(columnSize, optionComparator);
    }

    @Override
    protected void usage(@Nullable String programName, @Nullable String groupName, String commandName,
            CommandMetadata command, UsagePrinter out) throws IOException {
        //
        // NAME
        //
        out.append("NAME").newline();

        out.newIndentedPrinter(8).append(programName).append(groupName).append(commandName).append("-")
                .append(command.getDescription()).newline().newline();

        //
        // SYNOPSIS
        //
        out.append("SYNOPSIS").newline();
        UsagePrinter synopsis = out.newIndentedPrinter(8).newPrinterWithHangingIndent(8);
        List<OptionMetadata> options = newArrayList();
        if (programName != null) {
            synopsis.append(programName).appendWords(toSynopsisUsage(sortOptions(command.getGlobalOptions())));
            options.addAll(command.getGlobalOptions());
        }
        if (groupName != null) {
            synopsis.append(groupName).appendWords(toSynopsisUsage(sortOptions(command.getGroupOptions())));
            options.addAll(command.getGroupOptions());
        }
        synopsis.append(commandName).appendWords(toSynopsisUsage(sortOptions(command.getCommandOptions())));
        options.addAll(command.getCommandOptions());

        // command arguments (optional)
        ArgumentsMetadata arguments = command.getArguments();
        if (arguments != null) {
            synopsis.append("[--]").append(toUsage(arguments));
        }
        synopsis.newline();
        synopsis.newline();

        //
        // OPTIONS
        //
        if (options.size() > 0 || arguments != null) {
            options = sortOptions(options);

            out.append("OPTIONS").newline();

            for (OptionMetadata option : options) {
                // skip hidden options
                if (option.isHidden()) {
                    continue;
                }

                // option names
                UsagePrinter optionPrinter = out.newIndentedPrinter(8);
                optionPrinter.append(toDescription(option)).newline();
                optionPrinter.flush();

                // description
                UsagePrinter descriptionPrinter = optionPrinter.newIndentedPrinter(4);
                descriptionPrinter.append(option.getDescription()).newline();

                descriptionPrinter.newline();
                descriptionPrinter.flush();
            }

            if (arguments != null) {
                // "--" option
                UsagePrinter optionPrinter = out.newIndentedPrinter(8);
                optionPrinter.append("--").newline();
                optionPrinter.flush();

                // description
                UsagePrinter descriptionPrinter = optionPrinter.newIndentedPrinter(4);
                descriptionPrinter
                        .append("This option can be used to separate command-line options from the "
                                + "list of argument, (useful when arguments might be mistaken for command-line options")
                        .newline();
                descriptionPrinter.newline();

                // arguments name(s)
                optionPrinter.append(toDescription(arguments)).newline();

                // description
                descriptionPrinter.append(arguments.getDescription()).newline();
                descriptionPrinter.newline();
                descriptionPrinter.flush();
            }
        }

        if (command.getDiscussion() != null) {
            out.append("DISCUSSION").newline();
            UsagePrinter discussionPrinter = out.newIndentedPrinter(8);

            discussionPrinter.append(command.getDiscussion()).newline().newline();
            discussionPrinter.flush();
        }

        if (command.getExamples() != null && !command.getExamples().isEmpty()) {
            out.append("EXAMPLES").newline();
            UsagePrinter examplePrinter = out.newIndentedPrinter(8);

            examplePrinter.appendTable(Iterables.partition(command.getExamples(), 1));
            examplePrinter.flush();
        }
        
        if (command.getExitCodes() != null && !command.getExitCodes().isEmpty()) {
            out.append("EXIT STATUS").newline();
            out.flush();
            
            UsagePrinter exitPrinter = out.newIndentedPrinter(8);
            exitPrinter.append("The ");
            if (programName != null) {
                exitPrinter.append(programName).append(" ");
            }
            if (groupName != null) {
                exitPrinter.append(groupName).append(" ");
            }
            exitPrinter.append(commandName).append(" command exits with one of the following values:").newline().newline();
            
            
            for (Entry<Integer, String> exit : sortExitCodes(Lists.newArrayList(command.getExitCodes().entrySet()))) {
                // Print the exit code
                exitPrinter.append(exit.getKey().toString());
                exitPrinter.newline();
                exitPrinter.flush();
                
                // Include description if available
                if (!StringUtils.isEmpty(exit.getValue())) {
                    
                    UsagePrinter exitDescripPrinter = exitPrinter.newIndentedPrinter(4);
                    exitDescripPrinter.append(exit.getValue());
                    exitDescripPrinter.flush();
                }
                
                exitPrinter.newline();
                exitPrinter.flush();
            }
        }
    }

}
