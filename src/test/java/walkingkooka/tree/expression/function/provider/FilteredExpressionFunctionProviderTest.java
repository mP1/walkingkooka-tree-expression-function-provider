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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunctions;

import java.util.List;

public final class FilteredExpressionFunctionProviderTest implements ExpressionFunctionProviderTesting<FilteredExpressionFunctionProvider>,
        ToStringTesting<FilteredExpressionFunctionProvider> {

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testExpressionFunction() {
        final ExpressionFunctionName name = ExpressionFunctionName.with("node");
        final List<?> values = Lists.empty();

        this.expressionFunctionAndCheck(
                name,
                values,
                CONTEXT,
                ExpressionFunctionProviders.expressionFunctions()
                        .expressionFunction(
                                name,
                                values,
                                CONTEXT
                        )
        );
    }

    @Test
    public void testExpressionFunctionWithFilteredFails() {
        final ExpressionFunctionName name = ExpressionFunctionName.with("name");
        final List<?> values = Lists.empty();

        this.expressionFunctionAndCheck(
                ExpressionFunctionProviders.expressionFunctions(),
                name,
                values,
                CONTEXT,
                ExpressionFunctions.nodeName()
        );

        this.expressionFunctionFails(
                name,
                values,
                CONTEXT
        );
    }

    @Test
    public void testExpressionFunctionInfos() {
        this.expressionFunctionInfosAndCheck(
                ExpressionFunctionInfoSet.EMPTY.concat(
                        ExpressionFunctionInfo.parse("https://github.com/mP1/walkingkooka-tree-expression-function-provider/ExpressionFunction/node node")
                )
        );
    }

    @Override
    public FilteredExpressionFunctionProvider createExpressionFunctionProvider() {
        return FilteredExpressionFunctionProvider.with(
                ExpressionFunctionProviders.expressionFunctions(),
                ExpressionFunctionInfoSet.EMPTY.concat(
                        ExpressionFunctionInfo.parse("https://github.com/mP1/walkingkooka-tree-expression-function-provider/ExpressionFunction/node node")
                )
        );
    }

    @Override
    public void testExpressionFunctionWithNullContextFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testExpressionFunctionWithSelectorNullContextFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testExpressionFunctionWithNullValuesFails() {
        throw new UnsupportedOperationException();
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createExpressionFunctionProvider(),
                "TreeExpressionFunctionProvider"
        );
    }

    // class............................................................................................................

    @Override
    public Class<FilteredExpressionFunctionProvider> type() {
        return FilteredExpressionFunctionProvider.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
