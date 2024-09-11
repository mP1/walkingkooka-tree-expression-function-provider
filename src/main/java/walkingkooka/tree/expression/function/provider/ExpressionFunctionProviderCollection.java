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

import walkingkooka.plugin.ProviderCollection;
import walkingkooka.plugin.ProviderCollectionProviderGetter;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Objects;
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
                    public ExpressionFunction<?, ExpressionEvaluationContext> get(final ExpressionFunctionProvider provider,
                                                                                  final ExpressionFunctionName name,
                                                                                  final List<?> values,
                                                                                  final ProviderContext context) {
                        return provider.expressionFunction(
                                name,
                                values,
                                context
                        );
                    }

                    @Override
                    public ExpressionFunction<?, ExpressionEvaluationContext> get(final ExpressionFunctionProvider provider,
                                                                                  final ExpressionFunctionSelector selector,
                                                                                  final ProviderContext context) {
                        return provider.expressionFunction(
                                selector,
                                context
                        );
                    }
                },
                ExpressionFunctionProvider::expressionFunctionInfos,
                ExpressionFunction.class.getSimpleName(),
                providers
        );
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionSelector selector,
                                                                                 final ProviderContext context) {
        Objects.requireNonNull(selector, "selector");
        Objects.requireNonNull(context, "context");

        return this.providers.get(
                selector,
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

        return this.providers.get(
                name,
                values,
                context
        );
    }

    @Override
    public ExpressionFunctionInfoSet expressionFunctionInfos() {
        return ExpressionFunctionInfoSet.with(
                this.providers.infos()
        );
    }

    private final ProviderCollection<ExpressionFunctionProvider, ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionSelector, ExpressionFunction<?, ExpressionEvaluationContext>> providers;

    @Override
    public String toString() {
        return this.providers.toString();
    }
}
