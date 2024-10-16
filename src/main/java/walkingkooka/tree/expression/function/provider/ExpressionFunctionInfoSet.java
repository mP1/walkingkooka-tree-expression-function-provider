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

import walkingkooka.collect.set.ImmutableSet;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.plugin.PluginInfoSet;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * A read only {@link Set} of {@link ExpressionFunctionInfo} sorted by {@link ExpressionFunctionName}.
 */
public final class ExpressionFunctionInfoSet extends AbstractSet<ExpressionFunctionInfo> implements PluginInfoSetLike<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> {

    public final static ExpressionFunctionInfoSet EMPTY = new ExpressionFunctionInfoSet(
            PluginInfoSet.with(
                    Sets.<ExpressionFunctionInfo>empty()
            )
    );

    public static ExpressionFunctionInfoSet parse(final String text) {
        return new ExpressionFunctionInfoSet(
                PluginInfoSet.parse(
                        text,
                        ExpressionFunctionInfo::parse
                )
        );
    }

    public static ExpressionFunctionInfoSet with(final Set<ExpressionFunctionInfo> infos) {
        Objects.requireNonNull(infos, "infos");

        final PluginInfoSet<ExpressionFunctionName, ExpressionFunctionInfo> pluginInfoSet = PluginInfoSet.with(infos);
        return pluginInfoSet.isEmpty() ?
                EMPTY :
                new ExpressionFunctionInfoSet(pluginInfoSet);
    }

    private ExpressionFunctionInfoSet(final PluginInfoSet<ExpressionFunctionName, ExpressionFunctionInfo> pluginInfoSet) {
        this.pluginInfoSet = pluginInfoSet;
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
                this.pluginInfoSet.setElements(infos)
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

    // json.............................................................................................................

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return context.marshallCollection(this);
    }

    // @VisibleForTesting
    static ExpressionFunctionInfoSet unmarshall(final JsonNode node,
                                                final JsonNodeUnmarshallContext context) {
        return with(
                context.unmarshallSet(
                        node,
                        ExpressionFunctionInfo.class
                )
        );
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
