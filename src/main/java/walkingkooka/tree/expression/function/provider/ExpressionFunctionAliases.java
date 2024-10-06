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
import walkingkooka.plugin.PluginAliasSet;
import walkingkooka.plugin.PluginAliasesLike;
import walkingkooka.text.CharacterConstant;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Optional;

public final class ExpressionFunctionAliases implements PluginAliasesLike<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector> {

    /**
     * {@see PluginAliasSet#SEPARATOR}
     */
    public final static CharacterConstant SEPARATOR = PluginAliasSet.SEPARATOR;

    public static ExpressionFunctionAliases parse(final String text) {
        return new ExpressionFunctionAliases(
                PluginAliasSet.parse(
                        text,
                        ExpressionFunctionHelper.INSTANCE
                )
        );
    }

    private ExpressionFunctionAliases(final PluginAliasSet<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector> pluginAliasSet) {
        this.pluginAliasSet = pluginAliasSet;
    }

    @Override
    public Optional<ExpressionFunctionSelector> alias(final ExpressionFunctionName name) {
        return this.pluginAliasSet.alias(name);
    }

    @Override
    public Optional<ExpressionFunctionName> name(final ExpressionFunctionName name) {
        return this.pluginAliasSet.name(name);
    }

    @Override
    public ExpressionFunctionInfoSet merge(final ExpressionFunctionInfoSet infos) {
        return this.pluginAliasSet.merge(infos);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.pluginAliasSet.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof ExpressionFunctionAliases && this.equals0(Cast.to(other));
    }

    private boolean equals0(final ExpressionFunctionAliases other) {
        return this.pluginAliasSet.equals(other.pluginAliasSet);
    }

    @Override
    public String toString() {
        return this.pluginAliasSet.text();
    }

    private final PluginAliasSet<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector> pluginAliasSet;

    // Json.............................................................................................................

    static void register() {
        // helps force registry of json marshaller
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(
                this.pluginAliasSet.text()
    );
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
