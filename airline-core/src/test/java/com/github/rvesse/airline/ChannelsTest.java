package com.github.rvesse.airline;

import org.testng.annotations.Test;

import java.io.*;

import static org.testng.Assert.assertEquals;

public class ChannelsTest {

    @Test
    public void testOutput() {
        PrintStream oldOutput = System.out;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            Channels.output().append("some output");
            assertEquals(output.toString(), "some output");
        } finally {
          System.setOut(oldOutput);
        }
    }

    @Test
    public void testError() {
        PrintStream oldError = System.err;
        try {
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            System.setErr(new PrintStream(error));

            Channels.error().append("some error");
            assertEquals(error.toString(), "some error");
        } finally {
            System.setErr(oldError);
        }
    }

    @Test
    public void testInput() throws IOException {
        InputStream oldInput = System.in;
        try {
            byte[] inputData = "some input".getBytes();
            ByteArrayInputStream input = new ByteArrayInputStream(inputData);
            System.setIn(input);

            byte[] readData = new byte[inputData.length];
            Channels.input().read(readData);
            assertEquals(new String(readData), "some input");
        } finally {
            System.setIn(oldInput);
        }
    }
}
