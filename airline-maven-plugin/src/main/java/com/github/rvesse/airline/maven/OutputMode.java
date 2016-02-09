package com.github.rvesse.airline.maven;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;

public enum OutputMode {
    /**
     * Output the default kind of help (either {@link #CLI} or {@link #COMMAND})
     * depending on whether the source class is annotated with {@link Cli} or
     * {@link Command}
     */
    DEFAULT,
    /**
     * Output CLI help
     * <p>
     * If the source is {@link Command} annotated then no output is produced
     * </p>
     */
    CLI,
    /**
     * Output Group help for each individual group in a CLI
     * <p>
     * If the source is {@link Command} annotated or the CLI has no groups then
     * no output is produced
     * </p>
     */
    GROUP,
    /**
     * Output Command help for each individual command or command contained
     * within a CLI
     */
    COMMAND
}
