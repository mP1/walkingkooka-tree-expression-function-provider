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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MappedExpressionFunctionProviderTest implements ExpressionFunctionProviderTesting<MappedExpressionFunctionProvider>,
        ToStringTesting<MappedExpressionFunctionProvider> {

    private final static AbsoluteUrl URL = Url.parseAbsolute("https://example.com/function123");

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("different-function-name-123");

    private final static FunctionExpressionName ORIGINAL_NAME = FunctionExpressionName.with("original-function-123");

    private final static ExpressionFunction<?, ExpressionEvaluationContext> function(final FunctionExpressionName name) {
        return new FakeExpressionFunction() {
            @Override
            public Optional<FunctionExpressionName> name() {
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
    };

    @Test
    public void testWithNullViewFails() {
        assertThrows(
                NullPointerException.class,
                () -> MappedExpressionFunctionProvider.with(
                        null,
                        ExpressionFunctionProviders.fake()
                )
        );
    }

    @Test
    public void testWithNullProviderFails() {
        assertThrows(
                NullPointerException.class,
                () -> MappedExpressionFunctionProvider.with(
                        Sets.empty(),
                        null
                )
        );
    }

    @Test
    public void testExpressionFunction() {
        this.expressionFunctionAndCheck(
                NAME,
                function(NAME)
        );
    }

    @Test
    public void testExpressionFunctionUnknownFails() {
        this.expressionFunctionFails(
                FunctionExpressionName.with("unknown")
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

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createExpressionFunctionProvider(),
                "https://example.com/function123 different-function-name-123"
        );
    }

    @Override
    public MappedExpressionFunctionProvider createExpressionFunctionProvider() {
        return MappedExpressionFunctionProvider.with(
                Sets.of(
                        ExpressionFunctionInfo.with(
                                URL,
                                NAME
                        )
                ),
                new FakeExpressionFunctionProvider() {

                    @Override
                    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final FunctionExpressionName name) {
                        if(false == name.equals(ORIGINAL_NAME)) {
                            throw new IllegalArgumentException("Unknown function " + name);
                        }
                        return function(ORIGINAL_NAME);
                    }

                    @Override
                    public Set<ExpressionFunctionInfo> expressionFunctionInfos() {
                        return Sets.of(
                                ExpressionFunctionInfo.with(
                                        URL,
                                        ORIGINAL_NAME
                                )
                        );
                    }
                }
        );
    }

    @Override
    public Class<MappedExpressionFunctionProvider> type() {
        return MappedExpressionFunctionProvider.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
