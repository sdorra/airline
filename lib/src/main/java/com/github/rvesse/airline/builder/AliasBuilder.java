package com.github.rvesse.airline.builder;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.model.AliasMetadata;
import com.google.common.base.Preconditions;

public class AliasBuilder<C> {
    private final String name;
    private final List<String> arguments = newArrayList();

    AliasBuilder(String name) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(name) && !StringUtils.isWhitespace(name),
                "Alias name cannot be null/empty/whitespace");
        this.name = name;
    }
    
    public AliasBuilder<C> withArgument(String arg) {
        Preconditions.checkNotNull(arg, "Alias argument cannot be null");
        arguments.add(arg);
        return this;
    }
    
    public AliasBuilder<C> withArguments(String... args) {
        for (String arg : args) {
            Preconditions.checkNotNull(arg, "Alias argument cannot be null");
            arguments.add(arg);
        }
        return this;
    }
    
    public AliasMetadata build() {
        return new AliasMetadata(name, arguments);
    }
}
