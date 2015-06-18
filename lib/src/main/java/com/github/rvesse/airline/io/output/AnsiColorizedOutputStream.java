package com.github.rvesse.airline.io.output;

import java.io.OutputStream;

import com.github.rvesse.airline.io.colors.AnsiColor;
import com.github.rvesse.airline.io.colors.sources.AnsiBackgroundColorSource;
import com.github.rvesse.airline.io.colors.sources.AnsiForegroundColorSource;

public class AnsiColorizedOutputStream extends ColorizedOutputStream<AnsiColor> {

    public AnsiColorizedOutputStream(OutputStream out) {
        super(out, new AnsiForegroundColorSource(), new AnsiBackgroundColorSource());
    }

}
