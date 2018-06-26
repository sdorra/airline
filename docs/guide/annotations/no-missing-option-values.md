---
layout: page
title: NoMissingOptionValues Annotation
---

## `@NoMissingOptionValues`

The `@NoMissingOptionValues` annotation is a [Global Restriction](../restrictions/global.html).  It is applied to classes representing CLIs to indicate that specifying an option without also specifying the associated values should be considered illegal and cause parsing to fail

It can be applied directly to classes that have a [`@Cli`](cli.html) annotation or to [`@Command`](command.html) classes used with `SingleCommand`