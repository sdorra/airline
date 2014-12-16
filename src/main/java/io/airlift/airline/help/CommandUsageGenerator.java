package io.airlift.airline.help;

import java.io.IOException;
import java.io.OutputStream;

import io.airlift.airline.model.CommandMetadata;

import javax.annotation.Nullable;

/**
 * Interface implemented by classes that can generate help
 * 
 */
public interface CommandUsageGenerator {

    /**
     * Generate the help and output it on standard out
     * 
     * @param programName
     *            Program Name
     * @param groupName
     *            Group Name
     * @param commandName
     *            Command Name
     * @param command
     *            Command Metadata
     * @throws IOException 
     */
    public abstract void usage(@Nullable String programName, @Nullable String groupName, String commandName,
            CommandMetadata command) throws IOException;

    /**
     * Generate the help and output it to the stream
     * 
     * @param programName
     *            Program Name
     * @param groupName
     *            Group Name
     * @param commandName
     *            Command Name
     * @param command
     *            Command Metadata
     * @param out
     *            Stream to output to
     * @throws IOException 
     */
    public abstract void usage(@Nullable String programName, @Nullable String groupName, String commandName,
            CommandMetadata command, OutputStream output) throws IOException;
}