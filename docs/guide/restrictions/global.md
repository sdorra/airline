---
layout: page
title: Global Restrictions
---

Global restrictions apply over the final parser state and are used to implement restrictions that need to see  this final state. The following built-in defaults are provided and used by default for all CLIs and commands unless otherwise specified:

- The [`@CommandRequired`]({{ include.path}}command-required.html) annotation indicates that a command must be specified.
- The [`@NoMissingOptionValues`]({{ include.path}}no-missing-option-values.html) annotation indicates that specifying an option without its corresponding value(s) is not permitted.
- The [`@NoUnexpectedArguments`]({{ include.path}}no-unexpected-arguments.html) annotation indicates that any unrecognised inputs are not permitted.

These provide useful default behaviours that most developers will expect from the CLIs they are building.

### Selecting the Desired Restrictions

For CLIs you can set the desired restrictions via the `restrictions` field of the [`@Cli`](../annotations/cli.html) annotation and control whether the defaults are included via its `includeDefaultRestrictions` field.

Or you can add the relevant annotations to the class that has the `@Cli` annotation and they will be automatically detected.

#### Global Restrictions for Single Commands

{% include req-ver.md version="2.5.1" %} 

For single commands you can specify global restrictions directly on [`@Command`](../annotations/command.html) annotated classes and these will be automatically detected appropriately.

### Custom Global Restrictions

As with other kinds of restrictions you can create custom Global restrictions if desired, the [Custom Restrictions](custom.html) documentation describes the necessary steps for this.