package com.github.rvesse.airline.examples.simple;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;

/**
 * An example command that has required arguments
 *
 */
@Command(name = "req-args", description = "A command with required arguments")
public class RequiredArguments implements ExampleRunnable {

    @Arguments(required = true)
    private List<String> args = new ArrayList<String>();

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(RequiredArguments.class, args);
    }

    @Override
    public int run() {
        System.out.println("Arguments given were " + StringUtils.join(args, ", "));
        return 0;
    }
}
