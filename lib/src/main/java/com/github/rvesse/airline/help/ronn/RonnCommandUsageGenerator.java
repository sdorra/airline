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
package com.github.rvesse.airline.help.ronn;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.help.common.AbstractCommandUsageGenerator;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

/**
 * A command usage generator which generates help in <a
 * href="http://rtomayko.github.io/ronn/">Ronn format</a> which can then be
 * transformed into man pages or HTML pages as desired using the Ronn tooling
 * 
 */
public class RonnCommandUsageGenerator extends AbstractCommandUsageGenerator {

    private final int manSection;
    private final boolean standalone;
    private final RonnUsageHelper helper;

    public RonnCommandUsageGenerator() {
        this(ManSections.GENERAL_COMMANDS, false, true);
    }

    /**
     * Creates a new RONN usage generator
     * 
     * @param manSection
     *            Man section to which this command belongs, use constants from
     *            {@link ManSections}
     * @param standalone
     *            Whether this is a stand-alone RONN file, this controls the
     *            formatting of the title which is significant when using this
     *            in conjunction with things like the
     *            {@link RonnGlobalUsageGenerator} where the output from this is
     *            output a fragment of a larger document and RONN will not
     *            render the titles if stand-alone is enabled
     */
    public RonnCommandUsageGenerator(int manSection, boolean includeHidden, boolean standalone) {
        super(includeHidden);
        this.manSection = manSection;
        this.standalone = standalone;
        this.helper = createHelper(includeHidden);
    }

    protected RonnUsageHelper createHelper(boolean includeHidden) {
        return new RonnUsageHelper(UsageHelper.DEFAULT_OPTION_COMPARATOR, includeHidden);
    }

    @Override
    public void usage(String programName, String groupName, String commandName, CommandMetadata command,
            OutputStream output) throws IOException {
        String SECTION_HEADER = "## ";

        // Fall back to metadata declared name if necessary
        if (commandName == null)
            commandName = command.getName();

        Writer writer = new OutputStreamWriter(output);

        SECTION_HEADER = outputTitle(writer, programName, groupName, commandName, command, SECTION_HEADER);

        List<OptionMetadata> options = outputSynopsis(writer, programName, groupName, commandName, command,
                SECTION_HEADER);

        if (options.size() > 0 || command.getArguments() != null) {
            outputOptions(writer, command, options, SECTION_HEADER);
        }
        if (command.getDiscussion() != null && !command.getDiscussion().isEmpty()) {
            outputDiscussion(writer, command, SECTION_HEADER);
        }
        if (command.getExamples() != null && !command.getExamples().isEmpty()) {
            outputExamples(writer, command, SECTION_HEADER);
        }
        if (command.getExitCodes() != null && !command.getExitCodes().isEmpty()) {
            outputExitCodes(writer, programName, groupName, commandName, command, SECTION_HEADER);
        }

        // Flush the output
        writer.flush();
        output.flush();
    }

    /**
     * Outputs a documentation section detailing the options and their usages
     * 
     * @param writer
     *            Writer
     * @param command
     *            Command
     * @param options
     *            Option meta-data
     * @param sectionHeader
     *            Section header
     * 
     * @throws IOException
     */
    protected void outputOptions(Writer writer, CommandMetadata command, List<OptionMetadata> options,
            String sectionHeader) throws IOException {
        helper.outputOptions(writer, options, sectionHeader);
        helper.outputArguments(writer, command);
    }

    /**
     * Outputs a synopsis section for the documentation showing how to use a
     * command
     * 
     * @param writer
     *            Writer
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param commandName
     *            Command name
     * @param command
     *            Command
     * @param sectionHeader
     *            Section header
     * @return List of all the available options (global, group and command)
     * @throws IOException
     */
    protected List<OptionMetadata> outputSynopsis(Writer writer, String programName, String groupName,
            String commandName, CommandMetadata command, String sectionHeader) throws IOException {
        writer.append(RonnUsageHelper.NEW_PARA).append(sectionHeader).append("SYNOPSIS").append(RonnUsageHelper.NEW_PARA);
        List<OptionMetadata> options = new ArrayList<>();
        List<OptionMetadata> aOptions;
        if (programName != null) {
            writer.append("`").append(programName).append("`");
            aOptions = command.getGlobalOptions();
            if (aOptions != null && aOptions.size() > 0) {
                writer.append(" ").append(StringUtils.join(toSynopsisUsage(sortOptions(aOptions)), ' '));
                options.addAll(aOptions);
            }
        }
        if (groupName != null) {
            writer.append(" `").append(groupName).append("`");
            aOptions = command.getGroupOptions();
            if (aOptions != null && aOptions.size() > 0) {
                writer.append(" ").append(StringUtils.join(toSynopsisUsage(sortOptions(aOptions)), ' '));
                options.addAll(aOptions);
            }
        }
        aOptions = command.getCommandOptions();
        writer.append(" `").append(commandName).append("` ")
                .append(StringUtils.join(toSynopsisUsage(sortOptions(aOptions)), ' '));
        options.addAll(aOptions);

        // command arguments (optional)
        if (command.getArguments() != null) {
            writer.append(" [--] ").append(toUsage(command.getArguments()));
        }

        if (!this.standalone) {
            writer.append(RonnUsageHelper.NEW_PARA).append(command.getDescription());
        }
        return options;
    }

    /**
     * Outputs an exit codes section for the documentation
     * 
     * @param writer
     *            Writer
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param commandName
     *            Command name
     * @param command
     *            Command meta-data
     * @param sectionHeader
     *            Section header
     * 
     * @throws IOException
     */
    protected void outputExitCodes(Writer writer, String programName, String groupName, String commandName,
            CommandMetadata command, String sectionHeader) throws IOException {
        writer.append(RonnUsageHelper.NEW_PARA).append(sectionHeader).append("EXIT STATUS");
        writer.append(RonnUsageHelper.NEW_PARA).append("The `");
        writeFullCommandName(programName, groupName, commandName, writer);
        writer.append("` command exits with one of the following values:");
        writer.append(RonnUsageHelper.NEW_PARA);

        for (Entry<Integer, String> exit : sortExitCodes(new ArrayList<>(command.getExitCodes().entrySet()))) {
            // Print the exit code
            writer.append("* **").append(exit.getKey().toString()).append("**");

            // Include description if available
            if (!StringUtils.isEmpty(exit.getValue())) {
                writer.append(" - ").append(exit.getValue());
            }

            writer.append('\n');
        }
    }

    /**
     * Outputs an examples section for the documentation
     * 
     * @param writer
     *            Writer
     * @param command
     *            Command meta-data
     * @param sectionHeader
     *            Section header
     * @throws IOException
     */
    protected void outputExamples(Writer writer, CommandMetadata command, String sectionHeader) throws IOException {
        writer.append(RonnUsageHelper.NEW_PARA).append(sectionHeader).append("EXAMPLES");

        for (String example : command.getExamples()) {
            writer.append(RonnUsageHelper.NEW_PARA).append(example);
        }
    }

    /**
     * Outputs a discussion section for the documentation
     * 
     * @param writer
     *            Writer
     * @param command
     *            Command meta-data
     * @param sectionHeader
     *            Section header
     * @throws IOException
     */
    protected void outputDiscussion(Writer writer, CommandMetadata command, String sectionHeader) throws IOException {
        if (command.getDiscussion() == null || command.getDiscussion().isEmpty())
            return;

        writer.append(RonnUsageHelper.NEW_PARA).append(sectionHeader).append("DISCUSSION").append(RonnUsageHelper.NEW_PARA);
        for (String discussionPara : command.getDiscussion()) {
            if (StringUtils.isEmpty(discussionPara))
                continue;
            writer.append(discussionPara).append(RonnUsageHelper.NEW_PARA);
        }
    }

    /**
     * Outputs a title section for the document
     * 
     * @param writer
     *            Writer
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param commandName
     *            Command name
     * @param command
     *            Command meta-data
     * @param sectionHeader
     *            Section header
     * @return Section header
     * @throws IOException
     */
    protected String outputTitle(Writer writer, String programName, String groupName, String commandName,
            CommandMetadata command, String sectionHeader) throws IOException {
        if (!this.standalone) {
            writer.append(sectionHeader);
            sectionHeader = "#" + sectionHeader;
        }
        writeFullCommandName(programName, groupName, commandName, writer);
        if (this.standalone) {
            writer.append(" -- ");
            writer.append(command.getDescription()).append("\n");
            writer.append("==========");
        }
        return sectionHeader;
    }

    /**
     * Writes the full command name in man page syntax
     * 
     * @param programName
     *            Program name
     * @param groupName
     *            Group name
     * @param command
     *            Command meta-data
     * @param writer
     *            Writer
     * @throws IOException
     */
    protected void writeFullCommandName(String programName, String groupName, String commandName, Writer writer)
            throws IOException {
        if (programName != null) {
            writer.append(programName).append("-");
        }
        if (groupName != null) {
            writer.append(groupName).append("-");
        }
        writer.append(commandName).append("(").append(Integer.toString(this.manSection)).append(")");
    }

    @Override
    protected String toDescription(OptionMetadata option) {
        Set<String> options = option.getOptions();
        StringBuilder stringBuilder = new StringBuilder();

        final String argumentString;
        if (option.getArity() > 0) {
            argumentString = String.format("<%s>", option.getTitle());
        } else {
            argumentString = null;
        }

        boolean first = true;
        for (String name : options) {
            if (!first) {
                stringBuilder.append(", ");
            } else {
                first = false;
            }
            stringBuilder.append('`').append(name).append('`');
            if (argumentString != null)
                stringBuilder.append(' ').append(argumentString);
        }

        return stringBuilder.toString();
    }
}
