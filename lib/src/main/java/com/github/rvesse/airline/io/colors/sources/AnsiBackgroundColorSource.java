package com.github.rvesse.airline.io.colors.sources;

import com.github.rvesse.airline.io.colors.AnsiColorProvider;

/**
 * An ANSI colour source for background colours
 * 
 * @author rvesse
 *
 */
public class AnsiBackgroundColorSource<T extends AnsiColorProvider> extends AnsiColorSource<T> {

    public AnsiBackgroundColorSource() {
        super(false);
    }
}
