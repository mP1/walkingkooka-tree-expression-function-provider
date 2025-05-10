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

import walkingkooka.Cast;
import walkingkooka.collect.set.ImmutableSet;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.plugin.PluginInfoSet;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonArray;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonString;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A read only {@link Set} of {@link ExpressionFunctionInfo} sorted by {@link ExpressionFunctionName}.
 */
public final class ExpressionFunctionInfoSet extends AbstractSet<ExpressionFunctionInfo> implements PluginInfoSetLike<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> {

    public static ExpressionFunctionInfoSet empty(final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        return CaseSensitivity.SENSITIVE == caseSensitivity ?
            EMPTY_CASE_SENSITIVE :
            EMPTY_CASE_INSENSITIVE;
    }

    private final static ExpressionFunctionInfoSet EMPTY_CASE_SENSITIVE = new ExpressionFunctionInfoSet(
        PluginInfoSet.with(
            Cast.to(
                Sets.empty()
            )
        ),
        CaseSensitivity.SENSITIVE
    );

    private final static ExpressionFunctionInfoSet EMPTY_CASE_INSENSITIVE = new ExpressionFunctionInfoSet(
        PluginInfoSet.with(
            Cast.to(
                Sets.empty()
            )
        ),
        CaseSensitivity.INSENSITIVE
    );

    public static ExpressionFunctionInfoSet parse(final String text,
                                                  final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        return new ExpressionFunctionInfoSet(
            PluginInfoSet.parse(
                text,
                (final String info) -> ExpressionFunctionInfo.parse(
                    info,
                    caseSensitivity
                )
            ),
            caseSensitivity
        );
    }

    public static ExpressionFunctionInfoSet with(final Set<ExpressionFunctionInfo> infos,
                                                 final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(infos, "infos");
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        final PluginInfoSet<ExpressionFunctionName, ExpressionFunctionInfo> pluginInfoSet = PluginInfoSet.with(infos);
        return pluginInfoSet.isEmpty() ?
            empty(caseSensitivity) :
            new ExpressionFunctionInfoSet(
                pluginInfoSet,
                caseSensitivity
            );
    }

    private ExpressionFunctionInfoSet(final PluginInfoSet<ExpressionFunctionName, ExpressionFunctionInfo> pluginInfoSet,
                                      final CaseSensitivity caseSensitivity) {
        this.pluginInfoSet = pluginInfoSet;
        this.caseSensitivity = caseSensitivity;
    }

    // PluginInfoSetLike................................................................................................

    @Override
    public Set<ExpressionFunctionName> names() {
        return this.pluginInfoSet.names();
    }

    @Override
    public Set<AbsoluteUrl> url() {
        return this.pluginInfoSet.url();
    }

    @Override
    public ExpressionFunctionAliasSet aliasSet() {
        return ExpressionFunctionPluginHelper.instance(this.caseSensitivity)
            .toAliasSet(this);
    }

    @Override
    public ExpressionFunctionInfoSet filter(final ExpressionFunctionInfoSet infos) {
        return this.setElements(
            this.pluginInfoSet.filter(
                infos.pluginInfoSet
            )
        );
    }

    @Override
    public ExpressionFunctionInfoSet renameIfPresent(ExpressionFunctionInfoSet renameInfos) {
        return this.setElements(
            this.pluginInfoSet.renameIfPresent(
                renameInfos.pluginInfoSet
            )
        );
    }

    @Override
    public ExpressionFunctionInfoSet concat(final ExpressionFunctionInfo info) {
        return this.setElements(
            this.pluginInfoSet.concat(info)
        );
    }

    @Override
    public ExpressionFunctionInfoSet concatAll(final Collection<ExpressionFunctionInfo> infos) {
        return this.setElements(
            this.pluginInfoSet.concatAll(infos)
        );
    }

    @Override
    public ExpressionFunctionInfoSet delete(final ExpressionFunctionInfo info) {
        return this.setElements(
            this.pluginInfoSet.delete(info)
        );
    }

    @Override
    public ExpressionFunctionInfoSet deleteAll(final Collection<ExpressionFunctionInfo> infos) {
        return this.setElements(
            this.pluginInfoSet.deleteAll(infos)
        );
    }

    @Override
    public ExpressionFunctionInfoSet deleteIf(final Predicate<? super ExpressionFunctionInfo> predicate) {
        return this.setElements(
            this.pluginInfoSet.deleteIf(predicate)
        );
    }

    @Override
    public ExpressionFunctionInfoSet replace(final ExpressionFunctionInfo oldInfo,
                                             final ExpressionFunctionInfo newInfo) {
        return this.setElements(
            this.pluginInfoSet.replace(
                oldInfo,
                newInfo
            )
        );
    }

    @Override
    public ImmutableSet<ExpressionFunctionInfo> setElementsFailIfDifferent(final Set<ExpressionFunctionInfo> infos) {
        return this.setElements(
            this.pluginInfoSet.setElementsFailIfDifferent(
                infos
            )
        );
    }

    @Override
    public ExpressionFunctionInfoSet setElements(final Set<ExpressionFunctionInfo> infos) {
        final ExpressionFunctionInfoSet after = new ExpressionFunctionInfoSet(
            this.pluginInfoSet.setElements(infos),
            this.caseSensitivity
        );
        return this.pluginInfoSet.equals(infos) ?
            this :
            after;
    }

    @Override
    public Set<ExpressionFunctionInfo> toSet() {
        return this.pluginInfoSet.toSet();
    }

    // TreePrintable....................................................................................................

    @Override
    public String text() {
        return this.pluginInfoSet.text();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.pluginInfoSet.printTree(printer);
        }
        printer.outdent();
    }

    // AbstractSet......................................................................................................

    @Override
    public Iterator<ExpressionFunctionInfo> iterator() {
        return this.pluginInfoSet.iterator();
    }

    @Override
    public int size() {
        return this.pluginInfoSet.size();
    }

    private final PluginInfoSet<ExpressionFunctionName, ExpressionFunctionInfo> pluginInfoSet;

    private final CaseSensitivity caseSensitivity;

    // json.............................................................................................................

    // [ "@https://example.com/test-function-1 test-function-1", "@https://example.com/test-function-2 test-function-2" ]
    // [ "@" ]
    private JsonNode marshall(final JsonNodeMarshallContext context) {
        JsonArray json;

        final boolean caseInsensitive = CaseSensitivity.INSENSITIVE == this.caseSensitivity;

        if(caseInsensitive && this.isEmpty()) {
            json = EMPTY_CASE_INSENSITIVE_ARRAY;
        } else {
            json = context.marshallCollection(this)
                .cast(JsonArray.class);
//
//            if(CaseSensitivity.INSENSITIVE == this.caseSensitivity) {
//                json = json.insertChild(
//                    0,
//                    INSENSITIVE
//                );
//            }
        }

        return json;
    }

    private final static JsonString INSENSITIVE = JsonNode.string("@");

    private final static JsonArray EMPTY_CASE_INSENSITIVE_ARRAY = JsonNode.array()
        .appendChild(INSENSITIVE);

    // @VisibleForTesting
    static ExpressionFunctionInfoSet unmarshall(final JsonNode node,
                                                final JsonNodeUnmarshallContext context) {
        JsonArray array = node.cast(JsonArray.class);

        ExpressionFunctionInfoSet expressionFunctionInfos;
        if(array.isEmpty()) {
            expressionFunctionInfos = EMPTY_CASE_SENSITIVE;
        } else {
            CaseSensitivity caseSensitivity = CaseSensitivity.SENSITIVE;
            String first = array.get(0)
                .stringOrFail();
            if(first.equals("@")) {
                caseSensitivity = CaseSensitivity.INSENSITIVE;

                expressionFunctionInfos = empty(caseSensitivity);
            } else {
                if(first.startsWith("@")) {
                    caseSensitivity = CaseSensitivity.INSENSITIVE;
                }

                expressionFunctionInfos = with(
                    context.unmarshallSet(
                        array,
                        ExpressionFunctionInfo.class
                    ),
                    caseSensitivity
                );
            }
        }

        return expressionFunctionInfos;
    }

    static {
        ExpressionFunctionInfo.register(); // force registration

        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(ExpressionFunctionInfoSet.class),
            ExpressionFunctionInfoSet::unmarshall,
            ExpressionFunctionInfoSet::marshall,
            ExpressionFunctionInfoSet.class
        );
    }
}
