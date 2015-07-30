/**
 * Copyright (C) 2010-15 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.help.cli;

import static com.github.rvesse.airline.help.UsageHelper.DEFAULT_OPTION_COMPARATOR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.help.common.AbstractPrintedCommandUsageGenerator;
import com.github.rvesse.airline.help.common.UsagePrinter;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.utils.AirlineUtils;

public class CliCommandUsageGenerator extends AbstractPrintedCommandUsageGenerator {
    
    private final CliUsageHelper helper;

    public CliCommandUsageGenerator() {
        this(DEFAULT_COLUMNS, DEFAULT_OPTION_COMPARATOR, false);
    }

    public CliCommandUsageGenerator(boolean includeHidden) {
        this(DEFAULT_COLUMNS, DEFAULT_OPTION_COMPARATOR, includeHidden);
    }

    public CliCommandUsageGenerator(int columns) {
        this(columns, DEFAULT_OPTION_COMPARATOR, false);
    }

    public CliCommandUsageGenerator(int columns, boolean includeHidden) {
        this(columns, DEFAULT_OPTION_COMPARATOR, includeHidden);
    }

    public CliCommandUsageGenerator(int columns, Comparator<? super OptionMetadata> optionComparator,
            boolean includeHidden) {
        super(columns, optionComparator, includeHidden);
        helper = createHelper(optionComparator, includeHidden);
    }

    protected CliUsageHelper createHelper(Comparator<? super OptionMetadata> optionComparator,
            boolean includeHidden) {
        return new CliUsageHelper(optionComparator, includeHidden);
    }

    @Override
    protected void usage(String programName, String groupName, String commandName, CommandMetadata command,
            UsagePrinter out) throws IOException {
        //
        // Name and description
        //
        outputDescription(out, programName, groupName, commandName, command);

        // Synopsis
        List<OptionMetadata> options = outputSynopsis(out, programName, groupName, commandName, command);

        // Options
        ArgumentsMetadata arguments = command.getArguments();
        if (options.size() > 0 || arguments != null) {
            outputOptionsAndArguments(out, command, options, arguments);
        }

        // Discussion
        if (command.getDiscussion() != null && !command.getDiscussion().isEmpty()) {
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

        for (Entry<Integer, String> exit : sortExitCodes(new ArrayList<>(command.getExitCodes().entrySet()))) {
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

        List<Iterable<String>> examplesIters = new ArrayList<>();
        for (String example : command.getExamples()) {
            examplesIters.add(AirlineUtils.singletonList(example));
        }
        examplePrinter.appendTable(examplesIters, 1);
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
        if (command.getDiscussion() == null || command.getDiscussion().isEmpty())
            return;

        out.append("DISCUSSION").newline();
        UsagePrinter discussionPrinter = out.newIndentedPrinter(8);

        for (String discussionPara : command.getDiscussion()) {
            if (StringUtils.isEmpty(discussionPara))
                continue;
            discussionPrinter.append(discussionPara).newline().newline();
        }
        discussionPrinter.flush();
    }

    /**
     * Outputs a documentation section detailing options and their usages
     * 
     * @param out
     *            Usage printer
     * @param command
     *            Command meta-data
     * @param options
     *            Options meta-data
     * @param arguments
     *            Arguments meta-data
     * @throws IOException
     */
    protected void outputOptionsAndArguments(UsagePrinter out, CommandMetadata command, List<OptionMetadata> options,
            ArgumentsMetadata arguments) throws IOException {
        helper.outputOptions(out, options);
        helper.outputArguments(out, arguments);
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
        List<OptionMetadata> options = new ArrayList<>();
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
