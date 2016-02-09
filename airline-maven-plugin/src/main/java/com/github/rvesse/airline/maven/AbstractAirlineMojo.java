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
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;

import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.GlobalUsageGenerator;
import com.github.rvesse.airline.maven.formats.FormatMappingRegistry;
import com.github.rvesse.airline.maven.formats.FormatOptions;
import com.github.rvesse.airline.maven.formats.FormatProvider;
import com.github.rvesse.airline.maven.sources.PreparedSource;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;

public abstract class AbstractAirlineMojo extends AbstractMojo {

    @Component
    protected PluginDescriptor pluginDescriptor;

    @Component
    protected MavenProject project;
    /**
     * Location of the output
     */
    @Parameter(defaultValue = "${project.build.directory}/help/", property = "outputDir", required = true)
    protected File outputDirectory;

    @Parameter(required = true)
    protected List<Source> sources;

    /**
     * Provides format mappings which allow creating custom format mappings
     */
    @Parameter
    protected List<Mapping> formatMappings;

    /**
     * Provides formatting options
     */
    @Parameter
    protected RawFormatOptions defaultOptions;

    public AbstractAirlineMojo() {
        super();
    }

    /**
     * Ensures the necessary output directory exists or can be created failing
     * the build if not
     * 
     * @throws MojoFailureException
     */
    protected void ensureOutputDirectory() throws MojoFailureException {
        if (!outputDirectory.exists() || !outputDirectory.isDirectory()) {
            if (!outputDirectory.mkdirs())
                throw new MojoFailureException(String.format("Failed to create required output directory %s",
                        outputDirectory.getAbsolutePath()));
        }
    }

    /**
     * Prepares the sources for which help will be generated
     * 
     * @return Prepared sources
     * @throws MojoFailureException
     */
    protected List<PreparedSource> prepareSources() throws MojoFailureException {
        List<PreparedSource> prepared = new ArrayList<>();
        Log log = getLog();
        for (Source source : this.sources) {
            prepared.addAll(source.prepare(log));
        }
        if (prepared.size() == 0) {
            throw new MojoFailureException(
                    "Failed to locate any valid @Cli or @Command annotated classes to generate help for");
        }
        return prepared;
    }

    /**
     * Prepares the class realm failing the build if unable to do so
     * 
     * @throws MojoFailureException
     */
    protected void prepareClassRealm() throws MojoFailureException {
        try {
            ClassRealm realm = pluginDescriptor.getClassRealm();

            Set<String> processed = new HashSet<>();
            List<String> compileClasspathElements = project.getCompileClasspathElements();
            processClasspathElements(realm, processed, compileClasspathElements);
            processClasspathElements(realm, processed, project.getRuntimeClasspathElements());
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoFailureException("Failed to resolve dependencies", e);
        }
    }

    private void processClasspathElements(ClassRealm realm, Set<String> processed,
            List<String> compileClasspathElements) {
        for (String element : compileClasspathElements) {

            File elementFile = new File(element);
            try {
                realm.addURL(elementFile.toURI().toURL());
            } catch (MalformedURLException e) {
                getLog().warn(String.format("Failed to resolve classpath element %s", element));
            }

            processed.add(element);
        }
    }

    protected void outputCommandHelp(String format, FormatProvider provider, FormatOptions options,
            CommandUsageGenerator commandGenerator, PreparedSource source) throws MojoFailureException {
        Log log = getLog();
        log.debug(String.format("Generating command help for %s in format %s", source.getSourceClass(), format));

        CommandMetadata command = source.getCommmand();
        File commandHelpFile = new File(this.outputDirectory, command.getName() + provider.getExtension(options));
        try (OutputStream output = new FileOutputStream(commandHelpFile)) {
            commandGenerator.usage(null, null, command.getName(), command, source.getParserConfiguration(), output);
            output.flush();
            output.close();
            
            log.info(String.format("Generated command help for %s in format %s to file %s", source.getSourceClass(),
                    format, commandHelpFile));
        } catch (IOException e) {
            throw new MojoFailureException(
                    String.format("Failed to generate help for %s in format %s", command.getClass(), format), e);
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
            
            log.info(String.format("Generated CLI help for %s in format %s to file %s", source.getSourceClass(),
                    format, cliHelpFile));
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
                    if (Mapping.DEFAULT.equals(mapping.provider)) {
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
                    mappedOptions.put(mapping.format, mapping.options != null
                            ? new FormatOptions(mapping.options, defaultOptions) : defaultOptions);
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