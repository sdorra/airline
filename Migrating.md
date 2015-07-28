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

You can now also define `GlobalRestriction`s using the `withRestriction()` or `withRestrictions()` methods on your `CliBuilder<T>`.  If you don't define any restrictions a default set that provides backwards behavioural compatibility with Airline 1 is used.  This default set can also be explicitly added by calling `withDefaultRestrictions()` on your builder.

## Defining Single Commands

Previously you could not specify parser options when defining single commands, you are now able to pass in a `ParserMetadata<T>` in order to specify parser options for single command parsing.

You may now also pass in the `GlobalRestriction`s that should apply, if you don't define any restrictions a default set that provides backwards behavioural compatibility with Airline 1 is used

## Annotation Changes

All annotations are now located in the `com.github.rvesse.airline.annotations` package or in sub-packages thereof.

### Option Annotation Changes

Some fields of the `@Option` annotation that defined restrictions on options have been removed, namely these are the `required`, `allowedValues` and `ignoreCase` attributes.  Restrictions are now instead expressed with specific annotations.

When overriding existing options restrictions are inherited from the parent unless new restriction(s) are defined on the overridden option.  If you simply want to remove all existing restrictions when overriding an option you can add the `@Unrestricted` annotation to the option.

#### Migrating Required Options

For example a required option in Airline 1:

    @Option(name = "--example", arity = 1, required = true)
    private String example;

Becomes the following in Airline 2:

    @Option(name = "--example", arity = 1)
    @Required
    private String example;

#### Migrating Allowed Values

Similarly an option with allowed values in Airline 1:

    @Option(name = "--example", arity = 1, allowedValues = { "a", "b", "c" })
    private String example;
    
Becomes the following in Airline 2:

    @Option(name = "--example", arity = 1)
    @AllowedRawValues(allowedValues = { "a", "b", "c" })
    private String example;

#### Arguments Annotation Changes

Similar to `@Option` the `@Arguments` annotation has changed to have the `required` field removed, as with options you can now simply add the `@Required` annotation instead to indicate that arguments are required.

Arguments now allows for applying other restrictions such as `@AllowedRawValues` to arguments.

## Metadata Changes

### GlobalMetadata

The `GlobalMetadata` class is now a generic type i.e. `GlobalMetadata<T>` which provides better type safety but may require changing the type signatures of existing code you have written against Airline 1

A new `getRestrictions()` method provides access to `GlobalRestriction` instances which represent global restrictions on the CLI.

#### Accessing Parser Metadata

Parser settings were previously held directly on `GlobalMetadata`, they are now held in a `ParserMetadata<T>` class which is accessed via the `getParserConfiguration()` method on `GlobalMetadata<T>`

### OptionMetadata

Restrictions on options such as `required` are no longer expressed directly as fields but as instances of `OptionRestriction`.  The `isRequired()` method on `OptionMetadata` will check if the `IsRequiredRestriction` is present for an option.

A `getRestrictions()` method provides access to all the restrictions that are present for an option.

### ArgumentsMetadata

Restrictions on arguments such as `required` are no longer expressed directly as fields but as instances of `ArgumentsRestriction`.  The `isRequired()` method on `ArgumentsMetadata`will check if the `IsRequiredRestriction` is present for the arguments.

A `getRestrictions()` method provides access to all the restrictions that are present for arguments.

## Restrictions

A new restrictions framework is introduced which allows much more complex restrictions to be specified and enforced.

| Restriction Class | Applicability | Annotation Class | Restriction |
| -------------- | ---------------- | -------------- | --------------- |
| `IsRequiredRestriction` | Options and Arguments | `@Required` | Indicates that options/arguments are required |
| `AllowedRawValuesRestriction` | Options and Arguments | `@AllowedRawValues` | Indicates that the raw string values passed to options/arguments must be in a given set of values.  May be configured to use custom locale and case insensitive comparisons as desired |
| `RangeRestriction` | Options and Arguments | `@LongRange`, `@IntegerRange`, `@ShortRange`, `@ByteRange`, `@DoubleRange`, `@FloatRange` | Indicates that the instantiated values for options/arguments must fall within a range of values |
| `None` | Global, Options and Arguments | `@Unrestricted` | Indicates that no restrictions apply, primarily useful if you need to override restrictions inherited from a parent option |

Option and argument restrictions are automatically discovered by examining the annotations present on fields marked with `@Option` and `@Arguments`.  The `RestrictionRegistry` is used to map annotations into instances of `OptionRestriction` or `ArgumentsRestriction` as appropriate.

Global restrictions are defined when you create a `Cli` or `SingleCommand` instance, if you don't define any the default set which provides backwards compatibility with the global restrictions that applied in Airline 1 are used.

### Custom Restrictions

You can create your own custom restrictions by doing the following:

- Define an `OptionRestriction` and/or `ArgumentsRestriction` that implements the restriction.  Each restriction interface has a different method signature so a restriction can be implemented that applies to multiple things.
- Define an annotation that you will use to denote your restriction
- Define an `OptionRestrictionFactory` and/or `ArgumentsRestrictionFactory` that understands how to convert your annotation into a restriction instance
- Register your restriction:  
  `RestrictionRegistry.addOptionRestriction(MyAnnotation.class, new MyRestrictionFactory());`

## Parsing Changes

### Option Parsing Styles

Airline 1 supported only three option parsing styles in the following preference order:

* Classic GNU GetOpt i.e. `-nFoo`
* Long GNU GetOpt  i.e. `--name=Foo`
* Whitespace separated ie. `--name Foo`

In this release option parsing is now separated out into its own extensible `OptionParser` interface that allows you to customise how options are parsed.  You can call `withOptionParser()` or `withOptionParsers()` on your `ParserBuilder<T>` to specify the option parsers to use in your desired preference order.

You can also call `withDefaultOptionParsers()` to start from the default Airline 1 setup and then add your own, or call this after adding your own to use the default setup as your fallback.  If you don't explicitly configure any option parsers then the default Airline 1 setup is used automatically.

A couple of other parsing styles are now available but not enabled by default:

* `ListValueOptionParser` - Supports parsing options specified in the form `--name a,b,c` where the values is split on a separator (default `,` but configurable as desired) and passed to the option.  This parser is strict in that it requires users invoking commands to provide list values with the correct number of entries present
* `MaybePairValueOptionParser` - Supports parsing options of arity 2 where they may be specified either as `--name a=b` or `--name a b` i.e. where the user may specify the values either separated by whitespace or some separator (default `=` but configurable as desired)

### Customisable Type Converters

In Airline 1 how raw string values were converted into the appropriate Java types was fixed, with Airline 2 you can now configure the `TypeConverter` instance to use for this as desired.  This can be set via the `withTypeConverter()` method on your `ParserBuilder<T>` instance.

Creating a custom `TypeConverter` implementation allows you complete control over how you want to convert raw string values into Java types.

### Alias chaining

In Airline 1 aliases could not refer to other aliases, in Airline 2 this behaviour may be enabled and it permits aliases to reference each other unless a circular reference is generated during alias resolution.

To enable this you can call `withAliasesChaining()` on a `ParserBuilder<T>` or create an appropriate `ParserMetadata<T>` instance yourself with the appropriate parameter set to `true`.
