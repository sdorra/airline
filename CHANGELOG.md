# Airline - Change Log

## 0.9.2

- Bash Completion Fixes
    - Bash Completion Scripts for CLI that use groups are not valid Bash and function correctly 
    - Bash Completion Scripts generate more unique function names to avoid clashes between different airline generated completion scripts

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
