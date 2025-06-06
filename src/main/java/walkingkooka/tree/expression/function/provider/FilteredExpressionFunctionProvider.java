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

import walkingkooka.plugin.FilteredProviderGuard;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Objects;

/**
 * A {@link ExpressionFunctionProvider} that provides functions from one provider but lists more {@link ExpressionFunctionInfo}.
 */
final class FilteredExpressionFunctionProvider<C extends ExpressionEvaluationContext> implements ExpressionFunctionProvider<C> {

    static <C extends ExpressionEvaluationContext> FilteredExpressionFunctionProvider<C> with(final ExpressionFunctionProvider<C> provider,
                                                                                              final ExpressionFunctionInfoSet infos) {
        return new FilteredExpressionFunctionProvider<>(
            Objects.requireNonNull(provider, "provider"),
            Objects.requireNonNull(infos, "infos")
        );
    }

    private FilteredExpressionFunctionProvider(final ExpressionFunctionProvider<C> provider,
                                               final ExpressionFunctionInfoSet infos) {
        this.guard = FilteredProviderGuard.with(
            infos.names(),
            ExpressionFunctionPluginHelper.instance(provider.expressionFunctionNameCaseSensitivity())
        );
        this.provider = provider;
        this.infos = infos;
    }

    @Override
    public ExpressionFunction<?, C> expressionFunction(final ExpressionFunctionSelector selector,
                                                       final ProviderContext context) {
        return this.provider.expressionFunction(
            this.guard.selector(selector),
            Objects.requireNonNull(context, "context")
        );
    }

    @Override
    public ExpressionFunction<?, C> expressionFunction(final ExpressionFunctionName name,
                                                       final List<?> values,
                                                       final ProviderContext context) {
        return this.provider.expressionFunction(
            this.guard.name(
                name.setCaseSensitivity(this.expressionFunctionNameCaseSensitivity())
            ),
            Objects.requireNonNull(values, "values"),
            Objects.requireNonNull(context, "context")
        );
    }

    private final FilteredProviderGuard<ExpressionFunctionName, ExpressionFunctionSelector> guard;

    private final ExpressionFunctionProvider<C> provider;

    @Override
    public ExpressionFunctionInfoSet expressionFunctionInfos() {
        return this.infos;
    }

    private final ExpressionFunctionInfoSet infos;

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return this.provider.expressionFunctionNameCaseSensitivity();
    }

    @Override
    public String toString() {
        return this.provider.toString();
    }
}
