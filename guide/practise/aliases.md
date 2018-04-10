---
layout: page
title: User Defined Aliases
---

In many more powerful CLIs such as `git` much of the power comes from providing the user with the ability to define their own command aliases.  Aliases are essentially user defined shortcuts that allow users to define their own commands that call other commands with specific options.

For example say we have a `log` command that is going to output log information in a variety of formats:

```java
public class Logs implements ExampleRunnable {
    
    public static enum Format {
        PlainText,
        Json,
        Xml
    }
    
    @Option(name = { "-f", "--format" }, 
            title = "Format", 
            description = "Sets the desired output format")
    private Format format = Format.PlainText;

    @Override
    public int run() {
        // In a real command actual implementation would go here...
        System.out.println("Output Format: " + this.format.name());
        return 0;
    }
}
```

Here the user can choose from several possible formats with the `--format` option.  Users who always prefer a particular format may wish to define an alias `json` that always outputs the JSON format.  So in effect we want to allow the user to type  `cli json` rather than `cli logs --format json`.  Airline's user defined aliases mechanism allows us to do exactly this.

## Defining Aliases

Aliases can be defined in two ways:

- By the developer of the CLI using annotations/fluent API
- By the user provided that the developer configures the CLI to support aliases

For the first approach you can use the [`@Alias`](../annotations/alias.html) annotation in conjunction with your [`@Parser`](../annotations/parser.html) to pre-define some aliases for the user.

However what we are interested in here is enabling users to define their own aliases.  So let's take a look at a CLI that uses the second approach:

```java
/**
 * An example of creating a CLI that takes advantage of the aliases feature.
 * <p>
 * Aliases provide a means by which you can define additional top level commands
 * that simply delegate to actual commands. Often you actually want to leave
 * alias definition up to end users and so Airline supports reading in aliases
 * from a user configuration file out of the box
 * </p>
 *
 */
public class UserAliasedCli {

    public static void main(String[] args) {
        //@formatter:off
        @SuppressWarnings("unchecked")
        // The program name is cli
        CliBuilder<ExampleRunnable> builder = Cli.<ExampleRunnable>builder("cli")
                                                 // Add a description
                                                 .withDescription("A simple CLI with several commands available")
                                                 // Define some commands
                                                 .withCommand(Simple.class)
                                                 .withCommands(Parent.class, Child.class, GoodGrandchild.class, Logs.class)
                                                 .withCommand(Help.class);
        //@formatter:on
        // Read aliases from user configuration file
        // You can find this example configuration under
        // src/main/resources/aliases.config
        //@formatter:off
        builder.withParser()
               .withUserAliases("aliases.config", null, "~/.cli/", "src/main/resources/");
        //@formatter:on

        ExampleExecutor.executeCli(builder.build(), args);
    }

}
```

Here we see a simple CLI defined with a small selection of commands including our `Logs` command we previously defined.  The important piece that enables aliases is as follows:

```java
builder.withParser()
        withUserAliases("aliases.config", null, "~/.cli/", "src/main/resources/");
```

Note that if you were using the [`@Parser`](../annotations/parser.html) annotation you could also use the various fields of that annotation to configure the same functionality:

```java
@Parser(userAliasesFile = "aliases.config", 
        userAliasesSearchLocation = { "~/.cli/", "src/main/resources/" })
```

This configuration specifies that Airline should look for a file named `aliases.config` in either `~/.cli/` or in `src/main/resources/`.  Note that Airline allows use of `~/` as a reference to the users home directory.

### Search Locations

All locations are searched both on the filesystem and the JVM class path (if applicable) and their contents are merged.  

Search locations should be given in order of preference, so if files were found in both locations and both defined the same alias the definition from the first location would be used.

### Aliases File

The actual aliases file is a standard Java properties file, an alias is defined by adding a line of the form `alias=command`.  So for our earlier example our file should contain the following:

```
json=logs --format Json
```

So if Airline now finds that the user has typed in `json` as the command name it knows to expand this to `logs --format Json` before continuing parsing.

Essentially the left hand side is the name of the alias i.e. the name users will enter at your CLI and the right hand side is the expansion of that alias.

### Advanced Alias Behaviours

#### Aliases vs Built-In's

So what happens if a CLI defines a `logs` command and the user tries to define a `logs` alias?

The default is that the built-in command takes precedence so the users alias would be ignored.

Some times if might be desirable to allow users to override built-ins in which case you can either call `withAliasesOverridingBuiltIns()` on the Fluent API or adding the `aliasesOverrideBuiltIns = true` field to your `@Parser` annotation.

#### Alias Chaining

By default aliases cannot be defined in terms of other aliases, so the following would not be legal:

```
l=logs --format
json=l Json
```
If the user tries to use their `json` alias they would get an exception stating that the command `l` was not found because Airline only expands the first alias encountered (if any).

However developers may optionally enable this for their CLIs by enabling alias chaining.  This is done either by calling `withAliasesChaining()` on the Fluent API or adding the `aliasesMayChain = true` field to your `@Parser` annotation.

##### Circular Reference Chains

Even with alias chaining enabled Airline will still prevent you from creating circular reference chains.  For example consider the following aliases file:

```
foo=bar
bar=foo
```
While the definitions are acceptable, since they are only validated at resolution time, if a user attempted to invoke the alias `foo` they would receive an error like the following:

> Circular alias reference detected, aliases chain [foo, bar] references foo which was already resolved

##### Alias Chaining and Overriding Built-In's

It is legal to enable both overriding built-in's and chaining at the same time.  However in this case it may be possible to create alias definitions that look logical but fail to resolve.  Consider the following:

```
logs=logs --format Json
json=logs --format Json
xml=logs --format Xml
```

Here the user tries to define that the `logs` command always defaults to Json format.  They also define `json` and `xml` aliases to ask for the logs in those formats.  However when Airline attempts to resolve this you will end up with a circular reference error as above.  This is because when alias chaining is resolved Airline always tries to resolve as many aliases as possible until it hits a built-in or a non-alias.  If overriding is enabled it always favours aliases over built-in's so you have to be careful if trying to chain aliases and redefine built-ins.

Therefore it is generally recommended to only enable chaining or overriding but not both together.

#### Positional Parameters

Sometimes it may be desirable to define a more complex alias that does partial expansion while injecting other user inputs.  Airline provides positional parameter support to enable this.  A positional parameter is created by using a string like `$1` in your alias definition e.g.

```
l=logs --format $1
```
Positional parameters are resolved by taking the user inputs after the alias in order.  So this alias definition says that when the `l` alias is used it expands to `logs --format` followed by the first input after the `l`.  For example if a user types `cli l Json` this expands to `cli logs --format Json`.

{% include alert.html %}
If you use positional parameters in your aliases and no suitable user input is provided to resolve those parameters they are expanded as-is.  For example if a user types `cli l` this expands to `cli logs --format $1` which would result in a parsing error as `$1` is not a valid format.
{% include end-alert.html %}