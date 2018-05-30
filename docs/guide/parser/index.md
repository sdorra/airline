---
layout: page
title: Parsing
---

The core of Airline is its parser which takes in string arguments passed into the JVM and turns that into a command instance with appropriately populated fields that your code can then use to actually run its intended function.

The parser is designed to be highly customisable allowing various aspects to be configured to suit your purposes. For example the [User Defined Aliases](../practise/aliases.html) feature allows you to permit end users to introduce their own command shortcuts. Customisation is usually done by defining an appropriate [`@Parser`](../annotations/parser.html) annotation on your [`@Command`](../annotations/command.html) annotated class or via the `parserConfiguration` field of your [`@Cli`](../annotations/cli.html) annotation.

Please refer to the [`@Parser`](../annotation/parser.html) documentation to understand the available fields for defining your parser configuration. The rest of this page discusses the parser in general terms to help you understand how Airline works in more detail.

### The Parsing Process

Once you have a `Cli` or `SingleCommand` instance and call the `parse()` or `parseWithResult()` methods a complex process kicks off. Airline takes the provided input arguments and attempts to interpret them according to your CLIs metadata and your parser configuration. Broadly speaking the process has the following steps:

1. Parse any globally scoped option (if a CLI)
1. Apply command aliases (if a CLI)
1. Try to parse a command group (if a CLI)
1. Try to parse any group scoped options (if a CLI)
1. Try to parse a command (if a CLI)
1. Try to parse command options
1. Try to parse arguments
1. Applies global restrictions and does final validation for option and argument restrictions
1. Returns the parsed command/parser result (depending on whether `parse()` or `parseWithResult()` was called)

Steps 1-5 only apply to CLIs, for single commands step 6 onwards apply. Each of these steps may in of itself have multiple steps within it.

#### Option Parsing

`OptionParser` implementations control how Airline parses inputs into options and their values. By default Airline supports 3 common option styles with a further two that may be enabled if desired. The default parsers are as follows:

- {% include javadoc-ref.md class="StandardOptionParser" package="parser.options" %} - Simple white space separated option and values e.g. `--name value` sets the option `--name` to `value`
- {% include javadoc-ref.md class="LongGetOptParser" package="parser.options" %} - Long form GNU `getopt` style e.g. `--name=value` sets the option `--name` to `value`
- {% include javadoc-ref.md class="ClassicGetOptParser" package="parser.options" %} - Short form GNU `getopt` style e.g. `-n1` sets the option `-n` to `1`

Additionally the following alternative styles are supported:

- {% include javadoc-ref.md class="MaybePairValueOptionParser" package="parser.options" %} - Arity 2 options where the user may specify the values as whitespace/`=` separated e.g. `--name foo bar` and `--name foo=bar` are both acceptable and set the option `--name` to the values `foo` and `bar`
- {% include javadoc-ref.md class="ListValueOptionParser" package="parser.options" %} - Options that may be specified multiple times can be specified in a compact comma separated list form e.g. `--name foo,bar` sets the option `--name` to the values `foo` and `bar`

Users may create their own option parsers if desired as discussed on the [Custom Option Parsers](options.html) page.

##### `ClassicGetOptParser`

The classic `getopt` style parser only works with short form options i.e. those with a single character name e.g. `-a`. However, it is particularly useful for flag options, those which do not take a value (arity 0), in that it allows users to specify multiple options together.  For example the user input `-abcd` can be parsed into setting the `-a`, `-b`, `-c` and `-d` options.

It also allows for a value to be provided to the final option in the sequence provided that it is an arity 1 option e.g. `-abe value` sets the `-a` and `-b` options and sets the `-e` option to `value`.

##### `MaybePairValueOptionParser`

This parser is intended for arity 2 options where the user is providing key values pairs and so it may be natural for the user to enter these as a single value separated by an `=` sign but equally permits whitespace separation.

So both `--conf key=value` and `--conf key value` are acceptable to set the `--conf` option to the values `key` and `value`

##### `ListValueOptionParser`

This parser requires that the list of values provided be an exact multiple of the arity of the option being set.  So for example if option `--conf` has arity 2 it would allow `--conf foo,bar` but not `--conf foo,bar,faz`