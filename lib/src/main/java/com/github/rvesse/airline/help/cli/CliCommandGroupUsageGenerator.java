package com.github.rvesse.airline.help.cli;

import java.io.IOException;
import java.util.*;

import com.github.rvesse.airline.help.AbstractPrintedCommandGroupUsageGenerator;
import com.github.rvesse.airline.help.UsagePrinter;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

import static com.github.rvesse.airline.help.UsageHelper.DEFAULT_COMMAND_COMPARATOR;
import static com.github.rvesse.airline.help.UsageHelper.DEFAULT_OPTION_COMPARATOR;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newTreeMap;

public class CliCommandGroupUsageGenerator extends AbstractPrintedCommandGroupUsageGenerator {
    private final boolean hideGlobalOptions;

    public CliCommandGroupUsageGenerator() {
        this(79, false, DEFAULT_OPTION_COMPARATOR, DEFAULT_COMMAND_COMPARATOR, false);
    }

    public CliCommandGroupUsageGenerator(int columnSize) {
        this(columnSize, false, DEFAULT_OPTION_COMPARATOR, DEFAULT_COMMAND_COMPARATOR, false);
    }

    public CliCommandGroupUsageGenerator(int columnSize, boolean hideGlobalOptions) {
        this(columnSize, hideGlobalOptions, DEFAULT_OPTION_COMPARATOR, DEFAULT_COMMAND_COMPARATOR, false);
    }

    public CliCommandGroupUsageGenerator(int columnSize, boolean hideGlobalOptions,
            Comparator<? super OptionMetadata> optionComparator, Comparator<? super CommandMetadata> commandComparator,
            boolean includeHidden) {
        super(columnSize, optionComparator, commandComparator, includeHidden);
        this.hideGlobalOptions = hideGlobalOptions;
    }

    @Override
    protected void usage(GlobalMetadata global, CommandGroupMetadata group, UsagePrinter out) throws IOException {
        // Description and Name
        outputDescription(out, global, group);

        //
        // SYNOPSIS
        //
        outputSynopsis(out, global, group);

        //
        // OPTIONS
        //
        outputOptions(out, global, group);
    }

    /**
     * Outputs a documentation section detailing the available options and their
     * usages
     * 
     * @param out
     *            Usage printer
     * @param global
     *            Global meta-data
     * @param group
     *            Group meta-data
     * 
     * @throws IOException
     */
    protected void outputOptions(UsagePrinter out, GlobalMetadata global, CommandGroupMetadata group)
            throws IOException {
        List<OptionMetadata> options = newArrayList();
        options.addAll(group.getOptions());
        if (global != null && !hideGlobalOptions) {
            options.addAll(global.getOptions());
        }
        if (options.size() > 0) {
            options = sortOptions(options);

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

                descriptionPrinter.newline();
            }
        }
    }

    /**
     * Outputs a documentation section detailing a usage synopsis
     * 
     * @param out
     *            Usage printer
     * @param global
     *            Global meta-data
     * @param group
     *            Group meta-data
     * @throws IOException
     */
    protected void outputSynopsis(UsagePrinter out, GlobalMetadata global, CommandGroupMetadata group)
            throws IOException {
        out.append("SYNOPSIS").newline();
        UsagePrinter synopsis = out.newIndentedPrinter(8).newPrinterWithHangingIndent(8);

        List<CommandMetadata> commands = sortCommands(group.getCommands());

        // Populate group info via an extra for loop through commands
        String defaultCommand = "";
        if (group.getDefaultCommand() != null) {
            defaultCommand = group.getDefaultCommand().getName();
        }
        List<OptionMetadata> commonGroupOptions = null;
        String commonGroupArgs = null;
        List<String> allCommandNames = newArrayList();
        boolean hasCommandSpecificOptions = false, hasCommandSpecificArgs = false;
        for (CommandMetadata command : commands) {
            if (command.getName().equals(defaultCommand)) {
                allCommandNames.add(command.getName() + "*");
            } else {
                allCommandNames.add(command.getName());
            }
            if (commonGroupOptions == null) {
                commonGroupOptions = newArrayList(command.getCommandOptions());
            }
            if (commonGroupArgs == null) {
                commonGroupArgs = (command.getArguments() != null ? toUsage(command.getArguments()) : "");
            }

            commonGroupOptions.retainAll(command.getCommandOptions());
            if (command.getCommandOptions().size() > commonGroupOptions.size()) {
                hasCommandSpecificOptions = true;
            }
            if (commonGroupArgs != (command.getArguments() != null ? toUsage(command.getArguments()) : "")) {
                hasCommandSpecificArgs = true;
            }
        }
        // Print group summary line
        if (global != null) {
            synopsis.append(global.getName());
            if (!hideGlobalOptions) {
                synopsis.appendWords(toSynopsisUsage(commands.get(0).getGlobalOptions()));
            }
        }
        synopsis.append(group.getName()).appendWords(toSynopsisUsage(commands.get(0).getGroupOptions()));
        synopsis.append(" {").append(allCommandNames.get(0));
        for (int i = 1; i < allCommandNames.size(); i++) {
            synopsis.append(" | ").append(allCommandNames.get(i));
        }
        synopsis.append("} [--]");
        if (commonGroupOptions.size() > 0) {
            synopsis.appendWords(toSynopsisUsage(commonGroupOptions));
        }
        if (hasCommandSpecificOptions) {
            synopsis.append(" [cmd-options]");
        }
        if (hasCommandSpecificArgs) {
            synopsis.append(" <cmd-args>");
        }
        synopsis.newline();
        Map<String, String> cmdOptions = newTreeMap();
        Map<String, String> cmdArguments = newTreeMap();

        for (CommandMetadata command : commands) {

            if (!command.isHidden() || this.includeHidden()) {
                if (hasCommandSpecificOptions) {
                    List<OptionMetadata> thisCmdOptions = newArrayList(command.getCommandOptions());
                    thisCmdOptions.removeAll(commonGroupOptions);
                    StringBuilder optSB = new StringBuilder();
                    for (String s : toSynopsisUsage(thisCmdOptions)) {
                        optSB.append(s + " ");
                    }
                    cmdOptions.put(command.getName(), optSB.toString());
                }
                if (hasCommandSpecificArgs) {
                    cmdArguments.put(command.getName(),
                            (command.getArguments() != null ? toUsage(command.getArguments()) : ""));
                }
            }
        }
        if (hasCommandSpecificOptions) {
            synopsis.newline().append("Where command-specific options [cmd-options] are:").newline();
            UsagePrinter opts = synopsis.newIndentedPrinter(4);
            for (String cmd : cmdOptions.keySet()) {
                opts.append(cmd + ": " + cmdOptions.get(cmd)).newline();
            }
        }
        if (hasCommandSpecificArgs) {
            synopsis.newline().append("Where command-specific arguments <cmd-args> are:").newline();
            UsagePrinter args = synopsis.newIndentedPrinter(4);
            for (String arg : cmdArguments.keySet()) {
                args.append(arg + ": " + cmdArguments.get(arg)).newline();
            }
        }
        if (defaultCommand != "") {
            synopsis.newline().append(String.format("* %s is the default command", defaultCommand));
        }
        synopsis.newline().append("See").append("'" + global.getName()).append("help ").append(group.getName())
                .appendOnOneLine(" <command>' for more information on a specific command.").newline();
    }

    /**
     * Outputs a description of the group
     * 
     * @param out
     *            Usage printer
     * @param global
     *            Global meta-data
     * @param group
     *            Group meta-data
     * @throws IOException
     */
    protected void outputDescription(UsagePrinter out, GlobalMetadata global, CommandGroupMetadata group)
            throws IOException {
        out.append("NAME").newline();

        out.newIndentedPrinter(8).append(global.getName()).append(group.getName()).append("-")
                .append(group.getDescription()).newline().newline();
    }
}
