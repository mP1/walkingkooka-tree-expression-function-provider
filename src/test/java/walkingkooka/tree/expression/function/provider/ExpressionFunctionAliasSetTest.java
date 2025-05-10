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
import walkingkooka.collect.set.Sets;
import walkingkooka.plugin.PluginAliasSetLikeTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

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

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;
    
    // with.............................................................................................................

    @Test
    public void testEmptyWithNullCaseSensitivityFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionAliasSet.empty(null)
        );
    }

    // name.............................................................................................................

    @Test
    public void testAliasOrNameWithName() {
        final ExpressionFunctionName abs = ExpressionFunctionName.with("abs")
            .setCaseSensitivity(CASE_SENSITIVITY);

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
                .setCaseSensitivity(CASE_SENSITIVITY),
            ExpressionFunctionName.with("sum")
                .setCaseSensitivity(CASE_SENSITIVITY)
        );
    }

    @Test
    public void testAliasSelectorWithName() {
        this.aliasSelectorAndCheck(
            this.createSet(),
            ExpressionFunctionName.with("abs")
                .setCaseSensitivity(CASE_SENSITIVITY)
        );
    }

    @Test
    public void testAliasSelectorWithAlias() {
        this.aliasSelectorAndCheck(
            this.createSet(),
            ExpressionFunctionName.with("custom-alias")
                .setCaseSensitivity(CASE_SENSITIVITY),
            ExpressionFunctionSelector.parse(
                "custom(1)",
                CASE_SENSITIVITY
            )
        );
    }

    @Test
    public void testSetElementsWithCaseSensitive() {
        this.setElementsAndCheck(CaseSensitivity.SENSITIVE);
    }

    @Test
    public void testSetElementsWithCaseInsensitive() {
        this.setElementsAndCheck(CaseSensitivity.INSENSITIVE);
    }

    private void setElementsAndCheck(final CaseSensitivity caseSensitivity) {
        final String text = "hello";

        final ExpressionFunctionAlias alias = ExpressionFunctionAlias.parse(
            text,
            caseSensitivity
        );

        final ExpressionFunctionAlias aliasOppositeCaseSensitivity = ExpressionFunctionAlias.parse(
            text,
            caseSensitivity.invert()
        );

        this.checkEquals(
            ExpressionFunctionAliasSet.empty(caseSensitivity)
                .setElements(
                    Sets.of(alias)
                ),
            ExpressionFunctionAliasSet.empty(caseSensitivity)
                .setElements(
                    Sets.of(aliasOppositeCaseSensitivity)
                )
        );
    }

    @Override
    public ExpressionFunctionAliasSet createSet() {
        return this.parseString("abs, min, max, custom-alias custom(1) https://example.com/custom , sum-alias sum");
    }

    // parse............................................................................................................

    @Test
    public void testParseWithCaseSensitive() {
        this.parseStringAndCheck2(CaseSensitivity.SENSITIVE);
    }

    @Test
    public void testParseWithCaseInsensitive() {
        this.parseStringAndCheck2(CaseSensitivity.INSENSITIVE);
    }

    private void parseStringAndCheck2(final CaseSensitivity caseSensitivity) {
        this.parseStringAndCheck(
            "hello",
            ExpressionFunctionAliasSet.empty(caseSensitivity)
                .setElements(
                    Sets.of(
                        ExpressionFunctionAlias.parse(
                            "hello",
                            caseSensitivity
                        )
                    )
                )
        );
    }

    @Override
    public ExpressionFunctionAliasSet parseString(final String text) {
        return ExpressionFunctionAliasSet.parse(
            text,
            CASE_SENSITIVITY
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
            this.parseString("different")
        );
    }

    @Override
    public ExpressionFunctionAliasSet createObject() {
        return this.parseString("abs, custom-alias custom(1) https://example.com/custom");
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
        return this.parseString("alias1 name1, name2, alias3 name3(\"999\") https://example.com/name3");
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionAliasSet> type() {
        return ExpressionFunctionAliasSet.class;
    }
}
