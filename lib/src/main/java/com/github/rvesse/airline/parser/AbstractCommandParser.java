package com.github.rvesse.airline.parser;

import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.model.AliasMetadata;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.errors.ParseTooManyArgumentsException;
import com.github.rvesse.airline.parser.options.OptionParser;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.find;

/**
 * Abstract implementation of a parser for commands that can cope with both CLI
 * and Single Command parsing
 *
 * @param <T>
 *            Command type
 */
public abstract class AbstractCommandParser<T> extends AbstractParser<T> {

    /**
     * Tries to parse the arguments
     * 
     * @param metadata
     *            Global Metadata
     * @param args
     *            Arguments
     * @return Parser State
     */
    protected ParseState<T> tryParse(GlobalMetadata<T> metadata, String... args) {
        return tryParse(metadata, ImmutableList.copyOf(args));
    }

    /**
     * Tries to parse the arguments
     * 
     * @param metadata
     *            Global Metadata
     * @param args
     *            Arguments
     * @return Parser State
     */
    protected ParseState<T> tryParse(GlobalMetadata<T> metadata, Iterable<String> args) {
        PeekingIterator<String> tokens = Iterators.peekingIterator(args.iterator());

        //@formatter:off
        ParseState<T> state = ParseState.<T> newInstance()
                                        .pushContext(Context.GLOBAL)
                                        .withGlobal(metadata);
        //@formatter:on

        // Parse global options
        state = parseOptions(tokens, state, metadata.getOptions());

        // Apply aliases
        tokens = applyAliases(metadata, tokens, state);

        // Parse group
        state = parseGroup(tokens, state);

        // parse command
        state = parseCommand(tokens, state);

        return state;
    }

    /**
     * Tries to parse the arguments
     * 
     * @param parserConfig
     *            Parser Configuration
     * @param command
     *            Command metadata
     * @param args
     *            Arguments
     * @return Parser State
     */
    protected ParseState<T> tryParse(ParserMetadata<T> parserConfig, CommandMetadata command, Iterable<String> args) {
        PeekingIterator<String> tokens = Iterators.peekingIterator(args.iterator());
        //@formatter:off
        ParseState<T> state = ParseState.<T> newInstance()
                                        .pushContext(Context.GLOBAL)
                                        .withConfiguration(parserConfig)
                                        .withCommand(command)
                                        .pushContext(Context.COMMAND);
        //@formatter:off

        state = parseCommandOptionsAndArguments(tokens, state, command);
        return state;
    }

    protected ParseState<T> parseCommand(PeekingIterator<String> tokens, ParseState<T> state) {
        Predicate<? super CommandMetadata> findCommandPredicate;
        List<CommandMetadata> expectedCommands = state.getGlobal().getDefaultGroupCommands();
        if (state.getGroup() != null) {
            expectedCommands = state.getGroup().getCommands();
        }

        if (tokens.hasNext()) {
            //@formatter:off
            findCommandPredicate = (Predicate<? super CommandMetadata>) (state.getParserConfiguration().allowsAbbreviatedCommands() 
                                   ? new AbbreviatedCommandFinder(tokens.peek(), expectedCommands)
                                   : compose(equalTo(tokens.peek()), CommandMetadata.nameGetter()));
            //@formatter:on
            CommandMetadata command = find(expectedCommands, findCommandPredicate, state.getGroup() != null ? state
                    .getGroup().getDefaultCommand() : null);

            boolean usingDefault = false;
            if (command == null && state.getGroup() == null && state.getGlobal().getDefaultCommand() != null) {
                usingDefault = true;
                command = state.getGlobal().getDefaultCommand();
            }

            if (command == null) {
                while (tokens.hasNext()) {
                    state = state.withUnparsedInput(tokens.next());
                }
            } else {
                if (tokens.peek().equals(command.getName())
                        || (!usingDefault && state.getParserConfiguration().allowsAbbreviatedCommands())) {
                    tokens.next();
                }

                state = state.withCommand(command).pushContext(Context.COMMAND);

                state = parseCommandOptionsAndArguments(tokens, state, command);
            }
        }
        return state;
    }

    protected ParseState<T> parseCommandOptionsAndArguments(PeekingIterator<String> tokens, ParseState<T> state,
            CommandMetadata command) {
        while (tokens.hasNext()) {
            state = parseOptions(tokens, state, command.getCommandOptions());

            state = parseArgs(state, tokens, command.getArguments(), command.getDefaultOption());
        }
        return state;
    }

    protected ParseState<T> parseGroup(PeekingIterator<String> tokens, ParseState<T> state) {
        Predicate<? super CommandGroupMetadata> findGroupPredicate;
        if (tokens.hasNext()) {
            //@formatter:off
            findGroupPredicate = (Predicate<? super CommandGroupMetadata>) (state.getParserConfiguration().allowsAbbreviatedCommands() 
                                 ? new AbbreviatedGroupFinder(tokens.peek(), state.getGlobal().getCommandGroups()) 
                                 : compose(equalTo(tokens.peek()), CommandGroupMetadata.nameGetter()));
            //@formatter:on
            CommandGroupMetadata group = find(state.getGlobal().getCommandGroups(), findGroupPredicate, null);
            if (group != null) {
                tokens.next();
                state = state.withGroup(group).pushContext(Context.GROUP);

                state = parseOptions(tokens, state, state.getGroup().getOptions());
            }
        }
        return state;
    }

    protected PeekingIterator<String> applyAliases(GlobalMetadata<T> metadata, PeekingIterator<String> tokens,
            ParseState<T> state) {
        Predicate<? super CommandGroupMetadata> findGroupPredicate;
        Predicate<? super CommandMetadata> findCommandPredicate;
        // Check if we got an alias
        if (tokens.hasNext()) {
            if (state.getParserConfiguration().getAliases().size() > 0) {
                AliasMetadata alias = find(state.getParserConfiguration().getAliases(),
                        compose(equalTo(tokens.peek()), AliasMetadata.nameGetter()), null);
                if (alias != null) {
                    if (!state.getParserConfiguration().aliasesOverrideBuiltIns()) {
                        // Check we don't have a default group/command with the
                        // same name as otherwise that would take precedence
                        findGroupPredicate = compose(equalTo(tokens.peek()), CommandGroupMetadata.nameGetter());
                        findCommandPredicate = compose(equalTo(tokens.peek()), CommandMetadata.nameGetter());
                        if (find(metadata.getCommandGroups(), findGroupPredicate, null) != null
                                || find(metadata.getDefaultGroupCommands(), findCommandPredicate, null) != null)
                            alias = null;
                    }

                    // Apply the alias
                    if (alias != null) {
                        // Discard the alias
                        tokens.next();

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
                    }
                }
            }
        }
        return tokens;
    }

    private ParseState<T> parseOptions(PeekingIterator<String> tokens, ParseState<T> state,
            List<OptionMetadata> allowedOptions) {

        // Get the option parsers in use
        List<OptionParser<T>> optionParsers = state.getParserConfiguration().getOptionParsers();

        while (tokens.hasNext()) {
            // Try to parse next option(s) using different styles. If code
            // matches it returns the next parser state, otherwise it returns
            // null.

            // Try each option parser in turn
            boolean matched = false;
            for (OptionParser<T> optionParser : optionParsers) {
                ParseState<T> nextState = optionParser.parseOptions(tokens, state, allowedOptions);

                if (nextState != null) {
                    // If the current parser matched an option this token is
                    // processed and we don't need to consider other parsers
                    state = nextState;
                    matched = true;
                    break;
                }
            }

            // Parsed an option so continue parsing options
            if (matched)
                continue;

            // Otherwise did not match an option so parse no further options
            break;
        }

        return state;
    }

    private ParseState<T> parseArgs(ParseState<T> state, PeekingIterator<String> tokens, ArgumentsMetadata arguments,
            OptionMetadata defaultOption) {
        String sep = state.getParserConfiguration().getArgumentsSeparator();

        if (tokens.hasNext()) {
            if (tokens.peek().equals(sep)) {
                state = state.pushContext(Context.ARGS);
                tokens.next();

                // Consume all remaining tokens as arguments
                // Default option can't possibly apply at this point because we
                // saw the arguments separator
                while (tokens.hasNext()) {
                    state = parseArg(state, tokens, arguments, null);
                }
            } else {
                state = parseArg(state, tokens, arguments, defaultOption);
            }
        }

        return state;
    }

    private ParseState<T> parseArg(ParseState<T> state, PeekingIterator<String> tokens, ArgumentsMetadata arguments,
            OptionMetadata defaultOption) {
        if (arguments != null) {
            // Enforce maximum arity on arguments
            if (arguments.getArity() > 0 && state.getParsedArguments().size() == arguments.getArity()) {
                throw new ParseTooManyArgumentsException(
                        "Too many arguments, at most %d arguments are permitted but extra argument %s was encountered",
                        arguments.getArity(), tokens.peek());
            }

            // Argument
            state = state.withArgument(getTypeConverter(state).convert(arguments.getTitle().get(0),
                    arguments.getJavaType(), tokens.next()));
        } else if (defaultOption != null) {
            // Default Option
            state = state.pushContext(Context.OPTION).withOption(defaultOption);
            String tokenStr = tokens.next();
            checkValidValue(defaultOption, tokenStr);
            Object value = getTypeConverter(state).convert(defaultOption.getTitle(), defaultOption.getJavaType(),
                    tokenStr);
            state = state.withOptionValue(defaultOption, value).popContext();
        } else {
            // Unparsed input
            state = state.withUnparsedInput(tokens.next());
        }
        return state;
    }
}
