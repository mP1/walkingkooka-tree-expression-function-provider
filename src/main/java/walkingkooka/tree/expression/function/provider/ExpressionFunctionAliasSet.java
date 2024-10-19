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
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.SortedSet;

public final class ExpressionFunctionAliasSet extends AbstractSet<ExpressionFunctionAlias>
        implements PluginAliasSetLike<ExpressionFunctionName,
        ExpressionFunctionInfo,
        ExpressionFunctionInfoSet,
        ExpressionFunctionSelector,
        ExpressionFunctionAlias,
        ExpressionFunctionAliasSet>,
        ImmutableSortedSetDefaults<ExpressionFunctionAliasSet, ExpressionFunctionAlias> {

    /**
     * An empty {@link ExpressionFunctionAliasSet}.
     */
    public final static ExpressionFunctionAliasSet EMPTY = new ExpressionFunctionAliasSet(
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
     * Factory that creates {@link ExpressionFunctionAliasSet} with the given aliases.
     */
    public static ExpressionFunctionAliasSet with(final SortedSet<ExpressionFunctionAlias> aliases) {
        return EMPTY.setElements(aliases);
    }

    public static ExpressionFunctionAliasSet parse(final String text) {
        return new ExpressionFunctionAliasSet(
                PluginAliasSet.parse(
                        text,
                        ExpressionFunctionPluginHelper.INSTANCE
                )
        );
    }

    private ExpressionFunctionAliasSet(final PluginAliasSet<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> pluginAliasSet) {
        this.pluginAliasSet = pluginAliasSet;
    }

    @Override
    public Optional<ExpressionFunctionSelector> alias(final ExpressionFunctionName name) {
        return this.pluginAliasSet.alias(name);
    }

    @Override
    public Optional<ExpressionFunctionName> aliasOrName(final ExpressionFunctionName name) {
        return this.pluginAliasSet.aliasOrName(name);
    }

    @Override
    public ExpressionFunctionInfoSet merge(final ExpressionFunctionInfoSet infos) {
        return this.pluginAliasSet.merge(infos);
    }

    @Override
    public boolean containsNameOrAlias(final ExpressionFunctionName name) {
        return this.pluginAliasSet.containsNameOrAlias(name);
    }

    @Override
    public ExpressionFunctionAliasSet concatOrReplace(final ExpressionFunctionAlias alias) {
        return new ExpressionFunctionAliasSet(
                this.pluginAliasSet.concatOrReplace(alias)
        );
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
    public ExpressionFunctionAliasSet setElements(final SortedSet<ExpressionFunctionAlias> aliases) {
        final ExpressionFunctionAliasSet after = new ExpressionFunctionAliasSet(
                this.pluginAliasSet.setElements(aliases)
        );
        return this.pluginAliasSet.equals(aliases) ?
                this :
                after;
    }

    @Override
    public ExpressionFunctionAliasSet setElementsFailIfDifferent(SortedSet<ExpressionFunctionAlias> sortedSet) {
        return null;
    }

    @Override
    public SortedSet<ExpressionFunctionAlias> toSet() {
        return this.pluginAliasSet.toSet();
    }

    @Override
    public ExpressionFunctionAliasSet subSet(final ExpressionFunctionAlias from,
                                             final ExpressionFunctionAlias to) {
        return this.setElements(
                this.pluginAliasSet.subSet(
                        from,
                        to
                )
        );
    }

    @Override
    public ExpressionFunctionAliasSet headSet(final ExpressionFunctionAlias alias) {
        return this.setElements(
                this.pluginAliasSet.headSet(alias)
        );
    }

    @Override
    public ExpressionFunctionAliasSet tailSet(final ExpressionFunctionAlias alias) {
        return this.setElements(
                this.pluginAliasSet.tailSet(alias)
        );
    }

    @Override
    public ExpressionFunctionAliasSet concat(final ExpressionFunctionAlias alias) {
        return this.setElements(
                this.pluginAliasSet.concat(alias)
        );
    }

    @Override
    public ExpressionFunctionAliasSet concatAll(final Collection<ExpressionFunctionAlias> aliases) {
        return this.setElements(
                this.pluginAliasSet.concatAll(aliases)
        );
    }

    @Override
    public ExpressionFunctionAliasSet delete(final ExpressionFunctionAlias alias) {
        return this.setElements(
                this.pluginAliasSet.delete(alias)
        );
    }

    @Override
    public ExpressionFunctionAliasSet deleteAll(final Collection<ExpressionFunctionAlias> aliases) {
        return this.setElements(
                this.pluginAliasSet.deleteAll(aliases)
        );
    }

    @Override
    public ExpressionFunctionAliasSet replace(final ExpressionFunctionAlias oldAlias,
                                              final ExpressionFunctionAlias newAlias) {
        return this.setElements(
                this.pluginAliasSet.replace(
                        oldAlias,
                        newAlias
                )
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

    private final PluginAliasSet<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> pluginAliasSet;

    // Json.............................................................................................................

    static void register() {
        // helps force registry of json marshaller
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(
                this.pluginAliasSet.text()
    );
    }

    static ExpressionFunctionAliasSet unmarshall(final JsonNode node,
                                                 final JsonNodeUnmarshallContext context) {
        return parse(
                node.stringOrFail()
        );
    }

    static {
        JsonNodeContext.register(
                JsonNodeContext.computeTypeName(ExpressionFunctionAliasSet.class),
                walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet::unmarshall,
                ExpressionFunctionAliasSet::marshall,
                ExpressionFunctionAliasSet.class
        );
        ExpressionFunctionInfoSet.EMPTY.size(); // trigger static init and json marshall/unmarshall registry
    }
}
