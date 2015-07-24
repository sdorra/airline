package com.github.rvesse.airline.restrictions;

import java.util.Collection;
import java.util.Comparator;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseOptionOutOfRangeException;
import com.github.rvesse.airline.utils.predicates.ParsedOptionFinder;

/**
 * A restriction that requires the value (after type conversion) to be within a
 * given range
 *
 */
public class Range extends AbstractRestriction {

    private final Object min, max;
    private final boolean minInclusive, maxInclusive;
    private final Comparator<Object> comparator;

    public Range(Object min, boolean minInclusive, Object max, boolean maxInclusive, Comparator<Object> comparator) {
        if (comparator == null)
            throw new NullPointerException("comparator cannot be null");
        this.min = min;
        this.minInclusive = minInclusive;
        this.max = max;
        this.maxInclusive = maxInclusive;
        this.comparator = comparator;

        // Validate the range
        if (min != null && max != null) {
            int rangeComparison = this.comparator.compare(min, max);
            if (rangeComparison > 0)
                throw new IllegalArgumentException("min is greater than max");
            if (rangeComparison == 0 && (!minInclusive || !maxInclusive))
                throw new IllegalArgumentException(
                        "min and max are same but either minInclusive or maxInclusive was false");
        }
    }

    @Override
    public <T> void postValidate(ParseState<T> state, OptionMetadata option) {
        Collection<Pair<OptionMetadata, Object>> parsedOptions = CollectionUtils.select(state.getParsedOptions(),
                new ParsedOptionFinder(option));

        if (parsedOptions.isEmpty())
            return;

        for (Pair<OptionMetadata, Object> parsedOption : parsedOptions) {
            if (!inRange(parsedOption.getRight()))
                throw new ParseOptionOutOfRangeException(option.getTitle(), parsedOption.getRight(), parsedOptions,
                        minInclusive, parsedOption, maxInclusive);
        }
    }

    protected boolean inRange(Object value) {
        if (this.min != null) {
            int minComparison = this.comparator.compare(this.min, value);
            if (minComparison == 0)
                return this.minInclusive;
            if (minComparison > 0)
                return false;
        }
        if (this.max != null) {
            int maxComparison = this.comparator.compare(value, this.max);
            if (maxComparison == 0)
                return this.maxInclusive;
            if (maxComparison > 0)
                return false;
        }
        return true;
    }

}
