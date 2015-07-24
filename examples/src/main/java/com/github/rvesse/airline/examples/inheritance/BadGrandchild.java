package com.github.rvesse.airline.examples.inheritance;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.examples.ExampleExecutor;

/**
 * When inheriting from existing classes the default behaviour is to prevent
 * overriding of options as Airline assumes the conflicting definitions are an
 * error on the part of the developer. See {@link GoodGrandchild} for an example
 * of how to do option overrides correctly.
 *
 */
@Command(name = "bad-grandchild", description = "An illegal command which attempts to overrides an option defined by a parent without explicitly declaring the override")
public class BadGrandchild extends Child {

    /**
     * Trying to override the option here will fail because we didn't explicitly
     * state we were overriding
     */
    @Option(name = "--parent", description = "An option can be overridden if we are explicit about it")
    private boolean parent;

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(BadGrandchild.class, args);
    }
}
