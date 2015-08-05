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

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.help.common.AbstractPrintedGlobalUsageGenerator;
import com.github.rvesse.airline.io.printers.UsagePrinter;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

import static com.github.rvesse.airline.help.UsageHelper.DEFAULT_OPTION_COMPARATOR;

public class CliGlobalUsageGenerator<T> extends AbstractPrintedGlobalUsageGenerator<T> {
    
    private final CliUsageHelper helper;

    public CliGlobalUsageGenerator() {
        this(DEFAULT_COLUMNS, UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR,
                UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR, false);
    }

    public CliGlobalUsageGenerator(boolean includeHidden) {
        this(DEFAULT_COLUMNS, UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR,
                UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR, includeHidden);
    }

    public CliGlobalUsageGenerator(int columns) {
        this(columns, DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR,
                UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR, false);
    }

    public CliGlobalUsageGenerator(int columns, boolean includeHidden) {
        this(columns, DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR,
                UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR, includeHidden);
    }

    public CliGlobalUsageGenerator(int columnSize, Comparator<? super OptionMetadata> optionComparator,
            Comparator<? super CommandMetadata> commandComparator,
            Comparator<? super CommandGroupMetadata> commandGroupComparator, boolean includeHidden) {
        super(columnSize, optionComparator, commandComparator, commandGroupComparator, includeHidden);
        helper = createHelper(optionComparator, includeHidden);
    }

    protected CliUsageHelper createHelper(Comparator<? super OptionMetadata> optionComparator,
            boolean includeHidden) {
        return new CliUsageHelper(optionComparator, includeHidden);
    }

    @Override
    protected void usage(GlobalMetadata<T> global, UsagePrinter out) throws IOException {
        // Name and description
        outputDescription(out, global);

        // Synopsis
        outputSynopsis(out, global);

        // Options
        List<OptionMetadata> options = sortOptions(global.getOptions());
        if (options.size() > 0) {
            helper.outputOptions(out, options);
        }

        // Command list
        outputCommandList(out, global);
    }

    /**
     * Outputs a documentation section listing the commands
     * 
     * @param out
     *            Usage printer
     * @param global
     *            Global meta-data
     * @throws IOException
     */
    protected void outputCommandList(UsagePrinter out, GlobalMetadata<T> global) throws IOException {
        out.append("COMMANDS").newline();
        UsagePrinter commandPrinter = out.newIndentedPrinter(8);

        for (CommandMetadata command : sortCommands(global.getDefaultGroupCommands())) {
            outputCommandDescription(commandPrinter, null, command);
        }
        for (CommandGroupMetadata group : sortCommandGroups(global.getCommandGroups())) {
            if (group.isHidden() && !this.includeHidden())
                continue;

            for (CommandMetadata command : sortCommands(group.getCommands())) {
                outputCommandDescription(commandPrinter, group, command);
            }
        }
    }

    /**
     * Outputs a documentation section with a synopsis of CLI usage
     * 
     * @param out
     *            Usage printer
     * @param global
     *            Global meta-data
     * 
     * @throws IOException
     */
    protected void outputSynopsis(UsagePrinter out, GlobalMetadata<T> global) throws IOException {
        out.append("SYNOPSIS").newline();
        out.newIndentedPrinter(8).newPrinterWithHangingIndent(8).append(global.getName())
                .appendWords(toSynopsisUsage(global.getOptions())).append("<command> [ <args> ]").newline().newline();
    }

    /**
     * Outputs a documentation section with a description of the CLI
     * 
     * @param out
     *            Usage printer
     * @param global
     *            Global meta-data
     * @throws IOException
     */
    protected void outputDescription(UsagePrinter out, GlobalMetadata<T> global) throws IOException {
        out.append("NAME").newline();

        out.newIndentedPrinter(8).append(global.getName()).append("-").append(global.getDescription()).newline()
                .newline();
    }

    /**
     * Outputs the description for a command
     * 
     * @param out
     *            Usage printer
     * @param group
     *            Group meta-data
     * @param command
     *            Command meta-data
     * @throws IOException
     */
    protected void outputCommandDescription(UsagePrinter out, CommandGroupMetadata group, CommandMetadata command)
            throws IOException {
        if (!command.isHidden() || this.includeHidden()) {
            if (group != null) {
                out.append(group.getName());
            }
            out.append(command.getName()).newline();
            if (command.getDescription() != null) {
                out.newIndentedPrinter(4).append(command.getDescription()).newline();
            }
            out.newline();
        }
    }
}
