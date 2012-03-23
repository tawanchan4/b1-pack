/*
 * Copyright 2012 b1.org
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

package org.b1.pack.api.builder;

import java.util.List;
import java.util.ServiceLoader;

public abstract class PackBuilder {

    public abstract List<BuilderVolume> build(BuilderProvider provider, BuilderCommand command);

    protected abstract boolean isFormatSupported(String format);

    public static PackBuilder getInstance(String format) {
        for (PackBuilder builder : ServiceLoader.load(PackBuilder.class)) {
            if (builder.isFormatSupported(format)) return builder;
        }
        throw new IllegalArgumentException("Unsupported format: " + format);
    }
}