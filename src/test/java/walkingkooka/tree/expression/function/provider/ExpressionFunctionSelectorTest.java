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
import walkingkooka.InvalidCharacterException;
import walkingkooka.plugin.PluginSelectorLikeTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionSelectorTest implements PluginSelectorLikeTesting<ExpressionFunctionSelector, ExpressionFunctionName> {

    @Test
    public void testWithNullCaseSensitivityFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionSelector.parse(
                "hello",
                null
            )
        );
    }

    @Override
    public ExpressionFunctionSelector createPluginSelectorLike(final ExpressionFunctionName name,
                                                               final String text) {
        return ExpressionFunctionSelector.with(
            name,
            text
        );
    }

    @Override
    public ExpressionFunctionName createName(final String value) {
        return ExpressionFunctionName.with(value);
    }

    @Test
    public void testParseExpressionFunctionNameSpacePatternText() {
        final String name = "text-format";
        final String patternText = "@@";

        this.parseStringAndCheck(
            name + " " + patternText,
            ExpressionFunctionSelector.with(
                ExpressionFunctionName.with(name),
                patternText
            )
        );
    }

    @Override
    public ExpressionFunctionSelector parseString(final String text) {
        return ExpressionFunctionSelector.parse(
            text,
            ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ExpressionFunctionSelector> type() {
        return ExpressionFunctionSelector.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // Json.............................................................................................................

    @Test
    public void testExpressionFunctionNameWithAtSignPrefixFails() {
        final InvalidCharacterException thrown = assertThrows(
            InvalidCharacterException.class,
            () -> ExpressionFunctionName.with("@Hello")
        );

        this.checkEquals(
            0,
            thrown.position()
        );
    }

    @Test
    public void testMarshall() {
        this.marshallAndCheck(
            ExpressionFunctionSelector.parse(
                "Hello 123@",
                CaseSensitivity.SENSITIVE
            ),
            "\"Hello 123@\""
        );
    }

    @Test
    public void testMarshallCaseInsensitiveFunctionName() {
        this.marshallAndCheck(
            ExpressionFunctionSelector.parse(
                "Hello 123@@@",
                CaseSensitivity.INSENSITIVE
            ),
            "\"@Hello 123@@@\""
        );
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
            "\"function123 123@@\"",
            ExpressionFunctionSelector.parse(
                "function123 123@@",
                CaseSensitivity.SENSITIVE
            )
        );
    }

    @Test
    public void testUnmarshallCaseInsensitiveFunctionName() {
        this.unmarshallAndCheck(
            "\"@function123 @@@\"",
            ExpressionFunctionSelector.parse(
                "function123 @@@",
                CaseSensitivity.INSENSITIVE
            )
        );
    }

    @Test
    public void testRoundtripCaseInsensitiveFunctionName() {
        this.marshallRoundTripTwiceAndCheck(
            ExpressionFunctionSelector.parse(
                "Hello 123@@@",
                CaseSensitivity.INSENSITIVE
            )
        );
    }

    @Override
    public ExpressionFunctionSelector unmarshall(final JsonNode json,
                                                 final JsonNodeUnmarshallContext context) {
        return ExpressionFunctionSelector.unmarshall(
            json,
            context
        );
    }

    @Override
    public ExpressionFunctionSelector createJsonNodeMarshallingValue() {
        return ExpressionFunctionSelector.with(
            ExpressionFunctionName.with("function123")
                .setCaseSensitivity(ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY),
            "@@"
        );
    }

    // type name........................................................................................................

    @Override
    public String typeNamePrefix() {
        return ExpressionFunction.class.getSimpleName();
    }
}
