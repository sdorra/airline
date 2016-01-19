---
layout: page
title: Group Annotation
---

## `@Group`

The `@Group` annotation is used to specify group information.  This may be done as an alternative or in addition to using the relevant fields of the [`@Command`](command.html) or [`@Cli`](cli.html) annotations to specify groups.

{% include alert.html %}
Generally we recommend that you use the `groups` field of the [`@Cli`](cli.html) annotation to specify groups however this annotation can be useful if you are dynamically constructing your CLI and want to allow individual commands to specify their group memberships.

### Use on an `@Command` class

For example:

```java
@Command(name = "group-member")
@Group(name = "advanced",
       defaultCommand = GroupMember.class,
       commands = { Tool.class })
public class GroupMember { }
```

Here we use the `@Group` annotation to place our command in the `advanced` group.  We also specify that we are the default command for that group and that the group also contains the `Tool.class` command.

{% include alert.html %}
Note that since we are annotating directly on an `@Command` annotation class we don't need to specify ourselves in the list of `commands`

### Using as argument to `@Cli`

When used as an argument to an `@Cli` annotation via the `groups` field then we must specify all the commands in the `commands` field of the `@Group` annotation e.g.

```java
@Cli(name = "cli",
         groups = {
           @Group(name = "advanced",
                  defaultCommand = GroupMember.class,
                  commands = { GroupMember.class, Tool.class })
         })
public class GroupCli { }
```