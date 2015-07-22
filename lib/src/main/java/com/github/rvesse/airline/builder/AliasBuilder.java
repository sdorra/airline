package com.github.rvesse.airline.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.model.AliasMetadata;

public class AliasBuilder<C> {
    private final String name;
    private final List<String> arguments = new ArrayList<String>();

    AliasBuilder(String name) {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Alias name cannot be null/empty/whitespace");
        this.name = name;
    }

    public AliasBuilder<C> withArgument(String arg) {
        if (StringUtils.isEmpty(arg))
            throw new IllegalArgumentException("Alias argument cannot be null");
        arguments.add(arg);
        return this;
    }

    public AliasBuilder<C> withArguments(String... args) {
        for (String arg : args) {
            if (arg == null)
                throw new NullPointerException("Alias argument cannot be null");
            arguments.add(arg);
        }
        return this;
    }

    public AliasMetadata build() {
        return new AliasMetadata(name, arguments);
    }
}
