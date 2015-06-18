package com.github.rvesse.airline.io.colors.sources;

import com.github.rvesse.airline.io.colors.AnsiColor;

/**
 * An ANSI colour source for background colours
 * 
 * @author rvesse
 *
 */
public class AnsiBackgroundColorSource extends AnsiColorSource<AnsiColor> {

    public AnsiBackgroundColorSource() {
        super(false);
    }
}
