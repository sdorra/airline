package io.airlift.airline.help;

import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;

import com.google.common.base.Preconditions;

/**
 * Abstract command usage generator for generators that use a
 * {@link UsagePrinter} to generate the documentation
 */
public abstract class AbstractPrintedCommandUsageGenerator extends AbstractCommandUsageGenerator {

    private final int columnSize;

    public AbstractPrintedCommandUsageGenerator(int columnSize,
            Comparator<? super OptionMetadata> optionComparator) {
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
    protected abstract void usage(String programName, String groupName, String commandName,
            CommandMetadata command, UsagePrinter out) throws IOException;

    /**
     * Creates a usage printer for the given stream
     * 
     * @param out
     *            Output stream
     * @return Usage Printer
     */
    protected UsagePrinter createUsagePrinter(OutputStream out) {
        Preconditions.checkNotNull(out, "OutputStream cannot be null");
        OutputStreamWriter writer = new OutputStreamWriter(out);
        return new UsagePrinter(writer, columnSize);
    }

    @Override
    public void usage(String programName, String groupName, String commandName,
            CommandMetadata command, OutputStream out) throws IOException {
        UsagePrinter printer = createUsagePrinter(out);
        usage(programName, groupName, commandName, command, printer);
        printer.flush();
    }

}
