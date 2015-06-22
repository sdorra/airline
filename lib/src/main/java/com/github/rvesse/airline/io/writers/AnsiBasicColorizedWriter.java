package com.github.rvesse.airline.io.writers;

import java.io.Writer;

import com.github.rvesse.airline.io.colors.BasicColor;
import com.github.rvesse.airline.io.colors.sources.AnsiBackgroundColorSource;
import com.github.rvesse.airline.io.colors.sources.AnsiForegroundColorSource;

/**
 * A colorized writer supporting the basic ANSI colors
 * 
 * @author rvesse
 *
 */
public class AnsiBasicColorizedWriter extends ColorizedWriter<BasicColor> {

    public AnsiBasicColorizedWriter(Writer writer) {
        super(writer, new AnsiForegroundColorSource<BasicColor>(), new AnsiBackgroundColorSource<BasicColor>());
    }

}
