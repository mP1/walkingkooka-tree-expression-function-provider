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
import walkingkooka.plugin.PluginAliases;
import walkingkooka.plugin.PluginAliasesLike;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Optional;
import java.util.Set;

public final class ExpressionFunctionAliases implements PluginAliasesLike<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector> {

    public static ExpressionFunctionAliases parse(final String text) {
        return new ExpressionFunctionAliases(
                PluginAliases.parse(
                        text,
                        ExpressionFunctionName.PARSER, // nameParser
                        ExpressionFunctionInfo::with, // infoFactory
                        ExpressionFunctionInfoSet::with, // infoSet factory
                        ExpressionFunctionSelector::parse // selector parser
                )
        );
    }

    private ExpressionFunctionAliases(final PluginAliases<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector> pluginAliases) {
        this.pluginAliases = pluginAliases;
    }

    @Override
    public Optional<ExpressionFunctionSelector> alias(final ExpressionFunctionName name) {
        return this.pluginAliases.alias(name);
    }

    @Override
    public Set<ExpressionFunctionName> aliases() {
        return this.pluginAliases.aliases();
    }

    @Override
    public Optional<ExpressionFunctionName> name(final ExpressionFunctionName name) {
        return this.pluginAliases.name(name);
    }

    @Override
    public Set<ExpressionFunctionName> names() {
        return this.pluginAliases.names();
    }

    @Override
    public ExpressionFunctionInfoSet infos() {
        return this.pluginAliases.infos();
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.pluginAliases.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof ExpressionFunctionAliases && this.equals0(Cast.to(other));
    }

    private boolean equals0(final ExpressionFunctionAliases other) {
        return this.pluginAliases.equals(other.pluginAliases);
    }

    @Override
    public String toString() {
        return this.pluginAliases.toString();
    }

    private final PluginAliases<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector> pluginAliases;

    // Json.............................................................................................................

    static void register() {
        // helps force registry of json marshaller
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.toString());
    }

    static ExpressionFunctionAliases unmarshall(final JsonNode node,
                                                final JsonNodeUnmarshallContext context) {
        return parse(
                node.stringOrFail()
        );
    }

    static {
        JsonNodeContext.register(
                JsonNodeContext.computeTypeName(ExpressionFunctionAliases.class),
                ExpressionFunctionAliases::unmarshall,
                ExpressionFunctionAliases::marshall,
                ExpressionFunctionAliases.class
        );
        ExpressionFunctionInfoSet.EMPTY.size(); // trigger static init and json marshall/unmarshall registry
    }

}
