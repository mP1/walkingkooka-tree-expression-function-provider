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
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ExpressionFunctionProviderTesting<P extends ExpressionFunctionProvider<C>, C extends ExpressionEvaluationContext> extends ProviderTesting<P> {

    // expressionFunction(ExpressionFunctionSelector, ProviderContext)..................................................

    @Test
    default void testExpressionFunctionSelectorWithNullSelectorFails() {
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
    default void testExpressionFunctionSelectorWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createExpressionFunctionProvider()
                .expressionFunction(
                    ExpressionFunctionSelector.parse(
                        "selector",
                        CaseSensitivity.INSENSITIVE
                    ),
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

    default void expressionFunctionFails(final ExpressionFunctionProvider<C> provider,
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

    default void expressionFunctionAndCheck(final ExpressionFunctionProvider<C> provider,
                                            final ExpressionFunctionSelector selector,
                                            final ProviderContext context,
                                            final ExpressionFunction<?, ?> expected) {
        final ExpressionFunction<?, ?> function = provider.expressionFunction(
            selector,
            context
        );

        this.checkEquals(
            expected,
            function,
            () -> selector.toString()
        );

        final CaseSensitivity expressionFunctionNameCaseSensitivity = this.expressionFunctionNameCaseSensitivity();
        this.checkEquals(
            expressionFunctionNameCaseSensitivity,
            function.name()
                .get()
                .caseSensitivity(),
            () -> "function name has wrong case sensitivity"
        );
    }

    // expressionFunction(ExpressionFunctionName, List, ProviderContext)................................................

    @Test
    default void testExpressionFunctionNameWithNullFails() {
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
    default void testExpressionFunctionNameWithNullValuesFails() {
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
    default void testExpressionFunctionNameWithNullContextFails() {
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

    default void expressionFunctionAndCheck(final ExpressionFunctionProvider<C> provider,
                                            final ExpressionFunctionName name,
                                            final List<?> values,
                                            final ProviderContext context,
                                            final ExpressionFunction<?, ?> expected) {
        final ExpressionFunction<?, C> function = provider.expressionFunction(
            name,
            values,
            context
        );

        this.checkEquals(
            expected,
            function,
            () -> name.toString()
        );

        final CaseSensitivity expressionFunctionNameCaseSensitivity = provider.expressionFunctionNameCaseSensitivity();
        this.checkEquals(
            expressionFunctionNameCaseSensitivity,
            function.name()
                .get()
                .caseSensitivity(),
            () -> "function name has wrong case sensitivity"
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

    // expresionFunctionInfos...........................................................................................

    default void expressionFunctionInfosAndCheck(final ExpressionFunctionInfo... expected) {
        this.expressionFunctionInfosAndCheck(
            this.createExpressionFunctionProvider(),
            expected
        );
    }

    default void expressionFunctionInfosAndCheck(final ExpressionFunctionProvider<C> provider,
                                                 final ExpressionFunctionInfo... expected) {
        this.expressionFunctionInfosAndCheck(
            provider,
            ExpressionFunctionInfoSet.with(
                Sets.of(expected)
            )
        );
    }

    default void expressionFunctionInfosAndCheck(final ExpressionFunctionInfoSet expected) {
        this.expressionFunctionInfosAndCheck(
            this.createExpressionFunctionProvider(),
            expected
        );
    }

    default void expressionFunctionInfosAndCheck(final ExpressionFunctionProvider<C> provider,
                                                 final ExpressionFunctionInfoSet expected) {
        this.checkEquals(
            expected,
            provider.expressionFunctionInfos(),
            () -> provider.toString()
        );
    }

    // expressionFunctionNameCaseSensitivity............................................................................

    @Test
    default void testExpressionFunctionNameCaseSensitivityNotNull() {
        this.checkNotEquals(
            null,
            this.createExpressionFunctionProvider()
                .expressionFunctionNameCaseSensitivity()
        );
    }

    @Test
    default void testExpressionFunctionNameCaseSensitivity() {
        this.expressionFunctionNameCaseSensitivityAndCheck(
            this.createExpressionFunctionProvider(),
            this.expressionFunctionNameCaseSensitivity()
        );
    }

    default void expressionFunctionNameCaseSensitivityAndCheck(final P provider,
                                                               final CaseSensitivity expected) {
        this.checkEquals(
            expected,
            provider.expressionFunctionNameCaseSensitivity()
        );
    }

    P createExpressionFunctionProvider();

    CaseSensitivity expressionFunctionNameCaseSensitivity();
}
