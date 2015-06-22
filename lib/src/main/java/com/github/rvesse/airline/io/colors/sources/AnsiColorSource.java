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
    public String getResetControlCode(T attributeSource) {
        if (attributeSource.usesExtendedColors())
            return getFullResetControlCode();

        StringBuilder builder = new StringBuilder();
        builder.append(AnsiControlCodes.ESCAPE);
        builder.append(this.foreground ? AnsiControlCodes.DEFAULT_FOREGROUND : AnsiControlCodes.DEFAULT_BACKGROUND);
        builder.append(AnsiControlCodes.SELECT_GRAPHIC_RENDITION);
        return builder.toString();
    }

    public String getFullResetControlCode() {
        return AnsiControlCodes.getGraphicsResetCode();
    }
}
