package com.github.rvesse.airline.io.output;

import java.io.OutputStream;

import com.github.rvesse.airline.io.colors.Color256;
import com.github.rvesse.airline.io.colors.sources.AnsiBackgroundColorSource;
import com.github.rvesse.airline.io.colors.sources.AnsiForegroundColorSource;

/**
 * A colorized output stream supporting the ANSI 256 colour palette
 * @author rvesse
 *
 */
public class Ansi256ColorizedOutputStream extends ColorizedOutputStream<Color256> {

    public Ansi256ColorizedOutputStream(OutputStream out) {
        super(out, new AnsiForegroundColorSource<Color256>(), new AnsiBackgroundColorSource<Color256>());
    }

}
