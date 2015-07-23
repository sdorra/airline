package com.github.rvesse.airline.model;

import com.github.rvesse.airline.Accessor;
import com.github.rvesse.airline.OptionType;
import com.github.rvesse.airline.utils.AirlineUtils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.collections4.SetUtils;

public class OptionMetadata {
    private final OptionType optionType;
    private final Set<String> options;
    private final String title, description, completionCommand;
    private final int arity, completionBehaviour;
    private final boolean required, hidden, overrides, sealed;
    private final Set<String> allowedValues;
    private final boolean ignoreCase;
    private Set<Accessor> accessors;

    //@formatter:off
    public OptionMetadata(OptionType optionType, 
                          Iterable<String> options, 
                          String title, 
                          String description, 
                          int arity,
                          boolean required, 
                          boolean hidden, 
                          boolean overrides, 
                          boolean sealed,
                          Iterable<String> allowedValues,
                          boolean ignoreCase,
                          int completionBehaviours,
                          String completionCommand,
                          Iterable<Field> path) {
    //@formatter:on
        if (optionType == null)
            throw new NullPointerException("optionType cannot be null");
        if (options == null)
            throw new NullPointerException("options cannot be null");
        if (!options.iterator().hasNext())
            throw new NullPointerException("options cannot be empty");
        if (title == null)
            throw new NullPointerException("title cannot be null");

        this.optionType = optionType;
        this.options = AirlineUtils.unmodifiableSetCopy(options);
        this.title = title;
        this.description = description;
        this.arity = arity;
        this.required = required;
        this.hidden = hidden;
        this.overrides = overrides;
        this.sealed = sealed;
        this.completionBehaviour = completionBehaviours;
        this.completionCommand = completionCommand;
        this.ignoreCase = ignoreCase;

        if (allowedValues != null) {
            this.allowedValues = AirlineUtils.unmodifiableSetCopy(allowedValues);
        } else {
            this.allowedValues = null;
        }

        if (path != null) {
            this.accessors = SetUtils.unmodifiableSet(AirlineUtils.singletonSet(new Accessor(path)));
        }
    }

    public OptionMetadata(Iterable<OptionMetadata> options) {
        if (options == null)
            throw new NullPointerException("options cannot be null");
        if (!options.iterator().hasNext())
            throw new IllegalArgumentException("options cannot be empty");

        OptionMetadata option = options.iterator().next();

        this.optionType = option.optionType;
        this.options = option.options;
        this.title = option.title;
        this.description = option.description;
        this.arity = option.arity;
        this.required = option.required;
        this.hidden = option.hidden;
        this.overrides = option.overrides;
        this.sealed = option.sealed;
        if (option.allowedValues != null) {
            this.allowedValues = AirlineUtils.unmodifiableSetCopy(option.allowedValues);
        } else {
            this.allowedValues = null;
        }
        this.completionBehaviour = option.completionBehaviour;
        this.completionCommand = option.completionCommand;

        Set<Accessor> accessors = new LinkedHashSet<Accessor>();
        for (OptionMetadata other : options) {
            if (!option.equals(other))
                throw new IllegalArgumentException(
                        String.format("Duplicate options definitions: %s, %s", option, other));

            accessors.addAll(other.getAccessors());
        }
        this.accessors = SetUtils.unmodifiableSet(accessors);
        this.ignoreCase = option.ignoreCase;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public Set<String> getOptions() {
        return options;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getArity() {
        return arity;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isOverride() {
        return overrides;
    }

    public boolean isSealed() {
        return sealed;
    }

    public int getCompletionBehaviours() {
        return completionBehaviour;
    }

    public String getCompletionCommand() {
        return completionCommand;
    }

    public boolean isMultiValued() {
        return accessors.iterator().next().isMultiValued();
    }

    public Class<?> getJavaType() {
        return accessors.iterator().next().getJavaType();
    }

    public Set<Accessor> getAccessors() {
        if (accessors == null) {
            throw new NullPointerException("No accessors defined for option");
        }
        return accessors;
    }

    public Set<String> getAllowedValues() {
        return allowedValues;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OptionMetadata that = (OptionMetadata) o;

        if (arity != that.arity) {
            return false;
        }
        if (hidden != that.hidden) {
            return false;
        }
        if (required != that.required) {
            return false;
        }
        if (overrides != that.overrides) {
            return false;
        }
        if (sealed != that.sealed) {
            return false;
        }
        if (allowedValues != null ? !allowedValues.equals(that.allowedValues) : that.allowedValues != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (optionType != that.optionType) {
            return false;
        }
        if (!options.equals(that.options)) {
            return false;
        }
        if (!title.equals(that.title)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = optionType.hashCode();
        result = 31 * result + options.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + arity;
        result = 31 * result + (required ? 1 : 0);
        result = 31 * result + (hidden ? 1 : 0);
        result = 31 * result + (overrides ? 1 : 0);
        result = 31 * result + (sealed ? 1 : 0);
        result = 31 * result + (allowedValues != null ? allowedValues.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OptionMetadata");
        sb.append("{optionType=").append(optionType);
        sb.append(", options=").append(options);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", arity=").append(arity);
        sb.append(", required=").append(required);
        sb.append(", hidden=").append(hidden);
        sb.append(", override=").append(overrides);
        sb.append(", sealed=").append(sealed);
        sb.append(", allowedValues=");
        if (allowedValues != null) {
            sb.append("{");
            Iterator<String> iter = allowedValues.iterator();
            while (iter.hasNext()) {
                sb.append("'").append(iter.next()).append("'");
                if (iter.hasNext())
                    sb.append(", ");
            }
            sb.append("}");
        } else {
            sb.append("{}");
        }
        sb.append(", accessors=").append(accessors);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Tries to merge the option metadata together such that the child metadata
     * takes precedence. Not all options can be successfully overridden and an
     * error may be thrown in cases where merging is not possible
     * <p>
     * The following pieces of metadata may be overridden:
     * </p>
     * <ul>
     * <li>Title</li>
     * <li>Description</li>
     * <li>Required</li>
     * <li>Hidden</li>
     * </ul>
     * 
     * @param parent
     *            Parent
     * @param child
     *            Child
     * @return Merged metadata
     */
    public static OptionMetadata override(Set<String> names, OptionMetadata parent, OptionMetadata child) {
        // Cannot change option type, arity or names
        if (parent.optionType != child.optionType)
            throw new IllegalArgumentException(String.format("Cannot change optionType when overriding option %s",
                    names));
        if (parent.arity != child.arity)
            throw new IllegalArgumentException(String.format("Cannot change arity when overriding option %s", names));
        if (!parent.options.equals(child.options))
            throw new IllegalArgumentException(String.format("Cannot change option names when overriding option %s",
                    names));

        // Also cannot change the type of the option unless the change is a
        // narrowing conversion
        Class<?> parentType = parent.getJavaType();
        Class<?> childType = child.getJavaType();
        if (!parentType.equals(childType)) {
            if (!parentType.isAssignableFrom(childType)) {
                if (childType.isAssignableFrom(parentType)) {
                    // A widening conversion exists but this is illegal however
                    // we can give a slightly more informative error in this
                    // case
                    throw new IllegalArgumentException(
                            String.format(
                                    "Cannot change the Java type from %s to %s when overriding option %s as this is a widening type change - only narrowing type changes are permitted",
                                    parentType, childType, names));
                } else {
                    // No conversion exists
                    throw new IllegalArgumentException(
                            String.format(
                                    "Cannot change the Java type from %s to %s when overriding option %s - only narrowing type changes where a valid cast exists are permitted",
                                    parentType, childType, names));
                }
            }
        }

        // Check for duplicates
        boolean isDuplicate = parent == child || parent.equals(child);

        // Parent must not state it is sealed UNLESS it is a duplicate which can
        // happen when using @Inject to inject options via delegates
        if (parent.sealed && !isDuplicate)
            throw new IllegalArgumentException(String.format(
                    "Cannot override option %s as parent option declares it to be sealed", names));

        // Child must explicitly state that it overrides otherwise we cannot
        // override UNLESS it is the case that this is a duplicate which
        // can happen when using @Inject to inject options via delegates
        if (!child.overrides && !isDuplicate)
            throw new IllegalArgumentException(String.format(
                    "Cannot override option %s unless child option sets overrides to true", names));

        OptionMetadata merged;
        //@formatter:off
        merged = new OptionMetadata(child.optionType, 
                                    child.options, 
                                    child.title != null ? child.title : parent.title,
                                    child.description != null ? child.description : parent.description, 
                                    child.arity, 
                                    child.required,
                                    child.hidden, 
                                    child.overrides,
                                    child.sealed,
                                    child.allowedValues != null ? child.allowedValues : parent.allowedValues,
                child.ignoreCase,
                                    child.completionBehaviour,
                                    child.completionCommand,
                                    null);
        //@formatter:on

        // Combine both child and parent accessors - this is necessary so the
        // parsed value propagates to all classes in the hierarchy
        Set<Accessor> accessors = new LinkedHashSet<>(child.accessors);
        accessors.addAll(parent.accessors);
        merged.accessors = AirlineUtils.unmodifiableSetCopy(accessors);
        return merged;
    }
}
