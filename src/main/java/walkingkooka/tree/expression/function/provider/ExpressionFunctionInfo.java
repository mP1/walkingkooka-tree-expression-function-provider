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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.http.server.hateos.HateosResource;
import walkingkooka.plugin.PluginInfo;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

public final class ExpressionFunctionInfo implements PluginInfoLike<ExpressionFunctionInfo, ExpressionFunctionName>,
    HateosResource<ExpressionFunctionName> {

    public static ExpressionFunctionInfo parse(final String text) {
        return new ExpressionFunctionInfo(
            PluginInfo.parse(
                text,
                ExpressionFunctionName::with
            )
        );
    }

    public static ExpressionFunctionInfo with(final AbsoluteUrl url,
                                              final ExpressionFunctionName name) {
        return new ExpressionFunctionInfo(
            PluginInfo.with(
                url,
                name
            )
        );
    }

    private ExpressionFunctionInfo(final PluginInfo<ExpressionFunctionName> pluginInfo) {
        this.pluginInfo = pluginInfo;
    }

    // HasAbsoluteUrl...................................................................................................

    @Override
    public AbsoluteUrl url() {
        return this.pluginInfo.url();
    }

    // HasName..........................................................................................................

    @Override
    public ExpressionFunctionName name() {
        return this.pluginInfo.name();
    }

    @Override
    public ExpressionFunctionInfo setName(final ExpressionFunctionName name) {
        return this.name().equals(name) ?
            this :
            new ExpressionFunctionInfo(
                this.pluginInfo.setName(name)
            );
    }

    private final PluginInfo<ExpressionFunctionName> pluginInfo;

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final ExpressionFunctionInfo other) {
        return this.pluginInfo.compareTo(other.pluginInfo);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.pluginInfo.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof ExpressionFunctionInfo &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final ExpressionFunctionInfo other) {
        return this.pluginInfo.equals(other.pluginInfo);
    }

    @Override
    public String toString() {
        return this.pluginInfo.toString();
    }

    // Json.............................................................................................................

    static void register() {
        // helps force registry of json marshaller
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.toString());
    }

    static ExpressionFunctionInfo unmarshall(final JsonNode node,
                                             final JsonNodeUnmarshallContext context) {
        return ExpressionFunctionInfo.parse(
            node.stringOrFail()
        );
    }

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(ExpressionFunctionInfo.class),
            ExpressionFunctionInfo::unmarshall,
            ExpressionFunctionInfo::marshall,
            ExpressionFunctionInfo.class
        );
        ExpressionFunctionName.with("hello"); // trigger static init and json marshall/unmarshall registry
    }
}
