package com.github.rvesse.airline.io.colors;

import com.github.rvesse.airline.io.AnsiControlCodes;

/**
 * Standard ANSI Colours
 *
 */
public enum AnsiColor implements AnsiColorProvider {
    //@formatter:off
    BLACK(0, "Black"), 
    RED(1, "Red"), 
    GREEN(2, "Green"), 
    YELLOW(3, "Yellow"), 
    BLUE(4, "Blue"), 
    MAGENTA(5, "Magenta"), 
    CYAN(6, "Cyan"), 
    WHITE(7, "White");
    //@formatter:on

    private final int index;
    private final String name;

    AnsiColor(int index, String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public String getAnsiForegroundControlCode() {
        return getAnsiControlCode(AnsiControlCodes.FOREGROUND);
    }

    @Override
    public String getAnsiBackgroundControlCode() {
        return getAnsiControlCode(AnsiControlCodes.BACKGROUND);
    }

    private String getAnsiControlCode(int base) {
        StringBuilder builder = new StringBuilder();
        //@formatter:off
        builder.append(AnsiControlCodes.ESCAPE)
               .append(base + this.index)
               .append(AnsiControlCodes.SELECT_GRAPHIC_RENDITION);
        //@formatter:on
        return builder.toString();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
