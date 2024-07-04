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
import walkingkooka.plugin.PluginInfoLikeTesting;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

public final class ExpressionFunctionInfoTest implements PluginInfoLikeTesting<ExpressionFunctionInfo, FunctionExpressionName> {

    @Test
    public void testParseInvalidCharacterInNameFails() {
        final String text = "https://example.com/123 test-function-!-name";

        this.parseStringInvalidCharacterFails(
                text,
                text.indexOf('!')
        );
    }

    @Override
    public FunctionExpressionName createName(final String name) {
        return FunctionExpressionName.with(name);
    }

    @Override
    public ExpressionFunctionInfo createPluginInfoLike(final AbsoluteUrl url,
                                                       final FunctionExpressionName name) {
        return ExpressionFunctionInfo.with(
                url,
                name
        );
    }

    @Override
    public ExpressionFunctionInfo parseString(final String text) {
        return ExpressionFunctionInfo.parse(text);
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
