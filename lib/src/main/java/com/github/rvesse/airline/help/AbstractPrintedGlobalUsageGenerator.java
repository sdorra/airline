package com.github.rvesse.airline.help;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

/**
 * Abstract global usage generator for generators that use a
 * {@link UsagePrinter} to generate the documentation
 */
public abstract class AbstractPrintedGlobalUsageGenerator<T> extends AbstractGlobalUsageGenerator<T> {

    private final int columns;

    public AbstractPrintedGlobalUsageGenerator(int columns, Comparator<? super OptionMetadata> optionComparator,
            Comparator<? super CommandMetadata> commandComparator,
            Comparator<? super CommandGroupMetadata> commandGroupComparator, boolean includeHidden) {
        super(optionComparator, commandComparator, commandGroupComparator, includeHidden);
        if (columns <= 0)
            throw new IllegalArgumentException("columns must be greater than 0");
        this.columns = columns;
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
    protected abstract void usage(GlobalMetadata<T> global, UsagePrinter out) throws IOException;

    /**
     * Creates a usage printer for the given stream
     * 
     * @param out
     *            Output stream
     * @return Usage Printer
     */
    protected UsagePrinter createUsagePrinter(OutputStream out) {
        if (out == null)
            throw new NullPointerException("out cannot be null");
        return new UsagePrinter(new OutputStreamWriter(out), columns);
    }

    @Override
    public void usage(GlobalMetadata<T> global, OutputStream out) throws IOException {
        UsagePrinter printer = createUsagePrinter(out);
        usage(global, printer);
        printer.flush();
    }

}
