---
layout: page
title: Discussion Annotation
---

## `@Discussion`

The `@Discussion` annotation may be applied to classes and provides a longer form discussion of a command that may be used for whatever purpose you desire.  This will be included in [Help](../help/) as an additional section.

### Adding Discussion

To add a discussion section simply add the `@Discussion` annotated to a class like so:

```java
@Discussion(paragraphs = {
	"This is the first paragraph of discussion",
	"In our second paragraph we go into much more depth",
	"We can have as many paragraphs as we feel are necessary"})
public class MyClass { }```

The annotation takes a single `paragraphs` field which takes a `String[]` array where each entry in the array is treated as a separate paragraph.