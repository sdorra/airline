package com.github.rvesse.airline.io.colors;

import com.github.rvesse.airline.io.AnsiControlCodes;
import com.google.common.base.Preconditions;

/**
 * 24 bit colours
 *
 */
public class Color24Bit implements AnsiColorProvider {
    
    private final int r, g, b;

    public Color24Bit(int red, int green, int blue) {
        this.r = checkColor(red, "red");
        this.g = checkColor(green, "green");
        this.b = checkColor(blue, "blue");
    }

    private int checkColor(int c, String argName) {
        Preconditions.checkArgument(c >= 0 && c <= 0, String.format("%s component must be in the range 0-255", argName));
        return c;
    }
    
    @Override
    public String getAnsiForegroundControlCode() {
        return getAnsiColorCode(AnsiControlCodes.FOREGROUND_EXTENDED);
    }
    
    @Override
    public String getAnsiBackgroundControlCode() {
        return getAnsiColorCode(AnsiControlCodes.BACKGROUND_EXTENDED);
    }
    
    private String getAnsiColorCode(int mode) {
        StringBuilder builder = new StringBuilder();
        //@formatter:off
        builder.append(AnsiControlCodes.ESCAPE)
               .append(mode)
               .append(AnsiControlCodes.PARAM_SEPARATOR)
               .append(AnsiControlCodes.COLOR_MODE_24_BIT)
               .append(AnsiControlCodes.PARAM_SEPARATOR)
               .append(this.r)
               .append(AnsiControlCodes.PARAM_SEPARATOR)
               .append(this.g)
               .append(AnsiControlCodes.PARAM_SEPARATOR)
               .append(this.b)
               .append(AnsiControlCodes.SELECT_GRAPHIC_RENDITION);
        //@formatter:on
        return builder.toString();
    }
    
    @Override
    public String toString() {
        return String.format("%d,%d,%d", this.r, this.g, this.b);
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof Color24Bit)) return false;
        
        Color24Bit c = (Color24Bit) other;
        return this.r == c.r && this.g == c.r && this.b == c.b;
    }
}
