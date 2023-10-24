package com.github.svegon.capi.argument_types;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class MappedArgumentType<T, U> implements ArgumentType<U> {
    private final ArgumentType<T> model;
    private final Function<? super T, ? extends U> mapper;
    private final BiFunction<? super CommandContext<? extends CommandSource>, ? super SuggestionsBuilder,
            ? extends CompletableFuture<Suggestions>> suggestionCompleter;
    private final Collection<String> examples;

    private MappedArgumentType(ArgumentType<T> model, Function<? super T, ? extends U> mapper,
                               BiFunction<? super CommandContext<? extends CommandSource>, ? super SuggestionsBuilder,
                                       ? extends CompletableFuture<Suggestions>> suggestionCompleter,
                               Collection<String> examples) {
        this.model = model;
        this.mapper = mapper;
        this.suggestionCompleter = suggestionCompleter;
        this.examples = examples;
    }

    @Override
    public U parse(StringReader reader) throws CommandSyntaxException {
        return mapper.apply(model.parse(reader));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return suggestionCompleter.apply((CommandContext<CommandSource>) context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }

    public static <T, U> MappedArgumentType<T, U> of(ArgumentType<T> original,
                                                     Function<? super T, ? extends U> mapper,
                                                     BiFunction<? super CommandContext<? extends CommandSource>,
                                                             ? super SuggestionsBuilder,
                                                         ? extends CompletableFuture<Suggestions>> suggestionCompleter,
                                                     Collection<String> examples) {
        return new MappedArgumentType<>(original, mapper, suggestionCompleter, examples);
    }

    public static <T, U> MappedArgumentType<T, U> of(ArgumentType<T> original,
                                                     Function<? super T, ? extends U> mapper,
                                                     Collection<String> examples) {
        return of(original, mapper, original::listSuggestions, examples);
    }

    public static <T, U> MappedArgumentType<T, U> of(ArgumentType<T> original,
                                                     Function<? super T, ? extends U> mapper) {
        return of(original, mapper, original::listSuggestions, original.getExamples());
    }

    public static <T, U> MappedArgumentType<T, U> of(Supplier<ArgumentType<T>> originalSupplier,
                                                     Function<? super T, ? extends U> mapper) {
        return of(originalSupplier.get(), mapper);
    }
}
