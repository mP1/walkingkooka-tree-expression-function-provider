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
import walkingkooka.net.Url;
import walkingkooka.plugin.PluginInfoSetLikeTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionFunctionInfoSetTest implements PluginInfoSetLikeTesting<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet>,
        ClassTesting<ExpressionFunctionInfoSet> {

    @Test
    public void testImmutableSet() {
        final ExpressionFunctionInfoSet set = this.createSet();

        assertSame(
                set,
                Sets.immutable(set)
        );
    }

    @Test
    public void testDelete() {
        final ExpressionFunctionInfo info1 = ExpressionFunctionInfo.with(
                Url.parseAbsolute("https://example.com/function1"),
                ExpressionFunctionName.with("function1")
        );

        final ExpressionFunctionInfo info2 = ExpressionFunctionInfo.with(
                Url.parseAbsolute("https://example.com/function2"),
                ExpressionFunctionName.with("function2")
        );

        final ExpressionFunctionInfo info3 = ExpressionFunctionInfo.with(
                Url.parseAbsolute("https://example.com/function3"),
                ExpressionFunctionName.with("function3")
        );

        this.deleteAndCheck(
                ExpressionFunctionInfoSet.with(
                        Sets.of(
                                info1,
                                info2,
                                info3
                        )
                ),
                info1,
                ExpressionFunctionInfoSet.with(
                        Sets.of(
                                info2,
                                info3
                        )
                )
        );
    }

    // parse............................................................................................................

    @Override
    public ExpressionFunctionInfoSet parseString(final String text) {
        return ExpressionFunctionInfoSet.parse(text);
    }

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    // Set..............................................................................................................

    @Override
    public ExpressionFunctionInfoSet createSet() {
        return ExpressionFunctionInfoSet.with(
                Sets.of(
                        this.info()
                )
        );
    }

    @Override
    public ExpressionFunctionInfo info() {
        return ExpressionFunctionInfo.with(
                Url.parseAbsolute("https://example.com/test-function-1"),
                ExpressionFunctionName.with("test-function-1")
        );
    }

    // json.............................................................................................................

    @Test
    public void testMarshallEmpty() {
        this.marshallAndCheck(
                ExpressionFunctionInfoSet.with(Sets.empty()),
                JsonNode.array()
        );
    }

    @Test
    public void testMarshallNotEmpty() {
        final ExpressionFunctionInfoSet set = ExpressionFunctionInfoSet.with(
                Sets.of(
                        ExpressionFunctionInfo.with(
                                Url.parseAbsolute("https://example.com/1"),
                                ExpressionFunctionName.with("test-function-1")
                        )
                )
        );

        this.marshallAndCheck(
                set,
                "[\n" +
                        "  \"https://example.com/1 test-function-1\"\n" +
                        "]"
        );
    }

    // json............................................................................................................

    @Override
    public ExpressionFunctionInfoSet unmarshall(final JsonNode node,
                                                final JsonNodeUnmarshallContext context) {
        return ExpressionFunctionInfoSet.unmarshall(
                node,
                context
        );
    }

    @Override
    public ExpressionFunctionInfoSet createJsonNodeMarshallingValue() {
        return ExpressionFunctionInfoSet.with(
                Sets.of(
                        ExpressionFunctionInfo.with(
                                Url.parseAbsolute("https://example.com/test-function-1"),
                                ExpressionFunctionName.with("test-function-1")
                        ),
                        ExpressionFunctionInfo.with(
                                Url.parseAbsolute("https://example.com/test-function-2"),
                                ExpressionFunctionName.with("test-function-2")
                        )
                )
        );
    }

    // Class............................................................................................................

    @Override
    public Class<ExpressionFunctionInfoSet> type() {
        return ExpressionFunctionInfoSet.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
