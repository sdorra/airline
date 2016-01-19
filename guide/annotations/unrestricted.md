---
layout: page
title: Unrestricted Annotation
---

## `@Unrestricted`

The `@Unrestricted` annotation is applied to fields annotated with [`@Option`](option.html) or [`@Arguments`](arguments.html) to indicate that no restrictions should apply.

This is useful because by default restrictions are inherited so if you wish to remove restrictions when overriding an option definition then you need to use this annotation.

