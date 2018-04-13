---
layout: page
title: Restrictions
---

Airline includes a powerful and extensible restrictions system that allows users to significantly reduce the boiler plate code typical of many CLI libraries.  Restrictions work by simply applying appropriate annotations to the [`@Option`](../annotations/option.html) or [`@Arguments`](../annotations/arguments.html) annotated fields that you wish to restrict and Airline handles all the work of enforcing those restrictions for you.

**TODO** Simple restriction example

## Available Restrictions

{% include restrictions.md path="../annotations/" %}

## Custom Restrictions

**TODO Document creating custom restrictions**