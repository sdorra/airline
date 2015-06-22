package com.github.rvesse.airline.io.decorations.sources;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.AnsiControlCodes;
import com.github.rvesse.airline.io.decorations.AnsiDecorationProvider;

public class AnsiDecorationSource<T extends AnsiDecorationProvider> implements ControlCodeSource<T> {

    @Override
    public String getControlCode(T attributeSource) {
        return attributeSource.getAnsiDecorationEnabledControlCode();
    }

    @Override
    public String getResetControlCode(T attributeSource) {
        return attributeSource.getAnsiDecorationDisabledControlCode();
    }

    @Override
    public String getFullResetControlCode() {
        return AnsiControlCodes.getGraphicsResetCode();
    }
}
