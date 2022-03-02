/**
 * Copyright 2021 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.v3.jaxrs2.cdi2;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

public class DiscoveryTestExtension implements Extension {

    private final Set<Class<?>> providers = new HashSet<>();
    private final Set<Class<?>> resources = new HashSet<>();

    public Set<Class<?>> getProviders() {
        return providers;
    }

    public Set<Class<?>> getResources() {
        return resources;
    }

    public <T> void observeResources(@WithAnnotations({ Path.class }) @Observes ProcessAnnotatedType<T> event) {
        AnnotatedType<T> annotatedType = event.getAnnotatedType();

        if (!annotatedType.getJavaClass().isInterface()) {
            this.resources.add(annotatedType.getJavaClass());
        }
    }

    public <T> void observeProviders(@WithAnnotations({ Provider.class }) @Observes ProcessAnnotatedType<T> event) {
        AnnotatedType<T> annotatedType = event.getAnnotatedType();

        if (!annotatedType.getJavaClass().isInterface()) {
            this.providers.add(annotatedType.getJavaClass());
        }
    }

}
