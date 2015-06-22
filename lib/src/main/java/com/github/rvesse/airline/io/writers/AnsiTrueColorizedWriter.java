package com.github.rvesse.airline.io.writers;

import java.io.Writer;

import com.github.rvesse.airline.io.colors.TrueColor;
import com.github.rvesse.airline.io.colors.sources.AnsiBackgroundColorSource;
import com.github.rvesse.airline.io.colors.sources.AnsiForegroundColorSource;

/**
 * A colorized writer supporting ANSI true colour (24 bit i.e. 16 million
 * colours)
 * 
 * @author rvesse
 *
 */
public class AnsiTrueColorizedWriter extends ColorizedWriter<TrueColor> {

    public AnsiTrueColorizedWriter(Writer writer) {
        super(writer, new AnsiForegroundColorSource<TrueColor>(), new AnsiBackgroundColorSource<TrueColor>());
    }

}
