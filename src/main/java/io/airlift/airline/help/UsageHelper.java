package io.airlift.airline.help;

import com.google.common.collect.ComparisonChain;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

import java.util.Comparator;

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
}
