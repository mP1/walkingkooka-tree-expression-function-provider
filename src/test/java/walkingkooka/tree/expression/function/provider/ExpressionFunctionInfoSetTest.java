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
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionFunctionInfoSetTest implements PluginInfoSetLikeTesting<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet>,
    ClassTesting<ExpressionFunctionInfoSet> {

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    @Override
    public void testEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEmptyConcatText() {
        throw new UnsupportedOperationException();
    }

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
                .setCaseSensitivity(CASE_SENSITIVITY)
        );

        final ExpressionFunctionInfo info2 = ExpressionFunctionInfo.with(
            Url.parseAbsolute("https://example.com/function2"),
            ExpressionFunctionName.with("function2")
                .setCaseSensitivity(CASE_SENSITIVITY)
        );

        final ExpressionFunctionInfo info3 = ExpressionFunctionInfo.with(
            Url.parseAbsolute("https://example.com/function3"),
            ExpressionFunctionName.with("function3")
                .setCaseSensitivity(CASE_SENSITIVITY)
        );

        this.deleteAndCheck(
            ExpressionFunctionInfoSet.empty(CASE_SENSITIVITY)
                .setElements(
                    Sets.of(
                        info1,
                        info2,
                        info3
                    )
                ),
            info1,
            ExpressionFunctionInfoSet.empty(CASE_SENSITIVITY)
                .setElements(
                    Sets.of(
                        info2,
                        info3
                    )
                )
        );
    }

    @Test
    public void testSetElementsWithEmpty() {
        assertSame(
            ExpressionFunctionInfoSet.empty(CASE_SENSITIVITY),
            ExpressionFunctionInfoSet.empty(CASE_SENSITIVITY).setElements(
                Sets.empty()
            )
        );
    }

    // parse............................................................................................................

    @Override
    public ExpressionFunctionInfoSet parseString(final String text) {
        return ExpressionFunctionInfoSet.parse(
            text,
            CASE_SENSITIVITY
        );
    }

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    // Set..............................................................................................................

    @Override
    public ExpressionFunctionInfoSet createSet() {
        return ExpressionFunctionInfoSet.empty(CASE_SENSITIVITY)
            .setElements(
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
    public void testMarshallEmptyCaseSensitive() {
        this.marshallAndCheck(
            ExpressionFunctionInfoSet.empty(CaseSensitivity.SENSITIVE),
            JsonNode.array()
        );
    }

    @Test
    public void testMarshallEmptyCaseInsensitive() {
        this.marshallAndCheck(
            ExpressionFunctionInfoSet.empty(CaseSensitivity.INSENSITIVE),
            JsonNode.parse("[ \"@\" ]")
        );
    }

    @Test
    public void testMarshallNotEmptyCaseSensitive() {
        final CaseSensitivity caseSensitivity = CaseSensitivity.SENSITIVE;

        final ExpressionFunctionInfoSet set = ExpressionFunctionInfoSet.empty(caseSensitivity)
            .setElements(
                Sets.of(
                    ExpressionFunctionInfo.with(
                        Url.parseAbsolute("https://example.com/1"),
                        ExpressionFunctionName.with("test-function-1")
                            .setCaseSensitivity(caseSensitivity)
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

    @Test
    public void testMarshallNotEmptyCaseInsensitive() {
        final ExpressionFunctionInfoSet set = ExpressionFunctionInfoSet.empty(CaseSensitivity.INSENSITIVE)
            .setElements(
                Sets.of(
                    ExpressionFunctionInfo.with(
                        Url.parseAbsolute("https://example.com/1"),
                        ExpressionFunctionName.with("test-function-1")
                            .setCaseSensitivity(CaseSensitivity.INSENSITIVE)
                    )
                )
            );

        this.marshallAndCheck(
            set,
            "[\n" +
                "  \"@https://example.com/1 test-function-1\"\n" +
                "]"
        );
    }

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
        return ExpressionFunctionInfoSet.empty(CASE_SENSITIVITY)
            .setElements(
                Sets.of(
                    ExpressionFunctionInfo.with(
                        Url.parseAbsolute("https://example.com/test-function-1"),
                        ExpressionFunctionName.with("test-function-1")
                            .setCaseSensitivity(CASE_SENSITIVITY)
                    ),
                    ExpressionFunctionInfo.with(
                        Url.parseAbsolute("https://example.com/test-function-2"),
                        ExpressionFunctionName.with("test-function-2")
                            .setCaseSensitivity(CASE_SENSITIVITY)
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
