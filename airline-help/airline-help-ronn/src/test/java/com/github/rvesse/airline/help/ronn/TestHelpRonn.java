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
package com.github.rvesse.airline.help.ronn;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.Git.Add;
import com.github.rvesse.airline.Git.RemoteAdd;
import com.github.rvesse.airline.Git.RemoteShow;
import com.github.rvesse.airline.args.ArgsExamples;
import com.github.rvesse.airline.args.ArgsExitCodes;
import com.github.rvesse.airline.args.ArgsMultiParagraphDiscussion;
import com.github.rvesse.airline.help.Help;
import com.github.rvesse.airline.help.ronn.RonnCommandUsageGenerator;
import com.github.rvesse.airline.help.ronn.RonnGlobalUsageGenerator;
import com.github.rvesse.airline.help.ronn.RonnMultiPageGlobalUsageGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.github.rvesse.airline.SingleCommand.singleCommand;
import static org.testng.Assert.assertEquals;

@Test
@Deprecated
public class TestHelpRonn {
    private final Charset utf8 = Charset.forName("utf-8");

    /**
     * Helper method for if you're trying to determine the differences between
     * actual and expected output when debugging a new test and can't visually
     * see the difference e.g. differing white space
     * 
     * @param actual
     *            Actual
     * @param expected
     *            Expected
     */
    private void testStringAssert(String actual, String expected) {
        if (!actual.equals(expected)) {
            if (actual.length() != expected.length()) {
                System.err.println("Different lengths, expected " + expected.length() + " but got " + actual.length());
            }
            for (int i = 0; i < expected.length(); i++) {
                char e = expected.charAt(i);
                if (i >= actual.length()) {
                    System.err.println("Expected character '" + e + "' (Code " + (int) e + ") is at position " + i
                            + " which is beyond the length of the actual string");
                    break;
                }
                char a = actual.charAt(i);
                if (e != a) {
                    System.err.println("Expected character '" + e + "' (Code " + (int) e + ") at position " + i
                            + " does not match actual character '" + a + "' (Code " + (int) a + ")");
                    int start = Math.max(0, i - 10);
                    int end = Math.min(expected.length(), i + 10);
                    System.err.println("Expected Context:");
                    System.err.println(expected.substring(start, end));
                    System.err.println("Actual Context:");
                    end = Math.min(actual.length(), i + 10);
                    System.err.println(actual.substring(start, end));
                    break;
                }
            }
        }
        assertEquals(actual, expected);
    }

    /**
     * Helper method that reads in the given file
     * 
     * @param f
     *            File
     * @return File contents
     * @throws IOException
     */
    private String readFile(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line = reader.readLine();
        StringBuilder builder = new StringBuilder();
        while (line != null) {
            builder.append(line).append('\n');
            line = reader.readLine();
        }
        reader.close();
        return builder.toString();
    }
    
    public void testMultiParagraphDiscussionRonn() throws IOException {
        SingleCommand<ArgsMultiParagraphDiscussion> cmd = singleCommand(ArgsMultiParagraphDiscussion.class);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        RonnCommandUsageGenerator generator = new RonnCommandUsageGenerator();
        generator.usage(null, null, "ArgsMultiParagraphDiscussion", cmd.getCommandMetadata(), out);
        //@formatter:off
        testStringAssert(new String(out.toByteArray(), utf8), 
                "ArgsMultiParagraphDiscussion(1) -- null\n" +
                "==========\n" +
                "\n" +
                "## SYNOPSIS\n" +
                "\n" +
                " `ArgsMultiParagraphDiscussion` \n" +
                "\n" +
                "## DISCUSSION\n" +
                "\n" +
                "First paragraph\n" +
                "\n" +
                "Middle paragraph\n" +
                "\n" +
                "Final paragraph\n" + 
                "\n");
        //@formatter:on
    }
    
   
    
    public void testExamplesRonn() throws IOException {
        SingleCommand<ArgsExamples> cmd = singleCommand(ArgsExamples.class);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        RonnCommandUsageGenerator generator = new RonnCommandUsageGenerator();
        generator.usage(null, null, "ArgsExamples", cmd.getCommandMetadata(), out);
        //@formatter:off
        testStringAssert(new String(out.toByteArray(), utf8), 
                "ArgsExamples(1) -- null\n" +
                "==========\n" +
                "\n" +
                "## SYNOPSIS\n" +
                "\n" +
                " `ArgsExamples` \n" +
                "\n" +
                "## EXAMPLES\n" +
                "\n" +
                "    ArgsExample\n" +
                "\n" +
                "Does nothing\n" +
                "\n" +
                "    ArgsExample foo bar\n" +
                "\n" + 
                "Foos a bar\n" +
                "\n");
        //@formatter:on
    }

    @Test
    public void testRonn() throws IOException {
        //@formatter:off
        CliBuilder<Runnable> builder = Cli.<Runnable>builder("git")
                .withDescription("the stupid content tracker")
                .withDefaultCommand(Help.class)
                .withCommand(Help.class)
                .withCommand(Add.class);

        builder.withGroup("remote")
                .withDescription("Manage set of tracked repositories")
                .withDefaultCommand(RemoteShow.class)
                .withCommand(RemoteShow.class)
                .withCommand(RemoteAdd.class);

        Cli<Runnable> gitParser = builder.build();
        
        RonnGlobalUsageGenerator<Runnable> generator = new RonnGlobalUsageGenerator<Runnable>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.usage(gitParser.getMetadata(), out);
        String usage = new String(out.toByteArray(), utf8);
        assertEquals(usage,
                "git(1) -- the stupid content tracker\n" +
                "==========\n" +
                "\n" +
                "## SYNOPSIS\n" +
                "\n" +
                "`git` [ -v ] [<group>] <command> [command-args]\n" +
                "\n" +
                "## OPTIONS\n" +
                "\n" +
                "* `-v`:\n" +
                "  Verbose mode\n" +
                "\n" +
                "## COMMAND GROUPS\n" +
                "\n" +
                "Commands are grouped as follows:\n" +
                "\n" +
                "* Default (no <group> specified)\n" +
                "\n" +
                "  * `add`:\n" +
                "  Add file contents to the index\n" +
                "\n" +
                "  * `help`:\n" +
                "  Display help information\n" +
                "\n" +
                "* **remote**\n" +
                "\n" +
                "  Manage set of tracked repositories\n" +
                "\n" +
                "  * `add`:\n" +
                "  Adds a remote\n" +
                "\n" +
                "  * `show`:\n" +
                "  Gives some information about the remote <name>\n" +
                "\n" +
                "---\n" +
                "\n" +
                "## git-add(1)\n" +
                "\n" +
                "### SYNOPSIS\n" +
                "\n" +
                "`git` [ -v ] `add` [ -i ] [--] [ <patterns>... ]\n" +
                "\n" +
                "Add file contents to the index\n" +
                "\n" +
                "### OPTIONS\n" +
                "\n" +
                "* `-i`:\n" +
                "  Add modified contents interactively.\n" +
                "\n" +
                "* `-v`:\n" +
                "  Verbose mode\n" +
                "\n" +
                "* `--`:\n" +
                "This option can be used to separate command-line options from the list of arguments (useful when arguments might be mistaken for command-line options).\n" +
                "\n" +
                "* <patterns>:\n" +
                "Patterns of files to be added\n" +
                "\n" +
                "---\n" +
                "\n" +
                "## git-help(1)\n" +
                "\n" +
                "### SYNOPSIS\n" +
                "\n" +
                "`git` `help`  [--] [ <command>... ]\n" +
                "\n" +
                "Display help information\n" +
                "\n" +
                "### OPTIONS\n" +
                "\n" +
                "* `--`:\n" +
                "This option can be used to separate command-line options from the list of arguments (useful when arguments might be mistaken for command-line options).\n" +
                "\n" +
                "* <command>:\n" +
                "\n" +
                "\n" +
                "---\n" +
                "\n" +
                "## git-remote-add(1)\n" +
                "\n" +
                "### SYNOPSIS\n" +
                "\n" +
                "`git` [ -v ] `remote` `add` [ -t <branch> ] [--] [ <name> <url>... ]\n" +
                "\n" +
                "Adds a remote\n" +
                "\n" +
                "### OPTIONS\n" +
                "\n" +
                "* `-t` <branch>:\n" +
                "  Track only a specific branch\n" +
                "\n" +
                "* `-v`:\n" +
                "  Verbose mode\n" +
                "\n" +
                "* `--`:\n" +
                "This option can be used to separate command-line options from the list of arguments (useful when arguments might be mistaken for command-line options).\n" +
                "\n" +
                "* <name> <url>:\n" +
                "Name and URL of remote repository to add\n" +
                "\n" +
                "---\n" +
                "\n" +
                "## git-remote-show(1)\n" +
                "\n" +
                "### SYNOPSIS\n" +
                "\n" +
                "`git` [ -v ] `remote` `show` [ -n ] [--] [ <remote> ]\n" +
                "\n" +
                "Gives some information about the remote <name>\n" +
                "\n" +
                "### OPTIONS\n" +
                "\n" +
                "* `-n`:\n" +
                "  Do not query remote heads\n" +
                "\n" +
                "* `-v`:\n" +
                "  Verbose mode\n" +
                "\n" +
                "* `--`:\n" +
                "This option can be used to separate command-line options from the list of arguments (useful when arguments might be mistaken for command-line options).\n" +
                "\n" +
                "* <remote>:\n" +
                "Remote to show\n" +
                "\n" +
                "---\n" +
                "\n");
        //@formatter:on
    }

    @Test
    public void testRonnMultiPage() throws IOException {
        //@formatter:off
        CliBuilder<Runnable> builder = Cli.<Runnable>builder("git")
                .withDescription("the stupid content tracker")
                .withDefaultCommand(Help.class)
                .withCommand(Help.class)
                .withCommand(Add.class);

        builder.withGroup("remote")
                .withDescription("Manage set of tracked repositories")
                .withDefaultCommand(RemoteShow.class)
                .withCommand(RemoteShow.class)
                .withCommand(RemoteAdd.class);

        Cli<Runnable> gitParser = builder.build();
        
        RonnGlobalUsageGenerator<Runnable> generator = new RonnMultiPageGlobalUsageGenerator<Runnable>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        generator.usage(gitParser.getMetadata(), out);
        String usage = new String(out.toByteArray(), utf8);
        assertEquals(usage,
                "git(1) -- the stupid content tracker\n" +
                "==========\n" +
                "\n" +
                "## SYNOPSIS\n" +
                "\n" +
                "`git` [ -v ] [<group>] <command> [command-args]\n" +
                "\n" +
                "## OPTIONS\n" +
                "\n" +
                "* `-v`:\n" +
                "  Verbose mode\n" +
                "\n" +
                "## COMMAND GROUPS\n" +
                "\n" +
                "Commands are grouped as follows:\n" +
                "\n" +
                "* Default (no <group> specified)\n" +
                "\n" +
                "  * `git-add(1)`:\n" +
                "  Add file contents to the index\n" +
                "\n" +
                "  * `git-help(1)`:\n" +
                "  Display help information\n" +
                "\n" +
                "* **remote**\n" +
                "\n" +
                "  Manage set of tracked repositories\n" +
                "\n" +
                "  * `git-remote-add(1)`:\n" +
                "  Adds a remote\n" +
                "\n" +
                "  * `git-remote-show(1)`:\n" +
                "  Gives some information about the remote <name>");
        
        File gitHelp = new File("git-help.1.ronn");
        Assert.assertTrue(gitHelp.exists());
        usage = readFile(gitHelp);
        assertEquals(usage,
                "git-help(1) -- Display help information\n" +
                "==========\n" +
                "\n" +
                "## SYNOPSIS\n" +
                "\n" +
                "`git` `help`  [--] [ <command>... ]\n" +
                "\n" +
                "## OPTIONS\n" +
                "\n" +
                "* `--`:\n" +
                "This option can be used to separate command-line options from the list of arguments (useful when arguments might be mistaken for command-line options).\n" +
                "\n" +
                "* <command>:\n" +
                "\n\n" +
                "## GIT\n" +
                "\n" +
                "Part of the `git(1)` suite\n");
        gitHelp.delete();
        
        File gitAdd = new File("git-add.1.ronn");
        Assert.assertTrue(gitAdd.exists());
        usage = readFile(gitAdd);
        assertEquals(usage,
                "git-add(1) -- Add file contents to the index\n" +
                "==========\n" +
                "\n" +
                "## SYNOPSIS\n" +
                "\n" +
                "`git` [ -v ] `add` [ -i ] [--] [ <patterns>... ]\n" +
                "\n" +
                "## OPTIONS\n" +
                "\n" +
                "* `-i`:\n" +
                "  Add modified contents interactively.\n" +
                "\n" +
                "* `-v`:\n" +
                "  Verbose mode\n" +
                "\n" +
                "* `--`:\n" +
                "This option can be used to separate command-line options from the list of arguments (useful when arguments might be mistaken for command-line options).\n" +
                "\n" +
                "* <patterns>:\n" +
                "Patterns of files to be added\n" +
                "\n" +
                "## GIT\n" +
                "\n" +
                "Part of the `git(1)` suite\n");
        gitAdd.delete();
        
        File gitRemoteShow = new File("git-remote-show.1.ronn");
        Assert.assertTrue(gitRemoteShow.exists());
        usage = readFile(gitRemoteShow);
        assertEquals(usage,
                "git-remote-show(1) -- Gives some information about the remote <name>\n" +
                "==========\n" +
                "\n" +
                "## SYNOPSIS\n" +
                "\n" +
                "`git` [ -v ] `remote` `show` [ -n ] [--] [ <remote> ]\n" +
                "\n" +
                "## OPTIONS\n" +
                "\n" +
                "* `-n`:\n" +
                "  Do not query remote heads\n" +
                "\n" +
                "* `-v`:\n" +
                "  Verbose mode\n" +
                "\n" +
                "* `--`:\n" +
                "This option can be used to separate command-line options from the list of arguments (useful when arguments might be mistaken for command-line options).\n" +
                "\n" +
                "* <remote>:\n" +
                "Remote to show\n" +
                "\n" +
                "## GIT\n" +
                "\n" +
                "Part of the `git(1)` suite\n");
        gitRemoteShow.delete();
        
        File gitRemoteAdd = new File("git-remote-add.1.ronn");
        Assert.assertTrue(gitRemoteAdd.exists());
        usage = readFile(gitRemoteAdd);
        assertEquals(usage,
                "git-remote-add(1) -- Adds a remote\n" +
                "==========\n" +
                "\n" +
                "## SYNOPSIS\n" +
                "\n" +
                "`git` [ -v ] `remote` `add` [ -t <branch> ] [--] [ <name> <url>... ]\n" +
                "\n" +
                "## OPTIONS\n" +
                "\n" +
                "* `-t` <branch>:\n" +
                "  Track only a specific branch\n" +
                "\n" +
                "* `-v`:\n" +
                "  Verbose mode\n" +
                "\n" +
                "* `--`:\n" +
                "This option can be used to separate command-line options from the list of arguments (useful when arguments might be mistaken for command-line options).\n" +
                "\n" +
                "* <name> <url>:\n" +
                "Name and URL of remote repository to add\n" +
                "\n" +
                "## GIT\n" +
                "\n" +
                "Part of the `git(1)` suite\n");
        gitRemoteAdd.delete();
        //@formatter:on
    }

    @Test
    public void testExitCodesRonn() throws IOException {
        //@formatter:off
        SingleCommand<ArgsExitCodes> command = singleCommand(ArgsExitCodes.class);
    
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new RonnCommandUsageGenerator().usage(null, null, "test", command.getCommandMetadata(), out);
        assertEquals(new String(out.toByteArray(), utf8),
                "test(1) -- ArgsExitCodes description\n" +
                "==========\n" +
                "\n" +
                "## SYNOPSIS\n" +
                "\n" +
                " `test` \n" +
                "\n" +
                "## EXIT CODES\n" +
                "\n" +
                "This command returns one of the following exit codes:\n" +
                "\n" +
                "* ** 0 ** - Success\n" +
                "* ** 1 **\n" +
                "* ** 2 ** - Error 2\n");
        //@formatter:on
    }
}
