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

import walkingkooka.Cast;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.List;
import java.util.Objects;

/**
 * A {@link ExpressionFunctionProvider} that is always empty and returns no {@link ExpressionFunctionInfo} or {@link ExpressionFunction}.
 */
final class EmptyExpressionFunctionProvider<C extends ExpressionEvaluationContext> implements ExpressionFunctionProvider<C> {

    static <C extends ExpressionEvaluationContext> EmptyExpressionFunctionProvider<C> with(final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        return Cast.to(
            CaseSensitivity.SENSITIVE == caseSensitivity ?
                CASE_SENSITIVE :
                CASE_INSENSITIVE
        );
    }

    private final static EmptyExpressionFunctionProvider<?> CASE_INSENSITIVE = new EmptyExpressionFunctionProvider<>(CaseSensitivity.INSENSITIVE);

    private final static EmptyExpressionFunctionProvider<?> CASE_SENSITIVE = new EmptyExpressionFunctionProvider<>(CaseSensitivity.SENSITIVE);

    /**
     * Private ctor use factory
     */
    private EmptyExpressionFunctionProvider(final CaseSensitivity caseSensitivity) {
        super();
        this.caseSensitivity = caseSensitivity;
    }

    @Override
    public ExpressionFunctionInfoSet expressionFunctionInfos() {
        return ExpressionFunctionInfoSet.empty(this.caseSensitivity);
    }

    @Override
    public ExpressionFunction<?, C> expressionFunction(final ExpressionFunctionSelector selector,
                                                       final ProviderContext context) {
        Objects.requireNonNull(selector, "selector");
        Objects.requireNonNull(context, "context");

        throw new UnknownExpressionFunctionException(
            selector.name()
        );
    }

    @Override
    public ExpressionFunction<?, C> expressionFunction(final ExpressionFunctionName name,
                                                       final List<?> values,
                                                       final ProviderContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(context, "context");

        throw new UnknownExpressionFunctionException(name);
    }

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return this.caseSensitivity;
    }

    private final CaseSensitivity caseSensitivity;

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
