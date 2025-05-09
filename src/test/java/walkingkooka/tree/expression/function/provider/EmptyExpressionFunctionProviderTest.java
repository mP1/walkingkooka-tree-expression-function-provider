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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EmptyExpressionFunctionProviderTest implements ExpressionFunctionProviderTesting<EmptyExpressionFunctionProvider<FakeExpressionEvaluationContext>, FakeExpressionEvaluationContext> {

    @Test
    public void testWithNullCaseSensitivityFails() {
        assertThrows(
            NullPointerException.class,
            () -> EmptyExpressionFunctionProvider.with(null)
        );
    }

    @Test
    public void testWithCaseInSensitivite() {
        assertSame(
            EmptyExpressionFunctionProvider.with(CaseSensitivity.INSENSITIVE),
            EmptyExpressionFunctionProvider.with(CaseSensitivity.INSENSITIVE)
        );
    }

    @Test
    public void testWithCaseSensitivite() {
        assertSame(
            EmptyExpressionFunctionProvider.with(CaseSensitivity.SENSITIVE),
            EmptyExpressionFunctionProvider.with(CaseSensitivity.SENSITIVE)
        );
    }

    @Override
    public EmptyExpressionFunctionProvider<FakeExpressionEvaluationContext> createExpressionFunctionProvider() {
        return EmptyExpressionFunctionProvider.with(ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY);
    }

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY;
    }

    // class............................................................................................................

    @Override
    public Class<EmptyExpressionFunctionProvider<FakeExpressionEvaluationContext>> type() {
        return Cast.to(EmptyExpressionFunctionProvider.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
