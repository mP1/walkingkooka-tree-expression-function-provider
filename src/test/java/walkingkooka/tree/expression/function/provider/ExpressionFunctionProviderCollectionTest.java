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
import walkingkooka.net.UrlPath;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionProviderCollectionTest implements ExpressionFunctionProviderTesting<ExpressionFunctionProviderCollection<FakeExpressionEvaluationContext>, FakeExpressionEvaluationContext>,
    ToStringTesting<ExpressionFunctionProviderCollection<FakeExpressionEvaluationContext>> {

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    private final static AbsoluteUrl BASE_URL = Url.parseAbsolute("https://example.com/base/");

    private final static ExpressionFunctionName NAME1 = ExpressionFunctionName.with("testfunction1")
        .setCaseSensitivity(CASE_SENSITIVITY);

    private final static ExpressionFunctionName NAME2 = ExpressionFunctionName.with("testfunction2")
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

    private final static List<?> VALUES = Lists.empty();

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testWithNullNameCaseSensitivityFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionProviderCollection.with(
                null,
                Sets.empty()
            )
        );
    }

    @Test
    public void testWithNullProvidersFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionProviderCollection.with(
                CASE_SENSITIVITY,
                null
            )
        );
    }

    @Test
    public void testWithEmptyProvidersFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ExpressionFunctionProviderCollection.with(
                CASE_SENSITIVITY,
                Sets.empty()
            )
        );
    }

    @Test
    public void testExpressionFunctionNameDuplicateUnknown() {
        final ExpressionFunctionProviderCollection provider = ExpressionFunctionProviderCollection.with(
            CASE_SENSITIVITY,
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
        );

        this.expressionFunctionFails(
            provider,
            NAME1,
            VALUES,
            CONTEXT
        );
        this.expressionFunctionFails(
            provider,
            NAME2,
            VALUES,
            CONTEXT
        );
    }

    @Test
    public void testExpressionFunctionNameLookup1() {
        this.expressionFunctionAndCheck(
            NAME1,
            VALUES,
            CONTEXT,
            FUNCTION1.setName(
                Optional.of(
                    NAME1
                )
            )
        );
    }

    @Test
    public void testExpressionFunctionNameLookup2() {
        this.expressionFunctionAndCheck(
            NAME2,
            VALUES,
            CONTEXT,
            FUNCTION2.setName(
                Optional.of(
                    NAME2
                )
            )
        );
    }

    @Test
    public void testExpressionFunctionSelectorLookup1() {
        this.expressionFunctionAndCheck(
            ExpressionFunctionSelector.parse(
                NAME1 + "",
                CASE_SENSITIVITY
            ),
            CONTEXT,
            FUNCTION1.setName(
                Optional.of(
                    NAME1
                )
            )
        );
    }

    @Test
    public void testExpressionFunctionSelectorLookup2() {
        this.expressionFunctionAndCheck(
            ExpressionFunctionSelector.parse(
                NAME2 + "",
                CASE_SENSITIVITY
            ),
            CONTEXT,
            FUNCTION2.setName(
                Optional.of(
                    NAME2
                )
            )
        );
    }

    @Test
    public void testExpressionFunctionInfos() {
        this.expressionFunctionInfosAndCheck(
            this.createExpressionFunctionProvider(),
            ExpressionFunctionInfo.with(
                Url.parseAbsolute("https://example.com/base/testfunction1"),
                NAME1
            ),
            ExpressionFunctionInfo.with(
                Url.parseAbsolute("https://example.com/base/testfunction2"),
                NAME2
            )
        );
    }

    @Override
    public ExpressionFunctionProviderCollection createExpressionFunctionProvider() {
        return ExpressionFunctionProviderCollection.with(
            CASE_SENSITIVITY,
            Sets.of(
                ExpressionFunctionProviders.basic(
                    BASE_URL,
                    CASE_SENSITIVITY,
                    Sets.of(
                        FUNCTION1
                    )
                ),
                ExpressionFunctionProviders.basic(
                    BASE_URL,
                    CASE_SENSITIVITY,
                    Sets.of(
                        FUNCTION2
                    )
                )
            )
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
            "testfunction1, testfunction2"
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionProviderCollection<FakeExpressionEvaluationContext>> type() {
        return Cast.to(ExpressionFunctionProviderCollection.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
