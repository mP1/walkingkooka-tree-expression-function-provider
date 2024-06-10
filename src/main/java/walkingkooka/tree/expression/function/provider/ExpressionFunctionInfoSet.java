/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.tree.expression.function.provider;

import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.http.server.hateos.HateosResource;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * A read only {@link Set} of {@link ExpressionFunctionInfo} sorted by {@link FunctionExpressionName}.
 */
public final class ExpressionFunctionInfoSet extends AbstractSet<ExpressionFunctionInfo>
        implements PluginInfoSetLike<ExpressionFunctionInfo, FunctionExpressionName> {

    /**
     * Parses the given text into a {@link ExpressionFunctionInfo}
     */
    public static ExpressionFunctionInfoSet parse(final String text) {
        return PluginInfoSetLike.parse(
                text,
                ExpressionFunctionInfo::parse,
                ExpressionFunctionInfoSet::with
        );
    }

    /**
     * Factory that creates a {@link ExpressionFunctionInfoSet} with the provided {@link ExpressionFunctionInfo}.
     */
    public static ExpressionFunctionInfoSet with(final Set<ExpressionFunctionInfo> functionInfos) {
        Objects.requireNonNull(functionInfos, "functionInfos");

        final Set<ExpressionFunctionInfo> copy = Sets.sorted(HateosResource.comparator());
        copy.addAll(functionInfos);
        return new ExpressionFunctionInfoSet(copy);
    }

    private ExpressionFunctionInfoSet(final Set<ExpressionFunctionInfo> functionInfos) {
        this.functionInfos = functionInfos;
    }

    // AbstractSet......................................................................................................

    @Override
    public Iterator<ExpressionFunctionInfo> iterator() {
        return Iterators.readOnly(
                this.functionInfos.iterator()
        );
    }

    @Override
    public int size() {
        return this.functionInfos.size();
    }

    private final Set<ExpressionFunctionInfo> functionInfos;

    // json.............................................................................................................

    static {
        ExpressionFunctionInfo.register(); // helps force registry of json marshaller

        JsonNodeContext.register(
                JsonNodeContext.computeTypeName(ExpressionFunctionInfoSet.class),
                ExpressionFunctionInfoSet::unmarshall,
                ExpressionFunctionInfoSet::marshall,
                ExpressionFunctionInfoSet.class
        );
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return context.marshallCollection(this);
    }

    // @VisibleForTesting
    static ExpressionFunctionInfoSet unmarshall(final JsonNode node,
                                                final JsonNodeUnmarshallContext context) {
        return ExpressionFunctionInfoSet.with(
                context.unmarshallSet(
                        node,
                        ExpressionFunctionInfo.class
                )
        );
    }

    // toString.........................................................................................................

    @Override
    public String toString() {
        return PluginInfoSetLike.toString(this);
    }
}
