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
import walkingkooka.net.UrlPath;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctions;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.Objects;
import java.util.Set;

/**
 * A {@link ExpressionFunctionProvider} for {@link ExpressionFunctions}.
 */
final class TreeExpressionFunctionProvider implements ExpressionFunctionProvider {

    /**
     * Singleton
     */
    final static TreeExpressionFunctionProvider INSTANCE = new TreeExpressionFunctionProvider();

    private TreeExpressionFunctionProvider() {
        super();

        this.infos = Sets.of(
                nameToExpressionFunctionInfo(ExpressionFunctions.node()),
                nameToExpressionFunctionInfo(ExpressionFunctions.nodeName()),
                nameToExpressionFunctionInfo(ExpressionFunctions.typeName())
        );
    }

    private static ExpressionFunctionInfo nameToExpressionFunctionInfo(final ExpressionFunction<?, ?> function) {
        final FunctionExpressionName name = function.name()
                .get();

        return ExpressionFunctionInfo.with(
                ExpressionFunctionProviders.BASE_URL.appendPath(
                        UrlPath.parse(
                                name.value()
                        )
                ),
                name
        );
    }

    @Override
    public Set<ExpressionFunctionInfo> expressionFunctionInfos() {
        return this.infos;
    }

    private final Set<ExpressionFunctionInfo> infos;

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final FunctionExpressionName name,
                                                                                 final ProviderContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(context, "context");

        final ExpressionFunction function;

        switch (name.value()) {
            case "name":
                function = ExpressionFunctions.nodeName();
                break;
            case "node":
                function = ExpressionFunctions.node();
                break;
            case "typeName":
                function = ExpressionFunctions.typeName();
                break;
            default:
                throw new UnknownExpressionFunctionException(name);
        }

        return function;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
