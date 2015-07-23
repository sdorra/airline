package com.github.rvesse.airline.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.utils.AirlineUtils;

public class AliasMetadata {

    private final String name;
    private final List<String> arguments;

    public AliasMetadata(String name, List<String> arguments) {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Alias name cannot be null/empty/whitespace");
        this.name = name;
        this.arguments = AirlineUtils.unmodifiableListCopy(arguments);
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
}
