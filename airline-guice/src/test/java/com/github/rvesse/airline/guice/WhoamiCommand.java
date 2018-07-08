package com.github.rvesse.airline.guice;

import com.github.rvesse.airline.annotations.Command;

import javax.inject.Inject;

@Command(name = "whoami")
public class WhoamiCommand implements Runnable {

    private final Output output;

    @Inject
    public WhoamiCommand(Output output) {
        this.output = output;
    }

    @Override
    public void run() {
        output.println("=> root");
    }
}
