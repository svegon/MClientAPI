package io.github.svegon.capi.argument_types

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.command.CommandSource
import java.util.concurrent.CompletableFuture
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier

class MappedArgumentType<T, U> private constructor(
    private val model: ArgumentType<T>, private val mapper: Function<in T, out U>,
    private val suggestionCompleter: BiFunction<in CommandContext<out CommandSource>, in SuggestionsBuilder, out CompletableFuture<Suggestions>>,
    private val examples: Collection<String>
) : ArgumentType<U> {
    @Throws(CommandSyntaxException::class)
    override fun parse(reader: StringReader): U {
        return mapper.apply(model.parse(reader))
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return suggestionCompleter.apply(context as CommandContext<CommandSource>, builder)
    }

    override fun getExamples(): Collection<String> {
        return examples
    }

    companion object {
        fun <T, U> of(
            original: ArgumentType<T>,
            mapper: Function<in T, out U>,
            suggestionCompleter: BiFunction<in CommandContext<out CommandSource>, in SuggestionsBuilder, out CompletableFuture<Suggestions>>,
            examples: Collection<String>
        ): MappedArgumentType<T, U> {
            return MappedArgumentType(original, mapper, suggestionCompleter, examples)
        }

        fun <T, U> of(
            original: ArgumentType<T>,
            mapper: Function<in T, out U>,
            examples: Collection<String>
        ): MappedArgumentType<T, U> {
            return of(
                original,
                mapper,
                { context: CommandContext<out CommandSource>?, builder: SuggestionsBuilder? ->
                    original.listSuggestions(
                        context,
                        builder
                    )
                },
                examples
            )
        }

        fun <T, U> of(
            original: ArgumentType<T>,
            mapper: Function<in T, out U>
        ): MappedArgumentType<T, U> {
            return of(
                original,
                mapper,
                { context: CommandContext<out CommandSource>?, builder: SuggestionsBuilder? ->
                    original.listSuggestions(
                        context,
                        builder
                    )
                },
                original.examples
            )
        }

        fun <T, U> of(
            originalSupplier: Supplier<ArgumentType<T>>,
            mapper: Function<in T, out U>
        ): MappedArgumentType<T, U> {
            return of(originalSupplier.get(), mapper)
        }
    }
}
