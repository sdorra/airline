package io.airlift.airline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.airlift.airline.model.CommandGroupMetadata;
import com.google.common.base.Predicate;

public final class AbbreviatedGroupFinder implements Predicate<CommandGroupMetadata> {

    private List<CommandGroupMetadata> availableGroups = new ArrayList<CommandGroupMetadata>();
    private CommandGroupMetadata exactGroup;

    public AbbreviatedGroupFinder(String cmd, Collection<CommandGroupMetadata> groups) {
        this.availableGroups.addAll(groups);
        for (int i = 0; i < this.availableGroups.size(); i++) {
            CommandGroupMetadata metadata = this.availableGroups.get(i);
            if (metadata.getName().equals(cmd)) {
                exactGroup = metadata;
                continue;
            }
            if (metadata.getName().startsWith(cmd))
                continue;
            this.availableGroups.remove(i);
            i--;
        }
    }

    @Override
    public boolean apply(CommandGroupMetadata metadata) {
        return isExact(metadata) || isAbbreviation(metadata);
    }

    protected boolean isExact(CommandGroupMetadata metadata) {
        return exactGroup != null && exactGroup.equals(metadata);
    }

    protected boolean isAbbreviation(CommandGroupMetadata metadata) {
        return this.availableGroups.size() == 1 && this.availableGroups.contains(metadata);
    }
}
