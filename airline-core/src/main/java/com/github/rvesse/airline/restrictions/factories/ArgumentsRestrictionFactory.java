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
package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.List;

import com.github.rvesse.airline.restrictions.ArgumentsRestriction;

/**
 * Interface for arguments restriction factories
 */
public interface ArgumentsRestrictionFactory {

    /**
     * Tries to create an arguments restriction from the given annotation
     * 
     * @param annotation
     *            Annotation
     * @return Arguments restriction or {@code null} if this factory cannot
     *         create a restriction from the given annotation
     */
    public abstract ArgumentsRestriction createArgumentsRestriction(Annotation annotation);
    
    /**
     * Gets a list of annotations that this factory can convert into arguments
     * restrictions
     * 
     * @return List of supported annotations
     */
    public List<Class<? extends Annotation>> supportedArgumentsAnnotations();

}
