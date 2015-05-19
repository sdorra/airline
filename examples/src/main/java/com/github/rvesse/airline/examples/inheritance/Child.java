package com.github.rvesse.airline.examples.inheritance;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;
import com.github.rvesse.airline.examples.ExampleExecutor;

@Command(name = "child", description = "A child command inherits options from its parents and ")
public class Child extends Parent {

    @Option(name = "--child", arity = 1, title = "Value", description = "An option provided by the child")
    private double child;

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(Child.class, args);
    }

    @Override
    public int run() {
        super.run();
        if (!help.showHelpIfRequested()) {
            System.out.println("--child had value " + this.child);
        }
        return 0;
    }

}
