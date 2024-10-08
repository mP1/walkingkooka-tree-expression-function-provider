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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.plugin.PluginAliasSetLikeTesting;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionAliasesTest implements PluginAliasSetLikeTesting<ExpressionFunctionName,
        ExpressionFunctionInfo,
        ExpressionFunctionInfoSet,
        ExpressionFunctionSelector,
        ExpressionFunctionAlias,
        ExpressionFunctionAliases>,
        HashCodeEqualsDefinedTesting2<ExpressionFunctionAliases>,
        ToStringTesting<ExpressionFunctionAliases>,
        JsonNodeMarshallingTesting<ExpressionFunctionAliases> {

    // with.............................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionAliases.with(null)
        );
    }

    @Test
    public void testWithEmpty() {
        assertSame(
                ExpressionFunctionAliases.EMPTY,
                ExpressionFunctionAliases.with(SortedSets.empty())
        );
    }

    // name.............................................................................................................

    @Test
    public void testNameWithName() {
        final ExpressionFunctionName abs = ExpressionFunctionName.with("abs");

        this.nameAndCheck(
                this.createSet(),
                abs,
                abs
        );
    }

    @Test
    public void testNameWithAlias() {
        this.nameAndCheck(
                this.createSet(),
                ExpressionFunctionName.with("sum-alias")
        );
    }

    @Test
    public void testAliasWithName() {
        this.aliasAndCheck(
                this.createSet(),
                ExpressionFunctionName.with("abs")
        );
    }

    @Test
    public void testAliasWithAlias() {
        this.aliasAndCheck(
                this.createSet(),
                ExpressionFunctionName.with("custom-alias"),
                ExpressionFunctionSelector.parse("custom(1)")
        );
    }

    @Override
    public ExpressionFunctionAliases createSet() {
        return ExpressionFunctionAliases.parse("abs, min, max, custom-alias custom(1) https://example.com/custom , sum-alias sum");
    }

    // parse............................................................................................................

    @Override
    public ExpressionFunctionAliases parseString(final String text) {
        return ExpressionFunctionAliases.parse(text);
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
                ExpressionFunctionAliases.parse("different")
        );
    }

    @Override
    public ExpressionFunctionAliases createObject() {
        return ExpressionFunctionAliases.parse("abs, custom-alias custom(1) https://example.com/custom");
    }

    // json.............................................................................................................

    @Override
    public ExpressionFunctionAliases unmarshall(final JsonNode json,
                                                final JsonNodeUnmarshallContext context) {
        return ExpressionFunctionAliases.unmarshall(
                json,
                context
        );
    }

    @Override
    public ExpressionFunctionAliases createJsonNodeMarshallingValue() {
        return ExpressionFunctionAliases.parse("alias1 name1, name2, alias3 name3(\"999\") https://example.com/name3");
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionAliases> type() {
        return ExpressionFunctionAliases.class;
    }
}
