package com.github.rvesse.airline.examples.simple;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;

/**
 * A command that has some required options
 *
 */
@Command(name = "required", description = "A command with required options")
public class Required implements ExampleRunnable {
    
    @Option(name = "--required", title = "Value", arity = 1, description = "A required option")
    private String required;
    
    @Option(name = "--optional", title = "Value", arity = 1, description = "An optional option")
    private String optional;
    
    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(Required.class, args);
    }

    @Override
    public int run() {
        System.out.println("Required value given was " + this.required);
        System.out.println("Optional value was " + this.optional);
        return 0;
    }

}
