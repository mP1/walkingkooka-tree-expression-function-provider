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
import walkingkooka.plugin.ProviderTesting;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ExpressionFunctionProviderTesting<T extends ExpressionFunctionProvider> extends ProviderTesting<T> {

    // expressionFunction(ExpressionFunctionSelector, ProviderContext)..................................................

    @Test
    default void testExpressionFunctionWithNullSelectorFails() {
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
    default void testExpressionFunctionWithSelectorNullContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createExpressionFunctionProvider()
                        .expressionFunction(
                                ExpressionFunctionSelector.parse("selector"),
                                null
                        )
        );
    }

    default void expressionFunctionFails(final ExpressionFunctionSelector selector,
                                         final ProviderContext context) {
        this.expressionFunctionFails(
                this.createExpressionFunctionProvider(),
                selector,
                context
        );
    }

    default void expressionFunctionFails(final ExpressionFunctionProvider provider,
                                         final ExpressionFunctionSelector selector,
                                         final ProviderContext context) {
        assertThrows(
                RuntimeException.class,
                () -> provider.expressionFunction(
                        selector,
                        context
                )
        );
    }

    default void expressionFunctionAndCheck(final ExpressionFunctionSelector selector,
                                            final ProviderContext context,
                                            final ExpressionFunction<?, ?> expected) {
        this.expressionFunctionAndCheck(
                this.createExpressionFunctionProvider(),
                selector,
                context,
                expected
        );
    }

    default void expressionFunctionAndCheck(final ExpressionFunctionProvider provider,
                                            final ExpressionFunctionSelector selector,
                                            final ProviderContext context,
                                            final ExpressionFunction<?, ?> expected) {
        this.checkEquals(
                expected,
                provider.expressionFunction(
                        selector,
                        context
                ),
                () -> selector.toString()
        );
    }

    // expressionFunction(ExpressionFunctionName, List, ProviderContext)................................................

    @Test
    default void testExpressionFunctionWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createExpressionFunctionProvider()
                        .expressionFunction(
                                null,
                                Lists.empty(),
                                ProviderContexts.fake()
                        )
        );
    }

    @Test
    default void testFunctionWithNullValuesFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createExpressionFunctionProvider()
                        .expressionFunction(
                                ExpressionFunctionName.with("dummy"),
                                null,
                                ProviderContexts.fake()
                        )
        );
    }

    @Test
    default void testExpressionFunctionWithNullContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createExpressionFunctionProvider()
                        .expressionFunction(
                                ExpressionFunctionName.with("ignore"),
                                Lists.empty(),
                                null
                        )
        );
    }

    default void expressionFunctionFails(final ExpressionFunctionName name,
                                         final List<?> values,
                                         final ProviderContext context) {
        this.expressionFunctionFails(
                this.createExpressionFunctionProvider(),
                name,
                values,
                context
        );
    }

    default void expressionFunctionFails(final ExpressionFunctionProvider provider,
                                         final ExpressionFunctionName name,
                                         final List<?> values,
                                         final ProviderContext context) {
        assertThrows(
                RuntimeException.class,
                () -> provider.expressionFunction(
                        name,
                        values,
                        context
                )
        );
    }

    default void expressionFunctionAndCheck(final ExpressionFunctionName name,
                                            final List<?> values,
                                            final ProviderContext context,
                                            final ExpressionFunction<?, ?> expected) {
        this.expressionFunctionAndCheck(
                this.createExpressionFunctionProvider(),
                name,
                values,
                context,
                expected
        );
    }

    default void expressionFunctionAndCheck(final ExpressionFunctionProvider provider,
                                            final ExpressionFunctionName name,
                                            final List<?> values,
                                            final ProviderContext context,
                                            final ExpressionFunction<?, ?> expected) {
        this.checkEquals(
                expected,
                provider.expressionFunction(
                        name,
                        values,
                        context
                ),
                () -> name.toString()
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
