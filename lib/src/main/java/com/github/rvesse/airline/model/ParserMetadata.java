package com.github.rvesse.airline.model;

import java.util.List;

import com.github.rvesse.airline.CommandFactory;
import com.github.rvesse.airline.DefaultCommandFactory;
import com.github.rvesse.airline.TypeConverter;
import com.github.rvesse.airline.DefaultTypeConverter;
import com.github.rvesse.airline.parser.options.OptionParser;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Represents metadata about the parser configuration
 */
public class ParserMetadata<T> {

    private final boolean allowAbbreviatedCommands, allowAbbreviatedOptions, aliasesOverrideBuiltIns;
    private final List<OptionParser<T>> optionParsers;
    private final List<AliasMetadata> aliases;
    private final TypeConverter typeConverter;
    private final CommandFactory<T> commandFactory;

    public ParserMetadata(CommandFactory<T> commandFactory, List<OptionParser<T>> optionParsers,
            TypeConverter typeConverter, boolean allowAbbreviateCommands, boolean allowAbbreviatedOptions,
            List<AliasMetadata> aliases, boolean aliasesOverrideBuiltIns) {
        Preconditions.checkNotNull(optionParsers, "optionParsers cannot be null");
        Preconditions.checkNotNull(aliases, "aliases cannot be null");

        // Command parsing
        this.commandFactory = commandFactory != null ? commandFactory : new DefaultCommandFactory<T>();
        this.allowAbbreviatedCommands = allowAbbreviateCommands;

        // Option Parsing
        this.typeConverter = typeConverter != null ? typeConverter : new DefaultTypeConverter();
        this.optionParsers = ImmutableList.copyOf(optionParsers);
        this.allowAbbreviatedOptions = allowAbbreviatedOptions;

        // Aliases
        this.aliases = ImmutableList.copyOf(aliases);
        this.aliasesOverrideBuiltIns = aliasesOverrideBuiltIns;

    }

    /**
     * Gets the command factory to use
     * 
     * @return Command factory
     */
    public CommandFactory<T> getCommandFactory() {
        return commandFactory;
    }

    /**
     * Gets the type converter to use
     * 
     * @return Type converter
     */
    public TypeConverter getTypeConverter() {
        return typeConverter;
    }

    /**
     * Gets the defined command aliases
     * 
     * @return Aliases
     */
    public List<AliasMetadata> getAliases() {
        return aliases;
    }

    /**
     * Gets whether aliases can override built-in commands
     * 
     * @return True if they can override, false otherwise
     */
    public boolean aliasesOverrideBuiltIns() {
        return aliasesOverrideBuiltIns;
    }

    /**
     * Gets the option parsers to use
     * 
     * @return Option parsers
     */
    public List<OptionParser<T>> getOptionParsers() {
        return optionParsers;
    }

    /**
     * Gets whether command/group name abbreviation is allowed
     * 
     * @return True if allowed, false otherwise
     */
    public boolean allowsAbbreviatedCommands() {
        return allowAbbreviatedCommands;
    }

    /**
     * Gets whether option name abbreviation is allowed
     * 
     * @return True if allowed, false otherwise
     */
    public boolean allowsAbbreviatedOptions() {
        return allowAbbreviatedOptions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParserMetadata {");
        sb.append("commandFactory=").append(commandFactory.getClass().getCanonicalName());
        sb.append(", allowAbbreviatedCommands=").append(allowAbbreviatedCommands);
        sb.append(", optionParsers=").append(optionParsers);
        sb.append(", typeConverter=").append(typeConverter.getClass().getCanonicalName());
        sb.append(", allowAbbreviatedOptions=").append(allowAbbreviatedOptions);
        sb.append(", aliases=").append(aliases);
        sb.append(", aliasesOverrideBuiltIns=").append(aliasesOverrideBuiltIns);
        sb.append("}");
        return sb.toString();
    }
}
