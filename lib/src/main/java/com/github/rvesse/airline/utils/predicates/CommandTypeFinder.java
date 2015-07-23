package com.github.rvesse.airline.utils.predicates;

import org.apache.commons.collections4.Predicate;
import com.github.rvesse.airline.model.CommandMetadata;

public class CommandTypeFinder implements Predicate<CommandMetadata> {

    private final Class<?> cls;

    public CommandTypeFinder(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public boolean evaluate(CommandMetadata arg0) {
        if (arg0 == null)
            return false;
        if (this.cls == null) {
            return arg0.getClass() == null;
        } else {
            return this.cls.equals(arg0.getClass());
        }
    }
}
