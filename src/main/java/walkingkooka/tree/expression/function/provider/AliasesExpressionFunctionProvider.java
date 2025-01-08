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
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ExpressionFunctionProvider} that uses the given aliases definition and {@link ExpressionFunctionProvider} to present another view.
 */
final class AliasesExpressionFunctionProvider implements ExpressionFunctionProvider {

    static AliasesExpressionFunctionProvider with(final ExpressionFunctionAliasSet aliases,
                                                  final ExpressionFunctionProvider provider) {
        return new AliasesExpressionFunctionProvider(
            Objects.requireNonNull(aliases, "aliases"),
            Objects.requireNonNull(provider, "provider")
        );
    }

    private AliasesExpressionFunctionProvider(final ExpressionFunctionAliasSet aliases,
                                              final ExpressionFunctionProvider provider) {
        this.aliases = aliases;
        this.provider = provider;

        this.infos = aliases.merge(provider.expressionFunctionInfos());
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionSelector selector,
                                                                                 final ProviderContext context) {
        return this.provider.expressionFunction(
            this.aliases.selector(selector),
            context
        ).setName(
            Optional.of(
                selector.name()
            )
        );
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name,
                                                                                 final List<?> values,
                                                                                 final ProviderContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(context, "context");

        return this.expressionFunction0(
            name.setCaseSensitivity(this.expressionFunctionNameCaseSensitivity()),
            values,
            context
        );
    }

    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction0(final ExpressionFunctionName name,
                                                                                  final List<?> values,
                                                                                  final ProviderContext context) {
        ExpressionFunction<?, ExpressionEvaluationContext> function;

        final ExpressionFunctionAliasSet aliases = this.aliases;
        final ExpressionFunctionProvider provider = this.provider;

        final Optional<ExpressionFunctionSelector> selector = aliases.aliasSelector(
            name.setCaseSensitivity(
                provider.expressionFunctionNameCaseSensitivity()
            )
        );
        if (selector.isPresent()) {
            if (false == values.isEmpty()) {
                throw new IllegalArgumentException("Alias " + name + " should have no values");
            }
            // assumes that $provider caches selectors to function
            function = provider.expressionFunction(
                selector.get(),
                context
            ).setName(
                Optional.of(name)
            );
        } else {
            function = provider.expressionFunction(
                aliases.aliasOrName(name)
                    .orElseThrow(() -> new UnknownExpressionFunctionException(name)),
                values,
                context
            );
        }

        return function;
    }

    private final ExpressionFunctionAliasSet aliases;

    @Override
    public ExpressionFunctionInfoSet expressionFunctionInfos() {
        return this.infos;
    }

    private final ExpressionFunctionInfoSet infos;

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return this.provider.expressionFunctionNameCaseSensitivity();
    }

    private final ExpressionFunctionProvider provider;

    @Override
    public String toString() {
        return this.expressionFunctionInfos().toString();
    }
}
