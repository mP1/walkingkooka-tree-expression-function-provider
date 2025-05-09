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
import walkingkooka.text.CaseSensitivity;
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
import java.util.Objects;
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
    public static ExpressionFunctionAliasSet empty(final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        return CaseSensitivity.SENSITIVE == caseSensitivity ?
            EMPTY_CASE_SENSITIVE :
            EMPTY_CASE_INSENSITIVE;
    }

    private final static ExpressionFunctionAliasSet EMPTY_CASE_SENSITIVE = new ExpressionFunctionAliasSet(
        PluginAliasSet.with(
            SortedSets.empty(),
            ExpressionFunctionPluginHelper.instance(CaseSensitivity.SENSITIVE)
        )
    );

    private final static ExpressionFunctionAliasSet EMPTY_CASE_INSENSITIVE = new ExpressionFunctionAliasSet(
        PluginAliasSet.with(
            SortedSets.empty(),
            ExpressionFunctionPluginHelper.instance(CaseSensitivity.INSENSITIVE)
        )
    );

    /**
     * {@see PluginAliasSet#SEPARATOR}
     */
    public final static CharacterConstant SEPARATOR = PluginAliasSet.SEPARATOR;

    public static ExpressionFunctionAliasSet parse(final String text,
                                                   final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        return new ExpressionFunctionAliasSet(
            PluginAliasSet.parse(
                text,
                ExpressionFunctionPluginHelper.instance(caseSensitivity)
            )
        );
    }

    private ExpressionFunctionAliasSet(final PluginAliasSet<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> pluginAliasSet) {
        this.pluginAliasSet = pluginAliasSet;
    }

    @Override
    public ExpressionFunctionSelector selector(final ExpressionFunctionSelector selector) {
        return this.pluginAliasSet.selector(selector);
    }

    @Override
    public Optional<ExpressionFunctionSelector> aliasSelector(final ExpressionFunctionName name) {
        return this.pluginAliasSet.aliasSelector(name);
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
    public boolean containsAliasOrName(final ExpressionFunctionName aliasOrName) {
        return this.pluginAliasSet.containsAliasOrName(aliasOrName);
    }

    @Override
    public ExpressionFunctionAliasSet concatOrReplace(final ExpressionFunctionAlias alias) {
        return new ExpressionFunctionAliasSet(
            this.pluginAliasSet.concatOrReplace(alias)
        );
    }

    @Override
    public ExpressionFunctionAliasSet deleteAliasOrNameAll(final Collection<ExpressionFunctionName> aliasOrNames) {
        return this.setElements(
            this.pluginAliasSet.deleteAliasOrNameAll(aliasOrNames)
        );
    }

    @Override
    public ExpressionFunctionAliasSet keepAliasOrNameAll(final Collection<ExpressionFunctionName> aliasOrNames) {
        return this.setElements(
            this.pluginAliasSet.keepAliasOrNameAll(aliasOrNames)
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
    public ExpressionFunctionAliasSet setElementsFailIfDifferent(SortedSet<ExpressionFunctionAlias> aliases) {
        final ExpressionFunctionAliasSet after = new ExpressionFunctionAliasSet(
            this.pluginAliasSet.setElementsFailIfDifferent(aliases)
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
        String text = this.text();

        if (CaseSensitivity.INSENSITIVE == this.pluginAliasSet.<ExpressionFunctionPluginHelper>helper().caseSensitivity) {
            text = CASE_INSENSITIVE_PREFIX + text;
        }

        return JsonNode.string(text);
    }

    static ExpressionFunctionAliasSet unmarshall(final JsonNode node,
                                                 final JsonNodeUnmarshallContext context) {
        String text = node.stringOrFail();

        CaseSensitivity caseSensitivity = CaseSensitivity.SENSITIVE;
        if (text.length() > 0 && CASE_INSENSITIVE_PREFIX == text.charAt(0)) {
            text = text.substring(1);

            caseSensitivity = CaseSensitivity.INSENSITIVE;
        }

        return parse(
            text,
            caseSensitivity
        );
    }

    private final static char CASE_INSENSITIVE_PREFIX = '@';

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
