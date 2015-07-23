package com.github.rvesse.airline.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ToStringHelper {

    private final Map<String, Object> data = new LinkedHashMap<>();
    private final Class<?> type;
    
    public ToStringHelper(Class<?> type) {
        this.type = type;
    }
    
    public ToStringHelper add(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.type.getCanonicalName());
        builder.append('\n').append('{').append('\n');
        for (Entry<String, Object> item : this.data.entrySet()) {
            builder.append(' ');
            builder.append(item.getKey()).append('=');
            builder.append(item.getValue());
            builder.append('\n');
        }
        builder.append('\n').append('}');
        return builder.toString();
    }
}
