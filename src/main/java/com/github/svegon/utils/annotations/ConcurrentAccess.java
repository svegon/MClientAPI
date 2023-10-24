package com.github.svegon.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Elements annotated with ConcurrentAccess are visible to untrusted callers
 * but using them might break the class which declared them.
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PACKAGE,
        ElementType.RECORD_COMPONENT})
public @interface ConcurrentAccess {
}
