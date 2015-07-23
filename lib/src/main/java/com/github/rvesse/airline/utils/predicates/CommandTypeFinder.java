package com.github.rvesse.airline.utils.predicates;

import org.apache.commons.collections4.Predicate;
import com.github.rvesse.airline.model.CommandMetadata;

public class CommandTypeFinder implements Predicate<CommandMetadata> {

    private final Class<?> cls;

    public CommandTypeFinder(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public boolean evaluate(CommandMetadata command) {
        if (command == null)
            return false;
        if (this.cls == null) {
            return command.getClass() == null;
        } else {
            return this.cls.equals(command.getClass());
        }
    }
}
