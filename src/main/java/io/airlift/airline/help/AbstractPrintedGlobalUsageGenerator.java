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
 * Abstract global usage generator for generators that use a
 * {@link UsagePrinter} to generate the documentation
 */
public abstract class AbstractPrintedGlobalUsageGenerator extends AbstractGlobalUsageGenerator {

    private final int columnSize;

    public AbstractPrintedGlobalUsageGenerator(int columnSize,
            @Nullable Comparator<? super OptionMetadata> optionComparator,
            @Nullable Comparator<? super CommandMetadata> commandComparator,
            @Nullable Comparator<? super CommandGroupMetadata> commandGroupComparator) {
        super(optionComparator, commandComparator, commandGroupComparator);
        Preconditions.checkArgument(columnSize > 0, "columnSize must be greater than 0");
        this.columnSize = columnSize;
    }

    /**
     * Generate the help and output is using the provided {@link UsagePrinter}
     * 
     * @param global
     *            Global Metadata
     * @param out
     *            Usage printer to output with
     * @throws IOException
     */
    protected abstract void usage(GlobalMetadata global, UsagePrinter out) throws IOException;

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
    public void usage(GlobalMetadata global, OutputStream out) throws IOException {
        UsagePrinter printer = createUsagePrinter(out);
        usage(global, printer);
        printer.flush();
    }

}
