package com.github.rvesse.airline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.github.rvesse.airline.args.Args1;
import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.model.AliasMetadata;
import com.github.rvesse.airline.parser.errors.ParseOptionConversionException;

public class TestAliases {

    private static final File f = new File("target/test.config");

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
    public void user_aliases_default_01() throws IOException {
        prepareConfig(f, "foo=Args1 bar");

        //@formatter:off
        CliBuilder<Args1> builder = Cli.<Args1>builder("test")
                                       .withCommand(Args1.class);
        builder.withParser().withUserAliases("target/");
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getParserConfiguration().getAliases();
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
    public void user_aliases_default_02() throws IOException {
        prepareConfig(f, "foo=Args1 bar");

        //@formatter:off
        CliBuilder<Args1> builder = Cli.<Args1>builder("other")
                                       .withCommand(Args1.class);
        builder.withParser()
               .withUserAliases("target/");
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getParserConfiguration().getAliases();
        Assert.assertEquals(aliases.size(), 0);
    }

    @Test
    public void user_aliases_01() throws IOException {
        prepareConfig(f, "foo=Args1 bar");

        //@formatter:off
        CliBuilder<Args1> builder = Cli.<Args1>builder("test")
                            .withCommand(Args1.class);
        builder.withParser()
               .withUserAliases(f.getName(), null, "target/");
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getParserConfiguration().getAliases();
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
        CliBuilder<Args1> builder = Cli.<Args1>builder("test")
                                       .withCommand(Args1.class);
        builder.withParser()
               .withUserAliases(f.getName(), "b.", "target/");
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getParserConfiguration().getAliases();
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
        CliBuilder<Args1> builder = Cli.<Args1>builder("test")
                                       .withCommand(Args1.class);
        builder.withParser()
               .withUserAliases(f.getName(), null, "target/");
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getParserConfiguration().getAliases();
        Assert.assertEquals(aliases.size(), 1);

        AliasMetadata alias = aliases.get(0);
        Assert.assertEquals(alias.getName(), "foo");
        List<String> args = alias.getArguments();
        Assert.assertEquals(args.size(), 3);
        Assert.assertEquals(args.get(0), "Args1");
        Assert.assertEquals(args.get(1), "-long");
        Assert.assertEquals(args.get(2), "$1");

        // Check parsing
        // Should error because the positional parameter $1 will fail to expand
        // and be passed as-is to the -long option resulting in a type
        // conversion error
        cli.parse("foo");
    }

    @Test
    public void user_aliases_positional_02() throws IOException {
        prepareConfig(f, "foo=Args1 -long $1");

        //@formatter:off
        CliBuilder<Args1> builder = Cli.<Args1>builder("test")
                                       .withCommand(Args1.class);
        builder.withParser()
               .withUserAliases(f.getName(), null, "target/");
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getParserConfiguration().getAliases();
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
        Assert.assertEquals(cmd.parameters.size(), 0);
    }

    @Test
    public void user_aliases_positional_03() throws IOException {
        //@formatter:off
        CliBuilder<Args1> builder = Cli.<Args1>builder("test")
                                    .withCommand(Args1.class);
        builder.withParser()
               .withAlias("foo")
               .withArguments("Args1", "-long", "$1", "-float", "$3");
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check parsing
        Args1 cmd = cli.parse("foo", "345", "bar", "1.23");
        Assert.assertEquals(cmd.l, 345l);
        Assert.assertEquals(cmd.floa, 1.23f);
        List<String> args = cmd.parameters;
        Assert.assertEquals(args.size(), 1);
        Assert.assertEquals(args.get(0), "bar");
    }

    @Test
    public void user_aliases_positional_04() throws IOException {
        //@formatter:off
        CliBuilder<Args1> builder = Cli.<Args1>builder("test")
                                    .withCommand(Args1.class);
        builder.withParser()
               .withAlias("foo")
               .withArguments("Args1", "-long", "$1", "-float", "$5");
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check parsing
        Args1 cmd = cli.parse("foo", "345", "a", "b", "c", "1.23", "d", "e");
        Assert.assertEquals(cmd.l, 345l);
        Assert.assertEquals(cmd.floa, 1.23f);
        List<String> args = cmd.parameters;
        Assert.assertEquals(args.size(), 5);
        for (int i = 0, c = 'a'; i < args.size(); i++, c++) {
            Assert.assertEquals(args.get(i), new String(new char[] { (char) c }));
        }
    }

    @Test
    public void user_aliases_override_01() throws IOException {
        prepareConfig(f, "Args1=Args1 bar");

        //@formatter:off
        CliBuilder<Args1> builder = Cli.<Args1>builder("test")
                                       .withCommand(Args1.class);
        builder.withParser()
               .withUserAliases(f.getName(), null, "target/");
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getParserConfiguration().getAliases();
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
        CliBuilder<Args1> builder = Cli.<Args1>builder("test")
                                       .withCommand(Args1.class);
        builder.withParser()
               .withUserAliases(f.getName(), null, "target/")
               .withAliasesOverridingBuiltIns();
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getParserConfiguration().getAliases();
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

    @Test
    public void user_aliases_no_args() throws IOException {
        prepareConfig(f, "foo=Args1 bar");

        //@formatter:off
        CliBuilder<Args1> builder = Cli.<Args1>builder("test")
                                       .withCommand(Args1.class)
                                       .withDefaultCommand(Args1.class);
        builder.withParser()
               .withUserAliases(f.getName(), null, "target/");
        Cli<Args1> cli = builder.build();
        //@formatter:on

        // Check definition
        List<AliasMetadata> aliases = cli.getMetadata().getParserConfiguration().getAliases();
        Assert.assertEquals(aliases.size(), 1);

        AliasMetadata alias = aliases.get(0);
        Assert.assertEquals(alias.getName(), "foo");
        List<String> args = alias.getArguments();
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), "Args1");
        Assert.assertEquals(args.get(1), "bar");

        // Check parsing
        cli.parse();
    }
}
