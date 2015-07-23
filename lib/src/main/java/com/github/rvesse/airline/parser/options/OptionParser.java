package com.github.rvesse.airline.parser.options;

import java.util.List;

import org.apache.commons.collections4.iterators.PeekingIterator;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;

/**
 * Interface for option parsers
 *
 */
public interface OptionParser<T> {

    /**
     * Parses one/more options from the token stream
     * 
     * @param tokens
     *            Tokens
     * @param state
     *            Current parser state
     * @param allowedOptions
     *            Allowed options at this point of the parsing
     * @return New parser state, may return {@code null} if this parser could
     *         not parse the next token as an option
     */
    public abstract ParseState<T> parseOptions(PeekingIterator<String> tokens, ParseState<T> state,
            List<OptionMetadata> allowedOptions);
}
