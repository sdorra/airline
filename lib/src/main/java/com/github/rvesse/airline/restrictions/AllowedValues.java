package com.github.rvesse.airline.restrictions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseOptionIllegalValueException;
import com.github.rvesse.airline.utils.predicates.ParsedOptionFinder;

public class AllowedValues extends AbstractRestriction {

    private final Set<String> allowedValues = new HashSet<String>();

    public AllowedValues(boolean ignoreCase, Locale locale, String... values) {
        if (locale == null)
            locale = Locale.ENGLISH;
        for (String value : values) {
            if (ignoreCase)
                value = value.toLowerCase(locale);
            allowedValues.add(value);
        }
    }

    @Override
    public <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        Collection<Pair<OptionMetadata, Object>> parsedValues = CollectionUtils.select(state.getParsedOptions(),
                new ParsedOptionFinder(option));
        if (parsedValues.isEmpty())
            return;

        if (!this.allowedValues.contains(value))
            throw new ParseOptionIllegalValueException(option.getTitle(), value, allowedValues);
    }

    // TODO Support validating arguments

}
