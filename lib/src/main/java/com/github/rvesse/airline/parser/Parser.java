package com.github.rvesse.airline.parser;

import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.TypeConverter;
import com.github.rvesse.airline.model.AliasMetadata;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.options.ClassicGetOptParser;
import com.github.rvesse.airline.parser.options.LongGetOptParser;
import com.github.rvesse.airline.parser.options.OptionParser;
import com.github.rvesse.airline.parser.options.StandardOptionParser;
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

public class Parser {
    private static final OptionParser CLASSIC_GET_OPT_PARSER = new ClassicGetOptParser();
    private static final OptionParser LONG_GET_OPT_PARSER = new LongGetOptParser();
    private static final OptionParser STANDARD_PARSER = new StandardOptionParser();

    // global> (option value*)* (group (option value*)*)? (command (option
    // value* | arg)* '--'? args*)?
    public ParseState parse(GlobalMetadata metadata, String... params) {
        return parse(metadata, ImmutableList.copyOf(params));
    }

    public ParseState parse(GlobalMetadata metadata, Iterable<String> params) {
        PeekingIterator<String> tokens = Iterators.peekingIterator(params.iterator());

        ParseState state = ParseState.newInstance().pushContext(Context.GLOBAL).withGlobal(metadata);

        // Define needed predicates
        Predicate<? super CommandGroupMetadata> findGroupPredicate;
        Predicate<? super CommandMetadata> findCommandPredicate;

        // Parse global options
        state = parseOptions(tokens, state, metadata.getOptions());

        // Check if we got an alias
        if (tokens.hasNext()) {
            if (metadata.getAliases().size() > 0) {
                AliasMetadata alias = find(metadata.getAliases(),
                        compose(equalTo(tokens.peek()), AliasMetadata.nameGetter()), null);
                if (alias != null) {
                    if (!metadata.aliasesOverrideBuiltIns()) {
                        // Check we don't have a default group/command with the
                        // same
                        // name as otherwise that would take precedence
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

        // Parse group
        if (tokens.hasNext()) {
            //@formatter:off
            findGroupPredicate = (Predicate<? super CommandGroupMetadata>) (metadata != null && metadata.allowsAbbreviatedCommands() 
                                 ? new AbbreviatedGroupFinder(tokens.peek(), metadata.getCommandGroups()) 
                                 : compose(equalTo(tokens.peek()), CommandGroupMetadata.nameGetter()));
            //@formatter:on
            CommandGroupMetadata group = find(metadata.getCommandGroups(), findGroupPredicate, null);
            if (group != null) {
                tokens.next();
                state = state.withGroup(group).pushContext(Context.GROUP);

                state = parseOptions(tokens, state, state.getGroup().getOptions());
            }
        }

        // parse command
        List<CommandMetadata> expectedCommands = metadata.getDefaultGroupCommands();
        if (state.getGroup() != null) {
            expectedCommands = state.getGroup().getCommands();
        }

        if (tokens.hasNext()) {
            //@formatter:off
            findCommandPredicate = (Predicate<? super CommandMetadata>) (metadata != null && metadata.allowsAbbreviatedCommands() 
                                   ? new AbbreviatedCommandFinder(tokens.peek(), expectedCommands)
                                   : compose(equalTo(tokens.peek()), CommandMetadata.nameGetter()));
            //@formatter:on
            CommandMetadata command = find(expectedCommands, findCommandPredicate, state.getGroup() != null ? state
                    .getGroup().getDefaultCommand() : null);

            boolean usingDefault = false;
            if (command == null && state.getGroup() == null && metadata.getDefaultCommand() != null) {
                usingDefault = true;
                command = metadata.getDefaultCommand();
            }

            if (command == null) {
                while (tokens.hasNext()) {
                    state = state.withUnparsedInput(tokens.next());
                }
            } else {
                if (tokens.peek().equals(command.getName()) || (!usingDefault && metadata.allowsAbbreviatedCommands())) {
                    tokens.next();
                }

                state = state.withCommand(command).pushContext(Context.COMMAND);

                while (tokens.hasNext()) {
                    state = parseOptions(tokens, state, command.getCommandOptions());

                    state = parseArgs(state, tokens, command.getArguments(), command.getDefaultOption());
                }
            }
        }

        return state;
    }

    public ParseState parseCommand(CommandMetadata command, Iterable<String> params) {
        PeekingIterator<String> tokens = Iterators.peekingIterator(params.iterator());
        ParseState state = ParseState.newInstance().pushContext(Context.GLOBAL).withCommand(command);

        while (tokens.hasNext()) {
            state = parseOptions(tokens, state, command.getCommandOptions());

            state = parseArgs(state, tokens, command.getArguments(), command.getDefaultOption());
        }
        return state;
    }

    private ParseState parseOptions(PeekingIterator<String> tokens, ParseState state,
            List<OptionMetadata> allowedOptions) {

        // Get the option parsers in use
        List<OptionParser> optionParsers;
        if (state.getGlobal() != null) {
            // Using CLI defined set
            optionParsers = state.getGlobal().getOptionParsers();
        } else {
            // Using default set
            optionParsers = new ArrayList<OptionParser>();
            optionParsers.add(STANDARD_PARSER);
            optionParsers.add(LONG_GET_OPT_PARSER);
            optionParsers.add(CLASSIC_GET_OPT_PARSER);
        }

        while (tokens.hasNext()) {
            //
            // Try to parse next option(s) using different styles. If code
            // matches it returns the next parser state, otherwise it returns
            // null.

            // Try each option parser in turn
            boolean matched = false;
            for (OptionParser optionParser : optionParsers) {
                ParseState nextState = optionParser.parseOptions(tokens, state, allowedOptions);

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

    /**
     * Checks for a valid value and throws an error if the value for the option
     * is restricted and not in the set of allowed values
     * 
     * @param option
     *            Option meta data
     * @param tokenStr
     *            Token string
     */
    private void checkValidValue(OptionMetadata option, String tokenStr) {
        if (option.getAllowedValues() == null)
            return;
        if (option.getAllowedValues().contains(tokenStr))
            return;
        throw new ParseOptionIllegalValueException(option.getTitle(), tokenStr, option.getAllowedValues());
    }

    private ParseState parseArgs(ParseState state, PeekingIterator<String> tokens, ArgumentsMetadata arguments,
            OptionMetadata defaultOption) {
        if (tokens.hasNext()) {
            if (tokens.peek().equals("--")) {
                state = state.pushContext(Context.ARGS);
                tokens.next();

                // Consume all remaining tokens as arguments
                // Default option can't possibly apply at this point because we
                // saw the -- separator
                while (tokens.hasNext()) {
                    state = parseArg(state, tokens, arguments, null);
                }
            } else {
                state = parseArg(state, tokens, arguments, defaultOption);
            }
        }

        return state;
    }

    private ParseState parseArg(ParseState state, PeekingIterator<String> tokens, ArgumentsMetadata arguments,
            OptionMetadata defaultOption) {
        if (arguments != null) {
            // Enforce maximum arity on arguments
            if (arguments.getArity() > 0 && state.getParsedArguments().size() == arguments.getArity()) {
                throw new ParseTooManyArgumentsException(
                        "Too many arguments, at most %d arguments are permitted but extra argument %s was encountered",
                        arguments.getArity(), tokens.peek());
            }

            // Argument
            state = state.withArgument(TypeConverter.newInstance().convert(arguments.getTitle().get(0),
                    arguments.getJavaType(), tokens.next()));
        } else if (defaultOption != null) {
            // Default Option
            state = state.withOption(defaultOption);
            String tokenStr = tokens.next();
            checkValidValue(defaultOption, tokenStr);
            Object value = TypeConverter.newInstance().convert(defaultOption.getTitle(), defaultOption.getJavaType(),
                    tokenStr);
            state = state.withOptionValue(defaultOption, value).popContext();
        } else {
            // Unparsed input
            state = state.withUnparsedInput(tokens.next());
        }
        return state;
    }
}
