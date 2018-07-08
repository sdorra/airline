package com.github.rvesse.airline.guice;

import java.util.ArrayList;
import java.util.List;

public class Output {

    private List<String> lines = new ArrayList<>();

    public void println(String line) {
        lines.add(line);
    }

    public List<String> getLines() {
        return lines;
    }

}
