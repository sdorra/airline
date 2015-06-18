package com.github.rvesse.airline.io;

import java.io.IOException;

import com.google.common.base.Preconditions;

public abstract class ControlTracker<T> {

    protected final ControlCodeSource<T> provider;
    private T current, previous;
    private boolean requireWrite = false;
    
    public ControlTracker(ControlCodeSource<T> provider) {
        Preconditions.checkNotNull(provider);
        this.provider = provider;
    }
    
    public final void set(T value) {
        this.current = value;
        this.requireWrite = this.current != null && !this.current.equals(this.previous);
    }
    
    public final void reset() throws IOException {
        if (this.previous != null) {
            this.previous = null;
            this.resetInternal();
        }
    }
    
    protected abstract void resetInternal() throws IOException;
    
    public final void apply() throws IOException {
        if (this.requireWrite) {
            this.applyInternal(this.current);
            this.previous = this.current;
            this.requireWrite = false;
        }
    }
    
    protected abstract void applyInternal(T value) throws IOException;
}
