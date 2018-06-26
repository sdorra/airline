---
layout: page
title: CommandRequired Annotation
---

## `@CommandRequired`

The `@CommandRequired` annotation is a [Global Restriction](../restrictions/global.html).  It is applied to classes representing CLIs to indicate that a command must be explicitly specified or the CLI configuration must specify a default command otherwise parsing should fail.

It can be applied directly to classes that have a [`@Cli`](cli.html) annotation or to [`@Command`](command.html) classes used with `SingleCommand`