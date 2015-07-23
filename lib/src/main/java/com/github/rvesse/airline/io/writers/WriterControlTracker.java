package com.github.rvesse.airline.io.writers;

import java.io.IOException;
import java.io.Writer;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.ControlTracker;

public class WriterControlTracker<T> extends ControlTracker<T> {

    private final Writer writer;

    public WriterControlTracker(Writer writer, ControlCodeSource<T> provider) {
        super(provider);
        if (writer == null)
            throw new NullPointerException("writer cannot be null");
        this.writer = writer;
    }

    @Override
    protected void resetInternal(T value) throws IOException {
        this.writer.write(this.provider.getResetControlCode(value));
    }

    @Override
    protected void applyInternal(T value) throws IOException {
        this.writer.write(this.provider.getControlCode(value));
    }

}
