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
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;
import java.util.Optional;

public final class ExpressionFunctionInfo implements HasName<FunctionExpressionName>,
        Comparable<ExpressionFunctionInfo>,
        HateosResource<FunctionExpressionName> {

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

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final ExpressionFunctionInfo other) {
        return this.name.compareTo(other.name);
    }

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
        return this.url + " " + this.name;
    }

    // Json.............................................................................................................

    private final static String URL_PROPERTY_STRING = "url";

    private final static String NAME_PROPERTY_STRING = "name";

    // @VisibleForTesting
    final static JsonPropertyName URL_PROPERTY = JsonPropertyName.with(URL_PROPERTY_STRING);

    final static JsonPropertyName NAME_PROPERTY = JsonPropertyName.with(NAME_PROPERTY_STRING);

    static ExpressionFunctionInfo unmarshall(final JsonNode node,
                                             final JsonNodeUnmarshallContext context) {
        AbsoluteUrl url = null;
        FunctionExpressionName functionExpressionName = null;

        for (final JsonNode child : node.objectOrFail().children()) {
            final JsonPropertyName name = child.name();

            switch (name.value()) {
                case URL_PROPERTY_STRING:
                    url = context.unmarshall(
                            child,
                            AbsoluteUrl.class
                    );
                    break;
                case NAME_PROPERTY_STRING:
                    functionExpressionName = context.unmarshall(
                            child,
                            FunctionExpressionName.class
                    );
                    break;
                default:
                    JsonNodeUnmarshallContext.unknownPropertyPresent(name, node);
                    break;
            }
        }

        return with(
                url,
                functionExpressionName
        );
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.object()
                .set(URL_PROPERTY, context.marshall(this.url))
                .set(NAME_PROPERTY, context.marshall(this.name));
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

    // HateosResource...................................................................................................

    @Override
    public String hateosLinkId() {
        return this.name.value();
    }

    @Override
    public Optional<FunctionExpressionName> id() {
        return Optional.of(this.name);
    }
}
