package com.github.rvesse.airline.parser.options;

import static com.google.common.collect.Iterables.find;

import java.util.List;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.AbbreviatedOptionFinder;
import com.github.rvesse.airline.parser.ParseOptionIllegalValueException;
import com.github.rvesse.airline.parser.ParseState;
import com.google.common.base.Predicate;

/**
 * Abstract option parser that provides some useful helper methods to derived
 * classes
 */
public abstract class AbstractOptionParser implements OptionParser {

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
    protected final OptionMetadata findOption(ParseState state, List<OptionMetadata> options, final String name) {
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

        return find(options, findOptionPredicate, null);
    }

    /**
     * Checks for a valid value and throws an error if the value for the option
     * is restricted and not in the set of allowed values
     * 
     * @param option
     *            Option meta data
     * @param tokenStr
     *            Token string
     */
    protected final void checkValidValue(OptionMetadata option, String tokenStr) {
        if (option.getAllowedValues() == null)
            return;
        if (option.getAllowedValues().contains(tokenStr))
            return;
        throw new ParseOptionIllegalValueException(option.getTitle(), tokenStr, option.getAllowedValues());
    }
}