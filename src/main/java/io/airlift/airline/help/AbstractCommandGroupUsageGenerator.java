package io.airlift.airline.help;

import java.io.IOException;
import java.util.Comparator;

import io.airlift.airline.model.CommandGroupMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.GlobalMetadata;
import io.airlift.airline.model.OptionMetadata;

/**
 * Abstract command group usage generator
 */
public abstract class AbstractCommandGroupUsageGenerator extends AbstractUsageGenerator implements
        CommandGroupUsageGenerator {

    public AbstractCommandGroupUsageGenerator() {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR);
    }

    public AbstractCommandGroupUsageGenerator(Comparator<? super OptionMetadata> optionComparator, Comparator<? super CommandMetadata> commandComparator) {
        super(optionComparator, commandComparator);
    }

    @Override
    public void usage(GlobalMetadata global, CommandGroupMetadata group) throws IOException {
        usage(global, group, System.out);
    }
}