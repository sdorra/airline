package com.github.rvesse.airline.io.writers;

import java.io.IOException;
import java.io.Writer;

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
public class ColorizedWriter<T> extends ControlWriter {

    private WriterControlTracker<T> foregroundControl, backgroundControl;

    public ColorizedWriter(Writer writer, ControlCodeSource<T> foregroundColorSource,
            ControlCodeSource<T> backgroundColorSource) {
        super(writer);
        Preconditions.checkNotNull(foregroundColorSource);
        Preconditions.checkNotNull(backgroundColorSource);
        this.foregroundControl = new WriterControlTracker<T>(writer, foregroundColorSource);
        this.backgroundControl = new WriterControlTracker<T>(writer, backgroundColorSource);
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
