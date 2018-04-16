---
layout: page
title: ProseSection Annotation
---

## `@ProseSection`

The `@ProseSection` annotation may be applied to classes and provides a custom text section that may be used for whatever purpose you desire that will be included in [Help](../help/) as an additional section.

To add a prose section section simply add the `@ProseSection` annotation to a class like so:

```java
@ProseSection(title = "Additional Information"
              paragraphs = {
                   "This is additional information",
                   "We can have as many paragraphs as we feel are necessary"              },
              suggestedOrder = 55)
public class MyClass { }```

The annotation requires a `title` field which specifies the title that should be used for your help section.  It also takes a `paragraphs` field which takes a `String[]` array where each entry in the array is treated as a separate paragraph in the help output.

The optional `suggestedOrder` field is used to control where the section appears relative to other help sections in the generated help.  Values less than zero are used to indicate that the section should appear prior to the standard sections while values greater than zero are used to indicate that it should appear after the standard sections.  `CommonSections` provides the default values that are used for the other help annotations such as [`@Discussion`](discussion.html) if you wish to place your custom section relative to one of the other commonly used sections.