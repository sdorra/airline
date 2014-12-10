package io.airlift.airline.model;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import io.airlift.airline.Accessor;
import io.airlift.airline.OptionType;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class OptionMetadata {
    private final OptionType optionType;
    private final Set<String> options;
    private final String title;
    private final String description;
    private final int arity;
    private final boolean required, hidden, overrides, sealed;
    private final Set<String> allowedValues;
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
                          Iterable<Field> path) {
    //@formatter:on
        Preconditions.checkNotNull(optionType, "optionType is null");
        Preconditions.checkNotNull(options, "options is null");
        Preconditions.checkArgument(!Iterables.isEmpty(options), "options is empty");
        Preconditions.checkNotNull(title, "title is null");

        this.optionType = optionType;
        this.options = ImmutableSet.copyOf(options);
        this.title = title;
        this.description = description;
        this.arity = arity;
        this.required = required;
        this.hidden = hidden;
        this.overrides = overrides;
        this.sealed = sealed;

        if (allowedValues != null) {
            this.allowedValues = ImmutableSet.copyOf(allowedValues);
        } else {
            this.allowedValues = null;
        }

        if (path != null) {
            this.accessors = ImmutableSet.of(new Accessor(path));
        }
    }

    public OptionMetadata(Iterable<OptionMetadata> options) {
        Preconditions.checkNotNull(options, "options is null");
        Preconditions.checkArgument(!Iterables.isEmpty(options), "options is empty");

        Preconditions.checkArgument(options.iterator().hasNext());
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
            this.allowedValues = ImmutableSet.copyOf(option.allowedValues);
        } else {
            this.allowedValues = null;
        }

        Set<Accessor> accessors = newHashSet();
        for (OptionMetadata other : options) {
            Preconditions.checkArgument(option.equals(other), "Conflicting options definitions: %s, %s", option, other);

            accessors.addAll(other.getAccessors());
        }
        this.accessors = ImmutableSet.copyOf(accessors);
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
        if (sealed != that.overrides) {
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
        result = 31 * result + arity;
        result = 31 * result + (description != null ? description.hashCode() : 0);
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
    public static OptionMetadata override(String name, OptionMetadata parent, OptionMetadata child) {
        if (parent.optionType != child.optionType)
            throw new IllegalArgumentException(
                    String.format("Cannot change optionType when overriding option %s", name));
        if (parent.arity != child.arity)
            throw new IllegalArgumentException(String.format("Cannot change arity when overriding option %s", name));

        if (parent.sealed)
            throw new IllegalArgumentException(String.format(
                    "Cannot override option %s as parent option declares it to be sealed", name));

        if (!child.overrides)
            throw new IllegalArgumentException(String.format(
                    "Cannot override option %s unless child option sets overrides to true", name));

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
                                    child.allowedValues != null ? child.allowedValues : parent.allowedValues, null);
        //@formatter:on

        merged.accessors = ImmutableSet.copyOf(child.accessors);
        return merged;
    }

    public static Function<OptionMetadata, Set<String>> optionsGetter() {
        return new Function<OptionMetadata, Set<String>>() {
            public Set<String> apply(OptionMetadata input) {
                return input.getOptions();
            }
        };
    }

    public static Predicate<OptionMetadata> isHiddenPredicate() {
        return new Predicate<OptionMetadata>() {
            @Override
            public boolean apply(@Nullable OptionMetadata input) {
                return !input.isHidden();
            }
        };
    }
}
