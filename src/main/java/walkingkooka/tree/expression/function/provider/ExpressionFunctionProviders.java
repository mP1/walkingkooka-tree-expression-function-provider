/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.Set;

public final class ExpressionFunctionProviders implements PublicStaticHelper {

    /**
     * {@see BasicExpressionFunctionProvider}
     */
    public static ExpressionFunctionProvider basic(final AbsoluteUrl baseUrl,
                                                   final Set<ExpressionFunction<?, ExpressionEvaluationContext>> functions) {
        return BasicExpressionFunctionProvider.with(
                baseUrl,
                functions
        );
    }

    /**
     * {@see FakeExpressionFunctionProvider}
     */
    public static ExpressionFunctionProvider fake() {
        return new FakeExpressionFunctionProvider();
    }

    /**
     * Stop creation
     */
    private ExpressionFunctionProviders() {
        throw new UnsupportedOperationException();
    }
}