package com.github.rvesse.airline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.github.rvesse.airline.args.Args1;
import com.github.rvesse.airline.model.AliasMetadata;
import com.github.rvesse.airline.parser.ParseOptionConversionException;

public class TestAliases {

    private static final File f = new File("target/alias.config");

    @AfterClass
    public static void cleanup() {
        if (f.exists()) {
            f.delete();
        }
    }

    private static void prepareConfig(File f, String... lines) throws IOException {
        FileWriter writer = new FileWriter(f);
        for (String line : lines) {
            writer.append(line);
            writer.append('\n');
        }
        writer.close();
    }

    @Test
    public void user_aliases_01() throws IOException {
        prepareConfig(f, "foo=Args1 bar");

        //@formatter:off
        Cli<Args1> cli = Cli.<Args1>builder("test")
                            .withCommand(Args1.class)
                            .withUserAliases(f.getName(), null, "target/")
                            .build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getAliases();
        Assert.assertEquals(aliases.size(), 1);

        AliasMetadata alias = aliases.get(0);
        Assert.assertEquals(alias.getName(), "foo");
        List<String> args = alias.getArguments();
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "Args1");
        Assert.assertEquals(args.get(1), "bar");

        // Check parsing
        Args1 cmd = cli.parse("foo");
        Assert.assertEquals(cmd.parameters.size(), 1);
        Assert.assertEquals(cmd.parameters.get(0), "bar");
    }

    @Test
    public void user_aliases_02() throws IOException {
        prepareConfig(f, "a.foo=Args1 bar", "b.foo=Args1 faz");

        //@formatter:off
        Cli<Args1> cli = Cli.<Args1>builder("test")
                            .withCommand(Args1.class)
                            .withUserAliases(f.getName(), "b.", "target/")
                            .build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getAliases();
        Assert.assertEquals(aliases.size(), 1);

        AliasMetadata alias = aliases.get(0);
        Assert.assertEquals(alias.getName(), "foo");
        List<String> args = alias.getArguments();
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "Args1");
        Assert.assertEquals(args.get(1), "faz");

        // Check parsing
        Args1 cmd = cli.parse("foo");
        Assert.assertEquals(cmd.parameters.size(), 1);
        Assert.assertEquals(cmd.parameters.get(0), "faz");
    }
    
    @Test(expectedExceptions = ParseOptionConversionException.class)
    public void user_aliases_positional_01() throws IOException {
        prepareConfig(f, "foo=Args1 -long $1");

        //@formatter:off
        Cli<Args1> cli = Cli.<Args1>builder("test")
                            .withCommand(Args1.class)
                            .withUserAliases(f.getName(), null, "target/")
                            .build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getAliases();
        Assert.assertEquals(aliases.size(), 1);

        AliasMetadata alias = aliases.get(0);
        Assert.assertEquals(alias.getName(), "foo");
        List<String> args = alias.getArguments();
        Assert.assertEquals(args.size(), 3);
        Assert.assertEquals(args.get(0), "Args1");
        Assert.assertEquals(args.get(1), "-long");
        Assert.assertEquals(args.get(2), "$1");

        // Check parsing
        cli.parse("foo");
    }
    
    @Test
    public void user_aliases_positional_02() throws IOException {
        prepareConfig(f, "foo=Args1 -long $1");

        //@formatter:off
        Cli<Args1> cli = Cli.<Args1>builder("test")
                            .withCommand(Args1.class)
                            .withUserAliases(f.getName(), null, "target/")
                            .build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getAliases();
        Assert.assertEquals(aliases.size(), 1);

        AliasMetadata alias = aliases.get(0);
        Assert.assertEquals(alias.getName(), "foo");
        List<String> args = alias.getArguments();
        Assert.assertEquals(args.size(), 3);
        Assert.assertEquals(args.get(0), "Args1");
        Assert.assertEquals(args.get(1), "-long");
        Assert.assertEquals(args.get(2), "$1");

        // Check parsing
        Args1 cmd = cli.parse("foo", "345");
        Assert.assertEquals(cmd.l, 345l);
    }
    
    @Test
    public void user_aliases_override_01() throws IOException {
        prepareConfig(f, "Args1=Args1 bar");

        //@formatter:off
        Cli<Args1> cli = Cli.<Args1>builder("test")
                            .withCommand(Args1.class)
                            .withUserAliases(f.getName(), null, "target/")
                            .build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getAliases();
        Assert.assertEquals(aliases.size(), 1);

        AliasMetadata alias = aliases.get(0);
        Assert.assertEquals(alias.getName(), "Args1");
        List<String> args = alias.getArguments();
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "Args1");
        Assert.assertEquals(args.get(1), "bar");

        // Check parsing
        // By default aliases don't override built-ins
        Args1 cmd = cli.parse("Args1");
        Assert.assertEquals(cmd.parameters.size(), 0);
    }
    
    @Test
    public void user_aliases_override_02() throws IOException {
        prepareConfig(f, "Args1=Args1 bar");

        //@formatter:off
        Cli<Args1> cli = Cli.<Args1>builder("test")
                            .withCommand(Args1.class)
                            .withUserAliases(f.getName(), null, "target/")
                            .withAliasesOverridingBuiltIns()
                            .build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getAliases();
        Assert.assertEquals(aliases.size(), 1);

        AliasMetadata alias = aliases.get(0);
        Assert.assertEquals(alias.getName(), "Args1");
        List<String> args = alias.getArguments();
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "Args1");
        Assert.assertEquals(args.get(1), "bar");

        // Check parsing
        // When enabled aliases can override built-ins
        Args1 cmd = cli.parse("Args1");
        Assert.assertEquals(cmd.parameters.size(), 1);
        Assert.assertEquals(cmd.parameters.get(0), "bar");
    }
}
