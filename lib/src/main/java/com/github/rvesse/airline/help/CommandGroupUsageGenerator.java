package com.github.rvesse.airline.help;

import java.io.IOException;
import java.io.OutputStream;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;

/**
 * Interface implemented by classes that can generate usage documentation for a command group
 */
public interface CommandGroupUsageGenerator<T> {

    /**
     * Generate the help and output it on standard out
     * 
     * @param global
     *            Global Metadata
     * @throws IOException
     */
    public abstract void usage(GlobalMetadata<T> global, CommandGroupMetadata group) throws IOException;

    /**
     * Generate the help and output it to the stream
     * 
     * @param global
     *            Global metadata
     * @param out
     *            Stream to output to
     * @throws IOException
     */
    public abstract void usage(GlobalMetadata<T> global, CommandGroupMetadata group, OutputStream output) throws IOException;
}