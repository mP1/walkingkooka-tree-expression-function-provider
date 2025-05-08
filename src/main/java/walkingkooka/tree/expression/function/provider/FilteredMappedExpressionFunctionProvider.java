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

import walkingkooka.plugin.FilteredProviderMapper;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ExpressionFunctionProvider} that wraps a view of new {@link ExpressionFunctionName} to a wrapped {@link ExpressionFunctionProvider}.
 */
final class FilteredMappedExpressionFunctionProvider<C extends ExpressionEvaluationContext> implements ExpressionFunctionProvider<C> {

    static <C extends ExpressionEvaluationContext> FilteredMappedExpressionFunctionProvider<C> with(final ExpressionFunctionInfoSet infos,
                                                                                                    final ExpressionFunctionProvider<C> provider) {
        Objects.requireNonNull(infos, "infos");
        Objects.requireNonNull(provider, "provider");

        return new FilteredMappedExpressionFunctionProvider<>(
            infos,
            provider
        );
    }


    private FilteredMappedExpressionFunctionProvider(final ExpressionFunctionInfoSet infos,
                                                     final ExpressionFunctionProvider<C> provider) {
        this.mapper = FilteredProviderMapper.with(
            infos,
            provider.expressionFunctionInfos(),
            ExpressionFunctionPluginHelper.instance(provider.expressionFunctionNameCaseSensitivity())
        );

        this.provider = provider;
    }

    @Override
    public ExpressionFunction<?, C> expressionFunction(final ExpressionFunctionSelector selector,
                                                       final ProviderContext context) {
        Objects.requireNonNull(selector, "selector");
        Objects.requireNonNull(context, "context");

        return selector.evaluateValueText(
            this,
            context
        );
    }

    @Override
    public ExpressionFunction<?, C> expressionFunction(final ExpressionFunctionName name,
                                                       final List<?> values,
                                                       final ProviderContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(context, "context");

        return this.provider.expressionFunction(
            this.mapper.name(
                name.setCaseSensitivity(
                    this.expressionFunctionNameCaseSensitivity()
                )
            ),
            values,
            context
        ).setName(
            Optional.of(name)
        );
    }

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return this.provider.expressionFunctionNameCaseSensitivity();
    }

    /**
     * The original wrapped {@link ExpressionFunctionProvider}.
     */
    private final ExpressionFunctionProvider<C> provider;

    @Override
    public ExpressionFunctionInfoSet expressionFunctionInfos() {
        return this.mapper.infos();
    }

    private final FilteredProviderMapper<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> mapper;

    @Override
    public String toString() {
        return this.mapper.toString();
    }
}
