package com.github.rvesse.airline.io.writers;

import java.io.IOException;
import java.io.Writer;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.ControlTracker;
import com.google.common.base.Preconditions;

public class WriterControlTracker<T> extends ControlTracker<T> {

    private final Writer writer;

    public WriterControlTracker(Writer writer, ControlCodeSource<T> provider) {
        super(provider);
        Preconditions.checkNotNull(writer);
        this.writer = writer;
    }

    @Override
    protected void resetInternal() throws IOException {
        this.writer.write(this.provider.getResetControlCode());
    }

    @Override
    protected void applyInternal(T value) throws IOException {
        this.writer.write(this.provider.getControlCode(value));
    }

}
