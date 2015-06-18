package com.github.rvesse.airline.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.github.rvesse.airline.io.ControlCodeSource;
import com.github.rvesse.airline.io.ControlTracker;
import com.google.common.base.Preconditions;

public class OutputStreamControlTracker<T> extends ControlTracker<T> {

    private final OutputStream output;
    private final Charset charset;

    public OutputStreamControlTracker(OutputStream output, ControlCodeSource<T> provider) {
        this(output, null, provider);
    }

    public OutputStreamControlTracker(OutputStream output, Charset charset, ControlCodeSource<T> provider) {
        super(provider);
        Preconditions.checkNotNull(output);
        this.output = output;
        this.charset = charset;
    }

    @Override
    protected void resetInternal() throws IOException {
        String code = this.provider.getResetControlCode();
        this.output.write(getBytes(code));
    }

    private byte[] getBytes(String code) {
        return this.charset != null ? code.getBytes(this.charset) : code.getBytes();
    }

    @Override
    protected void applyInternal(T value) throws IOException {
        String code = this.provider.getControlCode(value);
        this.output.write(getBytes(code));
    }

}
