package com.github.rvesse.airline.model;

import com.github.rvesse.airline.*;
import com.github.rvesse.airline.help.Suggester;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.*;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class MetadataLoader {
    public static <C> GlobalMetadata<C> loadGlobal(String name, String description, CommandMetadata defaultCommand,
            Iterable<CommandMetadata> defaultGroupCommands, Iterable<CommandGroupMetadata> groups,
            ParserMetadata<C> parserConfig) {
        ImmutableList.Builder<OptionMetadata> globalOptionsBuilder = ImmutableList.builder();
        if (defaultCommand != null) {
            globalOptionsBuilder.addAll(defaultCommand.getGlobalOptions());
        }
        for (CommandMetadata command : defaultGroupCommands) {
            globalOptionsBuilder.addAll(command.getGlobalOptions());
        }
        for (CommandGroupMetadata group : groups) {
            for (CommandMetadata command : group.getCommands()) {
                globalOptionsBuilder.addAll(command.getGlobalOptions());
            }
        }
        List<OptionMetadata> globalOptions = mergeOptionSet(globalOptionsBuilder.build());
        return new GlobalMetadata<C>(name, description, globalOptions, defaultCommand, defaultGroupCommands, groups, parserConfig);
    }

    public static CommandGroupMetadata loadCommandGroup(String name, String description, boolean hidden,
            CommandMetadata defaultCommand, Iterable<CommandMetadata> commands) {
        ImmutableList.Builder<OptionMetadata> groupOptionsBuilder = ImmutableList.builder();
        if (defaultCommand != null) {
            groupOptionsBuilder.addAll(defaultCommand.getGroupOptions());
        }
        for (CommandMetadata command : commands) {
            groupOptionsBuilder.addAll(command.getGroupOptions());
        }
        List<OptionMetadata> groupOptions = mergeOptionSet(groupOptionsBuilder.build());
        return new CommandGroupMetadata(name, description, hidden, groupOptions, defaultCommand, commands);
    }

    public static <T> ImmutableList<CommandMetadata> loadCommands(Iterable<Class<? extends T>> defaultCommands) {
        return ImmutableList.copyOf(Iterables.transform(defaultCommands, new Function<Class<?>, CommandMetadata>() {
            public CommandMetadata apply(Class<?> commandType) {
                return loadCommand(commandType);
            }
        }));
    }

    public static CommandMetadata loadCommand(Class<?> commandType) {
        if (commandType == null) {
            return null;
        }
        Command command = null;
        List<Group> groups = Lists.newArrayList();

        for (Class<?> cls = commandType; command == null && !Object.class.equals(cls); cls = cls.getSuperclass()) {
            command = cls.getAnnotation(Command.class);

            if (cls.isAnnotationPresent(Groups.class)) {
                groups.addAll(Arrays.asList(cls.getAnnotation(Groups.class).value()));
            }
            if (cls.isAnnotationPresent(Group.class)) {
                groups.add(cls.getAnnotation(Group.class));
            }
        }
        Preconditions
                .checkArgument(command != null, "Command %s is not annotated with @Command", commandType.getName());
        String name = command.name();
        String description = command.description().isEmpty() ? null : command.description();
        List<String> groupNames = Arrays.asList(command.groupNames());
        boolean hidden = command.hidden();
        Map<Integer, String> exitCodes = new HashMap<>();
        if (command.exitCodes() != null) {
            String[] exitDescriptions = command.exitDescriptions() != null ? command.exitDescriptions()
                    : new String[command.exitCodes().length];
            for (int i = 0; i < command.exitCodes().length; i++) {
                String exitDescrip = exitDescriptions.length > i ? exitDescriptions[i] : null;
                exitCodes.put(command.exitCodes()[i], exitDescrip);
            }
        }

        InjectionMetadata injectionMetadata = loadInjectionMetadata(commandType);

        //@formatter:off
        CommandMetadata commandMetadata = new CommandMetadata(name, 
                                                              description, 
                                                              command.discussion().length == 0 ? null : Lists.newArrayList(command.discussion()), 
                                                              command.examples().length == 0 ? null : Lists.newArrayList(command.examples()),
                                                              hidden, 
                                                              injectionMetadata.globalOptions, 
                                                              injectionMetadata.groupOptions,
                                                              injectionMetadata.commandOptions, 
                                                              injectionMetadata.defaultOption,
                                                              Iterables.getFirst(injectionMetadata.arguments, null),
                                                              injectionMetadata.metadataInjections, 
                                                              commandType, 
                                                              groupNames, 
                                                              groups, 
                                                              exitCodes);
        //@formatter:on

        return commandMetadata;
    }

    public static SuggesterMetadata loadSuggester(Class<? extends Suggester> suggesterClass) {
        InjectionMetadata injectionMetadata = loadInjectionMetadata(suggesterClass);
        return new SuggesterMetadata(suggesterClass, injectionMetadata.metadataInjections);
    }

    public static InjectionMetadata loadInjectionMetadata(Class<?> type) {
        InjectionMetadata injectionMetadata = new InjectionMetadata();
        loadInjectionMetadata(type, injectionMetadata, ImmutableList.<Field> of());
        injectionMetadata.compact();
        return injectionMetadata;
    }

    public static void loadInjectionMetadata(Class<?> type, InjectionMetadata injectionMetadata, List<Field> fields) {
        if (type.isInterface()) {
            return;
        }
        for (Class<?> cls = type; !Object.class.equals(cls); cls = cls.getSuperclass()) {
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                ImmutableList<Field> path = concat(fields, field);

                Inject injectAnnotation = field.getAnnotation(Inject.class);
                if (injectAnnotation != null) {
                    if (field.getType().equals(GlobalMetadata.class)
                            || field.getType().equals(CommandGroupMetadata.class)
                            || field.getType().equals(CommandMetadata.class)) {
                        injectionMetadata.metadataInjections.add(new Accessor(path));
                    } else {
                        loadInjectionMetadata(field.getType(), injectionMetadata, path);
                    }
                }

                try {
                    @SuppressWarnings("unchecked")
                    Annotation aGuiceInject = field.getAnnotation((Class<? extends Annotation>) Class
                            .forName("com.google.inject.Inject"));
                    if (aGuiceInject != null) {
                        if (field.getType().equals(GlobalMetadata.class)
                                || field.getType().equals(CommandGroupMetadata.class)
                                || field.getType().equals(CommandMetadata.class)) {
                            injectionMetadata.metadataInjections.add(new Accessor(path));
                        } else {
                            loadInjectionMetadata(field.getType(), injectionMetadata, path);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    // this is ok, means Guice is not on the class path, so
                    // probably not being used
                    // and thus, ok that this did not work.
                } catch (ClassCastException e) {
                    // ignore this too, we're doing some funky cross your
                    // fingers type reflect stuff to play
                    // nicely with Guice
                }

                Option optionAnnotation = field.getAnnotation(Option.class);
                DefaultOption defaultOptionAnnotation = field.getAnnotation(DefaultOption.class);
                if (optionAnnotation != null) {
                    OptionType optionType = optionAnnotation.type();
                    String name;
                    if (!optionAnnotation.title().isEmpty()) {
                        name = optionAnnotation.title();
                    } else {
                        name = field.getName();
                    }

                    List<String> options = ImmutableList.copyOf(optionAnnotation.name());
                    String description = optionAnnotation.description();

                    int arity = optionAnnotation.arity();
                    Preconditions.checkArgument(arity >= 0 || arity == Integer.MIN_VALUE,
                            "Invalid arity for option %s", name);

                    if (optionAnnotation.arity() >= 0) {
                        arity = optionAnnotation.arity();
                    } else {
                        Class<?> fieldType = field.getType();
                        if (Boolean.class.isAssignableFrom(fieldType) || boolean.class.isAssignableFrom(fieldType)) {
                            arity = 0;
                        } else {
                            arity = 1;
                        }
                    }

                    boolean required = optionAnnotation.required();
                    boolean hidden = optionAnnotation.hidden();
                    boolean override = optionAnnotation.override();
                    boolean sealed = optionAnnotation.sealed();
                    List<String> allowedValues = ImmutableList.copyOf(optionAnnotation.allowedValues());
                    if (allowedValues.isEmpty()) {
                        allowedValues = null;
                    }
                    boolean ignoreCase = optionAnnotation.ignoreCase();

                    //@formatter:off
                    OptionMetadata optionMetadata = new OptionMetadata(optionType, 
                                                                       options,
                                                                       name, 
                                                                       description, 
                                                                       arity,
                                                                       required, 
                                                                       hidden, 
                                                                       override, 
                                                                       sealed, 
                                                                       allowedValues,
                            ignoreCase,
                                                                       optionAnnotation.completionBehaviour(),
                                                                       optionAnnotation.completionCommand(),
                                                                       path);
                    //@formatter:on
                    switch (optionType) {
                    case GLOBAL:
                        if (defaultOptionAnnotation != null)
                            throw new IllegalArgumentException(
                                    String.format(
                                            "Field %s which defines a global option cannot be annotated with @DefaultOption as this may only be applied to command options",
                                            field));
                        injectionMetadata.globalOptions.add(optionMetadata);
                        break;
                    case GROUP:
                        if (defaultOptionAnnotation != null)
                            throw new IllegalArgumentException(
                                    String.format(
                                            "Field %s which defines a global option cannot be annotated with @DefaultOption as this may only be applied to command options",
                                            field));
                        injectionMetadata.groupOptions.add(optionMetadata);
                        break;
                    case COMMAND:
                        // Do we also have a @DefaultOption annotation

                        if (defaultOptionAnnotation != null) {
                            // Can't have both @DefaultOption and @Arguments
                            if (injectionMetadata.arguments.size() > 0)
                                throw new IllegalArgumentException(
                                        String.format(
                                                "Field %s cannot be annotated with @DefaultOption because there are fields with @Arguments annotations present",
                                                field));
                            // Can't have more than one @DefaultOption
                            if (injectionMetadata.defaultOption != null)
                                throw new IllegalArgumentException(String.format(
                                        "Command type %s has more than one field with @DefaultOption declared upon it",
                                        type));
                            // Arity of associated @Option must be 1
                            if (optionMetadata.getArity() != 1)
                                throw new IllegalArgumentException(
                                        String.format(
                                                "Field %s annotated with @DefaultOption must also have an @Option annotation with an arity of 1",
                                                field));
                            injectionMetadata.defaultOption = optionMetadata;
                        }
                        injectionMetadata.commandOptions.add(optionMetadata);
                        break;
                    }
                }

                if (optionAnnotation == null && defaultOptionAnnotation != null) {
                    // Can't have @DefaultOption on a field without also @Option
                    throw new IllegalArgumentException(String.format(
                            "Field %s annotated with @DefaultOption must also have an @Option annotation", field));
                }

                Arguments argumentsAnnotation = field.getAnnotation(Arguments.class);
                if (field.isAnnotationPresent(Arguments.class)) {
                    // Can't have both @DefaultOption and @Arguments
                    if (injectionMetadata.defaultOption != null)
                        throw new IllegalArgumentException(
                                String.format(
                                        "Field %s cannot be annotated with @Arguments because there is a field with @DefaultOption present",
                                        field));

                    ImmutableList.Builder<String> titlesBuilder = ImmutableList.<String> builder();

                    if (!(argumentsAnnotation.title().length == 1 && argumentsAnnotation.title()[0].equals(""))) {
                        titlesBuilder.add(argumentsAnnotation.title());
                    } else {
                        titlesBuilder.add(field.getName());
                    }

                    String description = argumentsAnnotation.description();
                    String usage = argumentsAnnotation.usage();
                    boolean required = argumentsAnnotation.required();
                    int arity = argumentsAnnotation.arity() <= 0 ? Integer.MIN_VALUE : argumentsAnnotation.arity();

                    //@formatter:off
                    injectionMetadata.arguments.add(new ArgumentsMetadata(titlesBuilder.build(), 
                                                                          description, 
                                                                          usage,
                                                                          required, 
                                                                          arity,
                                                                          argumentsAnnotation.completionBehaviour(), 
                                                                          argumentsAnnotation.completionCommand(),
                                                                          path));
                    //@formatter:on
                }
            }
        }
    }

    private static List<OptionMetadata> mergeOptionSet(List<OptionMetadata> options) {
        Multimap<OptionMetadata, OptionMetadata> metadataIndex = Multimaps.newMultimap(
                Maps.<OptionMetadata, Collection<OptionMetadata>> newLinkedHashMap(),
                new Supplier<List<OptionMetadata>>() {
                    public List<OptionMetadata> get() {
                        return Lists.newArrayList();
                    }
                });
        for (OptionMetadata option : options) {
            metadataIndex.put(option, option);
        }

        options = ImmutableList.copyOf(transform(metadataIndex.asMap().values(),
                new Function<Collection<OptionMetadata>, OptionMetadata>() {
                    @Override
                    public OptionMetadata apply(Collection<OptionMetadata> options) {
                        return new OptionMetadata(options);
                    }
                }));

        Map<String, OptionMetadata> optionIndex = Maps.newLinkedHashMap();
        for (OptionMetadata option : options) {
            for (String optionName : option.getOptions()) {
                if (optionIndex.containsKey(optionName)) {
                    throw new IllegalArgumentException(String.format(
                            "Fields %s and %s have conflicting definitions of option %s", optionIndex.get(optionName)
                                    .getAccessors().iterator().next(), option.getAccessors().iterator().next(),
                            optionName));
                }
                optionIndex.put(optionName, option);
            }
        }

        return options;
    }

    private static List<OptionMetadata> overrideOptionSet(List<OptionMetadata> options) {
        options = ImmutableList.copyOf(options);

        Map<Set<String>, OptionMetadata> optionIndex = newHashMap();
        for (OptionMetadata option : options) {
            Set<String> names = option.getOptions();
            if (optionIndex.containsKey(names)) {
                // Multiple classes in the hierarchy define this option
                // Determine if we can successfully override this option
                tryOverrideOptions(optionIndex, names, option);
            } else {
                // Need to check there isn't another option with partial overlap
                // of names, this is considered an illegal override
                for (Set<String> existingNames : optionIndex.keySet()) {
                    Set<String> intersection = Sets.intersection(names, existingNames);
                    if (intersection.size() > 0) {
                        throw new IllegalArgumentException(
                                String.format(
                                        "Fields %s and %s have overlapping definitions of option %s, options can only be overridden if they have precisely the same set of option names",
                                        option.getAccessors().iterator().next(), optionIndex.get(existingNames)
                                                .getAccessors().iterator().next(), intersection));
                    }
                }

                optionIndex.put(names, option);
            }
        }

        return ImmutableList.copyOf(optionIndex.values());
    }

    private static void tryOverrideOptions(Map<Set<String>, OptionMetadata> optionIndex, Set<String> names,
            OptionMetadata parent) {

        // As the metadata is extracted from the deepest class in the hierarchy
        // going upwards we need to treat the passed option as the parent and
        // the pre-existing option definition as the child
        OptionMetadata child = optionIndex.get(names);

        Accessor parentField = parent.getAccessors().iterator().next();
        Accessor childField = child.getAccessors().iterator().next();

        // Check for duplicates
        boolean isDuplicate = parent == child || parent.equals(child);

        // Parent must not state it is sealed UNLESS it is a duplicate which can
        // happen when using @Inject to inject options via delegates
        if (parent.isSealed() && !isDuplicate)
            throw new IllegalArgumentException(
                    String.format(
                            "Fields %s and %s have conflicting definitions of option %s - parent field %s declares itself as sealed and cannot be overridden",
                            parentField, childField, names, parentField));

        // Child must explicitly state that it overrides otherwise we cannot
        // override UNLESS it is the case that this is a duplicate which
        // can happen when using @Inject to inject options via delegates
        if (!child.isOverride() && !isDuplicate)
            throw new IllegalArgumentException(
                    String.format(
                            "Fields %s and %s have conflicting definitions of option %s - if you wanted to override this option you must explicitly specify override = true in your child field annotation",
                            parentField, childField, names));

        // Attempt overriding, this will error if the overriding is not possible
        OptionMetadata merged = OptionMetadata.override(names, parent, child);
        optionIndex.put(names, merged);
    }

    private static <T> ImmutableList<T> concat(Iterable<T> iterable, T item) {
        return ImmutableList.<T> builder().addAll(iterable).add(item).build();
    }

    public static void loadCommandsIntoGroupsByAnnotation(List<CommandMetadata> allCommands,
            List<CommandGroupMetadata> commandGroups, List<CommandMetadata> defaultCommandGroup) {
        List<CommandMetadata> newCommands = new ArrayList<CommandMetadata>();

        // first, create any groups explicitly annotated
        createGroupsFromAnnotations(allCommands, newCommands, commandGroups, defaultCommandGroup);

        for (CommandMetadata command : allCommands) {
            boolean added = false;

            // now add the command to any groupNames specified in the Command
            // annotation
            for (String groupName : command.getGroupNames()) {
                CommandGroupMetadata group = find(commandGroups,
                        compose(equalTo(groupName), CommandGroupMetadata.nameGetter()), null);
                if (group != null) {
                    group.addCommand(command);
                    added = true;
                } else {
                    ImmutableList.Builder<OptionMetadata> groupOptionsBuilder = ImmutableList.builder();
                    groupOptionsBuilder.addAll(command.getGroupOptions());
                    CommandGroupMetadata newGroup = loadCommandGroup(groupName, "", false, null,
                            Collections.singletonList(command));
                    commandGroups.add(newGroup);
                    added = true;
                }
            }

            if (added && defaultCommandGroup.contains(command)) {
                defaultCommandGroup.remove(command);
            }
        }

        allCommands.addAll(newCommands);
    }

    @SuppressWarnings("rawtypes")
    private static void createGroupsFromAnnotations(List<CommandMetadata> allCommands,
            List<CommandMetadata> newCommands, List<CommandGroupMetadata> commandGroups,
            List<CommandMetadata> defaultCommandGroup) {
        for (CommandMetadata command : allCommands) {
            boolean added = false;

            // first, create any groups explicitly annotated
            for (Group groupAnno : command.getGroups()) {
                Class defaultCommandClass = null;
                CommandMetadata defaultCommand = null;

                // load default command if needed
                if (!groupAnno.defaultCommand().equals(Group.DEFAULT.class)) {
                    defaultCommandClass = groupAnno.defaultCommand();
                    defaultCommand = find(allCommands,
                            compose(equalTo(defaultCommandClass), CommandMetadata.typeGetter()), null);
                    if (null == defaultCommand) {
                        defaultCommand = loadCommand(defaultCommandClass);
                        newCommands.add(defaultCommand);
                    }
                }

                // load other commands if needed
                List<CommandMetadata> groupCommands = new ArrayList<CommandMetadata>(groupAnno.commands().length);
                CommandMetadata groupCommand = null;
                for (Class commandClass : groupAnno.commands()) {
                    groupCommand = find(allCommands, compose(equalTo(commandClass), CommandMetadata.typeGetter()), null);
                    if (null == groupCommand) {
                        groupCommand = loadCommand(commandClass);
                        newCommands.add(groupCommand);
                        groupCommands.add(groupCommand);
                    }
                }

                CommandGroupMetadata groupMetadata = find(commandGroups,
                        compose(equalTo(groupAnno.name()), CommandGroupMetadata.nameGetter()), null);
                if (null == groupMetadata) {
                    groupMetadata = loadCommandGroup(groupAnno.name(), groupAnno.description(), groupAnno.hidden(),
                            defaultCommand, groupCommands);
                    commandGroups.add(groupMetadata);
                }

                groupMetadata.addCommand(command);
                added = true;
            }

            if (added && defaultCommandGroup.contains(command)) {
                defaultCommandGroup.remove(command);
            }
        }
    }

    private static class InjectionMetadata {
        private List<OptionMetadata> globalOptions = newArrayList();
        private List<OptionMetadata> groupOptions = newArrayList();
        private List<OptionMetadata> commandOptions = newArrayList();
        private OptionMetadata defaultOption = null;
        private List<ArgumentsMetadata> arguments = newArrayList();
        private List<Accessor> metadataInjections = newArrayList();

        private void compact() {
            globalOptions = overrideOptionSet(globalOptions);
            groupOptions = overrideOptionSet(groupOptions);
            commandOptions = overrideOptionSet(commandOptions);
            if (defaultOption != null) {
                for (OptionMetadata option : commandOptions) {
                    if (Sets.intersection(option.getOptions(), defaultOption.getOptions()).size() > 0) {
                        defaultOption = option;
                        break;
                    }
                }
            }

            if (arguments.size() > 1) {
                arguments = ImmutableList.of(new ArgumentsMetadata(arguments));
            }
        }
    }
}
