package com.github.rvesse.airline.io.colors.sources;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.AnsiControlCodes;
import com.github.rvesse.airline.io.colors.AnsiColorProvider;

public abstract class AnsiColorSource<T extends AnsiColorProvider> implements ControlCodeSource<T> {

    private final boolean foreground;

    public AnsiColorSource() {
        this(true);
    }

    protected AnsiColorSource(boolean foreground) {
        this.foreground = foreground;
    }

    @Override
    public String getControlCode(T attributeSource) {
        return this.foreground ? attributeSource.getAnsiForegroundControlCode() : attributeSource
                .getAnsiBackgroundControlCode();
    }

    @Override
    public String getResetControlCode() {
        StringBuilder builder = new StringBuilder();
        //@formatter:off
        builder.append(AnsiControlCodes.ESCAPE)
               .append(this.foreground ? AnsiControlCodes.DEFAULT_FOREGROUND : AnsiControlCodes.DEFAULT_BACKGROUND)
               .append(AnsiControlCodes.SELECT_GRAPHIC_RENDITION);
        //@formatter:on
        return builder.toString();
    }
}
