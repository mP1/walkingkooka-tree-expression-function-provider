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

public final class ExpressionFunctionAliasSetTest implements PluginAliasSetLikeTesting<ExpressionFunctionName,
    ExpressionFunctionInfo,
    ExpressionFunctionInfoSet,
    ExpressionFunctionSelector,
    ExpressionFunctionAlias,
    ExpressionFunctionAliasSet>,
    HashCodeEqualsDefinedTesting2<ExpressionFunctionAliasSet>,
    ToStringTesting<ExpressionFunctionAliasSet>,
    JsonNodeMarshallingTesting<ExpressionFunctionAliasSet> {

    // with.............................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionAliasSet.with(null)
        );
    }

    @Test
    public void testWithEmpty() {
        assertSame(
            ExpressionFunctionAliasSet.EMPTY,
            ExpressionFunctionAliasSet.with(SortedSets.empty())
        );
    }

    // name.............................................................................................................

    @Test
    public void testAliasOrNameWithName() {
        final ExpressionFunctionName abs = ExpressionFunctionName.with("abs")
            .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity);

        this.aliasOrNameAndCheck(
            this.createSet(),
            abs,
            abs
        );
    }

    @Test
    public void testAliasOrNameWithAlias() {
        this.aliasOrNameAndCheck(
            this.createSet(),
            ExpressionFunctionName.with("sum-alias")
                .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity),
            ExpressionFunctionName.with("sum")
                .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity)
        );
    }

    @Test
    public void testAliasSelectorWithName() {
        this.aliasSelectorAndCheck(
            this.createSet(),
            ExpressionFunctionName.with("abs")
                .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity)
        );
    }

    @Test
    public void testAliasSelectorWithAlias() {
        this.aliasSelectorAndCheck(
            this.createSet(),
            ExpressionFunctionName.with("custom-alias")
                .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity),
            ExpressionFunctionSelector.parse("custom(1)")
        );
    }

    @Override
    public ExpressionFunctionAliasSet createSet() {
        return ExpressionFunctionAliasSet.parse("abs, min, max, custom-alias custom(1) https://example.com/custom , sum-alias sum");
    }

    // parse............................................................................................................

    @Override
    public ExpressionFunctionAliasSet parseString(final String text) {
        return ExpressionFunctionAliasSet.parse(text);
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
            ExpressionFunctionAliasSet.parse("different")
        );
    }

    @Override
    public ExpressionFunctionAliasSet createObject() {
        return ExpressionFunctionAliasSet.parse("abs, custom-alias custom(1) https://example.com/custom");
    }

    // json.............................................................................................................

    @Override
    public ExpressionFunctionAliasSet unmarshall(final JsonNode json,
                                                 final JsonNodeUnmarshallContext context) {
        return ExpressionFunctionAliasSet.unmarshall(
            json,
            context
        );
    }

    @Override
    public ExpressionFunctionAliasSet createJsonNodeMarshallingValue() {
        return ExpressionFunctionAliasSet.parse("alias1 name1, name2, alias3 name3(\"999\") https://example.com/name3");
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionAliasSet> type() {
        return ExpressionFunctionAliasSet.class;
    }
}
