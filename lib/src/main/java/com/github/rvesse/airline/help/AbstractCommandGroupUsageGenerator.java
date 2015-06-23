package com.github.rvesse.airline.help;

import java.io.IOException;
import java.util.Comparator;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

/**
 * Abstract command group usage generator
 */
public abstract class AbstractCommandGroupUsageGenerator<T> extends AbstractUsageGenerator implements
        CommandGroupUsageGenerator<T> {

    public AbstractCommandGroupUsageGenerator() {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR, false);
    }

    public AbstractCommandGroupUsageGenerator(Comparator<? super OptionMetadata> optionComparator,
            Comparator<? super CommandMetadata> commandComparator, boolean includeHidden) {
        super(optionComparator, commandComparator, includeHidden);
    }

    @Override
    public void usage(GlobalMetadata<T> global, CommandGroupMetadata group) throws IOException {
        usage(global, group, System.out);
    }
}