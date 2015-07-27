package com.github.rvesse.airline.utils.predicates.restrictions;

import org.apache.commons.collections4.Predicate;

import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.common.IsRequiredRestriction;

public class IsRequiredArgumentFinder implements Predicate<ArgumentsRestriction> {

    @Override
    public boolean evaluate(ArgumentsRestriction restriction) {
        return restriction instanceof IsRequiredRestriction;
    }

}
