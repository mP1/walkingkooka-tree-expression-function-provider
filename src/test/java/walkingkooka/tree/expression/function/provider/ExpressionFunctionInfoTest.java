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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionInfoTest implements ClassTesting2<ExpressionFunctionInfo>,
        HashCodeEqualsDefinedTesting2<ExpressionFunctionInfo>,
        JsonNodeMarshallingTesting<ExpressionFunctionInfo>,
        ComparableTesting2<ExpressionFunctionInfo> {

    private final static AbsoluteUrl URL = Url.parseAbsolute("http://example.com");

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("test-sine");

    @Test
    public void testWithNullUrlFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionInfo.with(
                        null,
                        NAME
                )
        );
    }

    @Test
    public void testWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionInfo.with(
                        URL,
                        null
                )
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentUrl() {
        this.checkNotEquals(
                ExpressionFunctionInfo.with(
                        Url.parseAbsolute("http://example.com/different"),
                        NAME
                )
        );
    }

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(
                ExpressionFunctionInfo.with(
                        URL,
                        FunctionExpressionName.with("different-123")
                )
        );
    }

    @Override
    public ExpressionFunctionInfo createObject() {
        return ExpressionFunctionInfo.with(
                URL,
                NAME
        );
    }

    // Comparable.......................................................................................................

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(
                ExpressionFunctionInfo.with(
                        URL,
                        FunctionExpressionName.with("xyz-456")
                )
        );
    }

    @Override
    public ExpressionFunctionInfo createComparable() {
        return this.createObject();
    }


    // json.............................................................................................................

    @Override
    public ExpressionFunctionInfo unmarshall(final JsonNode json,
                                             final JsonNodeUnmarshallContext context) {
        return ExpressionFunctionInfo.unmarshall(
                json,
                context
        );
    }

    @Override
    public ExpressionFunctionInfo createJsonNodeMarshallingValue() {
        return this.createObject();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ExpressionFunctionInfo> type() {
        return ExpressionFunctionInfo.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
