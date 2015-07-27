package com.github.rvesse.airline.utils.predicates.restrictions;

import org.apache.commons.collections4.Predicate;

import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.AllowedRawValuesRestriction;

public class AllowedValuesOptionFinder implements Predicate<OptionRestriction> {

    @Override
    public boolean evaluate(OptionRestriction restriction) {
        return restriction instanceof AllowedRawValuesRestriction;
    }

}
