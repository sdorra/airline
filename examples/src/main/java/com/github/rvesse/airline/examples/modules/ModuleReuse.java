package com.github.rvesse.airline.examples.modules;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.HelpOption;
import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;

/**
 * Here we have another command which reuses module classes we've defined and
 * also adds locally defined options, we can
 * 
 * @author rvesse
 *
 */
@Command(name = "module-reuse", description = "A command that demonstrates re-use of modules and composition with locally defined options")
public class ModuleReuse implements ExampleRunnable {

    @Inject
    private HelpOption<ExampleRunnable> help;

    /**
     * A field marked with {@link Inject} will also be scanned for options
     */
    @Inject
    private VerbosityModule verbosity = new VerbosityModule();

    @Arguments
    private List<String> args = new ArrayList<String>();

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(ModuleReuse.class, args);
    }

    @Override
    public int run() {
        if (!help.showHelpIfRequested()) {
            System.out.println("Verbosity is " + verbosity.verbosity);
            System.out.println("Arguments were " + StringUtils.join(args, ", "));
        }
        return 0;
    }
}
