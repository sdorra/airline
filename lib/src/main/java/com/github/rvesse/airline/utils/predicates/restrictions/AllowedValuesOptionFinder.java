package com.github.rvesse.airline.utils.predicates.restrictions;

import org.apache.commons.collections4.Predicate;

import com.github.rvesse.airline.restrictions.AllowedRawValuesRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;

public class AllowedValuesOptionFinder implements Predicate<OptionRestriction> {

    @Override
    public boolean evaluate(OptionRestriction restriction) {
        return restriction instanceof AllowedRawValuesRestriction;
    }

}
