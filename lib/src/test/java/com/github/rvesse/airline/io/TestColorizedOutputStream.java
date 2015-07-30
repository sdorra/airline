/**
 * Copyright (C) 2010-15 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.io;

import java.io.ByteArrayOutputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.rvesse.airline.io.colors.BasicColor;
import com.github.rvesse.airline.io.output.AnsiBasicColorizedOutputStream;
import com.github.rvesse.airline.io.output.ColorizedOutputStream;

public class TestColorizedOutputStream {

    @Test
    public void colorized_output_unecessary_01() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ColorizedOutputStream<BasicColor> colorOutput = new AnsiBasicColorizedOutputStream(output);
        colorOutput.setForegroundColor(BasicColor.RED);
        colorOutput.close();

        // Since nothing was actually output to the stream after setting the
        // colour then nothing should have been output
        Assert.assertEquals(output.size(), 0);
    }

    @Test
    public void colorized_output_unecessary_02() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ColorizedOutputStream<BasicColor> colorOutput = new AnsiBasicColorizedOutputStream(output);
        for (BasicColor color : BasicColor.values()) {
            colorOutput.setForegroundColor(color);
        }
        colorOutput.close();

        // Since nothing was actually output to the stream after setting the
        // colour then nothing should have been output
        Assert.assertEquals(output.size(), 0);
    }

    @Test
    public void colorized_output_unecessary_03() {
        String test = "Test";
        byte[] testBytes = test.getBytes();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ColorizedOutputStream<BasicColor> colorOutput = new AnsiBasicColorizedOutputStream(output);
        colorOutput.setForegroundColor(BasicColor.RED);
        colorOutput.print(test);

        // Since we output something should have some data
        // And should have more bytes than just the test string would produce
        Assert.assertTrue(output.size() > 0);
        Assert.assertTrue(output.size() > testBytes.length);

        // Changing the colour should not change the size because we shouldn't
        // output the new color until something is actually output
        int previousSize = output.size();
        colorOutput.setForegroundColor(BasicColor.CYAN);
        Assert.assertEquals(output.size(), previousSize);

        // When we close size should increase as we reset the stream to its
        // default state
        colorOutput.close();
        Assert.assertTrue(output.size() > previousSize);
    }

    @Test
    public void colorized_output_01() {
        String test = "Test";
        byte[] testBytes = test.getBytes();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ColorizedOutputStream<BasicColor> colorOutput = new AnsiBasicColorizedOutputStream(output);
        colorOutput.setForegroundColor(BasicColor.RED);
        colorOutput.print(test);

        // Since we output something should have some data
        // And should have more bytes than just the test string would produce
        Assert.assertTrue(output.size() > 0);
        Assert.assertTrue(output.size() > testBytes.length);

        // Now if we ask to reset the color this should result in additional
        // bytes output
        int previousSize = output.size();
        colorOutput.resetForegroundColor();
        Assert.assertTrue(output.size() > previousSize);
        previousSize = output.size();

        // Outputting some more text should only increase size by necessary
        // bytes because no colorization is currently set
        colorOutput.print(test);
        Assert.assertEquals(output.size(), previousSize + testBytes.length);

        colorOutput.close();
    }

    @Test
    public void colorized_output_02() {
        String test = "Test";
        byte[] testBytes = test.getBytes();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ColorizedOutputStream<BasicColor> colorOutput = new AnsiBasicColorizedOutputStream(output);
        colorOutput.setForegroundColor(BasicColor.RED);
        colorOutput.print(test);

        // Since we output something should have some data
        // And should have more bytes than just the test string would produce
        Assert.assertTrue(output.size() > 0);
        Assert.assertTrue(output.size() > testBytes.length);

        // Now if we ask to reset the color this should result in additional
        // bytes output
        int previousSize = output.size();
        colorOutput.resetForegroundColor();
        Assert.assertTrue(output.size() > previousSize);
        previousSize = output.size();

        // Outputting some more text should only increase size by necessary
        // bytes because no colorization is currently set
        colorOutput.print(test);
        Assert.assertEquals(output.size(), previousSize + testBytes.length);
        previousSize = output.size();

        // However if we colorized again we should get a larger than necessary
        // increase in bytes
        // Remember of course that bytes written won't increase until we
        // actually write some data that needs colorizing
        colorOutput.setBackgroundColor(BasicColor.BLUE);
        Assert.assertEquals(output.size(), previousSize);
        colorOutput.print(test);
        Assert.assertTrue(output.size() > previousSize + testBytes.length);

        colorOutput.close();
    }

    @Test
    public void colorized_output_reset_01() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ColorizedOutputStream<BasicColor> colorOutput = new AnsiBasicColorizedOutputStream(output);

        // Normal reset should result in no output if no controls changed
        colorOutput.reset(false);
        Assert.assertEquals(output.size(), 0);

        colorOutput.close();
    }
    
    @Test
    public void colorized_output_reset_02() {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ColorizedOutputStream<BasicColor> colorOutput = new AnsiBasicColorizedOutputStream(output);

        // Set color but don't perform any output
        colorOutput.setForegroundColor(BasicColor.RED);
        Assert.assertEquals(output.size(), 0);

        // Normal reset should result in no output if controls changed and
        // no data output
        colorOutput.reset(false);
        Assert.assertEquals(output.size(), 0);

        colorOutput.close();
    }

    @Test
    public void colorized_output_reset_03() {
        String test = "Test";
        byte[] testBytes = test.getBytes();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ColorizedOutputStream<BasicColor> colorOutput = new AnsiBasicColorizedOutputStream(output);

        // Set color and write some text
        colorOutput.setForegroundColor(BasicColor.RED);
        colorOutput.print(test);
        Assert.assertTrue(output.size() > testBytes.length);

        // Normal reset should result in some output if controls changed and
        // data output
        int previousSize = output.size();
        colorOutput.reset(false);
        Assert.assertTrue(output.size() > previousSize);

        colorOutput.close();
    }
    
    @Test
    public void colorized_output_reset_04() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ColorizedOutputStream<BasicColor> colorOutput = new AnsiBasicColorizedOutputStream(output);

        // Full reset should always create output
        colorOutput.reset(true);
        Assert.assertTrue(output.size() > 0);

        colorOutput.close();
    }
}
