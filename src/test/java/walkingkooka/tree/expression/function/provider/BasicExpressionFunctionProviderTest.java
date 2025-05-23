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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionFunctionProviderTest implements ExpressionFunctionProviderTesting<BasicExpressionFunctionProvider<FakeExpressionEvaluationContext>, FakeExpressionEvaluationContext>,
    ToStringTesting<BasicExpressionFunctionProvider<FakeExpressionEvaluationContext>> {

    private final static AbsoluteUrl BASE_URL = Url.parseAbsolute("https://example.com/base/");

    private final static CaseSensitivity CASE_SENSITIVITY = ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY;

    private final static ExpressionFunctionName NAME1 = ExpressionFunctionName.with("testExpressionFunction1")
        .setCaseSensitivity(CASE_SENSITIVITY);

    private final static ExpressionFunctionName NAME2 = ExpressionFunctionName.with("testExpressionFunction2")
        .setCaseSensitivity(CASE_SENSITIVITY);

    private static final FakeExpressionFunction<Object, FakeExpressionEvaluationContext> FUNCTION1 = new FakeExpressionFunction<>() {
        @Override
        public Optional<ExpressionFunctionName> name() {
            return Optional.of(NAME1);
        }
    };
    private static final FakeExpressionFunction<Object, FakeExpressionEvaluationContext> FUNCTION2 = new FakeExpressionFunction<>() {
        @Override
        public Optional<ExpressionFunctionName> name() {
            return Optional.of(NAME2);
        }
    };

    private final static Set<ExpressionFunction<?, FakeExpressionEvaluationContext>> FUNCTIONS = Sets.of(
        FUNCTION1,
        FUNCTION2
    );

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testWithNullBaseUrlFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionFunctionProvider.with(
                null,
                CASE_SENSITIVITY,
                FUNCTIONS
            )
        );
    }

    @Test
    public void testWithNullNameCaseSensitivityFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionFunctionProvider.with(
                BASE_URL,
                null,
                FUNCTIONS
            )
        );
    }

    @Test
    public void testWithNullFunctionsFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicExpressionFunctionProvider.with(
                BASE_URL,
                CASE_SENSITIVITY,
                null
            )
        );
    }

    @Test
    public void testWithEmptyFunctionFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> BasicExpressionFunctionProvider.with(
                BASE_URL,
                CASE_SENSITIVITY,
                Sets.empty()
            )
        );
    }

    @Test
    public void testWithDuplicateFunctionCaseSensitiveFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> BasicExpressionFunctionProvider.with(
                BASE_URL,
                CaseSensitivity.SENSITIVE,
                Sets.of(
                    new FakeExpressionFunction<>() {
                        @Override
                        public Optional<ExpressionFunctionName> name() {
                            return Optional.of(
                                ExpressionFunctionName.with("A1")
                            );
                        }
                    },
                    new FakeExpressionFunction<>() {
                        @Override
                        public Optional<ExpressionFunctionName> name() {
                            return Optional.of(
                                ExpressionFunctionName.with("B2")
                            );
                        }
                    },
                    new FakeExpressionFunction<>() {
                        @Override
                        public Optional<ExpressionFunctionName> name() {
                            return Optional.of(
                                ExpressionFunctionName.with("A1")
                            );
                        }
                    }
                )
            )
        );

        this.checkEquals(
            "Duplicate function A1",
            thrown.getMessage()
        );
    }

    @Test
    public void testWithDuplicateFunctionCaseInsensitiveFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> BasicExpressionFunctionProvider.with(
                BASE_URL,
                CaseSensitivity.INSENSITIVE,
                Sets.of(
                    new FakeExpressionFunction<>() {
                        @Override
                        public Optional<ExpressionFunctionName> name() {
                            return Optional.of(
                                ExpressionFunctionName.with("A1")
                            );
                        }
                    },
                    new FakeExpressionFunction<>() {
                        @Override
                        public Optional<ExpressionFunctionName> name() {
                            return Optional.of(
                                ExpressionFunctionName.with("B2")
                            );
                        }
                    },
                    new FakeExpressionFunction<>() {
                        @Override
                        public Optional<ExpressionFunctionName> name() {
                            return Optional.of(
                                ExpressionFunctionName.with("a1")
                            );
                        }
                    }
                )
            )
        );

        this.checkEquals(
            "Duplicate function a1",
            thrown.getMessage()
        );
    }

    private final static List<?> VALUES = Lists.empty();

    @Test
    public void testExpressionFunctionNameLookupWhenCaseSensitive() {
        final CaseSensitivity caseSensitivity = CaseSensitivity.SENSITIVE;

        this.expressionFunctionAndCheck(
            this.createExpressionFunctionProvider(caseSensitivity),
            NAME1.setCaseSensitivity(caseSensitivity),
            VALUES,
            CONTEXT,
            FUNCTION1.setName(
                Optional.of(
                    NAME1.setCaseSensitivity(caseSensitivity)
                )
            )
        );
    }

    @Test
    public void testExpressionFunctionNameLookupWhenCaseInsensitive() {
        this.expressionFunctionAndCheck(
            this.createExpressionFunctionProvider(),
            NAME1,
            VALUES,
            CONTEXT,
            FUNCTION1
        );
    }

    @Test
    public void testExpressionFunctionNameLookupWhenCaseInsensitive2() {
        this.expressionFunctionAndCheck(
            this.createExpressionFunctionProvider(),
            NAME2,
            VALUES,
            CONTEXT,
            FUNCTION2
        );
    }

    @Test
    public void testExpressionFunctionNameLookupDifferentCaseCaseSensitiveFails() {
        this.expressionFunctionFails(
            this.createExpressionFunctionProvider(
                CaseSensitivity.SENSITIVE
            ),
            ExpressionFunctionName.with(
                NAME2.value()
                    .toUpperCase()
            ),
            VALUES,
            CONTEXT
        );
    }

    @Test
    public void testExpressionFunctionNameLookupDifferentCaseCaseInsensitive() {
        final CaseSensitivity caseSensitivity = CaseSensitivity.INSENSITIVE;

        this.expressionFunctionAndCheck(
            this.createExpressionFunctionProvider(caseSensitivity),
            NAME2.setCaseSensitivity(caseSensitivity),
            VALUES,
            CONTEXT,
            FUNCTION2.setName(
                Optional.of(
                    NAME2.setCaseSensitivity(caseSensitivity)
                )
            )
        );
    }

    @Test
    public void testExpressionFunctionSelector() {
        final CaseSensitivity caseSensitivity = CaseSensitivity.INSENSITIVE;

        this.expressionFunctionAndCheck(
            this.createExpressionFunctionProvider(
                caseSensitivity
            ),
            ExpressionFunctionSelector.parse(
                NAME2 + "",
                caseSensitivity
            ),
            CONTEXT,
            FUNCTION2.setName(
                Optional.of(
                    NAME2.setCaseSensitivity(caseSensitivity)
                )
            )
        );
    }

    @Test
    public void testExpressionFunctionNameInfoWhenCaseSensitive() {
        this.expressionFunctionInfosAndCheck2(CaseSensitivity.SENSITIVE);
    }

    @Test
    public void testExpressionFunctionNameInfoWhenCaseInsensitive() {
        this.expressionFunctionInfosAndCheck2(CaseSensitivity.INSENSITIVE);
    }

    private void expressionFunctionInfosAndCheck2(final CaseSensitivity caseSensitivity) {
        final ExpressionFunctionProvider provider = this.createExpressionFunctionProvider(caseSensitivity);

        this.expressionFunctionInfosAndCheck(
            provider,
            ExpressionFunctionInfo.with(
                Url.parseAbsolute("https://example.com/base/testExpressionFunction1"),
                NAME1
            ),
            ExpressionFunctionInfo.with(
                Url.parseAbsolute("https://example.com/base/testExpressionFunction2"),
                NAME2
            )
        );

        final ExpressionFunctionInfoSet infos = provider.expressionFunctionInfos();

        this.checkEquals(
            Lists.empty(),
            infos.names()
                .stream()
                .filter(i -> i.caseSensitivity() != caseSensitivity)
                .collect(Collectors.toList()),
            () -> ExpressionFunctionInfoSet.class.getSimpleName() + ".names ExpressionFunctionName#value name case sensitivity incorrect"
        );

        this.checkEquals(
            Lists.empty(),
            infos.stream()
                .map(i -> i.name())
                .filter(n -> n.caseSensitivity() != caseSensitivity)
                .collect(Collectors.toList()),
            () -> ExpressionFunctionInfoSet.class.getSimpleName() + ".info ExpressionFunctionInfo#name case sensitivity incorrect"
        );
    }

    @Override
    public BasicExpressionFunctionProvider createExpressionFunctionProvider() {
        return this.createExpressionFunctionProvider(CASE_SENSITIVITY);
    }

    private BasicExpressionFunctionProvider createExpressionFunctionProvider(final CaseSensitivity caseSensitivity) {
        return BasicExpressionFunctionProvider.with(
            BASE_URL,
            caseSensitivity,
            FUNCTIONS
        );
    }

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return CASE_SENSITIVITY;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createExpressionFunctionProvider(),
            "testExpressionFunction1, testExpressionFunction2"
        );
    }

    // class............................................................................................................

    @Override
    public Class<BasicExpressionFunctionProvider<FakeExpressionEvaluationContext>> type() {
        return Cast.to(BasicExpressionFunctionProvider.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
