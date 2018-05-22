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

package com.github.rvesse.airline.builder;

import java.util.ArrayList;
import java.util.List;

import com.github.rvesse.airline.parser.aliases.UserAliasesSource;
import com.github.rvesse.airline.parser.aliases.locators.UserAliasSourceLocator;

/**
 * User alias source builder
 * 
 * @author rvesse
 *
 * @param <C>
 *            Command type
 */
public class UserAliasSourceBuilder<C> extends AbstractBuilder<UserAliasesSource<C>> {

    public static final String DEFAULT_EXTENSION = ".config";

    private List<String> searchLocations = new ArrayList<>();
    private String filename, prefix;
    private List<UserAliasSourceLocator> locators = new ArrayList<>();

    public UserAliasSourceBuilder<C> withProgramName(String programName) {
        this.filename = programName + DEFAULT_EXTENSION;
        return this;
    }

    public UserAliasSourceBuilder<C> withFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public UserAliasSourceBuilder<C> withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public UserAliasSourceBuilder<C> withDefaultLocators() {
        for (UserAliasSourceLocator locator : UserAliasSourceLocator.DEFAULTS) {
            this.locators.add(locator);
        }
        return this;
    }

    public UserAliasSourceBuilder<C> withLocator(UserAliasSourceLocator locator) {
        this.locators.add(locator);
        return this;
    }

    public UserAliasSourceBuilder<C> withLocators(List<UserAliasSourceLocator> locators) {
        this.locators.addAll(locators);
        return this;
    }

    public UserAliasSourceBuilder<C> withLocators(UserAliasSourceLocator... locators) {
        for (UserAliasSourceLocator locator : locators) {
            this.locators.add(locator);
        }
        return this;
    }

    public UserAliasSourceBuilder<C> withDefaultSearchLocation(String programName) {
        this.searchLocations.add("./," + programName + "/");
        return this;
    }

    public UserAliasSourceBuilder<C> withSearchLocation(String location) {
        this.searchLocations.add(location);
        return this;
    }

    public UserAliasSourceBuilder<C> withSearchLocations(String... locations) {
        for (String loc : locations) {
            this.searchLocations.add(loc);
        }
        return this;
    }
    
    public boolean isBuildable() {
        return this.filename != null && !this.searchLocations.isEmpty();
    }

    @Override
    public UserAliasesSource<C> build() {
        if (this.filename == null) {
            throw new IllegalStateException("Must specify a configuration file to search for");
        }
        if (this.searchLocations.isEmpty()) {
            throw new IllegalStateException("Must specify at least one search location");
        }
        return new UserAliasesSource<>(filename, prefix, locators.size() > 0 ? locators : null, searchLocations);
    }

}
