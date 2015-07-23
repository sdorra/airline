package com.github.rvesse.airline.parser;

import com.github.rvesse.airline.Accessor;
import com.github.rvesse.airline.CommandFactory;
import com.github.rvesse.airline.DefaultCommandFactory;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.errors.ParseException;
import com.github.rvesse.airline.utils.AirlineUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ParserUtil {

    public static <T> T createInstance(Class<T> type) {
        if (type != null) {
            try {
                return type.getConstructor().newInstance();
            } catch (Exception e) {
                throw new ParseException(e, "Unable to create instance %s", type.getName());
            }
        }
        return null;
    }

    public static <T> T createInstance(Class<?> type, Iterable<OptionMetadata> options,
            List<Pair<OptionMetadata, Object>> parsedOptions, ArgumentsMetadata arguments,
            Iterable<Object> parsedArguments, Iterable<Accessor> metadataInjection, Map<Class<?>, Object> bindings) {
        return createInstance(type, options, parsedOptions, arguments, parsedArguments, metadataInjection, bindings,
                new DefaultCommandFactory<T>());
    }

    @SuppressWarnings("unchecked")
    public static <T> T injectOptions(T commandInstance, Iterable<OptionMetadata> options,
            List<Pair<OptionMetadata, Object>> parsedOptions, ArgumentsMetadata arguments,
            Iterable<Object> parsedArguments, Iterable<Accessor> metadataInjection, Map<Class<?>, Object> bindings) {
        // inject options
        for (OptionMetadata option : options) {
            List<Object> values = new ArrayList<>();
            for (Pair<OptionMetadata, Object> parsedOption : parsedOptions) {
                if (option.equals(parsedOption.getLeft()))
                    values.add(parsedOption.getRight());
            }
            if (option.getArity() > 1 && !values.isEmpty()) {
                // hack: flatten the collections
                List<Object> flattenedValues = new ArrayList<Object>();
                for (Object value : values) {
                    flattenedValues.addAll(IteratorUtils.<Object>toList(((Iterable<Object>) value).iterator()));
                }
                values = flattenedValues;
            }
            if (values != null && !values.isEmpty()) {
                for (Accessor accessor : option.getAccessors()) {
                    accessor.addValues(commandInstance, values);
                }
            }
        }

        // inject args
        if (arguments != null && parsedArguments != null) {
            for (Accessor accessor : arguments.getAccessors()) {
                accessor.addValues(commandInstance, parsedArguments);
            }
        }

        for (Accessor accessor : metadataInjection) {
            Object injectee = bindings.get(accessor.getJavaType());

            if (injectee != null) {
                accessor.addValues(commandInstance, ListUtils.unmodifiableList(AirlineUtils.singletonList(injectee)));
            }
        }

        return commandInstance;
    }

    public static <T> T createInstance(Class<?> type, Iterable<OptionMetadata> options,
            List<Pair<OptionMetadata, Object>> parsedOptions, ArgumentsMetadata arguments,
            Iterable<Object> parsedArguments, Iterable<Accessor> metadataInjection, Map<Class<?>, Object> bindings,
            CommandFactory<T> commandFactory) {
        // create the command instance
        T commandInstance = (T) commandFactory.createInstance(type);

        return injectOptions(commandInstance, options, parsedOptions, arguments, parsedArguments, metadataInjection,
                bindings);
    }
}
