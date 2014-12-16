package io.airlift.airline.help;

import java.io.IOException;
import java.util.Comparator;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

import javax.annotation.Nullable;

/**
 * Abstract command usage generator
 * 
 */
public abstract class AbstractCommandUsageGenerator extends AbstractUsageGenerator implements CommandUsageGenerator {

    public AbstractCommandUsageGenerator() {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR);
    }

    public AbstractCommandUsageGenerator(Comparator<? super OptionMetadata> optionComparator) {
        super(optionComparator, UsageHelper.DEFAULT_COMMAND_COMPARATOR);
    }

    @Override
    public void usage(@Nullable String programName, @Nullable String groupName, String commandName,
            CommandMetadata command) throws IOException {
        usage(programName, groupName, commandName, command, System.out);
    }
}