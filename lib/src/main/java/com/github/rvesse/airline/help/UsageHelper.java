package com.github.rvesse.airline.help;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.google.common.collect.ComparisonChain;

import java.util.Comparator;
import java.util.Map.Entry;

public class UsageHelper {
    /**
     * Default comparator for options
     * <p>
     * Compares against the user readable portion of the option name omitting
     * any leading {@code -} characters
     * </p>
     */
    public static final Comparator<OptionMetadata> DEFAULT_OPTION_COMPARATOR = new Comparator<OptionMetadata>() {
        @Override
        public int compare(OptionMetadata o1, OptionMetadata o2) {
            String option1 = o1.getOptions().iterator().next();
            option1 = option1.replaceFirst("^-+", "");

            String option2 = o2.getOptions().iterator().next();
            option2 = option2.replaceFirst("^-+", "");

            return ComparisonChain.start().compare(option1.toLowerCase(), option2.toLowerCase())
                    .compare(option2, option1) // print lower case letters
                                               // before upper case
                    .compare(System.identityHashCode(o1), System.identityHashCode(o2)).result();
        }
    };

    /**
     * Default comparator for commands
     * <p>
     * Compares by alphabetical ordering
     * </p>
     */
    public static final Comparator<CommandMetadata> DEFAULT_COMMAND_COMPARATOR = new Comparator<CommandMetadata>() {
        @Override
        public int compare(CommandMetadata o1, CommandMetadata o2) {
            return ComparisonChain.start().compare(o1.getName().toLowerCase(), o2.getName().toLowerCase())
                    .compare(o2.getName(), o1.getName()) // print lower case
                                                         // letters before upper
                                                         // case
                    .compare(System.identityHashCode(o1), System.identityHashCode(o2)).result();
        }
    };

    public static final Comparator<CommandGroupMetadata> DEFAULT_COMMAND_GROUP_COMPARATOR = new Comparator<CommandGroupMetadata>() {
        @Override
        public int compare(CommandGroupMetadata o1, CommandGroupMetadata o2) {
            return ComparisonChain.start().compare(o1.getName().toLowerCase(), o2.getName().toLowerCase())
                    .compare(o2.getName(), o1.getName())
                    .compare(System.identityHashCode(o1), System.identityHashCode(o2)).result();
        }
    };

    /**
     * Default comparator for exit codes
     * <p>
     * Compares by numerical sorting on the exit codes and then alphabetical
     * sorting on the descriptions
     * </p>
     */
    public static final Comparator<Entry<Integer, String>> DEFAULT_EXIT_CODE_COMPARATOR = new Comparator<Entry<Integer, String>>() {
        @Override
        public int compare(Entry<Integer, String> o1, Entry<Integer, String> o2) {
            return ComparisonChain.start().compare(o1.getKey(), o2.getKey()).compare(o1.getValue(), o2.getValue())
                    .compare(System.identityHashCode(o1), System.identityHashCode(o2)).result();
        }
    };
}
