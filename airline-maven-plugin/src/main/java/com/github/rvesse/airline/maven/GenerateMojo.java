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

import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.GlobalUsageGenerator;
import com.github.rvesse.airline.maven.formats.FormatMappingRegistry;
import com.github.rvesse.airline.maven.formats.FormatOptions;
import com.github.rvesse.airline.maven.formats.FormatProvider;
import com.github.rvesse.airline.maven.sources.PreparedSource;

/**
 * Generates Airline powered help
 *
 */
//@formatter:off
@Mojo(name = "generate", 
      defaultPhase = LifecyclePhase.PROCESS_CLASSES, 
      requiresOnline = false, 
      requiresDependencyResolution = ResolutionScope.RUNTIME,
      threadSafe = true,
      requiresProject = true
)
//@formatter:on
public class GenerateMojo extends AbstractAirlineMojo {

    /**
     * Formats to produce help in
     */
    @Parameter(defaultValue = "MAN")
    private List<String> formats;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (project == null)
            throw new MojoFailureException("Maven project was not injected into Mojo");
        if (pluginDescriptor == null)
            throw new MojoFailureException("Plugin Descriptor was not injected into Mojo");

        Log log = getLog();

        // Prepare the class realm
        prepareClassRealm();

        // Discover classes and get their meta-data as appropriate
        List<PreparedSource> sources = prepareSources();

        // Ensure directory is created
        ensureOutputDirectory();

        // Prepare default format options
        FormatOptions defaultOptions = this.defaultOptions == null ? new FormatOptions()
                : new FormatOptions(this.defaultOptions);
        log.debug(String.format("Default format options are %s", defaultOptions));

        // Prepare format mappings
        Map<String, FormatOptions> mappedOptions = prepareFormatMappings(defaultOptions);

        for (String format : formats) {
            FormatProvider provider = FormatMappingRegistry.find(format);
            if (provider == null) {
                throw new MojoFailureException(
                        String.format("Format %s does not have a format mapping defined", format));
            }
            FormatOptions options = mappedOptions.get(format);
            if (options == null)
                options = defaultOptions;
            if (options != defaultOptions)
                log.debug(String.format("Format %s format options are %s", format, options));

            CommandUsageGenerator commandGenerator = provider.getCommandGenerator(options);
            if (commandGenerator == null) {
                log.warn("Command help is not supported by format " + format);
            } else {
                log.info(String.format("Using command help generator %s for format %s", commandGenerator.getClass(),
                        format));

                // Generate command help
                for (PreparedSource source : sources) {
                    if (!source.isCommand())
                        continue;

                    FormatOptions sourceOptions = source.getFormatOptions(options);
                    if (sourceOptions != options)
                        log.debug(String.format("Source %s format options are %s", source.getSourceClass(), options));
                    outputCommandHelp(format, provider, sourceOptions, commandGenerator, source);
                }
            }

            GlobalUsageGenerator<Object> globalGenerator = provider.getGlobalGenerator(options);
            if (globalGenerator == null) {
                log.warn("CLI help is not supported by format " + format);
            } else {
                log.info(
                        String.format("Using CLI help generator %s for format %s", globalGenerator.getClass(), format));

                // Generate global help
                for (PreparedSource source : sources) {
                    if (!source.isGlobal())
                        continue;

                    FormatOptions sourceOptions = source.getFormatOptions(options);
                    if (sourceOptions != options)
                        log.debug(String.format("Source %s format options are %s", source.getSourceClass(), options));
                    outputGlobalHelp(format, provider, sourceOptions, globalGenerator, source);
                }
            }

        }
    }
}
