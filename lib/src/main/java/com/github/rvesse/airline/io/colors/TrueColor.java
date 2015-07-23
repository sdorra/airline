package com.github.rvesse.airline.io.colors;

import com.github.rvesse.airline.io.AnsiControlCodes;

/**
 * True (24 bit) colours i.e. 16 million possible colours
 *
 */
public class TrueColor implements AnsiColorProvider {

    private final int r, g, b;

    public TrueColor(int red, int green, int blue) {
        this.r = checkColor(red, "red");
        this.g = checkColor(green, "green");
        this.b = checkColor(blue, "blue");
    }

    public TrueColor(String hex) {
        this.r = Integer.valueOf(hex.substring(1, 3), 16);
        this.g = Integer.valueOf(hex.substring(3, 5), 16);
        this.b = Integer.valueOf(hex.substring(5, 7), 16);
    }

    private int checkColor(int c, String argName) {
        if (c < 0 || c > 255)
            throw new IllegalArgumentException(String.format(
                    "%s component was given value %d but only values in the range 0-255 are acceptable", argName, c));
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

    @Override
    public boolean usesExtendedColors() {
        return true;
    }

    private String getAnsiColorCode(int mode) {
        StringBuilder builder = new StringBuilder();
        //@formatter:off
        builder.append(AnsiControlCodes.ESCAPE)
               .append(mode)
               .append(AnsiControlCodes.PARAM_SEPARATOR)
               .append(AnsiControlCodes.COLOR_MODE_TRUE)
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

    public String toHex() {
        return String.format("#%02X%02X%02X", this.r, this.g, this.b);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof TrueColor))
            return false;

        TrueColor c = (TrueColor) other;
        return this.r == c.r && this.g == c.r && this.b == c.b;
    }
}
