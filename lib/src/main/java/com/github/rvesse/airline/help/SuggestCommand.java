package com.github.rvesse.airline.help;

import com.github.rvesse.airline.Arguments;
import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.SuggesterMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.Parser;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.github.rvesse.airline.parser.ParserUtil.createInstance;
import static com.google.common.collect.Lists.newArrayList;

@Command(name = "suggest")
public class SuggestCommand<T>
        implements Runnable, Callable<Void>
{
    private static final Map<Context, Class<? extends Suggester>> BUILTIN_SUGGESTERS = ImmutableMap.<Context, Class<? extends Suggester>>builder()
            .put(Context.GLOBAL, GlobalSuggester.class)
            .put(Context.GROUP, GroupSuggester.class)
            .put(Context.COMMAND, CommandSuggester.class)
            .build();

    @Inject
    public GlobalMetadata<T> metadata;

    @Arguments
    public List<String> arguments = newArrayList();

    @VisibleForTesting
    public Iterable<String> generateSuggestions()
    {
        Parser<T> parser = new Parser<T>();
        ParseState<T> state = parser.parse(metadata, arguments);

        Class<? extends Suggester> suggesterClass = BUILTIN_SUGGESTERS.get(state.getLocation());
        if (suggesterClass != null) {
            SuggesterMetadata suggesterMetadata = MetadataLoader.loadSuggester(suggesterClass);

            if (suggesterMetadata != null) {
                ImmutableMap.Builder<Class<?>, Object> bindings = ImmutableMap.<Class<?>, Object>builder()
                        .put(GlobalMetadata.class, metadata);

                if (state.getGroup() != null) {
                    bindings.put(CommandGroupMetadata.class, state.getGroup());
                }

                if (state.getCommand() != null) {
                    bindings.put(CommandMetadata.class, state.getCommand());
                }

                Suggester suggester = createInstance(suggesterMetadata.getSuggesterClass(),
                        ImmutableList.<OptionMetadata>of(),
                        null,
                        null,
                        null,
                        suggesterMetadata.getMetadataInjections(),
                        bindings.build());

                return suggester.suggest();
            }
        }

        return ImmutableList.of();
    }

    @Override
    public void run()
    {
        System.out.println(Joiner.on("\n").join(generateSuggestions()));
    }

    @Override
    public Void call()
    {
        run();
        return null;
    }
}
