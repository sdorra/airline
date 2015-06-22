package com.github.rvesse.airline.io.output;

import java.io.OutputStream;

import com.github.rvesse.airline.io.colors.TrueColor;
import com.github.rvesse.airline.io.colors.sources.AnsiBackgroundColorSource;
import com.github.rvesse.airline.io.colors.sources.AnsiForegroundColorSource;

/**
 * A colorized output stream supporting ANSI true colour (24 bit i.e. 16 million
 * colours)
 * 
 * @author rvesse
 *
 */
public class AnsiTrueColorizedOutputStream extends ColorizedOutputStream<TrueColor> {

    public AnsiTrueColorizedOutputStream(OutputStream out) {
        super(out, new AnsiForegroundColorSource<TrueColor>(), new AnsiBackgroundColorSource<TrueColor>());
    }

}
