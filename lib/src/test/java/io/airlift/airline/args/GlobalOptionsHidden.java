package io.airlift.airline.args;

import io.airlift.airline.Command;
import io.airlift.airline.Option;
import io.airlift.airline.OptionType;

@Command(name="GlobalOptionsHidden")
public class GlobalOptionsHidden
{
    @Option(type = OptionType.GLOBAL, name = {"-hd", "--hidden"}, hidden = true)
    public boolean hiddenOption;

    @Option(type = OptionType.GLOBAL, name = {"-op" ,"--optional"}, hidden = false)
    public boolean optionalOption;
}
