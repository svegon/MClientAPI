package io.github.svegon.utils.interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;

@JsonAdapter(JsonSerializable.Serializer.class)
public interface JsonSerializable {
    JsonElement toJson();

    final class Serializer implements JsonSerializer<JsonSerializable> {
        @Override
        public JsonElement serialize(JsonSerializable src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJson();
        }
    }
}
