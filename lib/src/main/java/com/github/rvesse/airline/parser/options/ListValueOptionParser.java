package com.github.rvesse.airline.parser.options;

import java.util.List;

import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseOptionMissingValueException;
import com.github.rvesse.airline.parser.errors.ParseOptionUnexpectedException;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.PeekingIterator;

/**
 * An options parser that expects the name and value(s) to be white space
 * separated e.g. {@code --name value} but which allows for the values to be a
 * non-whitespace separated list
 * <p>
 * So for example {@code --name foo,bar} would be treated as the values
 * {@code foo} and {@code bar} passed to the {@code --name} option. This parser
 * differs from the {@link StandardOptionParser} in that the standard parser
 * would treat {@code foo,bar} as a single value passed to the name option. This
 * parser expects that the list it receives contains the correct number of items
 * for the arity of the option and if not produces an error
 * </p>
 * <p>
 * You can also omit the whitespace between the name and the value list when
 * using a single character name of the option similar to how the
 * {@link ClassicGetOptParser} works. For example {@code -nfoo,bar} is
 * equivalent to our previous example assuming that {@code -n} is an alternative
 * name for the same option as {@code --name}.
 * </p>
 * <p>
 * The default separator for values is {@code ,} but this can be configured as
 * desired.
 * </p>
 *
 */
public class ListValueOptionParser<T> extends AbstractOptionParser<T> {

    private static final char DEFAULT_SEPARATOR = ',';
    private final char separator;

    public ListValueOptionParser() {
        this(DEFAULT_SEPARATOR);
    }

    public ListValueOptionParser(char separator) {
        Preconditions.checkArgument(!Character.isWhitespace(separator),
                "List separator character cannot be a whitespace character");
        this.separator = separator;
    }

    protected final List<String> getValues(String list) {
        return Splitter.on(this.separator).splitToList(list);
    }

    @Override
    public ParseState<T> parseOptions(PeekingIterator<String> tokens, ParseState<T> state,
            List<OptionMetadata> allowedOptions) {
        String name = tokens.peek();
        boolean noSep = false;
        OptionMetadata option = findOption(state, allowedOptions, name);
        if (option == null) {
            // Check if we are looking at a maven style -Pa,b,c argument
            if (hasShortNamePrefix(name) && name.length() > 2) {
                String shortName = name.substring(0, 2);
                option = findOption(state, allowedOptions, shortName);
                noSep = option != null;
            }

            if (!noSep)
                return null;
        }

        tokens.next();
        state = state.pushContext(Context.OPTION).withOption(option);

        String list = noSep ? name.substring(2) : null;
        if (option.getArity() == 0) {
            // Zero arity option, consume token and continue
            state = state.withOptionValue(option, Boolean.TRUE).popContext();
        } else {
            if (list == null) {
                // Can't parse list value if there are no further tokens
                if (!tokens.hasNext())
                    return state;

                // Consume the value immediately, this option parser will now
                // either
                // succeed to parse the option or will error
                list = tokens.next();
            }

            // Parse value as a list
            List<String> listValues = getValues(list);
            if (listValues.size() < option.getArity())
                throw new ParseOptionMissingValueException(
                        "Too few option values received for option %s in list value '%s' (%d values expected)",
                        option.getTitle(), option.getOptions().iterator().next(), list, option.getArity());
            if (listValues.size() > option.getArity())
                throw new ParseOptionUnexpectedException(
                        "Too many option values received for option %s in list value '%s' (%d values expected)", option
                                .getOptions().iterator().next(), list, option.getArity());

            // Parse individual values and assign to option
            if (option.getArity() == 1) {
                // Arity 1 option
                checkValidValue(option, listValues.get(0));
                Object value = getTypeConverter(state).convert(option.getTitle(), option.getJavaType(),
                        listValues.get(0));
                state = state.withOptionValue(option, value).popContext();
            } else {
                // Arity > 1 option
                ImmutableList.Builder<Object> values = ImmutableList.builder();

                for (String value : listValues) {
                    checkValidValue(option, value);
                    values.add(getTypeConverter(state).convert(option.getTitle(), option.getJavaType(), value));
                }

                state = state.withOptionValue(option, values.build()).popContext();
            }
        }
        return state;
    }

}
