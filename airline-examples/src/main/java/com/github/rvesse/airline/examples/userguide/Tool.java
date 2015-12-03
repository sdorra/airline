package com.github.rvesse.airline.examples.userguide;

import com.github.rvesse.airline.annotations.Command;

@Command(name = "tool", description = "This tool does something interesting", hidden = true, groupNames = { "common",
        "foo bar" })
public class Tool implements Runnable {

    @Override
    public void run() {
        System.out.println("Not so interesting after all");
    }

}
