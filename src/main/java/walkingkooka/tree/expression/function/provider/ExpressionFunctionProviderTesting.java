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
import walkingkooka.collect.set.Sets;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.plugin.ProviderTesting;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ExpressionFunctionProviderTesting<T extends ExpressionFunctionProvider> extends ProviderTesting<T> {

    @Test
    default void testFunctionWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createExpressionFunctionProvider()
                        .expressionFunction(
                                null,
                                ProviderContexts.fake()
                        )
        );
    }

    @Test
    default void testFunctionWithNullContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createExpressionFunctionProvider()
                        .expressionFunction(
                                ExpressionFunctionName.with("ignore"),
                                null
                        )
        );
    }

    @Test
    default void testExpressionFunctionInfosReadOnly() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> this.createExpressionFunctionProvider()
                        .expressionFunctionInfos()
                        .add(ExpressionFunctionInfo.parse("https://example.com/" + this.getClass().getName() + " " + this.getClass().getSimpleName()))
        );
    }

    default void expressionFunctionFails(final ExpressionFunctionName name,
                                         final ProviderContext context) {
        this.expressionFunctionFails(
                this.createExpressionFunctionProvider(),
                name,
                context
        );
    }

    default void expressionFunctionFails(final ExpressionFunctionProvider provider,
                                         final ExpressionFunctionName name,
                                         final ProviderContext context) {
        assertThrows(
                RuntimeException.class,
                () -> provider.expressionFunction(
                        name,
                        context
                )
        );
    }

    default void expressionFunctionAndCheck(final ExpressionFunctionName name,
                                            final ProviderContext context,
                                            final ExpressionFunction<?, ?> expected) {
        this.expressionFunctionAndCheck(
                this.createExpressionFunctionProvider(),
                name,
                context,
                expected
        );
    }

    default void expressionFunctionAndCheck(final ExpressionFunctionProvider provider,
                                            final ExpressionFunctionName name,
                                            final ProviderContext context,
                                            final ExpressionFunction<?, ?> expected) {
        this.checkEquals(
                expected,
                provider.expressionFunction(
                        name,
                        context
                ),
                () -> name.toString()
        );
    }

    default void expressionFunctionInfosAndCheck(final ExpressionFunctionInfo... expected) {
        this.expressionFunctionInfosAndCheck(
                this.createExpressionFunctionProvider(),
                expected
        );
    }

    default void expressionFunctionInfosAndCheck(final ExpressionFunctionProvider provider,
                                                 final ExpressionFunctionInfo... expected) {
        this.expressionFunctionInfosAndCheck(
                provider,
                Sets.of(expected)
        );
    }

    default void expressionFunctionInfosAndCheck(final Set<ExpressionFunctionInfo> expected) {
        this.expressionFunctionInfosAndCheck(
                this.createExpressionFunctionProvider(),
                expected
        );
    }

    default void expressionFunctionInfosAndCheck(final ExpressionFunctionProvider provider,
                                                 final Set<ExpressionFunctionInfo> expected) {
        this.checkEquals(
                expected,
                provider.expressionFunctionInfos(),
                () -> provider.toString()
        );
    }

    T createExpressionFunctionProvider();
}
