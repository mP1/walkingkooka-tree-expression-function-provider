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

import walkingkooka.collect.set.Sets;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    public Set<ExpressionFunctionInfo> expressionFunctionInfos() {
        return Sets.empty();
    }

    @Override
    public Optional<ExpressionFunction<?, ExpressionEvaluationContext>> expressionFunction(final FunctionExpressionName name) {
        Objects.requireNonNull(name, "name");
        return Optional.empty();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
