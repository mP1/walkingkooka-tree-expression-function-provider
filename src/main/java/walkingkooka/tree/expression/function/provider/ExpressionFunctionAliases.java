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

import walkingkooka.collect.set.ImmutableSortedSetDefaults;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.plugin.PluginAliasSet;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.SortedSet;

public final class ExpressionFunctionAliases extends AbstractSet<ExpressionFunctionAlias>
        implements PluginAliasSetLike<ExpressionFunctionName,
        ExpressionFunctionInfo,
        ExpressionFunctionInfoSet,
        ExpressionFunctionSelector,
        ExpressionFunctionAlias>,
        ImmutableSortedSetDefaults<ExpressionFunctionAliases, ExpressionFunctionAlias> {

    /**
     * An empty {@link ExpressionFunctionAliases}.
     */
    public final static ExpressionFunctionAliases EMPTY = new ExpressionFunctionAliases(
            PluginAliasSet.with(
                    SortedSets.empty(),
                    ExpressionFunctionPluginHelper.INSTANCE
            )
    );

    /**
     * {@see PluginAliasSet#SEPARATOR}
     */
    public final static CharacterConstant SEPARATOR = PluginAliasSet.SEPARATOR;

    /**
     * Factory that creates {@link ExpressionFunctionAliases} with the given aliases.
     */
    public static ExpressionFunctionAliases with(final SortedSet<ExpressionFunctionAlias> aliases) {
        return EMPTY.setElements(aliases);
    }

    public static ExpressionFunctionAliases parse(final String text) {
        return new ExpressionFunctionAliases(
                PluginAliasSet.parse(
                        text,
                        ExpressionFunctionPluginHelper.INSTANCE
                )
        );
    }

    private ExpressionFunctionAliases(final PluginAliasSet<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias> pluginAliasSet) {
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

    // ImmutableSortedSet...............................................................................................

    @Override
    public Comparator<? super ExpressionFunctionAlias> comparator() {
        return this.pluginAliasSet.comparator();
    }

    @Override
    public Iterator<ExpressionFunctionAlias> iterator() {
        return this.pluginAliasSet.stream().iterator();
    }

    @Override
    public int size() {
        return this.pluginAliasSet.size();
    }

    @Override
    public ExpressionFunctionAliases setElements(final SortedSet<ExpressionFunctionAlias> aliases) {
        final ExpressionFunctionAliases after = new ExpressionFunctionAliases(
                this.pluginAliasSet.setElements(aliases)
        );
        return this.pluginAliasSet.equals(aliases) ?
                this :
                after;
    }

    @Override
    public SortedSet<ExpressionFunctionAlias> toSet() {
        return this.pluginAliasSet.toSet();
    }

    @Override
    public ExpressionFunctionAliases subSet(final ExpressionFunctionAlias from,
                                            final ExpressionFunctionAlias to) {
        return this.setElements(
                this.pluginAliasSet.subSet(
                        from,
                        to
                )
        );
    }

    @Override
    public ExpressionFunctionAliases headSet(final ExpressionFunctionAlias alias) {
        return this.setElements(
                this.pluginAliasSet.headSet(alias)
        );
    }

    @Override
    public SortedSet<ExpressionFunctionAlias> tailSet(final ExpressionFunctionAlias alias) {
        return this.setElements(
                this.pluginAliasSet.tailSet(alias)
        );
    }

    @Override
    public ExpressionFunctionAlias first() {
        return this.pluginAliasSet.first();
    }

    @Override
    public ExpressionFunctionAlias last() {
        return this.pluginAliasSet.last();
    }

    @Override
    public String text() {
        return this.pluginAliasSet.text();
    }

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.pluginAliasSet.printTree(printer);
    }

    private final PluginAliasSet<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias> pluginAliasSet;

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
