package com.github.rvesse.airline.examples.inheritance;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.modules.Modules;

/**
 * When inheriting from existing classes it is possible to override previously
 * defined options but only if you are explicit about it. In this case Airline
 * uses the bottom-most definition of the option as the effective definition
 * <p>
 * Note that any values set for options are propagated to all declarations in
 * the inheritance tree since an ancestor class will not have access to fields
 * set in the descendants.
 * </p>
 *
 */
@Command(name = "good-grandchild", description = "A legal command which overrides an option defined by a parent")
public class GoodGrandchild extends Child {

    /**
     * We can override the definition of an existing option under certain
     * conditions:
     * <ul>
     * <li>{@code arity}, {@code name} and {@code optionType} are unchanged</li>
     * <li>Type is same or a valid narrowing conversion exists from the
     * inherited option type to the overridden type</li>
     * <li>You explicitly declare {@code override} to be {@code true} and the
     * inherited option does not define {@code sealed} to be {@code true}</li>
     * </ul>
     * <p>
     * In practise this means you can change some informative properties of the
     * option such as the title and description. You can also choose to make it
     * visible/hidden and required/not required as desired.
     * </p>
     * <p>
     * Note that often if you find yourself needing to override options
     * frequently then this can be indicative of poorly thought out option
     * inheritance in which case using modules may be a better option. See
     * {@link Modules} for an example of this.
     * </p>
     */
    @Option(name = "--parent", description = "An option can be overridden if we are explicit about it", override = true)
    private boolean parent;

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(GoodGrandchild.class, args);
    }
}
