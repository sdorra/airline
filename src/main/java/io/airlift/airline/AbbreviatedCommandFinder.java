package io.airlift.airline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.airlift.airline.model.CommandMetadata;

import com.google.common.base.Predicate;

public class AbbreviatedCommandFinder implements Predicate<CommandMetadata> {

    private List<CommandMetadata> availableCommands = new ArrayList<CommandMetadata>();

    public AbbreviatedCommandFinder(String cmd, Collection<CommandMetadata> commands) {
        this.availableCommands.addAll(commands);
        for (int i = 0; i < this.availableCommands.size(); i++) {
            CommandMetadata metadata = this.availableCommands.get(i);
            if (metadata.getName().equals(cmd))
                continue;
            if (metadata.getName().startsWith(cmd))
                continue;
            this.availableCommands.remove(i);
            i--;
        }
    }

    @Override
    public boolean apply(CommandMetadata metadata) {
        return this.availableCommands.size() == 1 && this.availableCommands.contains(metadata);
    }
}
