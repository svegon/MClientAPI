package com.github.svegon.utils;

import com.github.svegon.utils.annotations.Unfinished;
import com.github.svegon.utils.collections.ArrayUtil;
import com.github.svegon.utils.hash.HashUtil;
import com.github.svegon.utils.interfaces.DeepClonable;
import com.github.svegon.utils.interfaces.IdentityComparable;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Util {
    private Util() {
        throw new UnsupportedOperationException();
    }

    private static final Map<Class<?>, DeepCopyMethod> DEEP_COPY_METHOD_CACHE =
            new Object2ObjectOpenCustomHashMap<>(HashUtil.identityStrategy());
    private static final Map<DeepCopyMethod, ArrayDeepCopy> ARRAY_DEEP_COPY_CACHE =
            new Object2ObjectOpenCustomHashMap<>(HashUtil.identityStrategy());
    private static final DeepCopyMethod IDENTITY_COPY = new DeepCopyMethod() {
        @Override
        public <E> E copy(E original) {
            return original;
        }
    };
    private static final DeepCopyMethod PURE_CLONE = new DeepCopyMethod() {
        @Override
        @SuppressWarnings("unchecked")
        public <E> E copy(E original) {
            try {
                Method cloneMethod = Object.class.getDeclaredMethod("clone");
                cloneMethod.setAccessible(true);
                return (E) cloneMethod.invoke(original, (Object[]) null);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            }
        }
    };
    private static final DeepCopyMethod DEEP_CLONE = new DeepCopyMethod() {
        @Override
        @SuppressWarnings("unchecked")
        public <E> E copy(E original) {
            return (E) ((DeepClonable) original).deepClone();
        }
    };
    private static final DeepCopyMethod OBJECT_ARRAY_COPY = new DeepCopyMethod() {
        @Override
        @SuppressWarnings("unchecked")
        public <E> E copy(E original) {
            Object[] array = (Object[]) original;
            Object[] copy = ArrayUtil.newArray(array);

            for (int i = 0; i < copy.length; i++) {
                copy[i] = deepCopy(array[i]);
            }

            return (E) copy;
        }
    };


    public static <E> E make(Supplier<E> creatingFunction) {
        return creatingFunction.get();
    }

    public static <E> E make(E obj, Consumer<E> initializer) {
        initializer.accept(obj);
        return obj;
    }

    /**
     * Creates a deep copy of the given objects.
     * It is possible that the result will be the original itself if its deeply immutable or a similar reason.
     * If original == null then return null.
     * @param original the objects who will be deeply copied
     * @return a deep copy o
     */
    @Unfinished
    public static <E> E deepCopy(@Nullable E original) {
        // first let's filter some built-in deeply immutable objects
        if (original == null) {
            return null;
        }

        Class<?> clazz = original.getClass();
        DeepCopyMethod copyMethod = DEEP_COPY_METHOD_CACHE.get(clazz);

        if (copyMethod != null) {
            return copyMethod.copy(original);
        }

        if (original instanceof DeepClonable) {
            DEEP_COPY_METHOD_CACHE.put(clazz, DEEP_CLONE);
            return DEEP_CLONE.copy(original);
        }

        if (original instanceof Enum<?> || clazz.getAnnotation(Immutable.class) != null) {
            DEEP_COPY_METHOD_CACHE.put(clazz, IDENTITY_COPY);
            return original;
        }

        if (original instanceof IdentityComparable) {
            DEEP_COPY_METHOD_CACHE.put(clazz, IDENTITY_COPY);
            return original;
        }

        if (clazz.isArray()) {
            Class<?> componentType = clazz.componentType();

            if (Modifier.isFinal(componentType.getModifiers())) {
                if (DEEP_COPY_METHOD_CACHE.containsKey(componentType)) {
                    copyMethod = ARRAY_DEEP_COPY_CACHE.computeIfAbsent(DEEP_COPY_METHOD_CACHE
                                    .get(componentType), ArrayDeepCopy::new);
                    DEEP_COPY_METHOD_CACHE.put(clazz, copyMethod);
                    return copyMethod.copy(original);
                }

                // well we weren't able to generate a deep copy method, so we're just try next time
                return OBJECT_ARRAY_COPY.copy(original);
            } else if (Enum.class.isAssignableFrom(componentType)) {
                DeepCopyMethod identityComponentArrayCopy = ARRAY_DEEP_COPY_CACHE.get(IDENTITY_COPY);
                DEEP_COPY_METHOD_CACHE.put(clazz, identityComponentArrayCopy);
                return identityComponentArrayCopy.copy(original);
            }

            DEEP_COPY_METHOD_CACHE.put(clazz, OBJECT_ARRAY_COPY);
            return OBJECT_ARRAY_COPY.copy(original);
        }

        copyMethod = PURE_CLONE;
        List<Field> allFields = ClassUtil.getAllFields(clazz);
        E copy;

        allFields.removeIf((f) -> Modifier.isFinal(f.getModifiers()) && Modifier.isFinal(f.getType().getModifiers())
                && DEEP_COPY_METHOD_CACHE.get(f.getType()) == IDENTITY_COPY);

        if (allFields.isEmpty()) {
            try {
                copy = PURE_CLONE.copy(original);
            } catch (IllegalArgumentException e) {
                copyMethod = IDENTITY_COPY;
                copy = IDENTITY_COPY.copy(original);
            }
        } else {
            allFields.removeIf(f -> Modifier.isFinal(f.getType().getModifiers())
                    && DEEP_COPY_METHOD_CACHE.get(f.getType()) == IDENTITY_COPY);

            if (allFields.isEmpty()) {
                try {
                    copy = PURE_CLONE.copy(original);
                } catch (IllegalArgumentException e) {
                    copyMethod = IDENTITY_COPY;
                    copy = copyMethod.copy(original);
                }
            } else {
                copyMethod = new ReflectiveFieldDeepCopy(ImmutableList.copyOf(allFields));
                copy = copyMethod.copy(original);
            }
        }

        DEEP_COPY_METHOD_CACHE.put(clazz, copyMethod);
        return copy;
    }

    @FunctionalInterface
    public interface DeepCopyMethod {
        <E> E copy(E original);
    }

    private static final class ArrayDeepCopy implements DeepCopyMethod {
        private final DeepCopyMethod componentMethod;

        private ArrayDeepCopy(DeepCopyMethod matchingMethod) {
            this.componentMethod = matchingMethod;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <E> E copy(E original) {
            Object[] array = (Object[]) original;
            Object[] copy = ArrayUtil.newArray(array);
            int length = copy.length;

            for (int i = 0; i < length; i++) {
                copy[i] = componentMethod.copy(array[i]);
            }

            return (E) copy;
        }
    }

    private static final class ReflectiveFieldDeepCopy implements DeepCopyMethod {
        private final ImmutableList<Field> fields;

        private ReflectiveFieldDeepCopy(ImmutableList<Field> fields) {
            this.fields = fields;
        }

        @Override
        public <E> E copy(E original) {
            // we can still make a new instance and deeply copy all fields
            try {
                E copy = PURE_CLONE.copy(original);

                for (Field field : fields) { // todo
                    field.set(copy, field.get(original));
                }

                return copy;
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    static {
        DEEP_COPY_METHOD_CACHE.put(Boolean.class, IDENTITY_COPY);
        DEEP_COPY_METHOD_CACHE.put(Byte.class, IDENTITY_COPY);
        DEEP_COPY_METHOD_CACHE.put(Short.class, IDENTITY_COPY);
        DEEP_COPY_METHOD_CACHE.put(Integer.class, IDENTITY_COPY);
        DEEP_COPY_METHOD_CACHE.put(Long.class, IDENTITY_COPY);
        DEEP_COPY_METHOD_CACHE.put(Float.class, IDENTITY_COPY);
        DEEP_COPY_METHOD_CACHE.put(Double.class, IDENTITY_COPY);
        DEEP_COPY_METHOD_CACHE.put(Class.class, IDENTITY_COPY);
        DEEP_COPY_METHOD_CACHE.put(String.class, IDENTITY_COPY);
        DEEP_COPY_METHOD_CACHE.put(boolean[].class, PURE_CLONE);
        DEEP_COPY_METHOD_CACHE.put(byte[].class, PURE_CLONE);
        DEEP_COPY_METHOD_CACHE.put(short[].class, PURE_CLONE);
        DEEP_COPY_METHOD_CACHE.put(int[].class, PURE_CLONE);
        DEEP_COPY_METHOD_CACHE.put(long[].class, PURE_CLONE);
        DEEP_COPY_METHOD_CACHE.put(float[].class, PURE_CLONE);
        DEEP_COPY_METHOD_CACHE.put(double[].class, PURE_CLONE);
        ARRAY_DEEP_COPY_CACHE.put(IDENTITY_COPY, new ArrayDeepCopy(IDENTITY_COPY));
    }
}
