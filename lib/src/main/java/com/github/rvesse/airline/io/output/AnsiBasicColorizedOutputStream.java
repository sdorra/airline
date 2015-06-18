package com.github.rvesse.airline.io.output;

import java.io.OutputStream;

import com.github.rvesse.airline.io.colors.BasicColor;
import com.github.rvesse.airline.io.colors.sources.AnsiBackgroundColorSource;
import com.github.rvesse.airline.io.colors.sources.AnsiForegroundColorSource;

/**
 * A colorized output stream supporting the 8 basic ANSI colours
 * @author rvesse
 *
 */
public class AnsiBasicColorizedOutputStream extends ColorizedOutputStream<BasicColor> {

    public AnsiBasicColorizedOutputStream(OutputStream out) {
        super(out, new AnsiForegroundColorSource<BasicColor>(), new AnsiBackgroundColorSource<BasicColor>());
    }

}
