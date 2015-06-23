package com.github.rvesse.airline.parser.options;

import static com.google.common.collect.Iterables.find;

import java.util.List;
import java.util.regex.Pattern;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.AbbreviatedOptionFinder;
import com.github.rvesse.airline.parser.AbstractParser;
import com.github.rvesse.airline.parser.ParseState;
import com.google.common.base.Predicate;

/**
 * Abstract option parser that provides some useful helper methods to derived
 * classes
 */
public abstract class AbstractOptionParser<T> extends AbstractParser<T> implements OptionParser<T> {

    private static final Pattern SHORT_OPTIONS_PREFIX = Pattern.compile("-[^-].*");

    /**
     * Tries to find an option with the given name
     * 
     * @param state
     *            Current parser state
     * @param options
     *            Allowed options
     * @param name
     *            Name
     * @return Option if found, {@code null} otherwise
     */
    protected final OptionMetadata findOption(ParseState<T> state, List<OptionMetadata> options, final String name) {
        return findOption(state, options, name, null);
    }

    /**
     * Tries to find an option with the given name
     * 
     * @param state
     *            Current parser state
     * @param options
     *            Allowed options
     * @param name
     *            Name
     * @param defaultValue
     *            Default value to return if nothing found
     * @return Option if found, {@code defaultValue} otherwise
     */
    protected final OptionMetadata findOption(ParseState<T> state, List<OptionMetadata> options, final String name,
            OptionMetadata defaultValue) {
        Predicate<? super OptionMetadata> findOptionPredicate;
        if (state.getGlobal() != null && state.getGlobal().getParserConfiguration().allowsAbbreviatedOptions()) {
            findOptionPredicate = new AbbreviatedOptionFinder(name, options);
        } else {
            findOptionPredicate = new Predicate<OptionMetadata>() {

                @Override
                public boolean apply(OptionMetadata op) {
                    return op.getOptions().contains(name);
                }
            };
        }

        return find(options, findOptionPredicate, defaultValue);
    }

    protected boolean hasShortNamePrefix(String name) {
        return SHORT_OPTIONS_PREFIX.matcher(name).matches();
    }
}