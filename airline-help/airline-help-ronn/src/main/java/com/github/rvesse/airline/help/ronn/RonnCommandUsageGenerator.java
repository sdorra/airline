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

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.help.common.AbstractCommandUsageGenerator;
import com.github.rvesse.airline.help.man.ManSections;
import com.github.rvesse.airline.help.sections.HelpSection;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;

/**
 * A command usage generator which generates help in
 * <a href="http://rtomayko.github.io/ronn/">Ronn format</a> which can then be
 * transformed into man pages or HTML pages as desired using the Ronn tooling
 * 
 * @author rvesse
 * @deprecated The RONN format has some know bugs and it is recommended to use
 *             classes from the airline-help-man module instead of classes from
 *             this module
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
    public <T> void usage(String programName, String[] groupNames, String commandName, CommandMetadata command,
            ParserMetadata<T> parserConfig, OutputStream output) throws IOException {
        String sectionHeader = "## ";
        
        if (parserConfig == null) {
            parserConfig = MetadataLoader.loadParser(command.getType());
        }

        // Fall back to metadata declared name if necessary
        if (commandName == null)
            commandName = command.getName();

        Writer writer = new OutputStreamWriter(output);

        sectionHeader = outputTitle(writer, programName, groupNames, commandName, command, sectionHeader);

        // Find the help sections
        List<HelpSection> preSections = new ArrayList<HelpSection>();
        List<HelpSection> postSections = new ArrayList<HelpSection>();
        findHelpSections(command, preSections, postSections);

        // Output pre help sections
        for (HelpSection section : preSections) {
            helper.outputHelpSection(writer, section, sectionHeader);
        }

        List<OptionMetadata> options = outputSynopsis(writer, programName, groupNames, commandName, command,
                sectionHeader);

        if (options.size() > 0 || command.getArguments() != null) {
            outputOptions(writer, command, options, sectionHeader, parserConfig);
        }

        // Output post help sections
        for (HelpSection section : postSections) {
            helper.outputHelpSection(writer, section, sectionHeader);
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
    protected <T> void outputOptions(Writer writer, CommandMetadata command, List<OptionMetadata> options,
            String sectionHeader, ParserMetadata<T> parserConfig) throws IOException {
        helper.outputOptions(writer, options, sectionHeader);
        helper.outputArguments(writer, command, parserConfig);
    }

    /**
     * Outputs a synopsis section for the documentation showing how to use a
     * command
     * 
     * @param writer
     *            Writer
     * @param programName
     *            Program name
     * @param groupNames
     *            Group name(s)
     * @param commandName
     *            Command name
     * @param command
     *            Command
     * @param sectionHeader
     *            Section header
     * @return List of all the available options (global, group and command)
     * @throws IOException
     */
    protected List<OptionMetadata> outputSynopsis(Writer writer, String programName, String[] groupNames,
            String commandName, CommandMetadata command, String sectionHeader) throws IOException {
        writer.append(RonnUsageHelper.NEW_PARA).append(sectionHeader).append("SYNOPSIS")
                .append(RonnUsageHelper.NEW_PARA);
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
        if (groupNames != null) {
            for (int i = 0; i < groupNames.length; i++) {
                writer.append(" `").append(groupNames[i]).append("`");
            }
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
     * Outputs a title section for the document
     * 
     * @param writer
     *            Writer
     * @param programName
     *            Program name
     * @param groupNames
     *            Group name(s)
     * @param commandName
     *            Command name
     * @param command
     *            Command meta-data
     * @param sectionHeader
     *            Section header
     * @return Section header
     * @throws IOException
     */
    protected String outputTitle(Writer writer, String programName, String[] groupNames, String commandName,
            CommandMetadata command, String sectionHeader) throws IOException {
        if (!this.standalone) {
            writer.append(sectionHeader);
            sectionHeader = "#" + sectionHeader;
        }
        writeFullCommandName(programName, groupNames, commandName, writer);
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
     * @param groupNames
     *            Group name(s)
     * @param command
     *            Command meta-data
     * @param writer
     *            Writer
     * @throws IOException
     */
    protected void writeFullCommandName(String programName, String[] groupNames, String commandName, Writer writer)
            throws IOException {
        if (programName != null) {
            writer.append(programName).append("-");
        }
        if (groupNames != null) {
            for (int i = 0; i < groupNames.length; i++) {
                writer.append(groupNames[i]).append("-");
            }
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
