package io.airlift.airline.help.cli;

import com.google.common.base.Function;
import com.google.common.base.Objects;
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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newTreeMap;

/**
 * 
 * <h4>Known Issues</h4>
 * <p>
 * This implementation does not currently respect the configured command comparator
 * </p>
 * 
 */
public class CliGlobalUsageSummaryGenerator extends AbstractPrintedGlobalUsageGenerator {
    public CliGlobalUsageSummaryGenerator() {
        this(79);
    }

    public CliGlobalUsageSummaryGenerator(int columnSize) {
        this(columnSize, UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR);
    }

    public CliGlobalUsageSummaryGenerator(int columnSize,
            @Nullable Comparator<? super OptionMetadata> optionComparator,
            @Nullable Comparator<? super CommandMetadata> commandComparator) {
        super(columnSize, optionComparator, commandComparator);
    }

    public void usage(GlobalMetadata global, UsagePrinter out) throws IOException {
        //
        // Usage
        //

        // build arguments
        List<String> commandArguments = newArrayList();
        Collection<String> args = Collections2.transform(global.getOptions(), new Function<OptionMetadata, String>() {
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

        //
        // Common commands
        //

        Map<String, String> commands = newTreeMap();
        for (CommandMetadata commandMetadata : global.getDefaultGroupCommands()) {
            if (!commandMetadata.isHidden()) {
                commands.put(commandMetadata.getName(), commandMetadata.getDescription());
            }
        }
        for (CommandGroupMetadata commandGroupMetadata : global.getCommandGroups()) {
            commands.put(commandGroupMetadata.getName(), commandGroupMetadata.getDescription());
        }

        out.append("Commands are:").newline();
        out.newIndentedPrinter(4).appendTable(
                Iterables.transform(commands.entrySet(), new Function<Entry<String, String>, Iterable<String>>() {
                    @SuppressWarnings("deprecation")
                    public Iterable<String> apply(Entry<String, String> entry) {
                        return ImmutableList.of(entry.getKey(), Objects.firstNonNull(entry.getValue(), ""));
                    }
                }));
        out.newline();
        out.append("See").append("'" + global.getName())
                .append("help <command>' for more information on a specific command.").newline();
    }
}
