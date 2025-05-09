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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.FakeExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AliasesExpressionFunctionProviderTest implements ExpressionFunctionProviderTesting<AliasesExpressionFunctionProvider<FakeExpressionEvaluationContext>, FakeExpressionEvaluationContext> {

    private final static String NAME1_STRING = "function1";

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    private final static ExpressionFunctionName NAME1 = ExpressionFunctionName.with(NAME1_STRING)
        .setCaseSensitivity(CASE_SENSITIVITY);

    private final static ExpressionFunctionInfo INFO1 = ExpressionFunctionInfo.parse("https://example.com/function1 " + NAME1);

    private final static ExpressionFunctionName ALIAS2 = ExpressionFunctionName.with("alias2")
        .setCaseSensitivity(CASE_SENSITIVITY);

    private final static ExpressionFunction<?, FakeExpressionEvaluationContext> FUNCTION1 = function(NAME1);

    private final static String NAME2_STRING = "function2";

    private final static ExpressionFunctionName NAME2 = ExpressionFunctionName.with(NAME2_STRING)
        .setCaseSensitivity(CASE_SENSITIVITY);

    private final static ExpressionFunction<?, FakeExpressionEvaluationContext> FUNCTION2 = function(NAME2);

    private final static ExpressionFunctionInfo INFO2 = ExpressionFunctionInfo.parse("https://example.com/function2 " + NAME2);

    private final static String NAME3_STRING = "function3";

    private final static ExpressionFunctionName NAME3 = ExpressionFunctionName.with(NAME3_STRING)
        .setCaseSensitivity(CASE_SENSITIVITY);

    private final static ExpressionFunction<?, FakeExpressionEvaluationContext> FUNCTION3 = function(NAME3);

    private final static ExpressionFunctionInfo INFO3 = ExpressionFunctionInfo.parse("https://example.com/function3 " + NAME3);

    private final static String VALUE3 = "Value3";

    private final static String NAME4_STRING = "custom4";

    private final static ExpressionFunctionName NAME4 = ExpressionFunctionName.with(NAME4_STRING)
        .setCaseSensitivity(CASE_SENSITIVITY);

    private final static ExpressionFunctionInfo INFO4 = ExpressionFunctionInfo.parse("https://example.com/custom4 " + NAME4);

    private static ExpressionFunction<?, FakeExpressionEvaluationContext> function(final ExpressionFunctionName name) {
        return new FakeExpressionFunction<>() {
            @Override
            public Optional<ExpressionFunctionName> name() {
                return Optional.of(name);
            }

            @Override
            public ExpressionFunction<Object, FakeExpressionEvaluationContext> setName(final Optional<ExpressionFunctionName> n) {
                Objects.requireNonNull(n, "name");

                return this.name().equals(n) ?
                    this :
                    Cast.to(
                        function(n.get())
                    );
            }

            @Override
            public int hashCode() {
                return this.name().hashCode();
            }

            @Override
            public boolean equals(final Object other) {
                return this == other || other instanceof ExpressionFunction && this.equals0((ExpressionFunction<?, ?>) other);
            }

            private boolean equals0(final ExpressionFunction<?, ?> other) {
                return this.name().equals(other.name());
            }

            @Override
            public String toString() {
                return this.name()
                    .get()
                    .toString();
            }
        };
    }

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testWithUnknownFunctionName() {
        AliasesExpressionFunctionProvider.with(
            ExpressionFunctionAliasSet.parse(
                "unknown-function404",
                CASE_SENSITIVITY
            ),
            new FakeExpressionFunctionProvider<>() {
                @Override
                public ExpressionFunctionInfoSet expressionFunctionInfos() {
                    return ExpressionFunctionInfoSet.parse("https://example.com/function111 function111");
                }

                @Override
                public CaseSensitivity expressionFunctionNameCaseSensitivity() {
                    return CASE_SENSITIVITY;
                }
            }
        );
    }

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
            ExpressionFunctionSelector.parse(
                NAME1 + "",
                ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY
            ),
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
            FUNCTION2.setName(
                Optional.of(ALIAS2)
            )
        );
    }

    @Test
    public void testExpressionFunctionSelectorWithAlias() {
        this.expressionFunctionAndCheck(
            ExpressionFunctionSelector.parse(
                ALIAS2 + "",
                ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY
            ),
            CONTEXT,
            FUNCTION2.setName(
                Optional.of(ALIAS2)
            )
        );
    }

    @Test
    public void testExpressionFunctionNameWithSelector() {
        this.expressionFunctionAndCheck(
            NAME4,
            Lists.empty(),
            CONTEXT,
            FUNCTION3.setName(
                Optional.of(NAME4)
            )
        );
    }

    @Test
    public void testExpressionFunctionSelectorWithSelector() {
        this.expressionFunctionAndCheck(
            ExpressionFunctionSelector.parse(
                NAME4 + "",
                ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY
            ),
            CONTEXT,
            FUNCTION3.setName(
                Optional.of(NAME4)
            )
        );
    }

    @Test
    public void testInfos() {
        this.expressionFunctionInfosAndCheck(
            INFO1,
            INFO2.setName(ALIAS2),
            INFO4.setName(NAME4) // from ExpressionFunctionAliasSet
        );
    }

    @Override
    public AliasesExpressionFunctionProvider createExpressionFunctionProvider() {
        final String aliases = "function1, alias2 function2, custom4 function3(\"Value3\") https://example.com/custom4";

        this.checkEquals(
            NAME1 + ", " + ALIAS2 + " " + NAME2 + ", " + NAME4 + " " + NAME3 + "(\"" + VALUE3 + "\") " + INFO4.url(),
            aliases
        );

        return AliasesExpressionFunctionProvider.with(
            ExpressionFunctionAliasSet.parse(
                aliases,
                CASE_SENSITIVITY
            ),
            new FakeExpressionFunctionProvider<FakeExpressionEvaluationContext>() {
                @Override
                public ExpressionFunction<?, FakeExpressionEvaluationContext> expressionFunction(final ExpressionFunctionSelector selector,
                                                                                                 final ProviderContext context) {
                    return selector.evaluateValueText(
                        this,
                        context
                    );
                }

                @Override
                public ExpressionFunction<?, FakeExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name,
                                                                                                 final List<?> values,
                                                                                                 final ProviderContext context) {
                    ExpressionFunction<?, FakeExpressionEvaluationContext> function;

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

                @Override
                public CaseSensitivity expressionFunctionNameCaseSensitivity() {
                    return CASE_SENSITIVITY;
                }
            }
        );
    }

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return CASE_SENSITIVITY;
    }

    // class............................................................................................................

    @Override
    public Class<AliasesExpressionFunctionProvider<FakeExpressionEvaluationContext>> type() {
        return Cast.to(AliasesExpressionFunctionProvider.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
