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

import walkingkooka.plugin.PluginAliases;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A {@link ExpressionFunctionProvider} that uses the given aliases definition and {@link ExpressionFunctionProvider} to present another view.
 */
final class AliasExpressionFunctionProvider implements ExpressionFunctionProvider {

    static AliasExpressionFunctionProvider with(final String aliases,
                                                final ExpressionFunctionProvider provider) {
        return new AliasExpressionFunctionProvider(
                Objects.requireNonNull(aliases, "aliases"),
                Objects.requireNonNull(provider, "provider")
        );
    }

    private AliasExpressionFunctionProvider(final String aliases,
                                            final ExpressionFunctionProvider provider) {
        this.aliases = PluginAliases.parse(
                aliases,
                NAME_PARSER, // nameParser
                ExpressionFunctionInfo::with, // infoFactory
                provider.expressionFunctionInfos(), // infos
                ExpressionFunctionSelector::parse // selector parser
        );
        this.provider = provider;
    }

    private final static BiFunction<TextCursor, ParserContext, Optional<ExpressionFunctionName>> NAME_PARSER = (t, c) ->
            Parsers.stringInitialAndPartCharPredicate(
                    cc -> ExpressionFunctionName.isChar(0, cc),
                    cc -> ExpressionFunctionName.isChar(1, cc),
                    ExpressionFunctionName.MIN_LENGTH,
                    ExpressionFunctionName.MAX_LENGTH
            ).parse(
                    t,
                    c
            ).map(
                    tt -> ExpressionFunctionName.with(tt.text())
            );

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionSelector selector,
                                                                                 final ProviderContext context) {
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

        ExpressionFunction<?, ExpressionEvaluationContext> function;

        final PluginAliases<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector> aliases = this.aliases;
        final ExpressionFunctionProvider provider = this.provider;

        final Optional<ExpressionFunctionSelector> selector = aliases.alias(name);
        if (selector.isPresent()) {
            if (false == values.isEmpty()) {
                throw new IllegalArgumentException("Alias " + name + " should have no values");
            }
            // assumes that $provider caches selectors to function
            function = provider.expressionFunction(
                    selector.get(),
                    context
            );
        } else {
            function = provider.expressionFunction(
                    aliases.name(name)
                            .orElseThrow(() -> new UnknownExpressionFunctionException(name)),
                    values,
                    context
            );
        }

        return function;
    }

    private final ExpressionFunctionProvider provider;

    @Override
    public ExpressionFunctionInfoSet expressionFunctionInfos() {
        return this.aliases.infos();
    }

    private final PluginAliases<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector> aliases;

    @Override
    public String toString() {
        return this.expressionFunctionInfos().toString();
    }
}