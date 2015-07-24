package com.github.rvesse.airline.annotations;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as the default option
 * <p>
 * This annotation can only be used <strong>once</strong> on a command field
 * provided the following requirements are met:
 * </p>
 * <ul>
 * <li>The field is also annotated with {@link Option}</li>
 * <li>The {@linkplain Option} annotation has an arity of 1</li>
 * <li>The {@linkplain Option} annotation has a type of {@link OptionType#COMMAND}</li>
 * <li>The command does not have any field annotated with {@link Arguments}</li>
 * </ul>
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface DefaultOption {

}
