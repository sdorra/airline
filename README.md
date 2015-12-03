# Airline 2

Airline is a Java library providing an annotation-based framework for parsing command line interfaces.

It supports both simple single commands through to complex git style interfaces with groups.

This is a substantially rewritten fork of the original [airline library](https://github.com/airlift/airline) created based on improvements predominantly developed by myself plus some minor improvements taken from the [Clark & Parsia](https://github.com/clarkparsia/airline) fork.

## Breaking Changes versus 1.x

Airline 2 contains significant breaking changes from Airline 1.x, please see [Migrating.md](Migrating.md) in this folder for more details on how to migrate code forward.

Airline 2.1 contains some further minor breaking changes that should only affect advanced users, again please see [Migrating.md](Migrating.md) in this folder for more details on how to migrate code forward.  Some users may need to add additional Maven dependencies if they were using help formats other than the basic CLI help.

## User Guide

We're currently working on our new website which will eventually include a comprehensive user guide though is currently a work in progress.

You can find the website at [http://rvesse.github.io/airline/](http://rvesse.github.io/airline/)

## Usage

To use airline you need to add a dependency to it to your own code, the Maven artifacts are described later in this file.

You then need to use the various annotations to annotate your command classes:

- `@Command` is used to annotate classes
- `@Option` is used to annotate fields to indicate they are options
- `@Arguments` is used to annotate fields that take in arguments
- `@Inject` can be used to modularise option definitions into separate classes

Please see the [examples](examples/) module for a range of examples that show off the many features of this library and practical examples of using the annotations.

### Single Commands

Simply create a parser instance via `SingleCommand.singleCommand()` passing in a class that is annotated with the `@Command` annotation e.g.

    public static void main(String[] args) {
        SingleCommand<YourClass> parser = SingleCommand.singleCommand(YourClass.class);
        YourClass cmd = parser.parse(args);
        
         // Execute your command however is appropriate e.g.
         cmd.run();   
    }

### Multiple Commands

Create an instance of a `Cli`, this can be done either using the `CliBuilder` or by annotating a class with the `@Cli` annotation.  This is somewhat more complicated so please see the [examples](examples/) module for proper examples.

### Executable JAR

Note that typically you will want to create an executable JAR for your CLI using something like the Maven Shade plugin.  This will then allow you to create a simple wrapper script that invokes your CLI.

    #!/bin/bash
    # myapp
    
    java -jar my-app.jar $*

If this is done you can then invoke your application e.g.

     myapp --global-option command --command-option arguments
     
Or:

    myapp --global-option group --group-option command --command-option arguments
    
## License

Airline is licensed under the Apache Software License Version 2.0, see provided **License.txt**

See provided **Notice.md** for Copyright Holders

## Maven Artifacts

This library is available from [Maven Central](http://search.maven.org) with the latest stable release being `2.1.0`

Use the following maven dependency declaration:

```xml
<dependency>
    <groupId>com.github.rvesse</groupId>
    <artifactId>airline</artifactId>
    <version>2.1.0</version>
</dependency>
```

Snapshot artifacts of the latest source are also available using the version `2.1.1-SNAPSHOT` from the [OSSRH repositories](http://central.sonatype.org/pages/ossrh-guide.html#ossrh-usage-notes).

## Build Status

CI builds are run on [Travis CI](http://travis-ci.org/) ![Build Status](https://travis-ci.org/rvesse/airline.png), see build information and history at https://travis-ci.org/rvesse/airline
