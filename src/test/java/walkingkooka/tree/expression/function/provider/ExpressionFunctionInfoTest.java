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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.plugin.PluginInfoLikeTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

public final class ExpressionFunctionInfoTest implements PluginInfoLikeTesting<ExpressionFunctionInfo, ExpressionFunctionName> {

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    @Override
    public void testParseStaticMethod() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseInvalidCharacterInNameFails() {
        final String text = "https://example.com/123 test-function-!-name";

        this.parseStringInvalidCharacterFails(
            text,
            text.indexOf('!')
        );
    }

    @Test
    public void testSetNameWithDifferent() {
        final AbsoluteUrl url = Url.parseAbsolute("https://example/function123");
        final ExpressionFunctionName different = ExpressionFunctionName.with("different")
            .setCaseSensitivity(CASE_SENSITIVITY);

        this.setNameAndCheck(
            ExpressionFunctionInfo.with(
                url,
                ExpressionFunctionName.with("original-function-name")
                    .setCaseSensitivity(CASE_SENSITIVITY)
            ),
            different,
            ExpressionFunctionInfo.with(
                url,
                different
            )
        );
    }

    @Override
    public ExpressionFunctionName createName(final String name) {
        return ExpressionFunctionName.with(name);
    }

    @Override
    public ExpressionFunctionInfo createPluginInfoLike(final AbsoluteUrl url,
                                                       final ExpressionFunctionName name) {
        return ExpressionFunctionInfo.with(
            url,
            name
        );
    }

    @Override
    public ExpressionFunctionInfo parseString(final String text) {
        return ExpressionFunctionInfo.parse(
            text,
            CASE_SENSITIVITY
        );
    }

    @Override
    public ExpressionFunctionInfo unmarshall(final JsonNode json,
                                             final JsonNodeUnmarshallContext context) {
        return ExpressionFunctionInfo.unmarshall(
            json,
            context
        );
    }

    @Override
    public Class<ExpressionFunctionInfo> type() {
        return ExpressionFunctionInfo.class;
    }
}
