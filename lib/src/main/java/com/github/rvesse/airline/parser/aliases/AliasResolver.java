package com.github.rvesse.airline.parser.aliases;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.find;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.github.rvesse.airline.model.AliasMetadata;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.parser.AbstractParser;
import com.github.rvesse.airline.parser.ParseState;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

public class AliasResolver<T> extends AbstractParser<T> {

    public PeekingIterator<String> resolveAliases(PeekingIterator<String> tokens, ParseState<T> state) {
        Predicate<? super CommandGroupMetadata> findGroupPredicate;
        Predicate<? super CommandMetadata> findCommandPredicate;

        // Nothing to do if no further tokens
        if (!tokens.hasNext())
            return tokens;

        // Nothing to do if no aliases defined
        if (state.getParserConfiguration().getAliases().size() == 0)
            return tokens;

        Set<String> aliasesSeen = new TreeSet<String>();

        do {
            // Try to find an alias
            AliasMetadata alias = find(state.getParserConfiguration().getAliases(),
                    compose(equalTo(tokens.peek()), AliasMetadata.nameGetter()), null);

            // Nothing further to do if no aliases found
            if (alias == null)
                return tokens;
            
            // Check for circular references
            if (!aliasesSeen.add(alias.getName())) {
                
            }

            // Can we override built-ins?
            if (!state.getParserConfiguration().aliasesOverrideBuiltIns()) {
                // If not we must check we don't have a default
                // group/command with the same name as otherwise that
                // would take precedence
                if (state.getGlobal() != null) {
                    GlobalMetadata<T> metadata = state.getGlobal();
                    findGroupPredicate = compose(equalTo(tokens.peek()), CommandGroupMetadata.nameGetter());
                    findCommandPredicate = compose(equalTo(tokens.peek()), CommandMetadata.nameGetter());
                    if (find(metadata.getCommandGroups(), findGroupPredicate, null) != null
                            || find(metadata.getDefaultGroupCommands(), findCommandPredicate, null) != null)
                        return tokens;
                }
            }

            // Discard the alias token
            tokens.next();

            // Apply the alias
            List<String> newParams = new ArrayList<String>();
            List<String> remainingParams = new ArrayList<String>();
            while (tokens.hasNext()) {
                remainingParams.add(tokens.next());
            }

            // Process alias arguments
            Set<Integer> used = new TreeSet<Integer>();
            for (String arg : alias.getArguments()) {
                if (arg.startsWith("$")) {
                    // May be a positional parameter
                    try {
                        int num = Integer.parseInt(arg.substring(1));
                        num--;

                        if (num >= 0 && num < remainingParams.size()) {
                            // Valid positional parameter
                            newParams.add(remainingParams.get(num));
                            used.add(num);
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore - the number was invalid so we'll
                        // treat it as an ordinary parameter
                    }
                }

                // Some other parameter
                newParams.add(arg);
            }

            // Remove used positional parameters
            int removed = 0;
            for (int pos : used) {
                remainingParams.remove(pos - removed);
                removed++;
            }

            // Pass through any remaining parameters
            for (String arg : remainingParams) {
                newParams.add(arg);
            }

            // Prepare a new tokens iterator
            tokens = Iterators.peekingIterator(newParams.iterator());
        } while (state.getParserConfiguration().aliasesMayChain());
        
        return tokens;
    }
}
