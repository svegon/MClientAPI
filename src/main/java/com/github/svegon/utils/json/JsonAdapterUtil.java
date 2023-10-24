package com.github.svegon.utils.json;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public final class JsonAdapterUtil {
    private JsonAdapterUtil() {
        throw new UnsupportedOperationException();
    }


    /**
     * copied from {@link com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory}
     *
     * @param constructorConstructor
     * @param gson
     * @param type
     * @param annotation
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" }) // Casts guarded by conditionals.
    public static <T> TypeAdapter<T> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson,
                                                    TypeToken<T> type, JsonAdapter annotation) {
        Object instance = constructorConstructor.get(TypeToken.get(annotation.value())).construct();

        TypeAdapter<?> typeAdapter;
        if (instance instanceof TypeAdapter) {
            typeAdapter = (TypeAdapter<?>) instance;
        } else if (instance instanceof TypeAdapterFactory) {
            typeAdapter = ((TypeAdapterFactory) instance).create(gson, type);
        } else if (instance instanceof JsonSerializer || instance instanceof JsonDeserializer) {
            JsonSerializer<?> serializer = instance instanceof JsonSerializer
                    ? (JsonSerializer) instance
                    : null;
            JsonDeserializer<?> deserializer = instance instanceof JsonDeserializer
                    ? (JsonDeserializer) instance
                    : null;
            typeAdapter = new TreeTypeAdapter(serializer, deserializer, gson, type, null);
        } else {
            throw new IllegalArgumentException("Invalid attempt to bind an instance of "
                    + instance.getClass().getName() + " as a @JsonAdapter for " + type.toString()
                    + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory,"
                    + " JsonSerializer or JsonDeserializer.");
        }

        if (typeAdapter != null && annotation.nullSafe()) {
            typeAdapter = typeAdapter.nullSafe();
        }

        return (TypeAdapter<T>) typeAdapter;
    }

    @SuppressWarnings({ "unchecked"}) // Casts guarded by conditionals.
    public static <T> @Nullable TypeAdapter<T> getTypeAdapter(final @NotNull ConstructorConstructor constructorConstructor,
                                                          final @NotNull Gson gson, final @NotNull Class<?> clazz) {
        if (clazz.isAnnotationPresent(JsonAdapter.class)) {
            return (TypeAdapter<T>) getTypeAdapter(constructorConstructor, gson, TypeToken.get(clazz),
                    clazz.getAnnotation(JsonAdapter.class));
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable TypeAdapter<T> getTypeAdapter(final @NotNull Gson gson, final @NotNull Class<?> clazz) {
        return getTypeAdapter(new ConstructorConstructor(clazz.isAnnotationPresent(JsonAdapter.class)
                ? Collections.singletonMap(clazz.getAnnotation(JsonAdapter.class).value(), (InstanceCreator<T>) (t) -> {
            try {
                return (T) (t instanceof Class ? (Class<?>) t : clazz.getAnnotation(JsonAdapter.class).value())
                        .getConstructor((Class<?>[]) null).newInstance((Object[]) null);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
                }) : Collections.emptyMap(), true, Collections.emptyList()), gson, clazz);
    }

    public static <T> @Nullable TypeAdapter<T> getTypeAdapter(final @NotNull Class<?> clazz) {
        return getTypeAdapter(JsonUtil.DYNAMIC_GSON.create(), clazz);
    }
}
