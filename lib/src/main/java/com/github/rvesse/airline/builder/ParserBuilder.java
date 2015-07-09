package com.github.rvesse.airline.builder;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.CommandFactory;
import com.github.rvesse.airline.DefaultCommandFactory;
import com.github.rvesse.airline.DefaultTypeConverter;
import com.github.rvesse.airline.TypeConverter;
import com.github.rvesse.airline.model.AliasMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.aliases.AliasArgumentsParser;
import com.github.rvesse.airline.parser.options.ClassicGetOptParser;
import com.github.rvesse.airline.parser.options.LongGetOptParser;
import com.github.rvesse.airline.parser.options.OptionParser;
import com.github.rvesse.airline.parser.options.StandardOptionParser;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Builder for parser configurations
 *
 * @param <C>
 *            Command type
 */
public class ParserBuilder<C> extends AbstractBuilder<ParserMetadata<C>> {

    protected TypeConverter typeConverter = new DefaultTypeConverter();
    protected final Map<String, AliasBuilder<C>> aliases = newHashMap();
    protected CommandFactory<C> commandFactory = new DefaultCommandFactory<C>();
    protected boolean allowAbbreviatedCommands, allowAbbreviatedOptions, aliasesOverrideBuiltIns, aliasesMayChain;
    protected final List<OptionParser<C>> optionParsers = newArrayList();
    protected String argsSeparator;
    
    public static <T> ParserMetadata<T> defaultConfiguration() {
        return new ParserBuilder<T>().build();
    }

    public ParserBuilder<C> withCommandFactory(CommandFactory<C> commandFactory) {
        this.commandFactory = commandFactory;
        return this;
    }

    public AliasBuilder<C> withAlias(final String name) {
        checkNotBlank(name, "Alias name");

        if (aliases.containsKey(name)) {
            return aliases.get(name);
        }

        AliasBuilder<C> alias = new AliasBuilder<C>(name);
        aliases.put(name, alias);
        return alias;
    }

    public AliasBuilder<C> getAlias(final String name) {
        checkNotBlank(name, "Alias name");
        Preconditions.checkArgument(aliases.containsKey(name), "Alias %s has not been declared", name);

        return aliases.get(name);
    }

    /**
     * Reads in user aliases from the default configuration file in the default
     * location.
     * <p>
     * The default configuration file name is constructed by appending the
     * {@code .config} extension to the defined program name
     * </p>
     * <p>
     * The default search location is a {@code .program} directory under the
     * users home directory where {@code program} is the defined program name.
     * </p>
     * <p>
     * If you prefer to control these values explicitly and for more detail on
     * the configuration format please see the
     * {@link #withUserAliases(String, String, String...)} method
     * </p>
     * 
     * @return Builder
     * @throws IOException
     */
    public ParserBuilder<C> withUserAliases(String programName) throws IOException {
        // Use default filename and search location
        return withUserAliases(programName + ".config", null, System.getProperty("user.home") + "/." + programName
                + "/");
    }

    /**
     * Reads in user aliases from the default configuration file in the default
     * location
     * <p>
     * The default configuration file name is constructed by appending the
     * {@code .config} extension to the defined program name
     * </p>
     * <p>
     * If you prefer to control this value explicitly and for more detail on the
     * configuration format please see the
     * {@link #withUserAliases(String, String, String...)} method
     * </p>
     * 
     * @param searchLocation
     *            Location to search
     * 
     * @return Builder
     * @throws IOException
     */
    public ParserBuilder<C> withUserAliases(String programName, String searchLocation) throws IOException {
        // Use default filename
        return withUserAliases(programName + ".config", null, searchLocation);
    }

    /**
     * Reads in user aliases from the default configuration file in the default
     * location
     * <p>
     * This file is in standard Java properties format with the key being the
     * alias and the value being the arguments for this alias. Arguments are
     * whitespace separated though quotes ({@code "}) may be used to wrap
     * arguments that need to contain whitespace. Quotes may be escaped within
     * quoted arguments and whitespace may be escaped within unquoted arguments.
     * Note that since Java property values are interpreted as Java strings it
     * is necessary to double escape the backslash i.e. {@code \\"} for this to
     * work properly.
     * </p>
     * 
     * <pre>
     * example=command --option value
     * quoted=command "long argument"
     * escaped=command whitespace\\ escape "quote\\"escape"
     * </pre>
     * <p>
     * The search locations should be given in order of preference, the file
     * will be loaded from all search locations in which it exists such that
     * values from the locations occurring first in the search locations list
     * take precedence. This allows for having multiple locations for your
     * configuration file and layering different sets of aliases over each other
     * e.g. system, user and local aliases.
     * </p>
     * <p>
     * The {@code prefix} is used to filter properties from the properties file
     * such that you can include aliases with other configuration settings in
     * your configuration files. When a prefix is used only properties that
     * start with the prefix are interpreted as alias definitions and the actual
     * alias is the property name with the prefix removed. For example if your
     * prefix was {@code alias.} and you had a property {@code alias.foo} the
     * resulting alias would be {@code foo}.
     * </p>
     * <h3>Notes</h3>
     * <ul>
     * <li>Recursive aliases are not supported and will result in errors when
     * used</li>
     * <li>Aliases cannot override built-ins unless you have called
     * {@link #withAliasesOverridingBuiltIns()} on your builder</li>
     * </ul>
     * 
     * @return
     * @throws IOException
     */
    public ParserBuilder<C> withUserAliases(final String filename, final String prefix, final String... searchLocations)
            throws IOException {
        // Search locations in reverse order overwriting previously found values
        // each time. Thus the first location in the list has highest precedence
        Properties properties = new Properties();
        for (int i = searchLocations.length - 1; i >= 0; i--) {
            File f = new File(searchLocations[i]);
            f = new File(f, filename);
            if (f.exists() && f.isFile() && f.canRead()) {
                try (FileInputStream input = new FileInputStream(f)) {
                    properties.load(input);
                } finally {
                    // No clean up actions, try-with-resources does clean up for
                    // us
                }
            }
        }

        // Strip any irrelevant properties
        if (StringUtils.isNotEmpty(prefix)) {
            List<Object> keysToRemove = new ArrayList<Object>();
            for (Object key : properties.keySet()) {
                if (!key.toString().startsWith(prefix))
                    keysToRemove.add(key);
            }
            for (Object key : keysToRemove) {
                properties.remove(key);
            }
        }

        // Generate the aliases
        for (Object key : properties.keySet()) {
            String name = key.toString();
            if (prefix != null)
                name = name.substring(prefix.length());
            AliasBuilder<C> alias = this.withAlias(name);

            String value = properties.getProperty(key.toString());
            if (StringUtils.isEmpty(value))
                continue;

            // Process property value into arguments
            List<String> args = AliasArgumentsParser.parse(value);
            alias.withArguments(args.toArray(new String[args.size()]));
        }

        return this;
    }

    public ParserBuilder<C> withAliasesOverridingBuiltIns() {
        this.aliasesOverrideBuiltIns = true;
        return this;
    }
    
    public ParserBuilder<C> withAliasesChaining() {
        this.aliasesMayChain = true;
        return this;
    }

    public ParserBuilder<C> withCommandAbbreviation() {
        this.allowAbbreviatedCommands = true;
        return this;
    }

    public ParserBuilder<C> withOptionAbbreviation() {
        this.allowAbbreviatedOptions = true;
        return this;
    }

    public ParserBuilder<C> withTypeConverter(TypeConverter converter) {
        this.typeConverter = converter;
        return this;
    }

    public ParserBuilder<C> withDefaultTypeConverter() {
        this.typeConverter = new DefaultTypeConverter();
        return this;
    }

    /**
     * Configures the CLI to use the given option parser
     * <p>
     * Order of registration is important, if you have previously registered any
     * parsers then those will be used prior to the one given here
     * </p>
     * 
     * @param optionParsers
     *            Option parsers
     * @return Builder
     */
    public ParserBuilder<C> withOptionParser(OptionParser<C> optionParser) {
        if (optionParser != null) {
            this.optionParsers.add(optionParser);
        }
        return this;
    }

    /**
     * Configures the CLI to use the given option parsers
     * <p>
     * Order of registration is important, if you have previously registered any
     * parsers then those will be used prior to those given here
     * </p>
     * 
     * @param optionParsers
     *            Option parsers
     * @return Builder
     */
    @SuppressWarnings("unchecked")
    public ParserBuilder<C> withOptionParsers(OptionParser<C>... optionParsers) {
        if (optionParsers != null) {
            for (OptionParser<C> parser : optionParsers) {
                if (parser != null) {
                    this.optionParsers.add(parser);
                }
            }
        }
        return this;
    }

    /**
     * Configures the CLI to use only the default set of option parsers
     * <p>
     * This is the default behaviour so this need only be called if you have
     * previously configured some option parsers using the
     * {@link #withOptionParser(Class)} or {@link #withOptionParsers(Class...)}
     * methods and wish to reset the configuration to the default.
     * </p>
     * <p>
     * If you wish to instead add the default parsers in addition to your custom
     * parsers you should instead call {@link #withDefaultOptionParsers()}
     * </p>
     * 
     * @return Builder
     */
    public ParserBuilder<C> withOnlyDefaultOptionParsers() {
        this.optionParsers.clear();
        return this.withDefaultOptionParsers();
    }

    /**
     * Configures the CLI to use the default set of option parsers in addition
     * to any previously registered
     * <p>
     * Order of registration is important, if you have previously registered any
     * parsers then those will be used prior to those in the default set.
     * </p>
     * 
     * @return Builder
     */
    @SuppressWarnings("unchecked")
    public ParserBuilder<C> withDefaultOptionParsers() {
        return this.withOptionParsers(new StandardOptionParser<C>(), new LongGetOptParser<C>(),
                new ClassicGetOptParser<C>());
    }

    /**
     * Sets the arguments separator, this is a token used to indicate the point
     * at which no further options will be seen and all further tokens should be
     * treated as arguments.
     * <p>
     * This is useful for disambiguating where arguments may be misinterpreted
     * as options. The default value of this is the standard {@code --} used by
     * many command line tools.
     * </p>>
     * 
     * @param separator
     * @return
     */
    public ParserBuilder<C> withArgumentsSeparator(String separator) {
        this.argsSeparator = separator;
        return this;
    }

    @Override
    public ParserMetadata<C> build() {
        // Ensure we have some option parsers if none configured
        if (this.optionParsers.size() == 0) {
            this.withDefaultOptionParsers();
        }

        // Build aliases
        List<AliasMetadata> aliasData;
        if (aliases != null) {
            aliasData = new ArrayList<AliasMetadata>();
            for (AliasBuilder<C> aliasBuilder : aliases.values()) {
                aliasData.add(aliasBuilder.build());
            }
        } else {
            aliasData = Lists.newArrayList();
        }

        return new ParserMetadata<C>(commandFactory, optionParsers, typeConverter, allowAbbreviatedCommands,
                allowAbbreviatedOptions, aliasData, aliasesOverrideBuiltIns, aliasesMayChain, argsSeparator);
    }
}