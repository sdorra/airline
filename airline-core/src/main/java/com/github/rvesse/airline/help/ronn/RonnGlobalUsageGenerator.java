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

import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.help.common.AbstractGlobalUsageGenerator;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

/**
 * <p>
 * A global usage generator which generates help in <a
 * href="http://rtomayko.github.io/ronn/">Ronn format</a> which can then be
 * transformed into man pages or HTML pages as desired using the Ronn tooling.
 * </p>
 * <p>
 * The individual sections of the documentation are each generated by a
 * protected method so this class can be used as a base and extended if you wish
 * to customise how sections are output
 * </p>
 */
public class RonnGlobalUsageGenerator<T> extends AbstractGlobalUsageGenerator<T> {

    protected final CommandUsageGenerator commandUsageGenerator;
    protected final int manSection;
    protected final RonnUsageHelper helper;

    public RonnGlobalUsageGenerator() {
        this(ManSections.GENERAL_COMMANDS, false, new RonnCommandUsageGenerator(ManSections.GENERAL_COMMANDS, false, false));
    }

    public RonnGlobalUsageGenerator(int manSection) {
        this(manSection, false, new RonnCommandUsageGenerator(manSection, false, false));
    }

    public RonnGlobalUsageGenerator(int manSection, boolean includeHidden) {
        this(manSection, includeHidden, new RonnCommandUsageGenerator(manSection, includeHidden, false));
    }

    protected RonnGlobalUsageGenerator(int manSection, boolean includeHidden, CommandUsageGenerator commandUsageGenerator) {
        super(includeHidden);
        this.commandUsageGenerator = commandUsageGenerator;
        this.manSection = manSection;
        this.helper = createHelper(includeHidden);
    }

    protected RonnUsageHelper createHelper(boolean includeHidden) {
        return new RonnUsageHelper(UsageHelper.DEFAULT_OPTION_COMPARATOR, includeHidden);
    }

    @Override
    public void usage(GlobalMetadata<T> global, OutputStream output) throws IOException {
        Writer writer = new OutputStreamWriter(output);

        outputTitle(global, writer);

        List<OptionMetadata> options = new ArrayList<>();
        if (global.getOptions() != null && global.getOptions().size() > 0) {
            options.addAll(global.getOptions());
            options = sortOptions(options);
        }
        outputSynopsis(writer, global);

        if (options.size() > 0) {
            helper.outputOptions(writer, options, "## ");
        }

        // TODO If we add Discussion and Examples to global meta-data reinstate
        // this
        //@formatter:off
//        if (global.getDiscussion() != null) {
//            writer.append(RonnUsageHelper.NEW_PARA).append("## DISCUSSION").append(RonnUsageHelper.NEW_PARA);
//            writer.append(global.getDiscussion());
//        }
//
//        if (global.getExamples() != null && !global.getExamples().isEmpty()) {
//            writer.append(RonnUsageHelper.NEW_PARA).append("## EXAMPLES");
//
//            // this will only work for "well-formed" examples
//            for (int i = 0; i < global.getExamples().size(); i += 3) {
//                String aText = global.getExamples().get(i).trim();
//
//                if (aText.startsWith("*")) {
//                    aText = aText.substring(1).trim();
//                }
//
//                writer.append(RonnUsageHelper.NEW_PARA).append("* ").append(aText).append(":\n");
//            }
//        }
        //@formatter:on

        writer.flush();
        output.flush();

        if (global.getCommandGroups().size() > 0) {
            // Command Groups
            outputGroupCommandList(writer, global);
            outputCommandUsages(output, writer, global);
        } else {
            // No Groups
            outputCommandList(writer, global);
            outputCommandUsages(output, writer, global);
        }

        // Flush the output
        writer.flush();
        output.flush();
    }

    /**
     * Outputs a documentation section that lists the available groups and the
     * commands they contain
     * <p>
     * Used only when a CLI has command groups, if no groups are present then
     * {@link #outputCommandList(Writer, GlobalMetadata)} is used instead.
     * </p>
     * 
     * @param writer
     *            Writer
     * @param global
     *            Global meta-data
     * 
     * @throws IOException
     */
    protected void outputGroupCommandList(Writer writer, GlobalMetadata<T> global) throws IOException {
        writer.append(RonnUsageHelper.NEW_PARA).append("## COMMAND GROUPS").append(RonnUsageHelper.NEW_PARA);
        writer.append("Commands are grouped as follows:");

        if (global.getDefaultGroupCommands().size() > 0) {
            writer.append(RonnUsageHelper.NEW_PARA).append("* Default (no <group> specified)");
            for (CommandMetadata command : sortCommands(global.getDefaultGroupCommands())) {
                if (command.isHidden() && !this.includeHidden())
                    continue;

                writer.append(RonnUsageHelper.NEW_PARA).append("  * `").append(getCommandName(global, null, command)).append("`:\n");
                writer.append("  ").append(command.getDescription());
            }
        }

        for (CommandGroupMetadata group : sortCommandGroups(global.getCommandGroups())) {
            if (group.isHidden() && !this.includeHidden())
                continue;

            writer.append(RonnUsageHelper.NEW_PARA).append("* **").append(group.getName()).append("**").append(RonnUsageHelper.NEW_PARA);
            writer.append("  ").append(group.getDescription());

            for (CommandMetadata command : sortCommands(group.getCommands())) {
                if (command.isHidden() && !this.includeHidden())
                    continue;

                writer.append(RonnUsageHelper.NEW_PARA).append("  * `").append(getCommandName(global, group.getName(), command))
                        .append("`:\n");
                writer.append("  ").append(command.getDescription());
            }
        }
    }

    /**
     * Outputs a documentation section that lists the available commands
     * <p>
     * Used only when a CLI does not have command groups, if groups are present
     * then {@link #outputGroupCommandList(Writer, GlobalMetadata)} is used
     * instead.
     * </p>
     * 
     * @param writer
     *            Writer
     * @param global
     *            Global meta-data
     * 
     * @throws IOException
     */
    protected void outputCommandList(Writer writer, GlobalMetadata<T> global) throws IOException {
        writer.append(RonnUsageHelper.NEW_PARA).append("## COMMANDS");

        for (CommandMetadata command : sortCommands(global.getDefaultGroupCommands())) {
            if (command.isHidden() && !this.includeHidden())
                continue;

            writer.append(RonnUsageHelper.NEW_PARA).append("* `").append(getCommandName(global, null, command)).append("`:\n");
            writer.append(command.getDescription());
        }
    }

    /**
     * Outputs a documentation section with a synopsis of how to use the CLI
     * 
     * @param writer
     *            Writer
     * @param global
     *            Global meta-data
     * 
     * @return
     * @throws IOException
     */
    protected void outputSynopsis(Writer writer, GlobalMetadata<T> global) throws IOException {
        writer.append(RonnUsageHelper.NEW_PARA).append("## SYNOPSIS").append(RonnUsageHelper.NEW_PARA);
        writer.append("`").append(global.getName()).append("`");
        if (global.getOptions() != null && global.getOptions().size() > 0) {
            writer.append(" ").append(StringUtils.join(toSynopsisUsage(sortOptions(global.getOptions())), ' '));
        }
        if (global.getCommandGroups().size() > 0) {
            writer.append(" [<group>] <command> [command-args]");
        } else {
            writer.append(" <command> [command-args]");
        }
    }

    /**
     * Outputs the title section for the documentation
     * 
     * @param global
     *            Global meta-data
     * @param writer
     *            Writer
     * @throws IOException
     */
    protected void outputTitle(GlobalMetadata<T> global, Writer writer) throws IOException {
        writer.append(global.getName()).append("(").append(Integer.toString(this.manSection)).append(") -- ");
        writer.append(global.getDescription()).append("\n");
        writer.append("==========");
    }

    /**
     * Outputs the command usages for all groups
     * 
     * @param output
     *            Output stream
     * @param writer
     *            Writer
     * @param global
     *            Global meta-data
     * 
     * @throws IOException
     */
    protected void outputCommandUsages(OutputStream output, Writer writer, GlobalMetadata<T> global) throws IOException {
        writer.append(RonnUsageHelper.NEW_PARA).append(RonnUsageHelper.HORIZONTAL_RULE).append(RonnUsageHelper.NEW_PARA);

        // Default group usages
        outputDefaultGroupCommandUsages(output, writer, global);

        // Other group usages
        for (CommandGroupMetadata group : sortCommandGroups(global.getCommandGroups())) {
            if (group.isHidden() && !this.includeHidden())
                continue;

            outputGroupCommandUsages(output, writer, global, group);
        }
    }

    /**
     * Gets the display name for a command
     * 
     * @param global
     *            Global meta-data
     * @param groupName
     *            Group name (may be null)
     * @param command
     *            Command meta-data
     * @return Display name for the command
     */
    protected String getCommandName(GlobalMetadata<T> global, String groupName, CommandMetadata command) {
        return command.getName();
    }

    /**
     * Outputs the command usages for the commands in the given group
     * 
     * @param output
     *            Output
     * @param writer
     *            Writer
     * @param global
     *            Global Meta-data
     * @param group
     *            Group Meta-data
     * 
     * @throws IOException
     */
    protected void outputGroupCommandUsages(OutputStream output, Writer writer, GlobalMetadata<T> global,
            CommandGroupMetadata group) throws IOException {
        for (CommandMetadata command : sortCommands(group.getCommands())) {
            if (command.isHidden() && !this.includeHidden())
                continue;

            writer.flush();
            output.flush();
            commandUsageGenerator.usage(global.getName(), group.getName(), command.getName(), command, output);
            writer.append(RonnUsageHelper.NEW_PARA).append(RonnUsageHelper.HORIZONTAL_RULE).append(RonnUsageHelper.NEW_PARA);
        }
    }

    /**
     * Outputs the command usages for the commands in the default group
     * 
     * @param output
     *            Output
     * @param writer
     *            Writer
     * @param global
     *            Global meta-data
     * 
     * @throws IOException
     */
    protected void outputDefaultGroupCommandUsages(OutputStream output, Writer writer, GlobalMetadata<T> global)
            throws IOException {
        for (CommandMetadata command : sortCommands(global.getDefaultGroupCommands())) {
            if (command.isHidden() && !this.includeHidden())
                continue;

            writer.flush();
            output.flush();
            commandUsageGenerator.usage(global.getName(), null, command.getName(), command, output);
            writer.append(RonnUsageHelper.NEW_PARA).append(RonnUsageHelper.HORIZONTAL_RULE).append(RonnUsageHelper.NEW_PARA);
        }
    }

    /**
     * Converts an option to its description form
     */
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