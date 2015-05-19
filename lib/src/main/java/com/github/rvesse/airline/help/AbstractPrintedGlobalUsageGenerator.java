package com.github.rvesse.airline.help;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.google.common.base.Preconditions;

/**
 * Abstract global usage generator for generators that use a
 * {@link UsagePrinter} to generate the documentation
 */
public abstract class AbstractPrintedGlobalUsageGenerator extends AbstractGlobalUsageGenerator {

    private final int columnSize;

    public AbstractPrintedGlobalUsageGenerator(int columnSize, Comparator<? super OptionMetadata> optionComparator,
            Comparator<? super CommandMetadata> commandComparator,
            Comparator<? super CommandGroupMetadata> commandGroupComparator, boolean includeHidden) {
        super(optionComparator, commandComparator, commandGroupComparator, includeHidden);
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
