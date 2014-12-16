package io.airlift.airline.help;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

import javax.annotation.Nullable;

/**
 * Abstract command usage generator
 * 
 */
public abstract class AbstractCommandUsageGenerator implements CommandUsageGenerator {

    protected final Comparator<? super OptionMetadata> optionComparator;

    public AbstractCommandUsageGenerator() {
        this(UsageHelper.DEFAULT_OPTION_COMPARATOR);
    }

    public AbstractCommandUsageGenerator(Comparator<? super OptionMetadata> optionComparator) {
        this.optionComparator = optionComparator;
    }

    @Override
    public void usage(@Nullable String programName, @Nullable String groupName, String commandName,
            CommandMetadata command) throws IOException {
        usage(programName, groupName, commandName, command, System.out);
    }

    /**
     * Sorts the options assuming a non-null comparator was provided at
     * instantiation time
     * 
     * @param options
     *            Options
     * @return Sorted options
     */
    protected List<OptionMetadata> sortOptions(List<OptionMetadata> options) {
        if (optionComparator != null) {
            options = new ArrayList<OptionMetadata>(options);
            Collections.sort(options, optionComparator);
        }
        return options;
    }

    protected final String htmlize(final String theStr) {
        return theStr.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>");
    }
}