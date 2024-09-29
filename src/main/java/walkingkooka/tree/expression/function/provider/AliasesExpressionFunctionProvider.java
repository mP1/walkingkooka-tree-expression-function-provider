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

import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.text.CharacterConstant;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link ExpressionFunctionProvider} that uses the given aliases definition and {@link ExpressionFunctionProvider} to present another view.
 */
final class AliasesExpressionFunctionProvider implements ExpressionFunctionProvider {

    static AliasesExpressionFunctionProvider with(final ExpressionFunctionAliases aliases,
                                                  final ExpressionFunctionProvider provider) {
        return new AliasesExpressionFunctionProvider(
                Objects.requireNonNull(aliases, "aliases"),
                Objects.requireNonNull(provider, "provider")
        );
    }

    private AliasesExpressionFunctionProvider(final ExpressionFunctionAliases aliases,
                                              final ExpressionFunctionProvider provider) {
        this.aliases = aliases;
        this.provider = provider;

        final ExpressionFunctionInfoSet providerInfos = provider.expressionFunctionInfos();

        // verify all aliases -> name and names exist
        final Set<ExpressionFunctionName> providerNames = providerInfos.names();

        final Set<ExpressionFunctionName> unknownNames = SortedSets.tree();

        this.aliases.names()
                .stream()
                .filter(n -> false == providerNames.contains(n))
                .forEach(unknownNames::add);

        // Fix all INFOs for each alias
        ExpressionFunctionInfoSet newInfos = providerInfos;

        final Set<ExpressionFunctionName> aliasNames = aliases.aliases();
        final ExpressionFunctionInfoSet aliasesInfos = aliases.infos();

        if (aliasNames.size() + aliasesInfos.size() > 0) {
            final Map<ExpressionFunctionName, ExpressionFunctionInfo> nameToProviderInfo = Maps.sorted();

            for (final ExpressionFunctionInfo providerInfo : providerInfos) {
                nameToProviderInfo.put(
                        providerInfo.name(),
                        providerInfo
                );
            }

            for (final ExpressionFunctionName aliasName : aliasNames) {
                final Optional<ExpressionFunctionSelector> selector = aliases.alias(aliasName);
                if (selector.isPresent()) {
                    final ExpressionFunctionInfo providerInfo = nameToProviderInfo.get(
                            selector.get()
                                    .name()
                    );
                    if (null != providerInfo) {
                        newInfos = newInfos.replace(
                                providerInfo,
                                providerInfo.setName(aliasName)
                        );
                    }
                }
            }

            for (final ExpressionFunctionInfo aliasInfo : aliasesInfos) {
                final ExpressionFunctionName name = aliasInfo.name();
                final ExpressionFunctionInfo providerInfo = nameToProviderInfo.get(name);
                if (null != providerInfo) {
                    newInfos = newInfos.replace(
                            providerInfo,
                            aliasInfo
                    );
                } else {
                    newInfos = newInfos.concat(
                            aliasInfo
                    );
                }
            }
        }

        if (false == unknownNames.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unknown functions: " +
                            CharacterConstant.COMMA.toSeparatedString(
                                    unknownNames,
                                    ExpressionFunctionName::toString
                            )
            );
        }

        this.infos = newInfos;
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionSelector selector,
                                                                                 final ProviderContext context) {
        return selector.evaluateText(
                this,
                context
        );
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name,
                                                                                 final List<?> values,
                                                                                 final ProviderContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(context, "context");

        ExpressionFunction<?, ExpressionEvaluationContext> function;

        final ExpressionFunctionAliases aliases = this.aliases;
        final ExpressionFunctionProvider provider = this.provider;

        final Optional<ExpressionFunctionSelector> selector = aliases.alias(name);
        if (selector.isPresent()) {
            if (false == values.isEmpty()) {
                throw new IllegalArgumentException("Alias " + name + " should have no values");
            }
            // assumes that $provider caches selectors to function
            function = provider.expressionFunction(
                    selector.get(),
                    context
            );
        } else {
            function = provider.expressionFunction(
                    aliases.name(name)
                            .orElseThrow(() -> new UnknownExpressionFunctionException(name)),
                    values,
                    context
            );
        }

        return function;
    }

    private final ExpressionFunctionAliases aliases;

    private final ExpressionFunctionProvider provider;

    @Override
    public ExpressionFunctionInfoSet expressionFunctionInfos() {
        return this.infos;
    }

    private final ExpressionFunctionInfoSet infos;

    @Override
    public String toString() {
        return this.expressionFunctionInfos().toString();
    }
}
