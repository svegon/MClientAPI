package io.github.svegon.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks that the annotated parameter is mutable but it trusts that
 * during the call to the executable it is only mutatated by the
 * executable and no parallel threads.
 *
 * Annotating an executable with this annotation means the previous
 * statement applies to all its parameters.
 *
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
public @interface TrustedMutableArg {
    /**
     * @return whether the argument itself is mutated by the executable
     */
    boolean isMutated() default false;
}
