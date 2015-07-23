package com.github.rvesse.airline.restrictions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.utils.AirlineUtils;
import com.github.rvesse.airline.utils.predicates.ParsedOptionFinder;

public class AllowedValues extends AbstractRestriction {

    private final boolean ignoreCase;
    private final Set<String> allowedValues = new HashSet<String>();
    
    public AllowedValues(boolean ignoreCase, String... values) {
        this.ignoreCase = ignoreCase;
        this.allowedValues.addAll(AirlineUtils.arrayToList(values));
    }

    @Override
    public <T> void validate(ParseState<T> state, OptionMetadata option) {
        Collection<Pair<OptionMetadata, Object>> parsedValues = CollectionUtils.select(state.getParsedOptions(), new ParsedOptionFinder(option));
        if (parsedValues.isEmpty()) return;
        
        for (String )
    }

    @Override
    public <T> void validate(ParseState<T> state, ArgumentsMetadata arguments) {
        // TODO Auto-generated method stub
        super.validate(state, arguments);
    }
    
    
}
