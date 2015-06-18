package com.github.rvesse.airline.io.output;

import java.io.IOException;
import java.io.OutputStream;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.google.common.base.Preconditions;

/**
 * An output stream that supports colorization
 * 
 * @author rvesse
 *
 * @param <T>
 *            Color type
 */
public class ColorizedOutputStream<T> extends ControlOutputStream {

    private OutputStreamControlTracker<T> foregroundControl, backgroundControl;

    public ColorizedOutputStream(OutputStream out, ControlCodeSource<T> foregroundColorSource,
            ControlCodeSource<T> backgroundColorSource) {
        super(out);
        Preconditions.checkNotNull(foregroundColorSource);
        Preconditions.checkNotNull(backgroundColorSource);
        this.foregroundControl = new OutputStreamControlTracker<T>(out, foregroundColorSource);
        this.backgroundControl = new OutputStreamControlTracker<T>(out, backgroundColorSource);
        this.registerControl(this.foregroundControl);
        this.registerControl(this.backgroundControl);
    }

    public void setForegroundColor(T color) {
        this.foregroundControl.set(color);
    }

    public void resetForegroundColor() throws IOException {
        this.foregroundControl.reset();
    }

    public void setBackgroundColor(T color) {
        this.backgroundControl.set(color);
    }

    public void resetBackgroundColor() throws IOException {
        this.backgroundControl.reset();
    }
}
