# Migrating to Airline 2

Airline 2 is a significant rewrite of Airline and as such there are major breaking changes to be aware of.  This document aims to guide you through the user facing changes, if you are developing in the internals there are lots of other changes which are not covered here.

## Defining CLIs

The major change in defining CLIs using the `CliBuilder<T>` is that all parser related options are now moved onto a new `ParserBuilder<T>`.  This builder can be accessed via the `withParser()` method.

So for example the following Airline 1 code:

```
CliBuilder<ExampleRunnable> builder 
  = Cli.<ExampleRunnable>builder("cli")
          // Add a description
          .withDescription("A simple CLI with several commands available in groups")
          // We can enable command and option abbreviation, this allows users to only
          // type part of the group/command/option name provided that the portion they
          // type is unambiguous
          .withCommandAbbreviation()
          .withOptionAbbreviation();
```

Would need changing to the following:

```
CliBuilder<ExampleRunnable> builder 
  = Cli.<ExampleRunnable>builder("cli")
          // Add a description
          .withDescription("A simple CLI with several commands available in groups");
          
// We can enable command and option abbreviation, this allows users to only
// type part of the group/command/option name provided that the portion they
// type is unambiguous
builder.withParser()
          .withCommandAbbreviation()
          .withOptionAbbreviation();
```

## Defining Single Commands

Previously you could not specify parser options when defining single commands, you are now able to pass in a `ParserMetadata<T>` in order to specify parser options for single command parsing.

## Metadata Changes

### GlobalMetadata

The `GlobalMetadata` class is now a generic type i.e. `GlobalMetadata<T>` which provides better type safety but may require changing the type signatures of existing code you have written against Airline 1

### Accessing Parser Metadata

Parser settings were previously held directly on `GlobalMetadata`, they are now held in a `ParserMetadata<T>` class which is accessed via the `getParserConfiguration()` method on `GlobalMetadata<T>`

## Parsing Changes

### Option Parsing Styles

Airline 1 supported only three option parsing styles in the following preference order:

* Classic GNU GetOpt i.e. `-nFoo`
* Long GNU GetOpt  i.e. `--name=Foo`
* Whitespace separated ie. `--name Foo`

In this release option parsing is now separated out into its own extensible `OptionParser` interface that allows you to customise how options are parsed.  You can call `withOptionParser()` or `withOptionParsers()` on your `ParserBuilder<T>` to specify the option parsers to use in your desired preference order.

You can also call `withDefaultOptionParsers()` to start from the default Airline 1 setup and then add your own, or call this after adding your own to use the default setup as your fallback.

A couple of other parsing styles are now available but not enabled by default:

* `ListValueOptionParser` - Supports parsing options specified in the form `--name a,b,c` where the values is split on a separator (default `,` but configurable as desired) and passed to the option.  This parser is strict in that it requires users invoking commands to provide list values with the correct number of entries present
* `MaybePairValueOptionParser` - Supports parsing options of arity 2 where they may be specified either as `--name a=b` or `--name a b` i.e. where the user may specify the values either separated by whitespace or some separator (default `=` but configurable as desired)

### Customisable Type Converters

In Airline 1 how arguments were converted into the appropriate Java types was fixed, with Airline 2 you can now configure the `TypeConverter` instance to use as desired.  This can be set via the `withTypeConverter()` method on your `ParserBuilder<T>` instance.

Creating a custom `TypeConverter` implementation allows you complete control over how you want to convert arguments into Java types.

### Alias chaining

In Airline 1 aliases could not refer to other aliases, in Airline 2 this behaviour may be enabled and it permits aliases to reference each other unless a circular reference is generated during alias resolution.

To enable this you can call `withAliasesChaining()` on a `ParserBuilder<T>` or create an appropriate `ParserMetadata<T>` instance yourself with the appropriate parameter set to `true`.
