package io.airlift.airline.help;

import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

/**
 * Abstract command usage generator for generators that use a
 * {@link UsagePrinter} to generate the documentation
 * 
 * @author rvesse
 * 
 */
public abstract class AbstractPrintedCommandUsageGenerator extends AbstractCommandUsageGenerator {

    private final int columnSize;

    public AbstractPrintedCommandUsageGenerator(int columnSize,
            @Nullable Comparator<? super OptionMetadata> optionComparator) {
        super(optionComparator);
        Preconditions.checkArgument(columnSize > 0, "columnSize must be greater than 0");
        this.columnSize = columnSize;
    }

    /**
     * Generate the help and output is using the provided {@link UsagePrinter}
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
     *            Usage printer to output with
     * @throws IOException 
     */
    public abstract void usage(@Nullable String programName, @Nullable String groupName, String commandName,
            CommandMetadata command, UsagePrinter out) throws IOException;

    /**
     * Creates a usage printer for the given stream
     * 
     * @param out
     *            Output stream
     * @return Usage Printer
     */
    protected UsagePrinter createUsagePrinter(OutputStream out) {
        Preconditions.checkNotNull(out, "StringBuilder cannot be null");
        return new UsagePrinter(new OutputStreamWriter(out), columnSize);
    }

    @Override
    public void usage(@Nullable String programName, @Nullable String groupName, String commandName,
            CommandMetadata command, OutputStream out) throws IOException {
        out = new BufferedOutputStream(out);
        usage(programName, groupName, commandName, command, createUsagePrinter(out));
        out.flush();
    }

}
