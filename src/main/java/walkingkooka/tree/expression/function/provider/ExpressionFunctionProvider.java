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

import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.Optional;
import java.util.Set;

/**
 * A provider of {@link ExpressionFunction functions}.
 */
public interface ExpressionFunctionProvider {

    /**
     * Returns the {@link ExpressionFunction} for the given {@link FunctionExpressionName}.
     */
    Optional<ExpressionFunction<?, ExpressionEvaluationContext>> expressionFunction(final FunctionExpressionName name);

    /**
     * Helper that invokes {@link #function(FunctionExpressionName)} and throws a {@link UnknownExpressionFunctionException}
     * if none was found.
     */
    default ExpressionFunction<?, ExpressionEvaluationContext> functionOrFail(final FunctionExpressionName name) {
        return this.expressionFunction(name)
                .orElseThrow(() -> new UnknownExpressionFunctionException(name));
    }

    /**
     * Returns all known {@link ExpressionFunctionInfo}.
     */
    Set<ExpressionFunctionInfo> expressionFunctionInfos();
}
