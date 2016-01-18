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
- The [`@Parser`](parser.html) annotation defines the parser behaviour
- The [`@DefaultOption`](default-option.html) annotation defines an `@Option` annotated field as being able to also be populated as if it were `@Arguments` annotated

## Restriction Annotations

The following annotations are used to define various restrictions on options and arguments that cause Airline to automatically enforce restrictions on their usage e.g. requiring an option take a value from a restricted set of values.

**TODO** - List restriction annotations

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