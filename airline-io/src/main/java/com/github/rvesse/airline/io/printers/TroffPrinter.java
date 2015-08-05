package com.github.rvesse.airline.io.printers;

import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;

public class TroffPrinter {

    private static final int DEFAULT_INDENTATION = 4;

    private static final String BULLET = "\"\\(bu\"";

    private final PrintWriter writer;
    private int level = 0;
    private boolean newline = true;
    private boolean inSection = false;
    private int indentation = DEFAULT_INDENTATION;
    
    public TroffPrinter(PrintWriter writer) {
        this(writer, 2);
    }

    public TroffPrinter(PrintWriter writer, int indentation) {
        if (writer == null)
            throw new NullPointerException("writer cannot be null");
        this.writer = writer;
    }

    public void start(String title, int manSection) {
        start(title, manSection, null, null, null);
    }

    public void start(String title, int manSection, String header, String footer, String footerExtra) {
        if (newline)
            prepareLine();

        writer.println(String.format(".TH %s %s %s %s %s", asArg(title), asArg(Integer.toString(manSection)),
                asArg(footer), asArg(footerExtra), asArg(header)));
        newline = true;
    }

    public void nextSection(String sectionTitle) {
        if (newline)
            prepareLine();

        writer.println(String.format(".SH %s", sectionTitle));
        newline = true;
        inSection = true;
    }

    public void print(String value) {
        String[] lines = StringUtils.split(value, '\n');
        if (lines.length == 0) {
            // Append some text value directly
            if (newline)
                prepareLine();
            writer.print(escape(value));
        } else {
            // Append a series of lines
            for (String line : lines) {
                this.appendLine(line);
            }
        }
    }

    public void println(String value) {
        print(value);
        if (!newline) {
            writer.println();
            newline = true;
        }
    }

    private void appendLine(String line) {
        if (StringUtils.isEmpty(line)) {
            writer.println();
            newline = true;
        }

        if (newline)
            prepareLine();

        writer.println(escape(line));
        newline = true;
    }

    public void startList() {
        if (!newline)
            writer.println();

        if (level > 0) {
            writer.println(".RS");
            printBullet();
        } else {
            printBullet();
        }
        level++;

        newline = false;
    }

    public void nextListItem() {
        if (!newline)
            writer.println();

        if (level > 0) {
            printBullet();
            newline = false;
        } else {
            throw new IllegalStateException("Cannot start a new list item when not currently in a list");
        }
    }

    public void endList() {
        if (!newline)
            writer.println();

        if (level > 1) {
            writer.println(".RE");
        } else if (level == 1) {
            // Nothing to do
        } else {
            throw new IllegalStateException("Cannot end a list when not currently in a list");
        }

        level--;
        newline = true;
    }

    private void prepareLine() {
        if (level > 0) {
            writer.println(".IP");
        } else if (inSection) {
            writer.println(".IP \"\" 0");
        } else {
            writer.println(".");
        }
        newline = false;
    }

    protected void printBullet() {
        writer.println(String.format(".IP %s %d", BULLET, this.indentation));
    }

    private String asArg(String arg) {
        return String.format("\"%s\"", escapeArg(arg));
    }

    private String escapeArg(String arg) {
        if (arg == null)
            return "";
        return arg.replace('"', ' ');
    }

    private String escape(String line) {
        if (StringUtils.isEmpty(line))
            return line;

        // A leading . must be escaped
        if (line.startsWith("."))
            line = "\\" + line;

        // Hyphen/Minus must be escaped
        line = line.replace("-", "\\-");

        return line;
    }

    public void finish() {
        while (level > 0) {
            this.endList();
        }
        writer.flush();
    }
}
