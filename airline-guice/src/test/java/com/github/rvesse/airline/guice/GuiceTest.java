package com.github.rvesse.airline.guice;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.List;

import static org.testng.Assert.*;

public class GuiceTest {

    @Test
    public void test() {
        assertOutput(
                Lists.newArrayList("repositories", "list"),
                Lists.newArrayList("repositories", "-> airline", "-> scm-manager", "-> jenkins")
        );
        assertOutput(
                Lists.newArrayList("repositories", "add", "--type", "git", "airline"),
                Lists.newArrayList("=> add repository: git/airline")
        );
        assertOutput(
                Lists.newArrayList("whoami"),
                Lists.newArrayList("=> root")
        );
        assertOutput(
                Lists.newArrayList("app"),
                Lists.newArrayList("=> assertOutput")
        );
    }

    private void assertOutput(List<String> args, List<String> expectedOutput) {
        Output output = new Output();
        Sample.main(output, args.toArray(new String[0]));

        assertEquals(output.getLines(), expectedOutput);
    }

}
