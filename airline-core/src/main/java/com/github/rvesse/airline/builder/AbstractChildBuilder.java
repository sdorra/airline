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

/**
 * A builder that is a child of another builder
 * 
 * @author rvesse
 *
 * @param <T>
 *            Child type being built
 * @param <TParent>
 *            Parent type being built
 * @param <TBuilder>
 *            Parent builder type
 */
public abstract class AbstractChildBuilder<T, TParent, TBuilder extends AbstractBuilder<TParent>>
        extends AbstractBuilder<T> {

    private final TBuilder parent;

    /**
     * Creates a child builder
     * 
     * @param parentBuilder
     *            Parent builder
     */
    protected AbstractChildBuilder(TBuilder parentBuilder) {
        if (parentBuilder == null)
            throw new NullPointerException("parentBuilder cannot be null");
        this.parent = parentBuilder;
    }

    /**
     * Gets the parent builder
     * 
     * @return Parent builder
     */
    public TBuilder parent() {
        return this.parent;
    }

}
