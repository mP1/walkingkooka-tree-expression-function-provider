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
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

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
                Function.identity(), // inputToName
                ExpressionFunctionProvider::expressionFunction,
                ExpressionFunctionProvider::expressionFunctionInfos,
                ExpressionFunction.class.getSimpleName(),
                providers
        );
    }

    @Override
    public Optional<ExpressionFunction<?, ExpressionEvaluationContext>> expressionFunction(final FunctionExpressionName selector) {
        Objects.requireNonNull(selector, "selector");

        return this.providers.get(selector);
    }

    @Override
    public Set<ExpressionFunctionInfo> expressionFunctionInfos() {
        return this.providers.infos();
    }

    private final ProviderCollection<FunctionExpressionName, ExpressionFunctionInfo, ExpressionFunctionProvider,
            FunctionExpressionName,
            ExpressionFunction<?, ExpressionEvaluationContext>> providers;

    @Override
    public String toString() {
        return this.providers.toString();
    }
}
