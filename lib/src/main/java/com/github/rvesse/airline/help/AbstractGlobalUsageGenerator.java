package com.github.rvesse.airline.help;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

/**
 * Abstract global usage generator
 */
public abstract class AbstractGlobalUsageGenerator<T> extends AbstractUsageGenerator implements GlobalUsageGenerator<T> {

    private final Comparator<? super CommandGroupMetadata> commandGroupComparator;

    public AbstractGlobalUsageGenerator() {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR,
                UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR, false);
    }

    public AbstractGlobalUsageGenerator(boolean includeHidden) {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR,
                UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR, includeHidden);
    }

    public AbstractGlobalUsageGenerator(Comparator<? super OptionMetadata> optionComparator,
            Comparator<? super CommandMetadata> commandComparator,
            Comparator<? super CommandGroupMetadata> commandGroupComparator, boolean includeHidden) {
        super(optionComparator, commandComparator, includeHidden);
        this.commandGroupComparator = commandGroupComparator;
    }

    @Override
    public void usage(GlobalMetadata<T> global) throws IOException {
        usage(global, System.out);
    }

    /**
     * Sorts the command groups assumign a non-null comparator was provided at
     * instantiation time
     * 
     * @param groups
     *            Command groups
     * @return Sorted command groups
     */
    protected List<CommandGroupMetadata> sortCommandGroups(List<CommandGroupMetadata> groups) {
        if (this.commandGroupComparator != null) {
            groups = new ArrayList<>(groups);
            Collections.sort(groups, this.commandGroupComparator);
        }
        return groups;
    }
}