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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FilteredMappedExpressionFunctionProviderTest implements ExpressionFunctionProviderTesting<FilteredMappedExpressionFunctionProvider>,
        ToStringTesting<FilteredMappedExpressionFunctionProvider> {

    private final static AbsoluteUrl URL = Url.parseAbsolute("https://example.com/function123");

    private final static ExpressionFunctionName NAME = ExpressionFunctionName.with("different-function-name-123")
            .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity);

    private final static ExpressionFunctionName ORIGINAL_NAME = ExpressionFunctionName.with("original-function-123")
            .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity);

    private static ExpressionFunction<?, ExpressionEvaluationContext> function(final ExpressionFunctionName name) {
        return new FakeExpressionFunction() {
            @Override
            public Optional<ExpressionFunctionName> name() {
                return Optional.of(name);
            }

            @Override
            public int hashCode() {
                return name.hashCode();
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
                return name.toString();
            }
        };
    }

    private final static List<?> VALUES = Lists.empty();

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testWithNullViewFails() {
        assertThrows(
                NullPointerException.class,
                () -> FilteredMappedExpressionFunctionProvider.with(
                        null,
                        ExpressionFunctionProviders.fake()
                )
        );
    }

    @Test
    public void testWithNullProviderFails() {
        assertThrows(
                NullPointerException.class,
                () -> FilteredMappedExpressionFunctionProvider.with(
                        ExpressionFunctionInfoSet.EMPTY,
                        null
                )
        );
    }

    @Test
    public void testExpressionFunctionName() {
        this.expressionFunctionAndCheck(
                NAME,
                VALUES,
                CONTEXT,
                function(NAME)
        );
    }

    @Test
    public void testExpressionFunctionNameUnknownFails() {
        this.expressionFunctionFails(
                ExpressionFunctionName.with("unknown"),
                VALUES,
                CONTEXT
        );
    }

    @Test
    public void testExpressionFunctionSelector() {
        this.expressionFunctionAndCheck(
                ExpressionFunctionSelector.parse(NAME + ""),
                CONTEXT,
                function(NAME)
        );
    }

    @Test
    public void testExpressionFunctionSelectorUnknownFails() {
        this.expressionFunctionFails(
                ExpressionFunctionSelector.parse("unknown"),
                CONTEXT
        );
    }

    @Test
    public void testInfos() {
        this.expressionFunctionInfosAndCheck(
                ExpressionFunctionInfo.with(
                        URL,
                        NAME
                )
        );
    }

    @Override
    public FilteredMappedExpressionFunctionProvider createExpressionFunctionProvider() {
        return FilteredMappedExpressionFunctionProvider.with(
                ExpressionFunctionInfoSet.EMPTY.concat(
                        ExpressionFunctionInfo.with(
                                URL,
                                NAME
                        )
                ),
                new FakeExpressionFunctionProvider() {

                    @Override
                    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name,
                                                                                                 final List<?> values,
                                                                                                 final ProviderContext context) {
                        if(false == name.equals(ORIGINAL_NAME)) {
                            throw new IllegalArgumentException("Unknown function " + name);
                        }
                        return function(ORIGINAL_NAME);
                    }

                    @Override
                    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionSelector selector,
                                                                                                 final ProviderContext context) {
                        return selector.evaluateValueText(
                                this,
                                context
                        );
                    }

                    @Override
                    public ExpressionFunctionInfoSet expressionFunctionInfos() {
                        return ExpressionFunctionInfoSet.EMPTY.concat(
                                ExpressionFunctionInfo.with(
                                        URL,
                                        ORIGINAL_NAME
                                )
                        );
                    }

                    @Override
                    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
                        return ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity;
                    }

                }
        );
    }

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createExpressionFunctionProvider(),
                "https://example.com/function123 different-function-name-123"
        );
    }

    // class............................................................................................................

    @Override
    public Class<FilteredMappedExpressionFunctionProvider> type() {
        return FilteredMappedExpressionFunctionProvider.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
