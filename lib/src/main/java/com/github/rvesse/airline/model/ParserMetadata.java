package com.github.rvesse.airline.model;

import java.util.List;

import com.github.rvesse.airline.parser.options.OptionParser;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Represents metadata about the parser configuration
 */
public class ParserMetadata {

    private final boolean allowAbbreviatedCommands, allowAbbreviatedOptions, aliasesOverrideBuiltIns;
    private final List<OptionParser> optionParsers;
    private final List<AliasMetadata> aliases;

    public ParserMetadata(List<OptionParser> optionParsers, boolean allowAbbreviateCommands,
            boolean allowAbbreviatedOptions, List<AliasMetadata> aliases, boolean aliasesOverrideBuiltIns) {
        Preconditions.checkNotNull(optionParsers, "optionParsers cannot be null");
        Preconditions.checkNotNull(aliases, "aliases cannot be null");

        this.optionParsers = ImmutableList.copyOf(optionParsers);
        this.allowAbbreviatedCommands = allowAbbreviateCommands;
        this.allowAbbreviatedOptions = allowAbbreviatedOptions;
        this.aliases = ImmutableList.copyOf(aliases);
        this.aliasesOverrideBuiltIns = aliasesOverrideBuiltIns;
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
    public List<OptionParser> getOptionParsers() {
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
        sb.append(", optionParsers=").append(optionParsers);
        sb.append(", allowAbbreviatedCommands=").append(allowAbbreviatedCommands);
        sb.append(", allowAbbreviatedOptions=").append(allowAbbreviatedOptions);
        sb.append(", aliases=").append(aliases);
        sb.append(", aliasesOverrideBuiltIns=").append(aliasesOverrideBuiltIns);
        sb.append("}");
        return sb.toString();
    }
}
