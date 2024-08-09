package io.github.svegon.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method or constructor annotated with @UnsafeAccess or any method
 * or a constructor of an annotated class is a native (or invokes a native)
 * method which meets the properties of an unsafe method according to JVM
 * specifications. In case a variable is annotated then its value may be
 * obtained using such method or constructor. In case a package is annotated
 * with @UnsafeAccess then all of its classes match the specifications
 * of being annotated with @UnsafeAccess.
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR,
        ElementType.LOCAL_VARIABLE, ElementType.PACKAGE, ElementType.TYPE_USE, ElementType.RECORD_COMPONENT})
public @interface UnsafeAccess {
}
