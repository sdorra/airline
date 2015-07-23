package com.github.rvesse.airline.help;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.utils.AirlineUtils;

/**
 * Helper for printing out usage information
 * <p>
 * Provides support for maintaining indents and wrapping text to a column width
 * </p>
 * 
 */
public class UsagePrinter {
    private final Writer out;
    private final int maxSize;
    private final int indent;
    private final int hangingIndent;
    private final AtomicInteger currentPosition;

    public UsagePrinter(Writer out) {
        this(out, 79);
    }

    public UsagePrinter(Writer out, int maxSize) {
        this(out, maxSize, 0, 0, new AtomicInteger());
    }

    public UsagePrinter(Writer out, int maxSize, int indent, int hangingIndent, AtomicInteger currentPosition) {
        if (out == null)
            throw new NullPointerException("Writer cannot be null");
        this.out = out;
        this.maxSize = maxSize;
        this.indent = indent;
        this.hangingIndent = hangingIndent;
        this.currentPosition = currentPosition;
    }

    public UsagePrinter newIndentedPrinter(int size) {
        return new UsagePrinter(out, maxSize, indent + size, hangingIndent, currentPosition);
    }

    public UsagePrinter newPrinterWithHangingIndent(int size) {
        return new UsagePrinter(out, maxSize, indent, hangingIndent + size, currentPosition);
    }

    public UsagePrinter newline() throws IOException {
        out.append("\n");
        currentPosition.set(0);
        return this;
    }

    public UsagePrinter appendTable(Iterable<? extends Iterable<String>> table, int rowSpacing) throws IOException {
        List<Integer> columnSizes = new ArrayList<>();
        for (Iterable<String> row : table) {
            int column = 0;
            for (String value : row) {
                while (column >= columnSizes.size()) {
                    columnSizes.add(0);
                }
                columnSizes.set(column, Math.max(value.length(), columnSizes.get(column)));
                column++;
            }
        }

        if (currentPosition.get() != 0) {
            currentPosition.set(0);
            out.append("\n");
        }

        for (Iterable<String> row : table) {
            int column = 0;
            StringBuilder line = new StringBuilder();
            for (String value : row) {
                int columnSize = columnSizes.get(column);
                line.append(value);
                line.append(spaces(columnSize - value.length()));
                line.append("   ");
                column++;
            }
            out.append(spaces(indent)).append(trimEnd(line.toString())).append("\n");

            for (int i = 0; i < rowSpacing; i++) {
                out.append('\n');
            }
        }

        return this;
    }

    public static String trimEnd(final String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        int end = str.length();
        while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
            end--;
        }

        return str.substring(0, end);
    }

    public UsagePrinter append(String value) throws IOException {
        return append(value, false);
    }

    public UsagePrinter appendOnOneLine(String value) throws IOException {
        return append(value, true);
    }

    public UsagePrinter appendWords(Iterable<String> words) throws IOException {
        return appendWords(words, false);
    }

    public UsagePrinter append(String value, boolean avoidNewlines) throws IOException {
        if (value == null)
            return this;
        if (avoidNewlines) {
            return appendWords(AirlineUtils.arrayToList(value.split("\\s+")), avoidNewlines);
        } else {
            return appendLines(AirlineUtils.arrayToList(StringUtils.split(value, '\n')), avoidNewlines);
        }
    }

    public UsagePrinter appendLines(Iterable<String> lines) throws IOException {
        return appendLines(lines, false);
    }

    public UsagePrinter appendLines(Iterable<String> lines, boolean avoidNewlines) throws IOException {
        Iterator<String> iter = lines.iterator();
        while (iter.hasNext()) {
            String line = iter.next();
            if (line == null || line.isEmpty())
                continue;
            appendWords(AirlineUtils.arrayToList(line.split("\\s+")), avoidNewlines);
            if (iter.hasNext()) {
                this.newline();
            }
        }
        return this;
    }

    public UsagePrinter appendWords(Iterable<String> words, boolean avoidNewlines) throws IOException {
        int bracketCount = 0;
        for (String word : words) {
            if (null == word || "".equals(word)) {
                continue;
            }
            if (currentPosition.get() == 0) {
                // beginning of line
                out.append(spaces(indent));
                currentPosition.getAndAdd((indent));
            } else if (word.length() > maxSize || currentPosition.get() + word.length() <= maxSize || bracketCount > 0
                    || avoidNewlines) {
                // between words
                out.append(" ");
                currentPosition.getAndIncrement();
            } else {
                // wrap line
                out.append("\n").append(spaces(indent)).append(spaces(hangingIndent));
                currentPosition.set(indent);
            }

            out.append(word);
            currentPosition.getAndAdd((word.length()));
            if (word.contains("{") || word.contains("[") || word.contains("<")) {
                bracketCount++;
            }
            if (word.contains("}") || word.contains("]") || word.contains(">")) {
                bracketCount--;
            }
        }
        return this;
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public void close() throws IOException {
        this.out.close();
    }

    private static String spaces(int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(" ");
        }
        return result.toString();
    }
}
