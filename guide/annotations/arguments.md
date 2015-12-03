---
layout: page
title: Arguments Annotation
---

## `@Arguments`

The `@Arguments` annotation is applied to fields of a class to indicate that the field should be populated from any string arguments that are not otherwise parsed as options.

Arguments are typically used to take in free-form inputs such as lists of files to operate on.

{% include alert.html %}
By default if no field is annotated with `@Arguments` then it is illegal to pass in string arguments that are not options.
	
### Simple Definition

At its simplest the annotation requires no arguments and may be applied like so:

```java
@Arguments
private List<String> files;
```

Here we define a field `files` which is a `List<String>` so each argument received will be treated as a `String` and the list populated appropriately.  Just like [`@Option`](option.html) the type of the arguments is inferred from the field to which the annotation applies, please see [Supported Types](../practise/types.html) for more detail on the types Airline supports.

{% include alert.html %}
If you do not explicitly instantiate collection arguments then they will be `null` if a user does not provide any appropriate arguments

### Titles

In more complex cases you may wish to have arguments where each argument denotes a different thing in which case you can use the `titles` field so set the titles of the arguments that will appear in [Help](../help/) e.g.

```java
@Arguments(title = { "host", "username" })
private List<String> args;
```
	
Here we define a field `args` which again is a `List<String>` and with the titles `host` and `username`.  This will cause these titles to be displayed in help as opposed to the default title which is simply the name of the annotated field.

### Description

The `description` field may be used to describe how arguments are intended to be used e.g.

```java
@Arguments(description = "Provides the names of one/more files to process")
private List<String> files;
```