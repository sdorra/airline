---
layout: page
title: Annotations
---

{% include toc.html %}

Airline uses a variety of different annotations to allow you to declaratively define various aspects of your CLIs.  This section of the user guide covers each annotation in-depth with examples of their practical usage.

## CLI Annotations

The following annotations are used to define the high level aspects of your CLIs:

- The [`@Command`](command.html) annotation defines classes as being commands
- The [`@Option`](option.html) annotation defines fields of a class as denoting options
- The [`@Arguments`](arguments.html) annotation defines a field of a class as denoting arguments
- The [`@Cli`](cli.html) annotation defines a class as being a CLI which consists of potentially many commands
    - The [`@Group`](group.html) annotation defines a command group within a CLI
    - The [`@Groups`](groups.html) annotation defines an `@Command` annotated class as belonging to some command groups within a CLI
- The [`@Parser`](parser.html) annotation defines the parser behaviour
    - The [`@Alias`](alias.html) annotation defines command aliases for a parser configuration
- The [`@DefaultOption`](default-option.html) annotation defines an `@Option` annotated field as being able to also be populated as if it were `@Arguments` annotated

## Restriction Annotations

The following annotations are used to define various restrictions on options and arguments that cause Airline to automatically enforce restrictions on their usage e.g. requiring an option take a value from a restricted set of values.

All these annotations are applied to fields that are annotated with [`@Option`](option.html) or [`@Arguments`](arguments.html) and are automatically discovered during meta-data extraction.  If you are overriding the definition of an option then restrictions are automatically inherited unless you specify new restrictions further as part of your override.  In the case where you wish to remove inherited restrictions you can use the special [`@Unrestricted`](unrestricted.html) annotation to indicate that.

### Requirement Restrictions

The following annotations are used to specify that options/arguments (or combinations thereof) are required:

- The [`@Required`](required.html) annotation indicates that an option/argument must be specified
- The [`@RequireSome`](require-some.html) annotation indicates that one/more from some set of options must be specified
- The [`@RequireOnlyOne`](require-only-one.html) annotation indicates that precisely one of some set of options must be specified
- The [`@MutuallyExclusiveWith`](mutually-exclusive-with.html) annotation indicates that precisely one of some set of options may be specified

### Occurrence Restrictions

The following annotations are used to specify the number of times that options/arguments can be specified:

- The [`@Once`](once.html) annotation indicates that at option/argument may be specified only once
- The [`@MinOccurrences`](min-occurrences.html) annotation indicates that an option/argument must be specified a minimum number of times
- The [`@MaxOccurrences`](max-occurrences.html) annotation indicates that an option/argument may be specified a maximum number of times

### Value Restrictions

The following annotations are used to specify restrictions on the values for options/arguments:

- The [`@AllowedRawValues`](allowed-raw-values.html) annotation specifies a set of strings that may be specified as the value
- The [`@AllowedValues`](allowed-values.html) annotation specifies a set of values that may be specified as the value
- The [`@MaxLength`](max-length.html) annotation specifies the maximum length of the value that may be given
- The [`@MinLength`](min-length.html) annotation specifies the minimum length of the value that may be given
- The [`@NotBlank`](not-blank.html) annotation specifies that a value may not consist entirely of white space
- The [`@NotEmpty`](not-empty.html) annotation specifies that the value may not be an empty string
- The [`@Path`](path.html) annotation specifies restrictions on values that refer to files and/or directories
- The [`@Pattern`](pattern.html) annotation specifies that a value must match a regular expression
- The [`@Port`](port.html) annotation specifies restrictions on values that represent port numbers

#### Range Value Restrictions

A further subset of annotations specify restrictions on the values for options/arguments in terms of ranges of acceptable values:

- The [`@ByteRange`](byte-range.html) annotation specifies a range of `byte` values that are acceptable
- The [`@DoubleRange`](double-range.html) annotation specifies a range of `double` values that are acceptable
- The [`@FloatRange`](float-range.html) annotation specifies a range of `float` values that are acceptable
- The [`@IntegerRange`](integer-range.html) annotation specifies a range of `int` values that are acceptable
- The [`@LexicalRange`](lexical-range.html) annotation specifies a range of `string` values that are acceptable
- The [`@LongRange`](long-range.html) annotation specifies a range of `long` values that are acceptable
- The [`@ShortRange`](short-range.html) annotation specifies a range of `short` values that are acceptable

## Help Annotations

The following annotations are used to add additional help information to commands that may be consumed by the various help generators provided by Airline by producing additional help sections.

All these annotations are added to an [`@Command`](command.html) annotated class or a parent class in the commands hierarchy.  They are automatically discovered from a command classes inheritance hierarchy, where an annotation occurs more than once the annotation specified furthest down the hierarchy is used.  This can be used to specify a default for some help annotation with the option of overriding it if necessary, the special [`@HideSection`](hide-section.html) annotation may be used to suppress an inherited help annotation.

- The [`@Copyright`](copyright.html) annotation adds a copyright statement
- The [`@Discussion`](discussion.html) annotation adds extended discussion 
- The [`@ExitCodes`](exit-codes.html) annotation adds documentation on exit codes
- The [`@Examples`](examples.html) annotation adds usage examples
- The [`@HideSection`](hide-section.html) annotation is used to hide an inherited help section
- The [`@License`](license.html) annotation adds a license statement
- The [`@ProseSection`](prose-section.html) annotation adds an custom titled text section
- The [`@Version`](version.html) annotation adds a version statement