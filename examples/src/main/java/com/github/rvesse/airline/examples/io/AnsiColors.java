package com.github.rvesse.airline.examples.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;
import com.github.rvesse.airline.io.colors.AnsiColor;
import com.github.rvesse.airline.io.output.AnsiColorizedOutputStream;

@Command(name = "ansi-colors", description = "Displays some text in ANSI colors")
public class AnsiColors implements ExampleRunnable {

    private List<String> args = new ArrayList<String>();

    @Option(name = { "-b", "--background" }, description = "When set changes the background rather than the foreground colour")
    private boolean background = false;

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(AnsiColors.class, args);
    }

    @Override
    public int run() {
        // Don't care about the resource leak since we are only wrapping
        // System.out which will be managed by the JVM
        @SuppressWarnings("resource")
        AnsiColorizedOutputStream output = new AnsiColorizedOutputStream(System.out);
        try {
            String text = StringUtils.join(args, " ");
            if (StringUtils.isEmpty(text)) {
                text = "Sample text";
            }
            byte[] bs = text.getBytes();

            AnsiColor[] colors = AnsiColor.values();
            for (AnsiColor color : colors) {
                // This text will appear in your terminal default colour
                System.out.format("\nANSI Color %s:\n", color.toString());

                // Set the colour
                if (this.background) {
                    output.setBackgroundColor(color);
                } else {
                    output.setForegroundColor(color);
                }

                try {
                    // Anything we write now will be appropriately colorized
                    output.write(bs);

                    // Reset back to default
                    if (this.background) {
                        output.resetBackgroundColor();
                    } else {
                        output.resetForegroundColor();
                    }                   
                } catch (IOException e) {
                    e.printStackTrace();
                    return 1;
                }
            }

            return 0;
        } finally {
            // Just in case we hit an error remember to reset the color
            // appropriately
            try {
                if (this.background) {
                    output.resetBackgroundColor();
                } else {
                    output.resetForegroundColor();
                }
            } catch (IOException e) {
                // Just cleaning up so don't care in this example
            }
        }
    }
}
