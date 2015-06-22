package com.github.rvesse.airline.examples.io;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;
import com.github.rvesse.airline.io.colors.BasicColor;
import com.github.rvesse.airline.io.output.AnsiBasicColorizedOutputStream;
import com.github.rvesse.airline.io.output.ColorizedOutputStream;

@Command(name = "colors-basic", description = "Displays some text in ANSI basic colors (if your terminal supports this)")
public class ColorsBasic extends ColorDemo<BasicColor> implements ExampleRunnable {

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(ColorsBasic.class, args);
    }

    @Override
    protected ColorizedOutputStream<BasicColor> openOutputStream() {
        return new AnsiBasicColorizedOutputStream(System.out);
    }

    @Override
    protected BasicColor[] getColors() {
        return BasicColor.values();
    }
}
