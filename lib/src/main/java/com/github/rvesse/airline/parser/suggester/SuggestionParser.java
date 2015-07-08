package com.github.rvesse.airline.parser.suggester;

import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.parser.AbstractCommandParser;
import com.github.rvesse.airline.parser.ParseState;

public class SuggestionParser<T> extends AbstractCommandParser<T> {

    public ParseState<T> parse(GlobalMetadata<T> metadata, Iterable<String> args) {
        return tryParse(metadata, args);
    }
}
