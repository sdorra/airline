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

## Metadata Changes

### GlobalMetadata

The `GlobalMetadata` class is not a generic type i.e. `GlobalMetadata<T>` which provides better type safety but may require changing the type signatures of existing code you have written against Airline 1

### Accessing Parser Metadata

Parser settings were previously held directly on `GlobalMetadata`, they are now held in a `ParserMetadata<T>` class which is accessed via the `getParserConfiguration()` method on `GlobalMetadata<T>`
