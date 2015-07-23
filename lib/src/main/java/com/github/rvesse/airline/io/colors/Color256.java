package com.github.rvesse.airline.io.colors;

import com.github.rvesse.airline.io.AnsiControlCodes;

/**
 * 256 colour i.e. palette of 256 colors that most modern terminals will support
 *
 */
public class Color256 implements AnsiColorProvider {

    private final int color;

    public Color256(int color) {
        this.color = checkColor(color);
    }

    private int checkColor(int c) {
        if (c < 0 || c > 255)
            throw new IllegalArgumentException(String.format(
                    "Color was given value %d but only values in the range 0-255 are acceptable", c));
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
               .append(AnsiControlCodes.COLOR_MODE_256)
               .append(AnsiControlCodes.PARAM_SEPARATOR)
               .append(this.color)
               .append(AnsiControlCodes.SELECT_GRAPHIC_RENDITION);
        //@formatter:on
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format("%d", this.color);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof Color256))
            return false;

        Color256 c = (Color256) other;
        return this.color == c.color;
    }
}
