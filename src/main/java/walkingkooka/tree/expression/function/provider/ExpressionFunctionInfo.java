/*
 * Copyright 2024 Miroslav Pokorny (github.com/mP1)
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

import walkingkooka.Cast;
import walkingkooka.naming.HasName;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.http.server.hateos.HateosResource;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;
import java.util.Optional;

public final class ExpressionFunctionInfo implements PluginInfoLike<ExpressionFunctionInfo, FunctionExpressionName>,
        HateosResource<FunctionExpressionName> {

    public static ExpressionFunctionInfo parse(final String text) {
        return PluginInfoLike.parse(
                text,
                FunctionExpressionName::with,
                ExpressionFunctionInfo::with
        );
    }

    public static ExpressionFunctionInfo with(final AbsoluteUrl url,
                                              final FunctionExpressionName name) {
        return new ExpressionFunctionInfo(
                Objects.requireNonNull(url, "url"),
                Objects.requireNonNull(name, "name")
        );
    }

    private ExpressionFunctionInfo(final AbsoluteUrl url,
                                   final FunctionExpressionName name) {
        this.url = url;
        this.name = name;
    }

    public AbsoluteUrl url() {
        return this.url;
    }

    private final AbsoluteUrl url;

    // HasName..........................................................................................................

    @Override
    public FunctionExpressionName name() {
        return this.name;
    }

    private final FunctionExpressionName name;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
                this.url,
                this.name
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ExpressionFunctionInfo &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final ExpressionFunctionInfo other) {
        return this.url.equals(other.url) &&
                this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return PluginInfoLike.toString(this);
    }

    // Json.............................................................................................................

    static ExpressionFunctionInfo unmarshall(final JsonNode node,
                                             final JsonNodeUnmarshallContext context) {
        return PluginInfoLike.unmarshall(
                node,
                context,
                FunctionExpressionName.class,
                ExpressionFunctionInfo::with
        );
    }

    static {
        JsonNodeContext.register(
                JsonNodeContext.computeTypeName(ExpressionFunctionInfo.class),
                ExpressionFunctionInfo::unmarshall,
                ExpressionFunctionInfo::marshall,
                ExpressionFunctionInfo.class
        );
        FunctionExpressionName.with("hello"); // trigger static init and json marshall/unmarshall registry
    }
}
