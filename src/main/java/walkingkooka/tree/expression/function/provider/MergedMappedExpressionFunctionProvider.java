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

import walkingkooka.plugin.MergedProviderMapper;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ExpressionFunctionProvider} that supports renamed {@link ExpressionFunctionName} before invoking a wrapped {@link ExpressionFunctionProvider}.
 */
final class MergedMappedExpressionFunctionProvider implements ExpressionFunctionProvider {

    static MergedMappedExpressionFunctionProvider with(final ExpressionFunctionInfoSet infos,
                                                       final ExpressionFunctionProvider provider) {
        Objects.requireNonNull(infos, "infos");
        Objects.requireNonNull(provider, "provider");

        return new MergedMappedExpressionFunctionProvider(
                infos,
                provider
        );
    }


    private MergedMappedExpressionFunctionProvider(final ExpressionFunctionInfoSet infos,
                                                   final ExpressionFunctionProvider provider) {
        this.mapper = MergedProviderMapper.with(
                infos,
                provider.expressionFunctionInfos(),
                ExpressionFunctionHelper.INSTANCE
        );

        this.provider = provider;
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionSelector selector,
                                                                                 final ProviderContext context) {
        Objects.requireNonNull(selector, "selector");
        Objects.requireNonNull(context, "context");

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

        return this.provider.expressionFunction(
                this.mapper.name(name),
                values,
                context
        ).setName(
                Optional.of(name)
        );
    }

    /**
     * The original wrapped {@link ExpressionFunctionProvider}.
     */
    private final ExpressionFunctionProvider provider;

    @Override
    public ExpressionFunctionInfoSet expressionFunctionInfos() {
        return this.mapper.infos();
    }

    private final MergedProviderMapper<ExpressionFunctionName, ExpressionFunctionSelector, ExpressionFunctionInfo, ExpressionFunctionInfoSet> mapper;

    @Override
    public String toString() {
        return this.mapper.toString();
    }
}
