package com.github.rvesse.airline.examples.io;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.io.colors.Color256;
import com.github.rvesse.airline.io.output.Ansi256ColorizedOutputStream;
import com.github.rvesse.airline.io.output.ColorizedOutputStream;

@Command(name = "colors-256", description = "Displays some text in 256 colors (if your terminal supports this)")
public class Colors256 extends ColorDemo<Color256> {
    
    @Option(name = "--colour", title = "Colours", description = "Sets the desired colour")
    private int color = -1;

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(Colors256.class, args);
    }

    @Override
    protected ColorizedOutputStream<Color256> openOutputStream() {
        return new Ansi256ColorizedOutputStream(System.out);
    }
    
    private int getColor(int c) {
        if (c == -1) return 0;
        if (c > 255) return 255;
        return c;
    }

    @Override
    protected Color256[] getColors() {
        if (color != -1) {
            return new Color256[] { new Color256(getColor(this.color)) };
        }
        
        Color256[] colors = new Color256[256];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new Color256(i);
        }
        return colors;
    }
}
