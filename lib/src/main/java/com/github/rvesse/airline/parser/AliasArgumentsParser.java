package com.github.rvesse.airline.parser;

import java.util.ArrayList;
import java.util.List;

public class AliasArgumentsParser {

    public static List<String> parse(String value) {
        AliasArgumentsParser parser = new AliasArgumentsParser(value);
        return parser.parse();
    }

    private CharSequence sequence;

    AliasArgumentsParser(String value) {
        this.sequence = value;
    }

    public List<String> parse() {
        List<String> args = new ArrayList<String>();

        for (int i = 0; i < this.sequence.length(); i++) {
            char c = this.sequence.charAt(i);

            switch (c) {
            case '"':
                // Start of a quoted argument
                i = parseQuotedArgument(i, args);
                break;
            default:
                if (Character.isWhitespace(c)) {
                    // Ignore separating white space
                    continue;
                }
                // Start on an unquoted argument
                // Parse to next unescaped whitespace character
                i = parseUnquotedArgument(i, args);
            }
        }
        
        return args;
    }

    private int parseQuotedArgument(int start, List<String> args) {
        StringBuilder arg = new StringBuilder();
        for (int i = start + 1; i < this.sequence.length(); i++) {
            char c = this.sequence.charAt(i);
            switch (c) {
            case '"':
                // End of quoted argument UNLESS we have a preceding escape
                if (arg.length() > 0) {
                    char prev = this.sequence.charAt(i - 1);
                    if (prev == '\\') {
                        // Escaped quote so continue accumulating
                        arg.append(c);
                        continue;
                    }
                }
                
                // Reached end of quoted argument
                args.add(arg.toString());
                return i;
            default:
                // Any other character just gets accumulated
                arg.append(c);
            }
        }

        // Reached end of input which means we have mismatched quotes
        throw new ParseException("Mismatched quotes in alias definition: %s", sequence);
    }

    private int parseUnquotedArgument(int start, List<String> args) {
        StringBuilder arg = new StringBuilder();
        for (int i = start; i < this.sequence.length(); i++) {
            char c = this.sequence.charAt(i);
            if (Character.isWhitespace(c)) {
                // End of argument UNLESS we have a preceding escape
                if (arg.length() > 0) {
                    char prev = this.sequence.charAt(i - 1);
                    if (prev == '\\') {
                        // Escaped whitespace so continue accumulating
                        arg.append(c);
                        continue;
                    }
                }
                
                // Reached end of argument
                args.add(arg.toString());
                return i;
            }
            
            // Otherwise accumulate
            arg.append(c);
        }

        // Reached end of input which is fine for unquoted arguments
        args.add(arg.toString());
        return this.sequence.length() - 1;
    }
}
