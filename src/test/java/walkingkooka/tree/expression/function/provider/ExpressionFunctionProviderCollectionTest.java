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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionProviderCollectionTest implements ExpressionFunctionProviderTesting<ExpressionFunctionProviderCollection>,
        ToStringTesting<ExpressionFunctionProviderCollection> {

    private final static AbsoluteUrl BASE_URL = Url.parseAbsolute("https://example.com/base/");

    private final static FunctionExpressionName NAME1 = FunctionExpressionName.with("testfunction1");

    private final static FunctionExpressionName NAME2 = FunctionExpressionName.with("testfunction2");

    private static final FakeExpressionFunction FUNCTION1 = new FakeExpressionFunction() {
        @Override
        public Optional<FunctionExpressionName> name() {
            return Optional.of(NAME1);
        }
    };
    private static final FakeExpressionFunction FUNCTION2 = new FakeExpressionFunction() {
        @Override
        public Optional<FunctionExpressionName> name() {
            return Optional.of(NAME2);
        }
    };

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testWithNullProvidersFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionProviderCollection.with(
                        null
                )
        );
    }

    @Test
    public void testWithEmptyProvidersFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ExpressionFunctionProviderCollection.with(
                        Sets.empty()
                )
        );
    }

    @Test
    public void testWithNameClassFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> ExpressionFunctionProviderCollection.with(
                        Sets.of(
                                ExpressionFunctionProviders.basic(
                                        BASE_URL.appendPath(UrlPath.parse("1")),
                                        CaseSensitivity.SENSITIVE,
                                        Sets.of(
                                                FUNCTION1,
                                                FUNCTION2
                                        )
                                ),
                                ExpressionFunctionProviders.basic(
                                        BASE_URL.appendPath(UrlPath.parse("2")),
                                        CaseSensitivity.SENSITIVE,
                                        Sets.of(
                                                FUNCTION1
                                        )
                                ),
                                ExpressionFunctionProviders.basic(
                                        BASE_URL.appendPath(UrlPath.parse("3")),
                                        CaseSensitivity.SENSITIVE,
                                        Sets.of(
                                                FUNCTION2
                                        )
                                )
                        )
                )
        );
        this.checkEquals(
                "Found multiple ExpressionFunction for testfunction1(https://example.com/base/2/testfunction1, https://example.com/base/1/testfunction1), testfunction2(https://example.com/base/3/testfunction2, https://example.com/base/1/testfunction2)",
                thrown.getMessage(),
                "message"
        );
    }


    @Test
    public void testFunctionLookup1() {
        this.expressionFunctionAndCheck(
                this.createExpressionFunctionProvider(),
                NAME1,
                CONTEXT,
                FUNCTION1
        );
    }

    @Test
    public void testFunctionLookup2() {
        this.expressionFunctionAndCheck(
                this.createExpressionFunctionProvider(),
                NAME2,
                CONTEXT,
                FUNCTION2
        );
    }

    @Test
    public void testExpressionFunctionInfo() {
        this.expressionFunctionInfosAndCheck(
                this.createExpressionFunctionProvider(),
                Sets.of(
                        ExpressionFunctionInfo.with(
                                Url.parseAbsolute("https://example.com/base/testfunction1"),
                                NAME1
                        ),
                        ExpressionFunctionInfo.with(
                                Url.parseAbsolute("https://example.com/base/testfunction2"),
                                NAME2
                        )
                )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createExpressionFunctionProvider(),
                "testfunction1, testfunction2"
        );
    }

    @Override
    public ExpressionFunctionProviderCollection createExpressionFunctionProvider() {
        return ExpressionFunctionProviderCollection.with(
                        Sets.of(
                                ExpressionFunctionProviders.basic(
                                        BASE_URL,
                                        CaseSensitivity.SENSITIVE,
                                        Sets.of(
                                                FUNCTION1
                                        )
                                ),
                                ExpressionFunctionProviders.basic(
                                        BASE_URL,
                                        CaseSensitivity.SENSITIVE,
                                        Sets.of(
                                                FUNCTION2
                                        )
                                )
                        )
                );
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<ExpressionFunctionProviderCollection> type() {
        return ExpressionFunctionProviderCollection.class;
    }
}
