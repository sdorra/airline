# Airline

Airline is a Java annotation-based framework for parsing Git like command line structures.

This fork contains several improvements over the upstream fork created both by myself and taken from the [Clark & Parsia](https://github.com/clarkparsia/airline) fork:

- Annotation improvements
    - `Option` improvements:
        - `allowedValues` properties is actually enforced and produces `ParseOptionIllegalValueException` if an invalid value is received
        - New `override` and `sealed` properties allowing derived commands to change some properties of the annotation
        - Overridden options may change the type when it is a narrowing conversion
        - New `completionBehaviour` and `completionCommand` properties allowing defining behaviours for the purposes of completion script generators
        - New `DefaultOption` to mark an option the default allowing the name to be omitted under some circumstances
    - `Arguments` improvements
        - New `completionBehaviour` and `completionCommand` properties allowing defining behaviours for the purposes of completion script generators
        - New `arity` for limiting maximum arity of arguments
    - `Group` annotation for specifying groups
    - `Command` improvements
        - `discussion` and `examples` properties for providing more detailed help information
        - `groupNames` property for specifying the groups to which a command should belong
        - `exitCodes` and `exitDescriptions` annotations for describing the exit statuses for a command
- Parser Improvements
    - Support for opt-in command abbreviation support
    - Support for opt-in option abbreviation support
- Help system improvements
    - Help printing respects new lines allowing them to be used in longer descriptions
    - Support for additional examples and discussion sections in command help
    - Documentation of `allowedValues` for options with restricted set of values
    - Documentation of `exitCodes` and `exitDescriptions` for commands
    - Abstracted out help generation system into interfaces with multiple concrete implementations:
        - Command Line (the existing help system)
        - [Ronn](http://rtomayko.github.io/ronn/) which can be easily converted into `man` pages
        - HTML
        - Bash auto-completion scripts
        - All generators break out their logic into `protected` methods so that individual portions of a generator can be overridden and customised
- Support for Command Factories

Please note that some of the features and improvements listed here may not yet have made it into the stable release build and may only be available in the SNAPSHOT builds

## License

Airline is licensed under the Apache Software License Version 2.0, see provided **License.txt**

See provided **Notice.md** for Copyright Holders

## Artifacts

This library is available from [Maven Central](http://search.maven.org) with the latest release being `0.9.2`

Use the following maven dependency declaration:

```xml
<dependency>
    <groupId>com.github.rvesse</groupId>
    <artifactId>airline</artifactId>
    <version>0.9.2</version>
</dependency>
```

Snapshot artifacts of the latest source are also available using the version `0.9.3-SNAPSHOT` from the [OSSRH repositories](http://central.sonatype.org/pages/ossrh-guide.html#ossrh-usage-notes)

## Build Status

CI builds are run on [Travis CI](http://travis-ci.org/) ![Build Status](https://travis-ci.org/rvesse/airline.png), see build information and history at https://travis-ci.org/rvesse/airline

# Example Usage

Here is a quick example:

```java
public class Git
{
    public static void main(String[] args)
    {
        CliBuilder<Runnable> builder = Cli.<Runnable>builder("git")
                .withDescription("the stupid content tracker")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, Add.class);

        builder.withGroup("remote")
                .withDescription("Manage set of tracked repositories")
                .withDefaultCommand(RemoteShow.class)
                .withCommands(RemoteShow.class, RemoteAdd.class);

        Cli<Runnable> gitParser = builder.build();

        gitParser.parse(args).run();
    }

    public static class GitCommand implements Runnable
    {
        @Option(type = OptionType.GLOBAL, name = "-v", description = "Verbose mode")
        public boolean verbose;

        public void run()
        {
            System.out.println(getClass().getSimpleName());
        }
    }

    @Command(name = "add", description = "Add file contents to the index")
    public static class Add extends GitCommand
    {
        @Arguments(description = "Patterns of files to be added")
        public List<String> patterns;

        @Option(name = "-i", description = "Add modified contents interactively.")
        public boolean interactive;
    }

    @Command(name = "show", description = "Gives some information about the remote <name>")
    public static class RemoteShow extends GitCommand
    {
        @Option(name = "-n", description = "Do not query remote heads")
        public boolean noQuery;

        @Arguments(description = "Remote to show")
        public String remote;
    }

    @Command(name = "add", description = "Adds a remote")
    public static class RemoteAdd extends GitCommand
    {
        @Option(name = "-t", description = "Track only a specific branch")
        public String branch;

        @Arguments(description = "Remote repository to add")
        public List<String> remote;
    }
}
```

Assuming you have packaged this as an executable program named 'git', you would be able to execute the following commands:

```shell
$ git add -p file

$ git remote add origin git@github.com:airlift/airline.git

$ git -v remote show origin
```

# Single Command Mode

Airline can also be used for simple, single-command programs:

```java
@Command(name = "ping", description = "network test utility")
public class Ping
{
    @Inject
    public HelpOption helpOption;

    @Option(name = {"-c", "--count"}, description = "Send count packets")
    public int count = 1;

    public static void main(String... args)
    {
        Ping ping = SingleCommand.singleCommand(Ping.class).parse(args);

        if (ping.helpOption.showHelpIfRequested()) {
            return;
        }

        ping.run();
    }

    public void run()
    {
        System.out.println("Ping count: " + count);
    }
}
```

Assuming you have packaged this as an executable program named 'ping', you would be able to execute the following commands:

```shell
$ ping

$ ping -c 5

$ ping --help
```

# Help System

Airline contains a fully automated help system, which generates man-page-like documentation driven by the Java
annotations.

As you may have noticed in the git code above, we added `Help.class` to the cli.  This command is provided by Airline and works as follows:

```shell
$ git help
usage: git [-v] <command> [<args>]

The most commonly used git commands are:
    add       Add file contents to the index
    help      Display help information
    remote    Manage set of tracked repositories

See 'git help <command>' for more information on a specific command.


$ git help git
NAME
        git - the stupid content tracker

SYNOPSIS
        git [-v] <command> [<args>]

OPTIONS
        -v
            Verbose mode

COMMANDS
        help
            Display help information

        add
            Add file contents to the index

        remote show
            Gives some information about the remote <name>

        remote add
            Adds a remote



$ git help add
NAME
        git add - Add file contents to the index

SYNOPSIS
        git [-v] add [-i] [--] [<patterns>...]

OPTIONS
        -i
            Add modified contents interactively.

        -v
            Verbose mode

        --
            This option can be used to separate command-line options from the
            list of argument, (useful when arguments might be mistaken for
            command-line options

        <patterns>
            Patterns of files to be added



$ git help remote
NAME
        git remote - Manage set of tracked repositories

SYNOPSIS
        git [-v] remote
        git [-v] remote add [-t <branch>]
        git [-v] remote show [-n]

OPTIONS
        -v
            Verbose mode

COMMANDS
        With no arguments, Gives some information about the remote <name>

        show
            Gives some information about the remote <name>

            With -n option, Do not query remote heads

        add
            Adds a remote

            With -t option, Track only a specific branch



$ git help remote show
NAME
        git remote show - Gives some information about the remote <name>

SYNOPSIS
        git [-v] remote show [-n] [--] [<remote>]

OPTIONS
        -n
            Do not query remote heads

        -v
            Verbose mode

        --
            This option can be used to separate command-line options from the
            list of argument, (useful when arguments might be mistaken for
            command-line options

        <remote>
            Remote to show
```

We have also, add `Help.class` as the default command for git, so if you execute git without any arguments, you will see the following:

```shell
$ git help
usage: git [-v] <command> [<args>]

The most commonly used git commands are:
    add       Add file contents to the index
    help      Display help information
    remote    Manage set of tracked repositories

See 'git help <command>' for more information on a specific command.
```

For simple, single-command programs like ping, use the `HelpOption` option as shown in the example above.
`HelpOption` handles the options `-h` and `--help` and provides the `showHelpIfRequested()` method
to automatically show the following help output:

```shell
$ ping -h
NAME
        ping - network test utility

SYNOPSIS
        ping [(-c <count> | --count <count>)] [(-h | --help)]

OPTIONS
        -c <count>, --count <count>
            Send count packets

        -h, --help
            Display help information
```