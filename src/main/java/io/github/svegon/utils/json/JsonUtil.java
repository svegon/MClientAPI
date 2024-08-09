package io.github.svegon.utils.json;

import com.google.gson.*;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public enum JsonUtil {
    ;

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
}
