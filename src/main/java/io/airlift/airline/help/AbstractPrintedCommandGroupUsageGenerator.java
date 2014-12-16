package io.airlift.airline.help;

import io.airlift.airline.model.CommandGroupMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.GlobalMetadata;
import io.airlift.airline.model.OptionMetadata;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

/**
 * Abstract command group usage generator for generators that use a
 * {@link UsagePrinter} to generate the documentation
 */
public abstract class AbstractPrintedCommandGroupUsageGenerator extends AbstractCommandGroupUsageGenerator {

    private final int columnSize;

    public AbstractPrintedCommandGroupUsageGenerator(int columnSize,
            @Nullable Comparator<? super OptionMetadata> optionComparator,
            @Nullable Comparator<? super CommandMetadata> commandComparator) {
        super(optionComparator, commandComparator);
        Preconditions.checkArgument(columnSize > 0, "columnSize must be greater than 0");
        this.columnSize = columnSize;
    }

    /**
     * Generate the help and output is using the provided {@link UsagePrinter}
     * 
     * @param global
     *            Global Metadata
     * @param group
     *            Group Metadata
     * @param out
     *            Usage printer to output with
     * @throws IOException
     */
    protected abstract void usage(GlobalMetadata global, CommandGroupMetadata group, UsagePrinter out)
            throws IOException;

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
    public void usage(GlobalMetadata global, CommandGroupMetadata group, OutputStream out) throws IOException {
        UsagePrinter printer = createUsagePrinter(out);
        usage(global, group, printer);
        printer.flush();
    }

}
