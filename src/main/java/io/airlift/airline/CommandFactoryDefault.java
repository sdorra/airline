package io.airlift.airline;

import io.airlift.airline.parser.ParserUtil;

public class CommandFactoryDefault<T> implements CommandFactory<T> {

    @SuppressWarnings("unchecked")
    @Override
    public T createInstance(Class<?> type) {
        return (T) ParserUtil.createInstance(type);
    }

}
