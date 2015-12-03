---
layout: page
title: User Guide
---

{% include toc.html %}

## Welcome

Welcome to the Airline Users Guide, this guide is intended to show you how to use every aspect of Airline.

All the examples contained in this user guide may be found in the repository on <a href="{{ site.github.repo }}/tree/master/airline-examples/src/main/java/com/github/rvesse/airline/examples/userguide">GitHub</a>

## Definitions

Before you get started reading this guide it is useful to introduce the terminology that Airline uses to make sure you are clear what we are referring to.

- **Command Line Interface (CLI)**  
  A Command Line Interface (CLI) is a collection of commands potentially grouped into hierarchical groupings.  `git` is a popular example of a CLI
- **Command**  
  A command is a single tool that is invoked by a user, a command may appear within multiple groups within a CLI and take a variety of options and arguments
- **Command Group**  
  A command group is a collection of commands within a CLI identified by a name.  Groups may themselves contain other groups to create hierarchies of commands. `git remote` is an example of a group within a CLI
- **Option**  
  An option is a combination of an identifying name e.g. `--name` followed by zero or more values that are used to populate a field of a command, for example `--name Example`
- **Arguments**  
  Arguments are any values passed to a command that are not otherwise interpreted i.e. they do not represent options, for example `Example`
- **Restriction**  
  A restriction is a constraint placed upon a CLI, options and/or arguments e.g. marking an option as required

## Getting Started

### Adding a Dependency

At a bare minimum you will need to add a dependency on the `airline` library to your project, assuming Maven you can do this like so:

```xml
<dependency>
  <groupId>com.github.rvesse</groupId>
  <artifactId>airline</artifactId>
  <version>X.Y.Z</version>
</dependency>
```

Where `X.Y.Z` is your desired version, the current stable release is `{{ site.version }}`

### Your First Command

At its most basic defining a command in Airline means adding some annotation to a class.

Let's take a look at `GettingStarted.java`:

```java
package com.github.rvesse.airline.examples.userguide;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "getting-started", description = "We're just getting started")
public class GettingStarted {

    @Option(name = { "-f", "--flag" }, description = "An option that requires no values")
    private boolean flag = false;

    @Arguments(description = "Additional arguments")
    private List<String> args;

    public static void main(String[] args) {
        SingleCommand<GettingStarted> parser = SingleCommand.singleCommand(GettingStarted.class);
        GettingStarted cmd = parser.parse(args);
        cmd.run();
    }

    private void run() {
        System.out.println("Flag was " + (this.flag ? "set" : "not set"));
        if (args != null)
            System.out.println("Arguments were " + StringUtils.join(args, ","));
    }
}
```
We'll talk about each of the things introduced in the subsequent sections and provide links to more in-depth pages where you can explore each introduced concept in detail.

#### Defining the Command

At a minimum we need to annotate our class with the `@Command` annotation.  This annotation tells Airline about the command, you must at a minimum specify the `name` field giving it the name of the command.  Names should ideally be short and memorable and they cannot contain whitespace.

Here we've also defined the `description` field which gives a short description of the command.

There are lots of other things that we can define on our command which are described in the [Command Annotation](annotations/command.html) documentation.