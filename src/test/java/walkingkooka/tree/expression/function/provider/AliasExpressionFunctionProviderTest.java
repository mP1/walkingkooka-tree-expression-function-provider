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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.FakeExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.List;
import java.util.Optional;

public final class AliasExpressionFunctionProviderTest implements ExpressionFunctionProviderTesting<AliasExpressionFunctionProvider> {

    private final static String NAME1_STRING = "function1";

    private final static ExpressionFunctionName NAME1 = ExpressionFunctionName.with(NAME1_STRING);

    private final static ExpressionFunctionInfo INFO1 = ExpressionFunctionInfo.parse("https://example.com/function1 " + NAME1);

    private final static ExpressionFunctionName ALIAS2 = ExpressionFunctionName.with("alias2");

    private final static ExpressionFunction<?, ExpressionEvaluationContext> FUNCTION1 = function(NAME1);

    private final static String NAME2_STRING = "function2";

    private final static ExpressionFunctionName NAME2 = ExpressionFunctionName.with(NAME2_STRING);

    private final static ExpressionFunction<?, ExpressionEvaluationContext> FUNCTION2 = function(NAME2);

    private final static ExpressionFunctionInfo INFO2 = ExpressionFunctionInfo.parse("https://example.com/function2 " + NAME2);

    private final static String NAME3_STRING = "function3";

    private final static ExpressionFunctionName NAME3 = ExpressionFunctionName.with(NAME3_STRING);

    private final static ExpressionFunction<?, ExpressionEvaluationContext> FUNCTION3 = function(NAME3);

    private final static ExpressionFunctionInfo INFO3 = ExpressionFunctionInfo.parse("https://example.com/function3 " + NAME3);

    private final static String VALUE3 = "Value3";

    private final static String NAME4_STRING = "custom4";

    private final static ExpressionFunctionName NAME4 = ExpressionFunctionName.with(NAME4_STRING);

    private final static ExpressionFunctionInfo INFO4 = ExpressionFunctionInfo.parse("https://example.com/custom4 " + NAME4);

    private static ExpressionFunction<?, ExpressionEvaluationContext> function(final ExpressionFunctionName name) {
        return new FakeExpressionFunction<>() {
            @Override
            public Optional<ExpressionFunctionName> name() {
                return Optional.of(name);
            }

            @Override
            public String toString() {
                return name.toString();
            }
        };
    }

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testExpressionFunctionNameWithName() {
        this.expressionFunctionAndCheck(
                NAME1,
                Lists.empty(),
                CONTEXT,
                FUNCTION1
        );
    }

    @Test
    public void testExpressionFunctionSelectorWithName() {
        this.expressionFunctionAndCheck(
                ExpressionFunctionSelector.parse(NAME1 + ""),
                CONTEXT,
                FUNCTION1
        );
    }

    @Test
    public void testExpressionFunctionNameWithAlias() {
        this.expressionFunctionAndCheck(
                ALIAS2,
                Lists.empty(),
                CONTEXT,
                FUNCTION2
        );
    }

    @Test
    public void testExpressionFunctionSelectorWithAlias() {
        this.expressionFunctionAndCheck(
                ExpressionFunctionSelector.parse(ALIAS2 + ""),
                CONTEXT,
                FUNCTION2
        );
    }

    @Test
    public void testExpressionFunctionNameWithSelector() {
        this.expressionFunctionAndCheck(
                NAME4,
                Lists.empty(),
                CONTEXT,
                FUNCTION3
        );
    }

    @Test
    public void testExpressionFunctionSelectorWithSelector() {
        this.expressionFunctionAndCheck(
                ExpressionFunctionSelector.parse(NAME4 + ""),
                CONTEXT,
                FUNCTION3
        );
    }

    @Test
    public void testInfos() {
        this.expressionFunctionInfosAndCheck(
                INFO1,
                INFO2.setName(ALIAS2),
                INFO3,
                INFO4.setName(NAME4)
        );
    }

    @Override
    public AliasExpressionFunctionProvider createExpressionFunctionProvider() {
        final String aliases = "function1, alias2 function2, custom4 function3(\"Value3\") https://example.com/custom4";

        this.checkEquals(
                NAME1 + ", " + ALIAS2 + " " + NAME2 + ", " + NAME4 + " " + NAME3 + "(\"" + VALUE3 + "\") " + INFO4.url(),
                aliases
        );

        return AliasExpressionFunctionProvider.with(
                aliases,
                new FakeExpressionFunctionProvider() {
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
                        ExpressionFunction<?, ExpressionEvaluationContext> function;

                        switch (name.toString()) {
                            case NAME1_STRING:
                                checkEquals(Lists.empty(), values, "values");
                                function = FUNCTION1;
                                break;
                            case NAME2_STRING:
                                checkEquals(Lists.empty(), values, "values");
                                function = FUNCTION2;
                                break;
                            case NAME3_STRING:
                                checkEquals(Lists.of(VALUE3), values, "values");
                                function = FUNCTION3;
                                break;
                            default:
                                throw new UnknownExpressionFunctionException(name);
                        }

                        return function;
                    }

                    @Override
                    public ExpressionFunctionInfoSet expressionFunctionInfos() {
                        return ExpressionFunctionInfoSet.with(
                                Sets.of(
                                        INFO1,
                                        INFO2,
                                        INFO3
                                )
                        );
                    }
                }
        );
    }

    // class............................................................................................................

    @Override
    public Class<AliasExpressionFunctionProvider> type() {
        return AliasExpressionFunctionProvider.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
