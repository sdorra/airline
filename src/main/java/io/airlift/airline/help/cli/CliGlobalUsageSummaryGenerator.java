package io.airlift.airline.help.cli;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import io.airlift.airline.help.AbstractPrintedGlobalUsageGenerator;
import io.airlift.airline.help.UsageHelper;
import io.airlift.airline.help.UsagePrinter;
import io.airlift.airline.model.CommandGroupMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.GlobalMetadata;
import io.airlift.airline.model.OptionMetadata;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.collect.Lists.newArrayList;

public class CliGlobalUsageSummaryGenerator extends AbstractPrintedGlobalUsageGenerator {
    public CliGlobalUsageSummaryGenerator() {
        this(79);
    }

    public CliGlobalUsageSummaryGenerator(int columnSize) {
        this(columnSize, UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR,
                UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR, false);
    }

    public CliGlobalUsageSummaryGenerator(int columnSize, Comparator<? super OptionMetadata> optionComparator,
            Comparator<? super CommandMetadata> commandComparator,
            Comparator<? super CommandGroupMetadata> commandGroupComparator, boolean includeHidden) {
        super(columnSize, optionComparator, commandComparator, commandGroupComparator, includeHidden);
    }

    public void usage(GlobalMetadata global, UsagePrinter out) throws IOException {
        // Synopsis
        outputSynopsis(out, global);

        // Command List
        outputCommandList(out, global);

        // Notes on how to get more help
        outputFooter(out, global);
    }

    /**
     * Outputs a documentation section detailing how to get more help
     * 
     * @param out
     *            Usage printer
     * @param global
     *            Global meta-data
     * 
     * @throws IOException
     */
    protected void outputFooter(UsagePrinter out, GlobalMetadata global) throws IOException {
        out.newline();
        out.append("See").append("'" + global.getName())
                .append("help <command>' for more information on a specific command.").newline();
    }

    /**
     * Outputs a documentation section listing the common commands
     * 
     * @param out
     *            Usage printer
     * @param global
     *            Global meta-data
     * @throws IOException
     */
    protected void outputCommandList(UsagePrinter out, GlobalMetadata global) throws IOException {
        Map<String, String> commands = new LinkedHashMap<>();
        for (CommandMetadata commandMetadata : sortCommands(global.getDefaultGroupCommands())) {
            if (!commandMetadata.isHidden()) {
                commands.put(commandMetadata.getName(), commandMetadata.getDescription());
            }
        }
        for (CommandGroupMetadata commandGroupMetadata : sortCommandGroups(global.getCommandGroups())) {
            commands.put(commandGroupMetadata.getName(), commandGroupMetadata.getDescription());
        }

        out.append("Commands are:").newline();
        out.newIndentedPrinter(4).appendTable(
                Iterables.transform(commands.entrySet(), new Function<Entry<String, String>, Iterable<String>>() {
                    public Iterable<String> apply(Entry<String, String> entry) {
                        return ImmutableList.of(entry.getKey(), entry.getValue() != null ? entry.getValue() : "");
                    }
                }));
    }

    /**
     * Outputs a documentation section with a brief synopsis of usage
     * 
     * @param out
     *            Usage printer
     * @param global
     *            Global meta-data
     * @throws IOException
     */
    protected void outputSynopsis(UsagePrinter out, GlobalMetadata global) throws IOException {
        List<String> commandArguments = newArrayList();
        Collection<String> args = Collections2.transform(sortOptions(global.getOptions()),
                new Function<OptionMetadata, String>() {
                    public String apply(OptionMetadata option) {
                        if (option.isHidden()) {
                            return "";
                        }
                        return toUsage(option);
                    }
                });

        commandArguments.addAll(args);
        out.newPrinterWithHangingIndent(8).append("usage:").append(global.getName()).appendWords(commandArguments)
                .append("<command> [ <args> ]").newline().newline();
    }
}
