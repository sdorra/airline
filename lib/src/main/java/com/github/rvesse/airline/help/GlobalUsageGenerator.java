package com.github.rvesse.airline.help;

import java.io.IOException;
import java.io.OutputStream;

import com.github.rvesse.airline.model.GlobalMetadata;

/**
 * Interface implemented by classes that can generate usage documentation for a
 * command line interface
 */
public interface GlobalUsageGenerator {

    /**
     * Generate the help and output it on standard out
     * 
     * @param global
     *            Global Metadata
     * @throws IOException
     */
    public abstract void usage(GlobalMetadata global) throws IOException;

    /**
     * Generate the help and output it to the stream
     * 
     * @param global
     *            Global metadata
     * @param out
     *            Stream to output to
     * @throws IOException
     */
    public abstract void usage(GlobalMetadata global, OutputStream output) throws IOException;
}