package io.github.svegon.utils.property_map;

import io.github.svegon.utils.ClassUtil;
import io.github.svegon.utils.interfaces.JsonSerializable;
import io.github.svegon.utils.json.JsonUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@JsonAdapter(PropertyMap.Serializer.class)
public class PropertyMap<E> extends AbstractMap<String, E> implements JsonSerializable {
    private final Map<String, Property<E>> parent;

    public PropertyMap(Map<String, Property<E>> parent) {
        this.parent = Preconditions.checkNotNull(parent);
    }

    @Override
    public E get(Object key) {
        if (key instanceof String) {
            return parent.computeIfAbsent((String) key, (k) -> Property.simple()).get();
        }

        return null;
    }

    @Override
    public E put(String key, E value) {
        return parent.computeIfAbsent(key, (k) -> Property.simple()).getAndSet(value);
    }

    @Override
    public int size() {
        return parent.size();
    }

    @Override
    public boolean isEmpty() {
        return parent.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return parent.containsKey(key);
    }

    @NotNull
    @Override
    public Set<Entry<String, E>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<Entry<String, E>> iterator() {
                return new Iterator<>() {
                    private final Iterator<Entry<String, Property<E>>> parentItr = parent.entrySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return parentItr.hasNext();
                    }

                    @Override
                    public Entry<String, E> next() {
                        return new Entry<>() {
                            private final Entry<String, Property<E>> backEntry = parentItr.next();

                            @Override
                            public String getKey() {
                                return backEntry.getKey();
                            }

                            @Override
                            public E getValue() {
                                return backEntry.getValue().get();
                            }

                            @Override
                            public E setValue(E value) {
                                E prev = backEntry.getValue().get();
                                backEntry.getValue().set(value);
                                return prev;
                            }
                        };
                    }
                };
            }

            @Override
            public boolean add(Entry<String, E> stringEEntry) {
                return replace(stringEEntry.getKey(), stringEEntry.getValue()) != null;
            }

            @Override
            public boolean remove(Object o) {
                if (o instanceof Entry entry) {
                    return PropertyMap.this.remove(entry.getKey(), entry.getValue());
                }

                return false;
            }

            @Override
            public boolean retainAll(@NotNull final Collection<?> c) {
                return removeIf(c::contains);
            }

            @Override
            public boolean removeAll(@NotNull final Collection<?> c) {
                return removeIf((e) -> !c.contains(e));
            }

            @Override
            public void clear() {
                PropertyMap.this.clear();
            }

            @Override
            public int size() {
                return PropertyMap.this.size();
            }

            @Override
            public boolean isEmpty() {
                return PropertyMap.this.isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                if (o instanceof Entry entry) {
                    E v = PropertyMap.this.get(entry.getKey());
                    return v != null && v.equals(entry.getValue());
                }

                return false;
            }
        };
    }

    @Override
    public E computeIfAbsent(final String key, @NotNull final Function<? super String, ? extends E> mappingFunction) {
        return parent.computeIfAbsent(key, (k) -> Property.simple(mappingFunction.apply(k))).get();
    }

    @Override
    public E computeIfPresent(final String key,
                              @NotNull final BiFunction<? super String, ? super E, ? extends E> remappingFunction) {
        final Property<E> property = parent.computeIfPresent(key, (k, v) -> Property.simple(remappingFunction.apply(k,
                v.get())));
        return property == null ? null : property.get();
    }

    @Override
    public E compute(final String key,
                     @NotNull final BiFunction<? super String, ? super E, ? extends E> remappingFunction) {
        final Property<E> property = parent.compute(key, (k, v) -> Property.simple(remappingFunction.apply(k,
                v == null ? null : v.get())));
        return property.get();
    }

    public boolean putProperty(final String key, final Property<E> property) {
        if (parent.containsKey(key)) {
            return false;
        }

        parent.put(key, property);
        return true;
    }

    public boolean overrideProperty(final String key, final Property<E> property) {
        return parent.put(key, property) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonElement toJson() {
        return Serializer.INSTANCE.create(JsonUtil.DYNAMIC_GSON.create(),
                        (TypeToken<PropertyMap<E>>) TypeToken.get(getClass())).toJsonTree(this);
    }

    public static class Serializer implements TypeAdapterFactory {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (!PropertyMap.class.isAssignableFrom(type.getRawType())) {
                return null;
            }

            return (TypeAdapter<T>) new PropertyMapAdapter<>(gson, ClassUtil.getClassTypeArguments(type.getType(),
                    PropertyMap.class)[0]);
        }
    }

    private static class PropertyMapAdapter<E> extends TypeAdapter<PropertyMap<E>> {
        private final Gson gson;
        private final Type type;

        private PropertyMapAdapter(final @NotNull Gson gson, Type type) {
            this.gson = gson;
            this.type = type;
        }

        @Override
        public void write(JsonWriter out, PropertyMap<E> value) throws IOException {
            out.beginObject();

            for (Entry<String, E> entry : value.entrySet()) {
                out.name(entry.getKey());
                gson.toJson(entry.getValue(), type, out);
            }

            out.endObject();
        }

        @Override
        public PropertyMap<E> read(JsonReader in) throws IOException {
            PropertyMap<E> map = new PropertyMap<>(Maps.newHashMap());

            in.beginObject();

            while (in.peek() != JsonToken.END_OBJECT) {
                map.put(in.nextName(), gson.fromJson(in, type));
            }

            in.endObject();

            return map;
        }
    }
}
