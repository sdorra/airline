package io.airlift.airline.help;

import java.io.IOException;
import java.util.Comparator;

import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.GlobalMetadata;
import io.airlift.airline.model.OptionMetadata;

/**
 * Abstract global usage generator
 */
public abstract class AbstractGlobalUsageGenerator extends AbstractUsageGenerator implements GlobalUsageGenerator {

    public AbstractGlobalUsageGenerator() {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR);
    }

    public AbstractGlobalUsageGenerator(Comparator<? super OptionMetadata> optionComparator, Comparator<? super CommandMetadata> commandComparator) {
        super(optionComparator, commandComparator);
    }

    @Override
    public void usage(GlobalMetadata global) throws IOException {
        usage(global, System.out);
    }
}