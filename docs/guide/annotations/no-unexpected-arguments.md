---
layout: page
title: NoUnexpectedArguments Annotation
---

## `@NoUnexpectedArguments`

The `@NoUnexpectedArgumentsAnnotation` annotation is a [Global Restriction](../restrictions/global.html).  It is applied to classes representing CLIs to indicate that specifying any inputs that are not recognized as options/arguments is considered illegal and should cause parsing to fail.

It can be applied directly to classes that have a [`@Cli`](cli.html) annotation or to [`@Command`](command.html) classes used with `SingleCommand`