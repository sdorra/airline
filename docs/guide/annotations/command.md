---
layout: page
title: Command Annotation
---

## `@Command`

The `@Command` annotation is the only annotation Airline ever requires, any class you want to use as a command must be annotated with this annotation otherwise Airline will refuse to use it as a command.

The annotation is actually very simple, at a minimum it looks like the following:

```java
@Command(name = "tool")
public class Tool { }
```

Where the `name` field states the name of the command, this name is the name by which users will refer to the command and that will be displayed in [Help](../help/) for the command.

{% include alert.html %}
Names are restricted to not contain whitespace but otherwise can contain whatever characters you want.
{% include end-alert.html %}
	
Remember that users need to be able to type the name at their command line terminal of choice so it is best to limit yourself to common characters i.e. alphanumerics and common punctuation marks.

### Description

Generally you'll also want to add a `description` which gives users a brief overview of what your command does e.g.

```java
@Command(name = "tool", 
         description = "This tool does something interesting")
public class Tool { }
```

This is just a string that describes the functionality of your command, generally this should be kept relatively short.  If you want to include more detailed descriptions of functionality then you probably also want to use some of the [Help](../help/) annotations such as [`@Discussion`](discussion.html)

### Hiding Commands

By default all commands in an Airline CLI are visible to users, however there are various scenarios in which you might want to include advanced commands in your CLIs that aren't intended for use by ordinary users.  To do this you can make your command as hidden like so:

```java
@Command(name = "tool", 
         description = "This tool does something interesting", 
         hidden = true)
public class Tool { }
```

When commands are hidden they will not be included in help (unless the help generator is explicitly specified to include them) so only users who are aware they are there can use them.

{% include alert.html %}
Marking a command `hidden = true` **DOES NOT** prevent users from using it.  **DO NOT** rely on hiding commands to prevent users from invoking them.
{% include end-alert.html %}

### Grouping Commands

When incorporating a command into a CLI you may wish to place the command into one/more groups.  Groups provide for complex Git style CLIs where commands are grouped together for ease of access.  Grouping commands also allows you to re-use the same short names for commands provided that those commands exist in different groups.

To specify which group(s) a command belongs to you use the `groupNames` fields like so:

```java
@Command(name = "tool", 
         description = "This tool does something interesting", 
         hidden = true, 
         groupNames = { "common", "foo bar"})
public class Tool { }
```
	
Here we add our command to the `common` group, we also add it to the `bar` group which is a sub-group of the `foo` group.

Note that since command and group names cannot contain whitespace **any** whitespace in the specified group names is interpreted as a separator and thus treated as a path to the actual group you want to place your command in.

{% include alert.html %}
You can also specify groups by using the [`@Group`](group.html) annotation or as part of the [`@Cli`](cli.html) annotation.
{% include end-alert.html %}

## Parser Configuration

If you are annotating a class which you intend to use as a single command i.e. via `SingleCommand.singleCommand()` and not as part of a larger CLI and you wish to change the parser configuration you can use the [`@Parser`](parser.html) annotation.