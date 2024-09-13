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
     * {@see BasicExpressionFunctionProvider}
     */
    public static ExpressionFunctionProvider basic(final AbsoluteUrl baseUrl,
                                                   final CaseSensitivity nameCaseSensitivity,
                                                   final Set<ExpressionFunction<?, ExpressionEvaluationContext>> functions) {
        return BasicExpressionFunctionProvider.with(
                baseUrl,
                nameCaseSensitivity,
                functions
        );
    }

    /**
     * {@see ExpressionFunctionProviderCollection}
     */
    public static ExpressionFunctionProvider collection(final Set<ExpressionFunctionProvider> providers) {
        return ExpressionFunctionProviderCollection.with(providers);
    }

    /**
     * {@see EmptyExpressionFunctionProvider}
     */
    public static ExpressionFunctionProvider empty() {
        return EmptyExpressionFunctionProvider.INSTANCE;
    }

    /**
     * {@see TreeExpressionFunctionProvider}
     */
    public static ExpressionFunctionProvider expressionFunctions() {
        return TreeExpressionFunctionProvider.INSTANCE;
    }

    /**
     * {@see FakeExpressionFunctionProvider}
     */
    public static ExpressionFunctionProvider fake() {
        return new FakeExpressionFunctionProvider();
    }

    /**
     * {@see FilteredExpressionFunctionProvider}
     */
    public static ExpressionFunctionProvider filtered(final ExpressionFunctionProvider provider,
                                                      final ExpressionFunctionInfoSet infos) {
        return FilteredExpressionFunctionProvider.with(
                provider,
                infos
        );
    }

    /**
     * {@see MappedExpressionFunctionProvider}
     */
    public static ExpressionFunctionProvider mapped(final ExpressionFunctionInfoSet infos,
                                                    final ExpressionFunctionProvider provider) {
        return MappedExpressionFunctionProvider.with(
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
