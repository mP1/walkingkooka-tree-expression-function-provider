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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctions;

public final class TreeExpressionFunctionProviderTest implements ExpressionFunctionProviderTesting<TreeExpressionFunctionProvider<ExpressionEvaluationContext>, ExpressionEvaluationContext>,
    ToStringTesting<TreeExpressionFunctionProvider<ExpressionEvaluationContext>> {

    @Test
    public void testExpressionFunctionNode() {
        this.expressionFunctionAndCheck2(
            ExpressionFunctions.node()
        );
    }

    @Test
    public void testExpressionFunctionNodeName() {
        this.expressionFunctionAndCheck2(
            ExpressionFunctions.nodeName()
        );
    }

    @Test
    public void testExpressionFunctionTypeName() {
        this.expressionFunctionAndCheck2(
            ExpressionFunctions.typeName()
        );
    }

    private void expressionFunctionAndCheck2(final ExpressionFunction<?, ?> function) {
        this.expressionFunctionAndCheck(
            function.name()
                .get(),
            Lists.empty(),
            ProviderContexts.fake(),
            function
        );
    }

    @Override
    public TreeExpressionFunctionProvider<ExpressionEvaluationContext> createExpressionFunctionProvider() {
        return TreeExpressionFunctionProvider.instance();
    }

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createExpressionFunctionProvider(),
            TreeExpressionFunctionProvider.class.getSimpleName()
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeExpressionFunctionProvider<ExpressionEvaluationContext>> type() {
        return Cast.to(TreeExpressionFunctionProvider.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
