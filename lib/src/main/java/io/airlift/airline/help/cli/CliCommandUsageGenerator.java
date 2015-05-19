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

    public CliCommandUsageGenerator(int columnSize,  Comparator<? super OptionMetadata> optionComparator) {
        super(columnSize, optionComparator);
    }

    @Override
    protected void usage( String programName,  String groupName, String commandName,
            CommandMetadata command, UsagePrinter out) throws IOException {
        //
        // Name and description
        //
        outputDescription(out, programName, groupName, commandName, command);

        // Synopsis
        List<OptionMetadata> options = outputSynopsis(out, programName, groupName, commandName, command);

        // Options
        ArgumentsMetadata arguments = command.getArguments();
        if (options.size() > 0 || arguments != null) {
            outputOptions(out, options, arguments);
        }

        // Discussion
        if (command.getDiscussion() != null) {
            outputDiscussion(out, command);
        }

        // Examples
        if (command.getExamples() != null && !command.getExamples().isEmpty()) {
            outputExamples(out, command);
        }

        // Exit Codes
        if (command.getExitCodes() != null && !command.getExitCodes().isEmpty()) {
            outputExitCodes(out, programName, groupName, commandName, command);
        }
    }

    /**
     * Outputs a documentation section detailing the exit codes
     * 
     * @param out
     *            Usage printer
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param commandName
     *            Command name
     * @param command
     *            Command meta-data
     * @throws IOException
     */
    protected void outputExitCodes(UsagePrinter out, String programName, String groupName, String commandName,
            CommandMetadata command) throws IOException {
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

    /**
     * Outputs a documentation section detailing examples
     * 
     * @param out
     *            Usage printer
     * @param command
     *            Command meta-data
     * 
     * @throws IOException
     */
    protected void outputExamples(UsagePrinter out, CommandMetadata command) throws IOException {
        out.append("EXAMPLES").newline();
        UsagePrinter examplePrinter = out.newIndentedPrinter(8);

        examplePrinter.appendTable(Iterables.partition(command.getExamples(), 1));
        examplePrinter.flush();
    }

    /**
     * Outputs a documentation section with discussion
     * 
     * @param out
     *            Usage printer
     * @param command
     *            Command meta-data
     * 
     * @throws IOException
     */
    protected void outputDiscussion(UsagePrinter out, CommandMetadata command) throws IOException {
        out.append("DISCUSSION").newline();
        UsagePrinter discussionPrinter = out.newIndentedPrinter(8);

        discussionPrinter.append(command.getDiscussion()).newline().newline();
        discussionPrinter.flush();
    }

    /**
     * Outputs a documentation section detailing options and their usages
     * 
     * @param out
     *            Usage printer
     * @param options
     *            Options meta-data
     * @param arguments
     *            Arguments meta-data
     * @throws IOException
     */
    protected void outputOptions(UsagePrinter out, List<OptionMetadata> options, ArgumentsMetadata arguments)
            throws IOException {
        options = sortOptions(options);

        out.append("OPTIONS").newline();

        for (OptionMetadata option : options) {
            // skip hidden options
            if (option.isHidden() && !this.includeHidden()) {
                continue;
            }

            // option names
            UsagePrinter optionPrinter = out.newIndentedPrinter(8);
            optionPrinter.append(toDescription(option)).newline();
            optionPrinter.flush();

            // description
            UsagePrinter descriptionPrinter = optionPrinter.newIndentedPrinter(4);
            descriptionPrinter.append(option.getDescription()).newline();

            // allowedValues
            if (option.getAllowedValues() != null && option.getAllowedValues().size() > 0 && option.getArity() >= 1) {
                outputAllowedValues(descriptionPrinter, option);
            }

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
            descriptionPrinter.append(
                    "This option can be used to separate command-line options from the "
                            + "list of argument, (useful when arguments might be mistaken for command-line options)")
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

    /**
     * Outputs a documentation section detailing allowed values for an option
     * 
     * @param descriptionPrinter
     *            Description printer
     * @param option
     *            Option meta-data
     * @throws IOException
     */
    protected void outputAllowedValues(UsagePrinter descriptionPrinter, OptionMetadata option) throws IOException {
        descriptionPrinter.newline();
        descriptionPrinter.append("This options value");
        if (option.getArity() == 1) {
            descriptionPrinter.append(" is ");
        } else {
            descriptionPrinter.append("s are ");
        }
        descriptionPrinter.append("restricted to the following value(s):").newline();

        UsagePrinter allowedValuesPrinter = descriptionPrinter.newIndentedPrinter(4);
        for (String value : option.getAllowedValues()) {
            allowedValuesPrinter.append(value).newline();
        }
        allowedValuesPrinter.flush();
    }

    /**
     * Outputs a documentation section with a synopsis of command usage
     * 
     * @param out
     *            Usage printer
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param commandName
     *            Command name
     * @param command
     *            Command meta-data
     * @return Collection of all options (Global, Group and Command)
     * @throws IOException
     */
    protected List<OptionMetadata> outputSynopsis(UsagePrinter out, String programName, String groupName,
            String commandName, CommandMetadata command) throws IOException {
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
        if (command.getArguments() != null) {
            synopsis.append("[--]").append(toUsage(command.getArguments()));
        }
        synopsis.newline();
        synopsis.newline();
        return options;
    }

    /**
     * Outputs a documentation section describing the command
     * 
     * @param out
     *            Usage printer
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param commandName
     *            Command name
     * @param command
     *            Command meta-data
     * @throws IOException
     */
    protected void outputDescription(UsagePrinter out, String programName, String groupName, String commandName,
            CommandMetadata command) throws IOException {
        out.append("NAME").newline();

        out.newIndentedPrinter(8).append(programName).append(groupName).append(commandName).append("-")
                .append(command.getDescription()).newline().newline();
    }

}
