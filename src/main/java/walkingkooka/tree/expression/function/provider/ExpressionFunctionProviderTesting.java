/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ExpressionFunctionProviderTesting<T extends ExpressionFunctionProvider> extends ClassTesting2<T> {

    @Test
    default void testFunctionWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createExpressionFunctionProvider()
                        .function(null)
        );
    }

    @Test
    default void testExpressionFunctionInfosNotEmpty() {
        this.checkNotEquals(
                Sets.empty(),
                this.createExpressionFunctionProvider().expressionFunctionInfos()
        );
    }

    @Test
    default void testExpressionFunctionInfosReadOnly() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> this.createExpressionFunctionProvider()
                        .expressionFunctionInfos()
                        .clear()
        );
    }

    default void expressionFunctionAndCheck(final ExpressionFunctionProvider provider,
                                            final FunctionExpressionName name,
                                            final ExpressionFunction<?, ?> expected) {
        this.checkEquals(
                expected,
                provider.function(name),
                () -> name.toString()
        );
    }

    default void expressionFunctionAndFail(final ExpressionFunctionProvider provider,
                                           final FunctionExpressionName name) {
        final UnknownExpressionFunctionException thrown = assertThrows(
                UnknownExpressionFunctionException.class,
                () -> provider.function(name)
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
