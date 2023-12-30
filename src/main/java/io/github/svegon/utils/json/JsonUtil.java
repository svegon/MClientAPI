package io.github.svegon.utils.json;

import com.google.gson.*;
import io.github.svegon.utils.optional.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public final class JsonUtil {
    private JsonUtil() {
        throw new UnsupportedOperationException();
    }

    public static final Gson GSON = new Gson();
    public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final GsonBuilder DYNAMIC_GSON = new GsonBuilder();

    public static JsonElement parseFile(Path path) throws IOException, JsonParseException {
        try(BufferedReader reader = Files.newBufferedReader(path)) {
            return JsonParser.parseReader(reader);
        }
    }

    public static JsonElement parseURL(String url) throws IOException, JsonParseException {
        try(InputStream input = URI.create(url).toURL().openStream()) {
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return JsonParser.parseReader(bufferedReader);
        }
    }

    public static void saveToFile(JsonElement json, Path path) throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(path)) {
            PRETTY_GSON.toJson(json, writer);
        }
    }

    public static Optional<JsonArray> parseFileToArray(Path path) throws IOException, JsonParseException {
        JsonElement json = parseFile(path);

        if (json.isJsonArray()) {
            return Optional.of(json.getAsJsonArray());
        }

        return Optional.empty();
    }

    public static Optional<JsonArray> parseURLToArray(String url) throws IOException, JsonParseException {
        JsonElement json = parseURL(url);

        return Optional.ofNullable(json.isJsonArray() ? json.getAsJsonArray() : null);
    }

    public static Optional<JsonObject> parseFileToObject(Path path) throws IOException {
        JsonElement json = parseFile(path);

        return Optional.ofNullable(json.isJsonObject() ? json.getAsJsonObject() : null);
    }

    public static Optional<JsonObject> parseURLToObject(String url) throws IOException {
        JsonElement json = parseURL(url);

        return Optional.ofNullable(json.isJsonObject() ? json.getAsJsonObject() : null);
    }

    public static Optional<JsonElement> getProperty(JsonObject map, String property) {
        return Optional.ofNullable(map.get(property));
    }

    public static Optional<JsonObject> getAsJsonObject(JsonElement json) {
        return Optional.ofNullable(json.isJsonObject() ? json.getAsJsonObject() : null);
    }

    public static Optional<JsonArray> getAsJsonArray(JsonElement json) {
        return Optional.ofNullable(json.isJsonArray() ? json.getAsJsonArray() : null);
    }

    public static Optional<String> getAsString(JsonElement json) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isString()) {
                return Optional.of(primitive.getAsString());
            }
        }

        return Optional.empty();
    }

    public static OptionalBoolean getAsBoolean(JsonElement json) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isBoolean()) {
                return OptionalBoolean.of(json.getAsBoolean());
            }
        }

        return OptionalBoolean.empty();
    }

    public static OptionalByte getAsByte(JsonElement json) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isNumber()) {
                return OptionalByte.of(json.getAsByte());
            }
        }

        return OptionalByte.empty();
    }

    public static OptionalShort getAsShort(JsonElement json) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isNumber()) {
                return OptionalShort.of(json.getAsShort());
            }
        }

        return OptionalShort.empty();
    }

    public static OptionalInt getAsInt(JsonElement json) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isNumber()) {
                return OptionalInt.of(json.getAsInt());
            }
        }

        return OptionalInt.empty();
    }

    public static OptionalLong getAsLong(JsonElement json) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isNumber()) {
                return OptionalLong.of(json.getAsLong());
            }
        }

        return OptionalLong.empty();
    }

    public static OptionalChar getAsChar(JsonElement json) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isNumber()) {
                return OptionalChar.of((char) primitive.getAsInt());
            } else if (primitive.isString()) {
                return OptionalChar.of(primitive.getAsCharacter());
            }
        }

        return OptionalChar.empty();
    }

    public static OptionalFloat getAsFloat(JsonElement json) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isNumber()) {
                return OptionalFloat.of(json.getAsFloat());
            }
        }

        return OptionalFloat.empty();
    }

    public static OptionalDouble getAsDouble(JsonElement json) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isNumber()) {
                return OptionalDouble.of(json.getAsDouble());
            }
        }

        return OptionalDouble.empty();
    }

    public static Optional<BigInteger> getAsBigInteger(JsonElement json) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isNumber()) {
                return Optional.of(json.getAsBigInteger());
            }
        }

        return Optional.empty();
    }

    public static Optional<BigDecimal> getAsBigDecimal(JsonElement json) {
        if (json.isJsonPrimitive()) {
            if (json.getAsJsonPrimitive().isNumber()) {
                return Optional.of(json.getAsBigDecimal());
            }
        }

        return Optional.empty();
    }
}
