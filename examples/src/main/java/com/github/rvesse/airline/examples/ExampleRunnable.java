package com.github.rvesse.airline.examples;

public interface ExampleRunnable {

    /**
     * Runs the command and returns an exit code that the application should
     * return
     * 
     * @return Exit code
     */
    public int run();
}
