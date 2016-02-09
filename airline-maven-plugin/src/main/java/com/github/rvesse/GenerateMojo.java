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
package com.github.rvesse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.GlobalUsageGenerator;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.github.rvesse.formats.FormatMappingRegistry;
import com.github.rvesse.formats.FormatProvider;

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
public class GenerateMojo extends AbstractMojo {

    @Component
    private PluginDescriptor pluginDescriptor;

    @Component
    private MavenProject project;

    /**
     * Location of the output
     */
    @Parameter(defaultValue = "${project.build.directory}/help/", property = "outputDir", required = true)
    private File outputDirectory;

    /**
     * Formats to produce help in
     */
    @Parameter(defaultValue = "MAN")
    private List<String> formats;

    /**
     * Classes to produce help for, each must be annotated with either
     * {@link Cli} or {@link Command}
     */
    @Parameter(required = true)
    private List<String> classes;

    /**
     * Provides format mappings which allow creating custom format mappings
     */
    @Parameter
    private List<Mapping> formatMappings;

    /**
     * Provides formatting options
     */
    @Parameter
    private FormatOptions options;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (project == null)
            throw new MojoFailureException("Maven project was not injected into Mojo");
        if (pluginDescriptor == null)
            throw new MojoFailureException("Plugin Descriptor was not injected into Mojo");

        // Prepare the class realm
        try {
            updateClassRealm();
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoFailureException("Failed to resolve dependencies", e);
        }

        // Discover classes and get their meta-data as appropriate
        List<GlobalMetadata<Object>> globals = new ArrayList<>();
        List<CommandMetadata> commands = new ArrayList<>();

        Log log = getLog();
        for (String className : classes) {
            try {
                Class<?> cls = getClass().getClassLoader().loadClass(className);
                if (cls.getAnnotation(Command.class) != null) {
                    commands.add(MetadataLoader.loadCommand(cls));
                } else if (cls.getAnnotation(Cli.class) != null) {
                    globals.add(MetadataLoader.loadGlobal(cls));
                } else {
                    log.warn(String.format("Class %s is not annotated with @Cli or @Command", className));
                }
            } catch (ClassNotFoundException e) {
                log.warn(String.format("Failed to locate class %s", className));
            }
        }
        if (globals.size() + commands.size() == 0) {
            throw new MojoFailureException(
                    "Failed to locate any valid @Cli or @Command annotated classes to generate help for");
        }

        // Ensure directory is created
        if (!outputDirectory.exists() || !outputDirectory.isDirectory()) {
            if (!outputDirectory.mkdirs())
                throw new MojoFailureException(String.format("Failed to create required output directory %s",
                        outputDirectory.getAbsolutePath()));
        }

        // Prepare format mappings
        if (this.formatMappings != null) {
            for (Mapping mapping : this.formatMappings) {
                try {
                    @SuppressWarnings("unchecked")
                    Class<? extends FormatProvider> cls = (Class<? extends FormatProvider>) getClass().getClassLoader()
                            .loadClass(mapping.provider);
                    Constructor<? extends FormatProvider> constructor = cls.getDeclaredConstructor(Properties.class);
                    FormatProvider provider;
                    if (constructor != null) {
                        provider = constructor.newInstance(mapping.configuration);
                    } else {
                        provider = cls.newInstance();
                    }
                    FormatMappingRegistry.addMapping(provider);
                } catch (Throwable e) {
                    throw new MojoFailureException(
                            String.format("Format mapping for format %s specifies provider %s which is not valid",
                                    mapping.format, mapping.provider),
                            e);
                }
            }
        }

        // Prepare format options
        if (this.options == null) {
            this.options = new FormatOptions();
        }

        for (String format : formats) {
            FormatProvider provider = FormatMappingRegistry.find(format);
            if (provider == null) {
                throw new MojoFailureException(
                        String.format("Format %s does not have a format mapping defined", format));
            }

            if (commands.size() > 0) {
                CommandUsageGenerator generator = provider.getCommandGenerator(this.options);
                if (generator == null) {
                    log.warn("Command help is not supported by format " + format);
                } else {
                    // Generate command help
                    for (CommandMetadata command : commands) {
                        log.debug(String.format("Generating command help for %s in format %s", command.getType(),
                                format));

                        try (OutputStream output = new FileOutputStream(new File(this.outputDirectory,
                                command.getName() + provider.getExtension(this.options)))) {
                            generator.usage(null, null, command.getName(), command,
                                    MetadataLoader.loadParser(command.getClass()), output);
                            output.flush();
                            output.close();
                        } catch (IOException e) {
                            throw new MojoFailureException(String.format("Failed to generate help for %s in format %s",
                                    command.getClass(), format), e);
                        }
                    }
                }
            }

            if (globals.size() > 0) {
                GlobalUsageGenerator<Object> generator = provider.getGlobalGenerator(this.options);
                if (generator == null) {
                    log.warn("CLI help is not supported by format " + format);
                } else {
                    // Generate global help
                    for (GlobalMetadata<Object> global : globals) {
                        try (OutputStream output = new FileOutputStream(new File(this.outputDirectory,
                                global.getName() + provider.getExtension(this.options)))) {
                            generator.usage(global, output);
                            output.flush();
                            output.close();
                        } catch (IOException e) {
                            throw new MojoFailureException(
                                    String.format("Failed to generate CLI help in format %s", format), e);
                        }
                    }
                }
            }
        }
    }

    private void updateClassRealm() throws DependencyResolutionRequiredException, MojoFailureException {
        ClassRealm realm = pluginDescriptor.getClassRealm();

        Set<String> processed = new HashSet<>();
        for (String element : project.getCompileClasspathElements()) {

            File elementFile = new File(element);
            try {
                realm.addURL(elementFile.toURI().toURL());
            } catch (MalformedURLException e) {
                getLog().warn(String.format("Failed to resolve classpath element %s", element));
            }

            processed.add(element);
        }
        for (String element : project.getRuntimeClasspathElements()) {
            if (processed.contains(element))
                continue;

            File elementFile = new File(element);
            try {
                realm.addURL(elementFile.toURI().toURL());
            } catch (MalformedURLException e) {
                getLog().warn(String.format("Failed to resolve classpath element %s", element));
            }

            processed.add(element);
        }
    }
}
