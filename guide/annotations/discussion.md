---
layout: page
title: Discussion Annotation
---

## `@Discussion`

The `@Discussion` annotation may be applied to classes and provides a longer form discussion of a command that will be included in [Help](../help/) as an additional section.

To add a discussion section simply add the `@Discussion` annotation to a class like so:

```java
@Discussion(paragraphs = {
	"This is the first paragraph of discussion",
	"In our second paragraph we go into much more depth",
	"We can have as many paragraphs as we feel are necessary"})
public class MyClass { }```

The annotation takes a single `paragraphs` field which takes a `String[]` array where each entry in the array is treated as a separate paragraph of discussion in the help output.