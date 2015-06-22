# Airline

Airline is a Java library providing an annotation-based framework for parsing command line interfaces.

It supports both simple single commands through to complex git style interfaces with groups.

This is a substantially improved fork of the original [airline library](https://github.com/airlift/airline) created based on improvements predominantly developed by myself plus some taken from the [Clark & Parsia](https://github.com/clarkparsia/airline) fork.

## Usage

To use airline you need to add a dependency to it to your own code, the Maven artifacts are described later in this file.

You then need to use the various annotations to annotate your command classes:

- `@Command` is used to annotate classes
- `@Option` is used to annotate fields to indicate they are options
- `@Arguments` is used to annotate fields that take in arguments
- `@Inject` can be used to modularize option definitions into separate classes

Please see the [examples](examples/) module for a range of examples that show off the many features of this library and practical examples of using the annotations.

In your `main(String[] args)` method you then need to create a parser instance either via `SingleCommand.singleCommand()` or by creating an instance of a `Cli` using the `CliBuilder` and then call the `parse()` method passing in the provided `args`.  This will return you an instance of the command the user wants to execute and then you can go ahead and execute that however you want.

Note that typically you will want to create an executable JAR for your CLI using something like the Maven Shade plugin.  This will then allow you to create a simple wrapper script that invokes your CLI.

Once that is done you can then invoke your application e.g.

     myapp --global-option command --command-option arguments
     
Or:

    myapp --global-option group --group-option command --command-option arguments
    
### Option Styles
    
Airline supports several option styles and will automatically use the relevant parsing mode depending on how the user passes in the option and how your options are configured.

#### Classic GNU getopt style

    myapp command -abc
    
Could do one of the following three things:

- Set options `-a`, `-b` and `-c`
- Set option `-a` to value `bc`
- Set options `-a` and sets `-b` to value c

Options are processed left to right and as soon as an option with an non-zero arity is seen the remainder of the token up to the next whitespace is considered as the value for the last option seen.  So as in this example exact behaviour will depend on your option definitions.

This option style is only supported for options with a `-N` name where `N` is any single character.

#### Long GNU getopt style

    myapp command -a=bc

Or:

    myapp command --alpha=bc 
   
Sets the option `-a`/`--alpha` option to the value `bc`

May be used with any option provided the option name does not contain an `=`

#### Whitespace separated

    myapp command -a bc
    
Or:

    myapp command --alpha bc

Sets the option `-a`/`--alpha` to the value `bc`

May be used with any option

## License

Airline is licensed under the Apache Software License Version 2.0, see provided **License.txt**

See provided **Notice.md** for Copyright Holders

## Maven Artifacts

This library is available from [Maven Central](http://search.maven.org) with the latest stable release being `0.9.2`

Use the following maven dependency declaration:

```xml
<dependency>
    <groupId>com.github.rvesse</groupId>
    <artifactId>airline</artifactId>
    <version>0.9.2</version>
</dependency>
```

Snapshot artifacts of the latest source are also available using the version `1.0.0-SNAPSHOT` from the [OSSRH repositories](http://central.sonatype.org/pages/ossrh-guide.html#ossrh-usage-notes)

Note that `1.0.0-SNAPSHOT` represents significant breaking changes from the existing `0.9.2` release

## Build Status

CI builds are run on [Travis CI](http://travis-ci.org/) ![Build Status](https://travis-ci.org/rvesse/airline.png), see build information and history at https://travis-ci.org/rvesse/airline
