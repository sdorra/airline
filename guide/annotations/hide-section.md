---
layout: Page
title: HideSection Annotation
---

## `@HideSection`

The `@HideSection` annotation is a special annotation used to prevent the inheritance of another help section provided by a help annotation.  Usually Airline will automatically inherit help annotations which allows for specifying common information (such as [`@Copyright`](copyright.html) and [`@License`](license.html)) higher up the class hierarchy instead of having to specify it on every command.  However sometimes you may wish to hide a help section that would otherwise be inherited in which case the `@HideSection` annotation is used e.g.

```java
@HideSection(title = CommonSections.TITLE_DISCUSSION)
public class MyClass { }
```

The above example would hide the Discussion section, hiding of sections is done based on section titles thus allowing any custom section to be hidden as needed.

{% include alert.html %}
This annotation **cannot** be used to hide the standard sections that help generators produce, if you wish to do that you will need to create a custom help generator.