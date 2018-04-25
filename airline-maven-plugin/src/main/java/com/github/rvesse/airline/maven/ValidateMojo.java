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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.rvesse.airline.maven.sources.PreparedSource;

//@formatter:off
@Mojo(name = "validate", 
    defaultPhase = LifecyclePhase.VERIFY, 
    requiresOnline = false, 
    requiresDependencyResolution = ResolutionScope.RUNTIME,
    threadSafe = true,
    requiresProject = true
)
//@formatter:on
public class ValidateMojo extends AbstractAirlineMojo {

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
        // Don't ignore bad sources, this will fail if any bad source is found
        // thus failing the mojo and the build
        List<PreparedSource> sources = prepareSources(false);
        if (sources.size() == 0) {
            log.info("No valid sources discovered so nothing to do");
            return;
        }
        
        for (PreparedSource source : sources) {
            log.info(String.format("Validated Airline metadata for class %s", source.getSourceClass()));
        }
    }

}
