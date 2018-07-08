package com.github.rvesse.airline;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Factory for various channels used by airline.
 */
public interface ChannelFactory {

    /**
     * Returns output channel.
     *
     * @return output channel
     */
    PrintStream createOutput();

    /**
     * Returns error channel.
     *
     * @return error channel
     */
    PrintStream createError();

    /**
     * Returns input channel.
     *
     * @return input channel
     */
    InputStream createInput();
}
