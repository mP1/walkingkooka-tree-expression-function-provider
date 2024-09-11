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

import walkingkooka.plugin.ProviderContext;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.List;
import java.util.Objects;

/**
 * A {@link ExpressionFunctionProvider} that is always empty and returns no {@link ExpressionFunctionInfo} or {@link ExpressionFunction}.
 */
final class EmptyExpressionFunctionProvider implements ExpressionFunctionProvider {

    /**
     * Singleton
     */
    final static EmptyExpressionFunctionProvider INSTANCE = new EmptyExpressionFunctionProvider();

    private EmptyExpressionFunctionProvider() {
        super();
    }

    @Override
    public ExpressionFunctionInfoSet expressionFunctionInfos() {
        return ExpressionFunctionInfoSet.EMPTY;
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionSelector selector,
                                                                                 final ProviderContext context) {
        Objects.requireNonNull(selector, "selector");
        Objects.requireNonNull(context, "context");

        throw new UnknownExpressionFunctionException(
                selector.name()
        );
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name,
                                                                                 final List<?> values,
                                                                                 final ProviderContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(context, "context");

        throw new UnknownExpressionFunctionException(name);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
