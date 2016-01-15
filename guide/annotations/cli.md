---
layout: page
title: Cli Annotation
---

## `@Cli`

The `@Cli` annotation is applied to a class to define a complex CLI consisting of potentially many commands.

At a minimum you need to define a name and at least one command for your CLI e.g.

```java
@Cli(name = "basic", 
     defaultCommand = GettingStarted.class, 
     commands = { GettingStarted.class, Tool.class })
public class BasicCli { }
```

Here we define a CLI named `basic` that has a default command of `GettingStarted.class` which we saw on the introductory page of the [User Guide](../) and also contains the command `Tool.class` which we saw in the examples for the [`@Command`](command.html) annotation.

{% include alert.html %}
Names are restricted to not contain whitespace but otherwise can contain whatever characters you want.
	
Remember that users need to be able to type the name at their command line terminal of choice so it is best to limit yourself to common characters i.e. alphanumerics and common punctuation marks.

### Description

As with commands typically we also want to add a `description` that describes what a CLI does e.g.

```java
@Cli(name = "basic", 
    description = "Provides a basic example CLI",
    defaultCommand = GettingStarted.class, 
    commands = { GettingStarted.class, Tool.class })
public class BasicCli {
```

### Commands

### Default Command

### Groups

### Global Restrictions

### Parser Configuration

Parser configuration for a CLI may be specified via the `parserConfiguration` field which takes a [`@Parser`](parser.html) annotation.  Please see the documentation for that annotation for notes on controlling the parser configuration.

**TODO Write up the rest of the @Cli annotation**