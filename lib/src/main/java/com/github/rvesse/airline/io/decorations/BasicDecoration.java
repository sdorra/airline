package com.github.rvesse.airline.io.decorations;

import com.github.rvesse.airline.io.AnsiControlCodes;

/**
 * Support for the basic ANSI decorations
 * @author rvesse
 *
 */
public enum BasicDecoration implements AnsiDecorationProvider {

    BOLD(AnsiControlCodes.BOLD, AnsiControlCodes.NORMAL_INTENSITY, "Bold"),
    FAINT(AnsiControlCodes.FAINT, AnsiControlCodes.NORMAL_INTENSITY, "Faint"),
    ITALIC(AnsiControlCodes.ITALIC, AnsiControlCodes.ITALIC_OFF, "Italic"),
    UNDERLINE(AnsiControlCodes.UNDERLINE, AnsiControlCodes.UNDERLINE_OFF, "Underline"),
    BLINK_SLOW(AnsiControlCodes.BLINK_SLOW, AnsiControlCodes.BLINK_OFF, "Blink (Slow)"),
    BLINK_RAPID(AnsiControlCodes.BLINK_RAPID, AnsiControlCodes.BLINK_OFF, "Blink (Rapid)"),
    IMAGE_NEGATIVE(AnsiControlCodes.IMAGE_NEGATIVE, AnsiControlCodes.IMAGE_POSITIVE, "Image Negative"),
    CONCEAL(AnsiControlCodes.CONCEAL, AnsiControlCodes.REVEAL, "Concealed"),
    STRIKE_THROUGH(AnsiControlCodes.STRIKE_THROUGH, AnsiControlCodes.STRIKE_THROUGH_OFF, "Strike-Through");
    
    private final int enableCode, disableCode;
    private final String name;
    
    private BasicDecoration(int enable, int disable, String name) {
        this.enableCode = enable;
        this.disableCode = disable;
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getAnsiDecorationEnabledControlCode() {
        return getAnsiControlCode(this.enableCode);
    }

    @Override
    public String getAnsiDecorationDisabledControlCode() {
        return getAnsiControlCode(this.disableCode);
    }
    
    private String getAnsiControlCode(int code) {
        StringBuilder builder = new StringBuilder();
        //@formatter:off
        builder.append(AnsiControlCodes.ESCAPE)
               .append(code)
               .append(AnsiControlCodes.SELECT_GRAPHIC_RENDITION);
        //@formatter:on
        return builder.toString();
    }
    
}
