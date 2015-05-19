package com.github.rvesse.airline.help.cli;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import com.github.rvesse.airline.help.AbstractPrintedGlobalUsageGenerator;
import com.github.rvesse.airline.help.UsageHelper;
import com.github.rvesse.airline.help.UsagePrinter;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

import static com.github.rvesse.airline.help.UsageHelper.DEFAULT_OPTION_COMPARATOR;

public class CliGlobalUsageGenerator extends AbstractPrintedGlobalUsageGenerator {
    public CliGlobalUsageGenerator() {
        this(79);
    }

    public CliGlobalUsageGenerator(int columnSize) {
        this(columnSize, DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR,
                UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR, false);
    }

    public CliGlobalUsageGenerator(int columnSize, Comparator<? super OptionMetadata> optionComparator,
            Comparator<? super CommandMetadata> commandComparator,
            Comparator<? super CommandGroupMetadata> commandGroupComparator, boolean includeHidden) {
        super(columnSize, optionComparator, commandComparator, commandGroupComparator, includeHidden);
    }

    @Override
    protected void usage(GlobalMetadata global, UsagePrinter out) throws IOException {
        // Name and description
        outputDescription(out, global);

        // Synopsis
        outputSynopsis(out, global);

        // Options
        List<OptionMetadata> options = sortOptions(global.getOptions());
        if (options.size() > 0) {
            outputOptions(out, options);
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
    protected void outputCommandList(UsagePrinter out, GlobalMetadata global) throws IOException {
        out.append("COMMANDS").newline();
        UsagePrinter commandPrinter = out.newIndentedPrinter(8);

        for (CommandMetadata command : sortCommands(global.getDefaultGroupCommands())) {
            outputCommandDescription(commandPrinter, null, command);
        }
        for (CommandGroupMetadata group : sortCommandGroups(global.getCommandGroups())) {
            for (CommandMetadata command : sortCommands(group.getCommands())) {
                outputCommandDescription(commandPrinter, group, command);
            }
        }
    }

    /**
     * Outputs a documentation section detailing options and their usages
     * 
     * @param out
     *            Usage printer
     * @param options
     *            Options
     * @throws IOException
     */
    protected void outputOptions(UsagePrinter out, List<OptionMetadata> options) throws IOException {
        out.append("OPTIONS").newline();

        for (OptionMetadata option : options) {

            if (option.isHidden() && !this.includeHidden()) {
                continue;
            }

            // option names
            UsagePrinter optionPrinter = out.newIndentedPrinter(8);
            optionPrinter.append(toDescription(option)).newline();

            // description
            UsagePrinter descriptionPrinter = optionPrinter.newIndentedPrinter(4);
            descriptionPrinter.append(option.getDescription()).newline();

            // allowedValues
            if (option.getAllowedValues() != null && option.getAllowedValues().size() > 0 && option.getArity() >= 1) {
                outputAllowedValues(descriptionPrinter, option);
            }

            descriptionPrinter.newline();
        }

        // Note - Global meta-data does not allow arguments, those are command
        // specific hence their omission
    }

    /**
     * Outputs a documentation section detailing the allowed values for an
     * option
     * 
     * @param out
     *            Usage printer
     * @param option
     *            Option meta-data
     * @throws IOException
     */
    protected void outputAllowedValues(UsagePrinter out, OptionMetadata option) throws IOException {
        out.newline();
        out.append("This options value");
        if (option.getArity() == 1) {
            out.append(" is ");
        } else {
            out.append("s are ");
        }
        out.append("restricted to the following value(s):").newline();

        UsagePrinter allowedValuesPrinter = out.newIndentedPrinter(4);
        for (String value : option.getAllowedValues()) {
            allowedValuesPrinter.append(value).newline();
        }
        allowedValuesPrinter.flush();
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
    protected void outputSynopsis(UsagePrinter out, GlobalMetadata global) throws IOException {
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
    protected void outputDescription(UsagePrinter out, GlobalMetadata global) throws IOException {
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
