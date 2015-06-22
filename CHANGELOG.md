# Airline - Change Log

## 1.0.1

- Fix regression in `RonnCommandUsageGenerator`

## 1.0.0

- Code Structure Refactoring
    - Root package is now `com.github.rvesse.airline`
    - Main library now lives under `lib/` in source control
    - New examples module under `examples/` in source control
- Annotation Improvements
    - `@Group` can now be marked as `hidden`
    - `discussion` parameter of `@Command` is now a `String[]` rather than a single string making it easier to specify long descriptions
- CLI Improvements
    - User defined command aliases are now supported
    - CLI builder classes are now public and have their own `builder` package
    - Parser Improvements
        - All parser errors in the `parser` package have public constructors
- Help Improvements
    - Help generators can now optionally display hidden commands and options
    - `Help.help()` static improved:
         - Respects command abbreviation when enabled
         - Additional overloads for enabled output of hidden commands and options
    - Bash completion fixes and improvements:
        - Default command and default group command completion now included
        - Functions for default group commands (i.e. top level commands) are no longer missing if there are also groups present
    - Fixed a bug with hidden options not displaying in synopsis even when including hidden options was enabled
    - `HelpOption` improved:
        - Generated help will include program and group name where applicable
        - It can now be used to show help with an arbitrary usage generator
        - `showHelpIfRequested()` guarantees to only display help once
        - `showHelp()` can be used to display help regardless
    - Command usage generators now print each item in the discussion as a separate paragraph
- IO Improvements
    - New `com.github.rvesse.airline.io` package with useful helper stuff for doing advanced console IO
    - Support for colorised output streams and writers:
        - Basic ANSI Colors (8 Colors, normal and bright variants)
        - 256 Colors (Basic ANSI Colors plus 3 colour palettes plus grayscale palette) - See [color chart](https://camo.githubusercontent.com/6378594a85c578517c5a4e494789bd4d66c9e46b/68747470733a2f2f7261772e6769746875622e636f6d2f666f697a652f676f2e7367722f6d61737465722f787465726d5f636f6c6f725f63686172742e706e67) for more detail
        - True Color (24 bit colour i.e. 16 million colours) - Many terminals may not support this mode
        - Includes basic text decorations e.g. bold, underline

## 0.9.2

- Annotation Improvements
    - `Arguments` improvements
        - An `arity` can now be specified to set the maximum arity for arguments and throws a `ParseTooManyArgumentsException` if too many arguments are seen
    - New `DefaultOption` annotation
        - Allows a field already annotated with `@Option` to also be marked as the default option under certain circumstances.  This allows one option to be specified arguments style i.e. the `-n` or `--name` can be omitted provided only one field is annotated this way, it has an arity of 1 and no fields are annotated with `@Arguments`
- Bash Completion Fixes
    - Bash Completion Scripts for CLI that use groups are now valid Bash and function correctly 
    - Generated scripts use more unique function names to avoid clashes between different airline generated completion scripts
- CLI Improvements
    - CLIs can now have command abbreviation enabled which allows users to only type part of the command name provided that the portion typed is unambiguous
    - CLIs can now have option abbreviation enabled which allows users to only type part of the option name provided that the portion typed is unambiguous
- Package Refactoring
    - Parser functionality moved into `io.airlift.airline.parser` package

## 0.9.1

- Help improvements
    - Refactored various usage generators to make them easier to extend

## 0.9

- Annotation improvements
    - `Command` improvements
        - Added `exitCodes` and `exitDescriptions` for declaring the exit codes a command can produce and their meanings
- Help improvements
    - All command help generator now include exit code information if declared for a command via the `exitCodes` and `exitDescriptions` properties
    - Add new `RonnMultiPageGlobalUsageGenerator` which generates a top level overview RONN page and then individual RONN files for each sub-command
    - Fix broken sort order of commands and groups in various help generator implementations
    - Add documentation of `allowedValues` to all help generators
    - Fix typo in presentation of `--` option in various help generators
    - Fix presentation of `examples` to avoid need for users to use markup in their annotations

## 0.8

- Forked from upstream repository
    - Group ID changed to `com.github.rvesse`
    - Currently package names remain same as upstream (bar the help system) to make it easy to migrate existing apps to this fork.  
    - **Please note** that future releases will change the package names to align with the Group ID.
- Annotation improvements
    - `Option` improvements:
        - `allowedValues` properties is actually enforced and produces `ParseOptionIllegalValueException` if an invalid value is received
        - New `override` and `sealed` properties allowing derived commands to change some properties of the annotation
        - Overridden options may change the type when it is a narrowing conversion
        - New `completionBehaviour` and `completionCommand` properties allowing defining behaviours for the purposes of completion script generators
    - `Arguments` improvements
        - New `completionBehaviour` and `completionCommand` properties allowing defining behaviours for the purposes of completion script generators
    - `Group` annotation for specifying groups
    - `Command` annotation supports discussion and examples
- Help system improvements
    - Most portions moved to `io.airlift.airline.help` package
    - Help printing respects new lines allowing them to be used in longer descriptions
    - Support for additional examples and discussion sections in command help
    - Abstracted out help generation into interfaces with multiple concrete implementations:
        - Command Line (the existing help system)
        - [Ronn](http://rtomayko.github.io/ronn/)
        - HTML
        - Bash auto-completion script
- Support for Command Factories
