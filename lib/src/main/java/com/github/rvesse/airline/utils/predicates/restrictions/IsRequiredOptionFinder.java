package com.github.rvesse.airline.utils.predicates.restrictions;

import org.apache.commons.collections4.Predicate;

import com.github.rvesse.airline.restrictions.IsRequiredRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;

public class IsRequiredOptionFinder implements Predicate<OptionRestriction> {

    @Override
    public boolean evaluate(OptionRestriction restriction) {
        return restriction instanceof IsRequiredRestriction;
    }

}
