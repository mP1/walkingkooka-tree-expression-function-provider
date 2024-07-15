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

import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple {@link ExpressionFunctionProvider} that supports an easy approach to batches of functions using a base
 * {@link walkingkooka.net.Url} and appending the function name.
 */
final class BasicExpressionFunctionProvider implements ExpressionFunctionProvider {

    static BasicExpressionFunctionProvider with(final AbsoluteUrl baseUrl,
                                                final CaseSensitivity nameCaseSensitivity,
                                                final Set<ExpressionFunction<?, ExpressionEvaluationContext>> functions) {
        return new BasicExpressionFunctionProvider(
                Objects.requireNonNull(baseUrl, "baseUrl"),
                Objects.requireNonNull(nameCaseSensitivity, "nameCaseSensitivity"),
                Sets.immutable(
                        Objects.requireNonNull(functions, "functions")
                )
        );
    }

    private BasicExpressionFunctionProvider(final AbsoluteUrl baseUrl,
                                            final CaseSensitivity nameCaseSensitivity,
                                            final Set<ExpressionFunction<?, ExpressionEvaluationContext>> functions) {
        if(functions.isEmpty()) {
            throw new IllegalArgumentException("Functions cannot be empty");
        }

        final Map<FunctionExpressionName, ExpressionFunction<?, ExpressionEvaluationContext>> nameToFunction = Maps.sorted(
                FunctionExpressionName.comparator(nameCaseSensitivity)
        );
        for (final ExpressionFunction<?, ExpressionEvaluationContext> function : functions) {
            final FunctionExpressionName name = function.name()
                    .orElseThrow(
                            () -> new IllegalArgumentException("Cannot add unnamed functions to provider")
                    );
            nameToFunction.put(
                    name,
                    function
            );
        }

        this.nameToFunction = nameToFunction;

        this.expressionFunctionInfos = Sets.readOnly(
                functions.stream()
                        .map(
                                f -> {
                                    final FunctionExpressionName name = f.name()
                                            .get();
                                    return ExpressionFunctionInfo.with(
                                            baseUrl.appendPath(
                                                    UrlPath.parse(
                                                            name.value()
                                                    )
                                            ),
                                            name
                                    );
                                }
                        ).collect(Collectors.toCollection(Sets::sorted))
        );
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final FunctionExpressionName name) {
        Objects.requireNonNull(name, "name");

        final ExpressionFunction<?, ExpressionEvaluationContext> function = this.nameToFunction.get(name);
        if(null == function) {
            throw new UnknownExpressionFunctionException(name);
        }
        return function;
    }

    private final Map<FunctionExpressionName, ExpressionFunction<?, ExpressionEvaluationContext>> nameToFunction;

    @Override
    public Set<ExpressionFunctionInfo> expressionFunctionInfos() {
        return this.expressionFunctionInfos;
    }

    private final Set<ExpressionFunctionInfo> expressionFunctionInfos;

    @Override
    public String toString() {
        return this.nameToFunction.keySet()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }
}
