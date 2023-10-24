package com.github.svegon.utils;

import com.github.svegon.utils.annotations.Unfinished;
import com.github.svegon.utils.property_map.PropertyMap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class ClassUtil {
    private ClassUtil() {
        throw new UnsupportedOperationException();
    }

    private static final BiMap<Class<?>, String> INTERNAL_NAMES = HashBiMap.create(9);
    public static final Class<?>[] EMPTY_CLASS_ARRAY = {};
    public static final char ANONYMOUS_CLASS_PART_SEPERATOR = '^';

    public static String internalName(final @NotNull Class<?> clazz) {
        return INTERNAL_NAMES.computeIfAbsent(clazz, c -> c.isAnonymousClass()
                ? simplifiedName(c) : c.getName().replace('.', '/'));
    }

    private static String simplifiedName(@NotNull Class<?> clazz) {
        Class<?> declaring = clazz.getDeclaringClass();
        Class<?> superclass;
        int index = 0;

        if (clazz.getSuperclass() != Object.class) {
            superclass = clazz.getSuperclass();
        } else {
            Class<?>[] interfaces = clazz.getInterfaces();

            if (interfaces.length > 0) {
                superclass = interfaces[0];
            } else {
                superclass = Object.class;
            }
        }

        if (superclass.isInterface()) {
            for (Class<?> declared : declaring.getDeclaredClasses()) {
                if (declared == clazz) {
                    break;
                }

                if (Arrays.asList(declared.getInterfaces()).contains(superclass)) {
                    index++;
                }
            }
        } else {
            for (Class<?> declared : declaring.getDeclaredClasses()) {
                if (declared == clazz) {
                    break;
                }

                if (declared.getSuperclass() == superclass) {
                    index++;
                }
            }
        }

        return internalName(declaring) + ANONYMOUS_CLASS_PART_SEPERATOR + internalName(superclass)
                + ANONYMOUS_CLASS_PART_SEPERATOR + index;
    }

    public static Class<?> bySimplifiedInternalName(@NotNull String name) {
        return INTERNAL_NAMES.inverse().computeIfAbsent(name, s -> {
            try {
                return Class.forName((s.charAt(s.length() - 1) == ';' ? s.substring(1, s.length() - 1) : s)
                        .replace('/', '.'));
            } catch (ClassNotFoundException e) {
                return parseSimplifiedName(s);
            }
        });
    }

    private static Class<?> parseSimplifiedName(@NotNull String name) {
        int nextNextCarret = 0;
        int carretIndex = name.indexOf(ANONYMOUS_CLASS_PART_SEPERATOR, nextNextCarret);
        int index;
        Class<?> superclass;
        Class<?> declaring;

        try {
            declaring = Class.forName(name.substring(0, carretIndex));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }

        do {
            carretIndex = name.indexOf(ANONYMOUS_CLASS_PART_SEPERATOR, nextNextCarret);
            int nextCarret = name.indexOf(ANONYMOUS_CLASS_PART_SEPERATOR, carretIndex + 1);

            if (nextCarret < 0) {
                index = 0;
                nextNextCarret = -1;

                try {
                    superclass = Class.forName(name.substring(carretIndex));
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(e);
                }
            } else {
                nextNextCarret = name.indexOf(ANONYMOUS_CLASS_PART_SEPERATOR, nextCarret + 1);
                index = Integer.parseInt(name.substring(nextCarret + 1,
                        nextNextCarret < 0 ? name.length() : nextNextCarret));

                try {
                    superclass = Class.forName(name.substring(carretIndex, nextCarret - 1));
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            if (superclass.isInterface()) {
                for (Class<?> clazz : declaring.getDeclaredClasses()) {
                    if (Arrays.asList(clazz.getInterfaces()).contains(superclass)) {
                        if (index-- <= 0) {
                            declaring = clazz;
                            break;
                        }
                    }
                }
            } else {
                for (Class<?> clazz : declaring.getDeclaredClasses()) {
                    if (clazz.getSuperclass() == superclass) {
                        if (index-- <= 0) {
                            declaring = clazz;
                            break;
                        }
                    }
                }
            }
        } while (nextNextCarret >= 0);

        return declaring;
    }

    /**
     * Returns a list of all non-static fields declared on the given class and all of
     * its superclasses rdered from the top most class (Object) to the current class.
     *
     * @param clazz the class objects
     * @return a list of non-static fields declared on the given class and all
     * of its superclasses
     */
    public static List<Field> getAllFields(@Nullable Class<?> clazz) {
        List<Field> list = Lists.newArrayList();

        while (clazz != null) {
            list.addAll(0, Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        list.removeIf((field) -> Modifier.isStatic(field.getModifiers()));

        return list;
    }

    public static Set<Class<?>> getAllSuperclasses(Class<?> clazz) {
        Set<Class<?>> col = Sets.newCopyOnWriteArraySet();

        while (clazz != null) {
            col.add(clazz);
            clazz = clazz.getSuperclass();
        }

        return col;
    }

    public static Set<Class<?>> getAllInterfaces(@NotNull Class<?> clazz) {
        Set<Class<?>> set = Sets.newHashSet();

        if (clazz.isInterface()) {
            set.add(clazz);
        }

        for (Class<?> c : clazz.getInterfaces()) {
            set.addAll(getAllInterfaces(c));
        }

        return set;
    }

    public static Set<Class<?>> getAllSuperclassesAndInterfaces(@NotNull Class<?> clazz) {
        Set<Class<?>> set = getAllInterfaces(clazz);

        set.addAll(getAllSuperclasses(clazz));

        return set;
    }

    /**
     * finds the lowest superclass of the given elements to which they all
     * can be assigned
     *
     * returns {@code null} if all elements are null
     *
     * @param iterable an iterable
     * @return the lowest common assignable superclass of the array's elements
     */
    public static Class<?> commonAssignableSuperclass(@NotNull Iterable<?> iterable) {
        Class<?> superclass = null;

        for (Object o : iterable) {
            if (o != null) {
                superclass = commonAssignableSuperclass(superclass, o.getClass());

                if (superclass == Object.class) {
                    break;
                }
            }
        }

        return superclass;
    }

    public static Class<?> commonAssignableSuperclass(Object @NotNull [] a) {
        return commonAssignableSuperclass(Arrays.asList(a));
    }

    private static Class<?> commonAssignableSuperclass(Class<?> clazz0, Class<?> clazz1) {
        if (clazz0 == null) {
            return clazz1;
        }

        Set<Class<?>> superclasses = getAllSuperclasses(clazz0);

        superclasses.retainAll(getAllSuperclasses(clazz1));

        return superclasses.parallelStream().reduce((c0, c1) -> c0.isAssignableFrom(c1) ? c0 : c1).orElse(Object.class);
    }

    /**
     * finds the interfaces common to all elements
     *
     * ignores null elements
     *
     * returns null if all elements are null
     *
     * @param iterable iterable
     * @return set of interfaces common to all elements
     */
    public static Set<Class<?>> commonInterfaces(@NotNull Iterable<?> iterable) {
        Set<Class<?>> set = null;

        for (Object o : iterable) {
            if (o != null) {
                if (set == null) {
                    set = getAllInterfaces(o.getClass());
                } else {
                    set.retainAll(getAllInterfaces(o.getClass()));
                }

                if (set.isEmpty()) {
                    break;
                }
            }
        }

        return set;
    }

    public static Set<Class<?>> commonInterfaces(Object @NotNull [] a) {
        return commonInterfaces(Arrays.asList(a));
    }

    /**
     * finds the interfaces and class common to all elements
     * as specified in {@link commonAssignableSuperclass} and {@link commonInterfaces}
     *
     * returns null if all elements are null
     *
     * @param iterable an iterable of classes
     * @return set of interfaces common to all elements
     */
    public static Set<Class<?>> commonAssignableClasses(@NotNull Iterable<?> iterable) {
        Set<Class<?>> set = commonInterfaces(iterable);

        if (set == null) {
            return null;
        }

        set.add(commonAssignableSuperclass(iterable));

        return set;
    }

    public static Set<Class<?>> commonAssignableClasses(Object @NotNull [] a) {
        return commonAssignableClasses(Arrays.asList(a));
    }

    public static Type[] getClassTypeArguments(@NotNull Type type, final @NotNull Class<?> parametrizedClass) {
        while (type instanceof WildcardType wildcard) {
            Type[] upperBounds = wildcard.getUpperBounds();
            Type[] lowerBounds = wildcard.getLowerBounds();

            if (upperBounds.length > 0) {
                type = upperBounds[0];
            } else if (lowerBounds.length > 0) {
                type = lowerBounds[0];
            } else {
                return parametrizedClass.getTypeParameters();
            }
        }

        while (type instanceof ParameterizedType parameterizedType) {
            Class<?> clazz = (Class<?>) parameterizedType.getRawType();

            if (clazz == parametrizedClass) {
                return parameterizedType.getActualTypeArguments();
            }

            type = clazz.getGenericSuperclass();
        }

        while (type instanceof Class<?> clazz) {
            if (clazz == parametrizedClass) {
                return parametrizedClass.getTypeParameters();
            }

            type = clazz.getGenericSuperclass();
        }

        if (type instanceof TypeVariable<?> typeVariable) {
            for (Type bound : typeVariable.getBounds()) {
                try {
                    return getClassTypeArguments(bound, parametrizedClass);
                } catch (IllegalArgumentException ignore) {

                }
            }
        }

        throw new IllegalArgumentException(type + " doesn't refer to a " + parametrizedClass.getName() + " type");
    }

    @Unfinished
    public static Type[] getInterfaceTypeArguments(@NotNull Type type, final @NotNull Class<?> parameterizedInterface) {
        while (type instanceof WildcardType wildcard) {
            Type[] upperBounds = wildcard.getUpperBounds();
            Type[] lowerBounds = wildcard.getLowerBounds();

            if (upperBounds.length > 0) {
                type = upperBounds[0];
            } else if (lowerBounds.length > 0) {
                type = lowerBounds[0];
            } else {
                return parameterizedInterface.getTypeParameters();
            }
        }

        if (type instanceof ParameterizedType parameterizedType) {
            Class<?> clazz = (Class<?>) parameterizedType.getRawType();

            if (clazz == parameterizedInterface) {
                return parameterizedType.getActualTypeArguments();
            }

            for (Type interf : clazz.getGenericInterfaces()) {
                try {
                    return getInterfaceTypeArguments(interf, parameterizedInterface);
                } catch (IllegalArgumentException ignore) {

                }
            }
        } else if (type instanceof Class<?> clazz) {
            if (clazz == parameterizedInterface) {
                return parameterizedInterface.getTypeParameters();
            }

            for (Type interf : clazz.getGenericInterfaces()) {
                try {
                    return getInterfaceTypeArguments(interf, parameterizedInterface);
                } catch (IllegalArgumentException ignore) {

                }
            }
        } else if (type instanceof TypeVariable<?> typeVariable) {
            for (Type bound : typeVariable.getBounds()) {
                try {
                    return getInterfaceTypeArguments(bound, parameterizedInterface);
                } catch (IllegalArgumentException ignore) {

                }
            }
        }

        throw new IllegalArgumentException(type + " doesn't refer to a " + parameterizedInterface.getName() + " type!");
    }

    public static Type[] getTypeArguments(@NotNull Type type, final @NotNull Class<?> parametrizedClass) {
        return parametrizedClass.isInterface() ? getInterfaceTypeArguments(type, parametrizedClass)
                : getClassTypeArguments(type, parametrizedClass);
    }

    public static String methodDescriptor(final @NotNull Class<?> returnType, final @NotNull List<Class<?>> params) {
        if (params.size() == 0) {
            return "()" + returnType.descriptorString();
        }

        StringBuilder builder = new StringBuilder(36 + params.size() * 24).append("(")
                .append(params.get(0).descriptorString());

        for (int i = 1; i < params.size(); i++) {
            builder.append(params.get(i).descriptorString());
        }

        return builder.append(")").append(returnType.descriptorString()).toString();
    }

    public static String methodDescriptor(Class<?> returnType, Class<?> @NotNull ... params) {
        return methodDescriptor(returnType, Arrays.asList(params));
    }

    static {
        INTERNAL_NAMES.inverse().put("V", Void.TYPE);
        INTERNAL_NAMES.inverse().put("Z", Boolean.TYPE);
        INTERNAL_NAMES.inverse().put("B", Byte.TYPE);
        INTERNAL_NAMES.inverse().put("S", Short.TYPE);
        INTERNAL_NAMES.inverse().put("I", Integer.TYPE);
        INTERNAL_NAMES.inverse().put("J", Long.TYPE);
        INTERNAL_NAMES.inverse().put("C", Character.TYPE);
        INTERNAL_NAMES.inverse().put("F", Float.TYPE);
        INTERNAL_NAMES.inverse().put("D", Double.TYPE);
    }
}
