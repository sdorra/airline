package com.github.rvesse.airline.annotations.restrictions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation that indicates that an option/argument denotes a port number and
 * its value should be restricted as such
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface Port {

    /**
     * The acceptable port types, defaults to {@code PortType#OS_ALLOCATED},
     * {@code PortType#USER} and {@code PortType#DYNAMIC}
     * 
     * @return Acceptable port types
     */
    PortType[] acceptablePorts() default { PortType.OS_ALLOCATED, PortType.USER, PortType.DYNAMIC };
}
