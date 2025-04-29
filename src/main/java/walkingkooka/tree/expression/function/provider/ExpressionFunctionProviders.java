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

import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.Set;

public final class ExpressionFunctionProviders implements PublicStaticHelper {

    /**
     * This is the base {@link AbsoluteUrl} for all {@link ExpressionFunction} in this package. The name of each
     * function will be appended to this base.
     */
    public final static AbsoluteUrl BASE_URL = Url.parseAbsolute(
        "https://github.com/mP1/walkingkooka-tree-expression-function-provider/" + ExpressionFunction.class.getSimpleName()
    );

    /**
     * {@see AliasesExpressionFunctionProvider}.
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunctionProvider<C> aliases(final ExpressionFunctionAliasSet aliases,
                                                                                                final ExpressionFunctionProvider<C> provider) {
        return AliasesExpressionFunctionProvider.with(
            aliases,
            provider
        );
    }

    /**
     * {@see BasicExpressionFunctionProvider}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunctionProvider<C> basic(final AbsoluteUrl baseUrl,
                                                                                              final CaseSensitivity nameCaseSensitivity,
                                                                                              final Set<ExpressionFunction<?, C>> functions) {
        return BasicExpressionFunctionProvider.with(
            baseUrl,
            nameCaseSensitivity,
            functions
        );
    }

    /**
     * {@see ExpressionFunctionProviderCollection}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunctionProvider<C> collection(final CaseSensitivity expressionFunctionNameCaseSensitivity,
                                                                                                   final Set<ExpressionFunctionProvider<C>> providers) {
        return ExpressionFunctionProviderCollection.with(
            expressionFunctionNameCaseSensitivity,
            providers
        );
    }

    /**
     * {@see EmptyExpressionFunctionProvider}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunctionProvider<C> empty(final CaseSensitivity expressionFunctionNameCaseSensitivity) {
        return EmptyExpressionFunctionProvider.with(expressionFunctionNameCaseSensitivity);
    }

    /**
     * {@see TreeExpressionFunctionProvider}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunctionProvider<C> expressionFunctions() {
        return TreeExpressionFunctionProvider.instance();
    }

    /**
     * {@see FakeExpressionFunctionProvider}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunctionProvider<C> fake() {
        return new FakeExpressionFunctionProvider<>();
    }

    /**
     * {@see FilteredExpressionFunctionProvider}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunctionProvider<C> filtered(final ExpressionFunctionProvider<C> provider,
                                                                                                 final ExpressionFunctionInfoSet infos) {
        return FilteredExpressionFunctionProvider.with(
            provider,
            infos
        );
    }

    /**
     * {@see FilteredMappedExpressionFunctionProvider}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunctionProvider<C> filteredMapped(final ExpressionFunctionInfoSet infos,
                                                                                                       final ExpressionFunctionProvider<C> provider) {
        return FilteredMappedExpressionFunctionProvider.with(
            infos,
            provider
        );
    }

    /**
     * {@see MergedMappedExpressionFunctionProvider}
     */
    public static <C extends ExpressionEvaluationContext> ExpressionFunctionProvider<C> mergedMapped(final ExpressionFunctionInfoSet infos,
                                                                                                     final ExpressionFunctionProvider<C> provider) {
        return MergedMappedExpressionFunctionProvider.with(
            infos,
            provider
        );
    }

    /**
     * Stop creation
     */
    private ExpressionFunctionProviders() {
        throw new UnsupportedOperationException();
    }
}
