package com.github.rvesse.airline.io.writers;

import java.io.Writer;

import com.github.rvesse.airline.io.colors.Color256;
import com.github.rvesse.airline.io.colors.sources.AnsiBackgroundColorSource;
import com.github.rvesse.airline.io.colors.sources.AnsiForegroundColorSource;

/**
 * A colorized writer supporting the ANSI 256 colour palette
 * @author rvesse
 *
 */
public class Ansi256ColorizedWriter extends ColorizedWriter<Color256> {

    public Ansi256ColorizedWriter(Writer writer) {
        super(writer, new AnsiForegroundColorSource<Color256>(), new AnsiBackgroundColorSource<Color256>());
    }

}
