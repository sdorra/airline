/**
 * Copyright (C) 2010-16 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.maven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.rvesse.airline.help.CommandGroupUsageGenerator;
import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.GlobalUsageGenerator;
import com.github.rvesse.airline.maven.formats.FormatMappingRegistry;
import com.github.rvesse.airline.maven.formats.FormatOptions;
import com.github.rvesse.airline.maven.formats.FormatProvider;
import com.github.rvesse.airline.maven.sources.PreparedSource;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.ParserMetadata;

public abstract class AbstractAirlineOutputMojo extends AbstractAirlineMojo {

    /**
     * Formats to produce help in
     */
    @Parameter(defaultValue = "MAN")
    protected List<String> formats;

    @Parameter(defaultValue = "true")
    protected boolean failOnUnknownFormat = true;

    @Parameter(defaultValue = "false")
    protected boolean failOnUnsupportedOutputMode = false;

    @Parameter(defaultValue = "true")
    protected boolean skipBadSources = true;
    /**
     * Location of the output
     */
    @Parameter(defaultValue = "${project.build.directory}/help/", property = "outputDir", required = true)
    protected File outputDirectory;

    /**
     * Provides format mappings which allow creating custom format mappings or
     * customising options on a per-format basis
     */
    @Parameter
    protected List<Mapping> formatMappings;

    /**
     * Provides formatting options
     */
    @Parameter
    protected RawFormatOptions defaultOptions;

    public AbstractAirlineOutputMojo() {
        super();
    }

    protected void outputGroupCommandsHelp(String format, FormatProvider provider, FormatOptions sourceOptions,
            CommandUsageGenerator commandGenerator, PreparedSource source, Collection<CommandMetadata> commands,
            ParserMetadata<Object> parser, String programName, String... groupNames) throws MojoFailureException {
        for (CommandMetadata command : commands) {
            outputCommandHelp(format, provider, sourceOptions, commandGenerator, source, command, parser, programName,
                    groupNames);
        }
    }

    protected void outputGroupCommandsHelp(String format, FormatProvider provider, FormatOptions sourceOptions,
            CommandUsageGenerator commandGenerator, PreparedSource source, CommandGroupMetadata group,
            ParserMetadata<Object> parser, String programName, String... groupNames) throws MojoFailureException {

        // Add our group name to the group names path
        groupNames = concatGroupNames(groupNames, group.getName());

        // Output help for commands in this group
        outputGroupCommandsHelp(format, provider, sourceOptions, commandGenerator, source, group.getCommands(), parser,
                programName, groupNames);

        // Recurse to output help for commands in sub-groups
        for (CommandGroupMetadata subGroup : group.getSubGroups()) {
            outputGroupCommandsHelp(format, provider, sourceOptions, commandGenerator, source, subGroup, parser,
                    programName, groupNames);
        }
    }

    protected void outputGroupsHelp(String format, FormatProvider provider, FormatOptions sourceOptions,
            CommandGroupUsageGenerator<Object> groupGenerator, PreparedSource source, CommandGroupMetadata[] groups,
            ParserMetadata<Object> parser, String programName) throws MojoFailureException {

        // Output help for this group
        outputGroupHelp(format, provider, sourceOptions, groupGenerator, source, groups);

        // Recurse to output help for sub-groups
        CommandGroupMetadata group = groups[groups.length - 1];
        for (CommandGroupMetadata subGroup : group.getSubGroups()) {
            CommandGroupMetadata[] subGroups = Arrays.copyOf(groups, groups.length + 1);
            subGroups[subGroups.length - 1] = subGroup;

            outputGroupsHelp(format, provider, sourceOptions, groupGenerator, source, subGroups, parser, programName);
        }
    }

    private String[] concatGroupNames(String[] names, String finalName) {
        String[] finalNames;
        if (names != null) {
            finalNames = Arrays.copyOf(names, names.length + 1);
        } else {
            finalNames = new String[1];
        }
        finalNames[finalNames.length - 1] = finalName;
        return finalNames;
    }

    protected CommandUsageGenerator prepareCommandGenerator(FormatProvider provider, PreparedSource source,
            FormatOptions sourceOptions) {
        Log log = getLog();
        CommandUsageGenerator sourceCommandGenerator;
        log.debug(String.format("Source %s format options are %s", source.getSourceClass(), sourceOptions));
        sourceCommandGenerator = provider.getCommandGenerator(this.outputDirectory, sourceOptions);
        log.info(String.format("Using command help generator %s for source %s", sourceCommandGenerator.getClass(),
                source.getSourceClass()));
        return sourceCommandGenerator;
    }

    protected CommandGroupUsageGenerator<Object> prepareCommandGroupUsageGenerator(FormatProvider provider,
            PreparedSource source, FormatOptions sourceOptions) {
        Log log = getLog();
        CommandGroupUsageGenerator<Object> sourceGroupGenerator;
        log.debug(String.format("Source %s format options are %s", source.getSourceClass(), sourceOptions));
        sourceGroupGenerator = provider.getGroupGenerator(this.outputDirectory, sourceOptions);
        log.info(String.format("Using command group help generator %s for source %s", sourceGroupGenerator.getClass(),
                source.getSourceClass()));
        return sourceGroupGenerator;
    }

    protected GlobalUsageGenerator<Object> prepareGlobalUsageGenerator(FormatProvider provider, PreparedSource source,
            FormatOptions sourceOptions) {
        Log log = getLog();
        GlobalUsageGenerator<Object> sourceGlobalGenerator;
        log.debug(String.format("Source %s format options are %s", source.getSourceClass(), sourceOptions));
        sourceGlobalGenerator = provider.getGlobalGenerator(this.outputDirectory, sourceOptions);
        log.info(String.format("Using global generator %s for source %s", sourceGlobalGenerator.getClass(),
                source.getSourceClass()));
        return sourceGlobalGenerator;
    }

    /**
     * Ensures the necessary output directory exists or can be created failing
     * the build if not
     * 
     * @throws MojoFailureException
     *             Thrown if the output directory does not exist or cannot be
     *             created
     */
    protected void ensureOutputDirectory() throws MojoFailureException {
        if (!outputDirectory.exists() || !outputDirectory.isDirectory()) {
            if (!outputDirectory.mkdirs())
                throw new MojoFailureException(String.format("Failed to create required output directory %s",
                        outputDirectory.getAbsolutePath()));
        }
    }

    protected void outputCommandHelp(String format, FormatProvider provider, FormatOptions options,
            CommandUsageGenerator commandGenerator, PreparedSource source, String programName, String[] groupNames)
            throws MojoFailureException {
        Log log = getLog();
        log.debug(String.format("Generating command help for %s in format %s", source.getSourceClass(), format));

        outputCommandHelp(format, provider, options, commandGenerator, source, source.getCommmand(),
                source.getParserConfiguration(), programName, groupNames);
    }

    protected void outputCommandHelp(String format, FormatProvider provider, FormatOptions options,
            CommandUsageGenerator commandGenerator, PreparedSource source, CommandMetadata command,
            ParserMetadata<Object> parser, String programName, String[] groupNames) throws MojoFailureException {
        File commandHelpFile = new File(this.outputDirectory, command.getName() + provider.getExtension(options));
        outputCommandHelp(format, commandGenerator, source, commandHelpFile, command, parser, programName, groupNames);
    }

    protected void outputCommandHelp(String format, CommandUsageGenerator commandGenerator, PreparedSource source,
            File commandHelpFile, CommandMetadata command, ParserMetadata<Object> parser, String programName,
            String[] groupNames) throws MojoFailureException {
        Log log = getLog();
        try (OutputStream output = new FileOutputStream(commandHelpFile)) {
            commandGenerator.usage(programName, groupNames, command.getName(), command, parser, output);
            output.flush();
            output.close();

            if (!commandHelpFile.exists())
                throw new MojoFailureException(String.format("Failed to create help file %s", commandHelpFile));

            log.info(String.format("Generated command help for %s in format %s to file %s", source.getSourceClass(),
                    format, commandHelpFile));
        } catch (IOException e) {
            throw new MojoFailureException(
                    String.format("Failed to generate help for %s in format %s", source.getSourceClass(), format), e);
        }
    }

    protected void outputCommandHelp(String format, FormatProvider provider, FormatOptions options,
            CommandUsageGenerator commandGenerator, PreparedSource source) throws MojoFailureException {
        outputCommandHelp(format, provider, options, commandGenerator, source, null, null);
    }

    protected void outputGroupHelp(String format, FormatProvider provider, FormatOptions options,
            CommandGroupUsageGenerator<Object> groupGenerator, PreparedSource source, CommandGroupMetadata[] groups)
            throws MojoFailureException {
        Log log = getLog();
        log.debug(String.format("Generating Group help for %s in format %s", source.getSourceClass(), format));

        GlobalMetadata<Object> global = source.getGlobal();
        File groupHelpFile = new File(this.outputDirectory, global.getName() + provider.getExtension(options));
        try (OutputStream output = new FileOutputStream(groupHelpFile)) {
            groupGenerator.usage(global, groups, output);
            output.flush();
            output.close();

            if (!groupHelpFile.exists())
                throw new MojoFailureException(String.format("Failed to create help file %s", groupHelpFile));

            log.info(String.format("Generated Group help for %s in format %s to file %s", source.getSourceClass(),
                    format, groupHelpFile));
        } catch (IOException e) {
            throw new MojoFailureException(
                    String.format("Failed to generate Group help for %s in format %s", source.getSourceClass(), format),
                    e);
        }
    }

    protected void outputGlobalHelp(String format, FormatProvider provider, FormatOptions options,
            GlobalUsageGenerator<Object> globalGenerator, PreparedSource source) throws MojoFailureException {
        Log log = getLog();
        log.debug(String.format("Generating CLI help for %s in format %s", source.getSourceClass(), format));

        GlobalMetadata<Object> global = source.getGlobal();
        File cliHelpFile = new File(this.outputDirectory, global.getName() + provider.getExtension(options));
        try (OutputStream output = new FileOutputStream(cliHelpFile)) {
            globalGenerator.usage(global, output);
            output.flush();
            output.close();

            if (!cliHelpFile.exists())
                throw new MojoFailureException(String.format("Failed to create help file %s", cliHelpFile));

            log.info(String.format("Generated CLI help for %s in format %s to file %s", source.getSourceClass(), format,
                    cliHelpFile));
        } catch (IOException e) {
            throw new MojoFailureException(
                    String.format("Failed to generate CLI help for %s in format %s", source.getSourceClass(), format),
                    e);
        }
    }

    protected Map<String, FormatOptions> prepareFormatMappings(FormatOptions defaultOptions)
            throws MojoFailureException {
        Map<String, FormatOptions> mappedOptions = new HashMap<>();

        // Set defaults
        for (String format : FormatMappingRegistry.availableFormatNames()) {
            mappedOptions.put(format, defaultOptions);
        }

        // Discover additional mappings
        if (this.formatMappings != null) {
            for (Mapping mapping : this.formatMappings) {
                try {
                    FormatProvider provider;
                    // If default specified or not specified use the default from the ServiceLoader built registry
                    if (Mapping.DEFAULT.equals(mapping.provider) || StringUtils.isEmpty(mapping.provider)) {
                        provider = FormatMappingRegistry.find(mapping.format);
                        if (provider == null)
                            throw new MojoFailureException(String.format(
                                    "Format mapping for format %s specifies to use the default provider but there is no ServiceLoader discovered default provider",
                                    mapping.format));
                    } else {
                        @SuppressWarnings("unchecked")
                        Class<? extends FormatProvider> cls = (Class<? extends FormatProvider>) getClass()
                                .getClassLoader().loadClass(mapping.provider);
                        provider = cls.newInstance();
                        FormatMappingRegistry.add(mapping.format, provider);
                    }

                    // If specific options are defined use them (with
                    // inheritance from the defaults) otherwise just use the
                    // defaults
                    mappedOptions.put(mapping.format,
                            mapping.options != null ? new FormatOptions(mapping.options, defaultOptions)
                                    : defaultOptions);
                } catch (Throwable e) {
                    throw new MojoFailureException(
                            String.format("Format mapping for format %s specifies provider %s which is not valid",
                                    mapping.format, mapping.provider),
                            e);
                }
            }
        }
        return mappedOptions;
    }

}