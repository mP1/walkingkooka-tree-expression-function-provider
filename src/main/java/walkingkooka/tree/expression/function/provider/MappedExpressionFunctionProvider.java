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

import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.text.CharacterConstant;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A {@link ExpressionFunctionProvider} that wraps a view of new {@link ExpressionFunctionName} to a wrapped {@link ExpressionFunctionProvider}.
 */
final class MappedExpressionFunctionProvider implements ExpressionFunctionProvider {

    static MappedExpressionFunctionProvider with(final Set<ExpressionFunctionInfo> infos,
                                                 final ExpressionFunctionProvider provider) {
        Objects.requireNonNull(infos, "infos");
        Objects.requireNonNull(provider, "provider");

        return new MappedExpressionFunctionProvider(
                infos,
                provider
        );
    }


    private MappedExpressionFunctionProvider(final Set<ExpressionFunctionInfo> infos,
                                             final ExpressionFunctionProvider provider) {
        this.nameMapper = PluginInfoSetLike.nameMapper(
                infos,
                provider.expressionFunctionInfos()
        );
        this.provider = provider;
        this.infos = PluginInfoSetLike.merge(
                infos,
                provider.expressionFunctionInfos()
        );
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name,
                                                                                 final ProviderContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(context, "context");

        return this.provider.expressionFunction(
                this.nameMapper.apply(name)
                        .orElseThrow(() -> new UnknownExpressionFunctionException(name)),
                context
        ).setName(
                Optional.of(name)
        );
    }

    /**
     * A function that maps incoming {@link ExpressionFunctionName} to the target provider after mapping them across using the {@link walkingkooka.net.AbsoluteUrl}.
     */
    private final Function<ExpressionFunctionName, Optional<ExpressionFunctionName>> nameMapper;

    /**
     * The original wrapped {@link ExpressionFunctionProvider}.
     */
    private final ExpressionFunctionProvider provider;

    @Override
    public Set<ExpressionFunctionInfo> expressionFunctionInfos() {
        return this.infos;
    }

    private final Set<ExpressionFunctionInfo> infos;

    @Override
    public String toString() {
        return CharacterConstant.COMMA.toSeparatedString(
                this.infos,
                ExpressionFunctionInfo::toString
        );
    }
}
