package io.airlift.airline.help;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.airlift.airline.model.CommandGroupMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.GlobalMetadata;
import io.airlift.airline.model.OptionMetadata;

/**
 * Abstract global usage generator
 */
public abstract class AbstractGlobalUsageGenerator extends AbstractUsageGenerator implements GlobalUsageGenerator {

    private final Comparator<? super CommandGroupMetadata> commandGroupComparator;

    public AbstractGlobalUsageGenerator() {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR,
                UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR);
    }

    public AbstractGlobalUsageGenerator(Comparator<? super OptionMetadata> optionComparator,
            Comparator<? super CommandMetadata> commandComparator,
            Comparator<? super CommandGroupMetadata> commandGroupComparator) {
        super(optionComparator, commandComparator);
        this.commandGroupComparator = commandGroupComparator;
    }

    @Override
    public void usage(GlobalMetadata global) throws IOException {
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