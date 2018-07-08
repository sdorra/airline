package com.github.rvesse.airline;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Default implementation of {@link ChannelFactory} which uses {@code System.out}, {@code System.err} and
 * {@code System.in} for the channels.
 */
public final class SystemChannelFactory implements ChannelFactory {

    @Override
    public PrintStream createOutput() {
        return System.out;
    }

    @Override
    public PrintStream createError() {
        return System.err;
    }

    @Override
    public InputStream createInput() {
        return System.in;
    }
}
