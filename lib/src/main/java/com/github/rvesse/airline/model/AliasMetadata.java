package com.github.rvesse.airline.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class AliasMetadata {

    private final String name;
    private final List<String> arguments;

    public AliasMetadata(String name, List<String> arguments) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(name) || !StringUtils.isWhitespace(name),
                "Alias name cannot be null/empty/whitespace");
        this.name = name;
        this.arguments = arguments != null ? ImmutableList.copyOf(arguments) : ImmutableList.<String> of();
    }

    public String getName() {
        return this.name;
    }

    public List<String> getArguments() {
        return this.arguments;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AliasMetadata { name = '").append(this.name).append("', ");
        builder.append("arguments = [");
        for (int i = 0; i < this.arguments.size(); i++) {
            if (i > 0)
                builder.append(", ");
            builder.append("'").append(this.arguments.get(i)).append("'");
        }
        builder.append("] }");
        return builder.toString();
    }
    
    public static Function<AliasMetadata, String> nameGetter()
    {
        return new Function<AliasMetadata, String>()
        {
            public String apply(AliasMetadata input)
            {
                return input.getName();
            }
        };
    }
}
