package com.github.rvesse.airline.io.colors;

import com.github.rvesse.airline.io.AnsiControlCodes;

/**
 * The 8 basic ANSI Colours
 */
public enum BasicColor implements AnsiColorProvider {
    //@formatter:off
    BLACK(0, "Black"), 
    RED(1, "Red"), 
    GREEN(2, "Green"), 
    YELLOW(3, "Yellow"), 
    BLUE(4, "Blue"), 
    MAGENTA(5, "Magenta"), 
    CYAN(6, "Cyan"), 
    WHITE(7, "White"),
    BRIGHT_BLACK(0, "Black", true), 
    BRIGHT_RED(1, "Red", true), 
    BRIGHT_GREEN(2, "Green", true), 
    BRIGHT_YELLOW(3, "Yellow", true), 
    BRIGHT_BLUE(4, "Blue", true), 
    BRIGHT_MAGENTA(5, "Magenta", true), 
    BRIGHT_CYAN(6, "Cyan", true), 
    BRIGHT_WHITE(7, "White", true);
    //@formatter:on

    private final int index;
    private final String name;
    private final boolean highIntensity;

    BasicColor(int index, String name) {
        this(index, name, false);
    }

    BasicColor(int index, String name, boolean highIntensity) {
        this.index = index;
        this.name = name;
        this.highIntensity = highIntensity;
    }

    @Override
    public String getAnsiForegroundControlCode() {
        return getAnsiControlCode(this.highIntensity ? AnsiControlCodes.FOREGROUND_BRIGHT : AnsiControlCodes.FOREGROUND);
    }

    @Override
    public String getAnsiBackgroundControlCode() {
        return getAnsiControlCode(this.highIntensity ? AnsiControlCodes.BACKGROUND_BRIGHT : AnsiControlCodes.BACKGROUND);
    }

    @Override
    public boolean usesExtendedColors() {
        return false;
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
        if (this.highIntensity)
            return String.format("Bright %s", this.name);
        return this.name;
    }
}
