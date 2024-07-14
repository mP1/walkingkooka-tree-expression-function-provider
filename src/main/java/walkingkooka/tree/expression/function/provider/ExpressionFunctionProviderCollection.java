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
import walkingkooka.collect.list.Lists;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.plugin.ProviderCollection;
import walkingkooka.plugin.ProviderCollectionProviderGetter;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link ExpressionFunctionProvider} view of a collection of {@link ExpressionFunctionProvider providers}.
 */
final class ExpressionFunctionProviderCollection implements ExpressionFunctionProvider {

    static ExpressionFunctionProviderCollection with(final Set<ExpressionFunctionProvider> providers) {
        return new ExpressionFunctionProviderCollection(
                Objects.requireNonNull(providers, "providers")
        );
    }

    private ExpressionFunctionProviderCollection(final Set<ExpressionFunctionProvider> providers) {
        this.providers = ProviderCollection.with(
                new ProviderCollectionProviderGetter<>() {
                    @Override
                    public ExpressionFunction<?, ?> get(final ExpressionFunctionProvider provider,
                                                        final FunctionExpressionName name,
                                                        final List<?> values) {
                        return Cast.to(
                                provider.expressionFunction(
                                        name
                                )
                        );
                    }

                    @Override
                    public ExpressionFunction<?, ?> get(final ExpressionFunctionProvider provider,
                                                        final PluginSelectorLike<FunctionExpressionName> selector) {
                        throw new UnsupportedOperationException();
                    }
                },
                ExpressionFunctionProvider::expressionFunctionInfos,
                ExpressionFunction.class.getSimpleName(),
                providers
        );
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final FunctionExpressionName name) {
        Objects.requireNonNull(name, "name");

        return Cast.to(
                this.providers.get(
                        name,
                        Lists.empty()
                )
        );
    }

    @Override
    public Set<ExpressionFunctionInfo> expressionFunctionInfos() {
        return this.providers.infos();
    }

    private final ProviderCollection<ExpressionFunctionProvider, FunctionExpressionName, ExpressionFunctionInfo, PluginSelectorLike<FunctionExpressionName>, ExpressionFunction<?, ?>> providers;

    @Override
    public String toString() {
        return this.providers.toString();
    }
}
