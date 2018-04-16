---
layout: page
title: Alias Annotation
---

# `@Alias`

The `@Alias` annotation is used as an argument to the `aliases` field of the [`@Parser`](parser.html) annotation.  It is used to define a command alias in the same manner as users can provide aliases via the [User Defined Aliases](../practise/aliases.html) feature.

The `@Alias` annotation has two required fields, `name` specifies the name of the alias i.e. the name typed to invoke the alias while `arguments` takes an array of the arguments that are used to expand the alias e.g.

```java
@Parser(aliases = {
  @Alias(name = "rem", 
         arguments = { "remove" })
})
```

Here we define a single alias `rem` which invokes the `remove` command.