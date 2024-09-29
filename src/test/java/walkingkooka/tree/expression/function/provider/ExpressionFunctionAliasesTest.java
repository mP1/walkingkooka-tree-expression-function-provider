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
import walkingkooka.plugin.PluginAliasesLikeTesting;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

public final class ExpressionFunctionAliasesTest implements PluginAliasesLikeTesting<ExpressionFunctionAliases, ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector>,
        HashCodeEqualsDefinedTesting2<ExpressionFunctionAliases>,
        ToStringTesting<ExpressionFunctionAliases>,
        JsonNodeMarshallingTesting<ExpressionFunctionAliases> {

    @Test
    public void testNameWithName() {
        final ExpressionFunctionName abs = ExpressionFunctionName.with("abs");

        this.nameAndCheck(
                this.createPluginAliases(),
                abs,
                abs
        );
    }

    @Test
    public void testNameWithAlias() {
        this.nameAndCheck(
                this.createPluginAliases(),
                ExpressionFunctionName.with("sum-alias")
        );
    }

    @Test
    public void testAliasWithName() {
        this.aliasAndCheck(
                this.createPluginAliases(),
                ExpressionFunctionName.with("abs")
        );
    }

    @Test
    public void testAliasWithAlias() {
        this.aliasAndCheck(
                this.createPluginAliases(),
                ExpressionFunctionName.with("custom-alias"),
                ExpressionFunctionSelector.parse("custom(1)")
        );
    }

    @Test
    public void testNames() {
        this.namesAndCheck(
                this.createPluginAliases(),
                ExpressionFunctionName.with("abs"),
                ExpressionFunctionName.with("min"),
                ExpressionFunctionName.with("max"),
                ExpressionFunctionName.with("custom"),
                ExpressionFunctionName.with("sum")
        );
    }

    @Test
    public void testAliases() {
        this.aliasesAndCheck(
                this.createPluginAliases(),
                ExpressionFunctionName.with("sum-alias")
        );
    }

    @Test
    public void testInfos() {
        this.infosAndCheck(
                this.createPluginAliases(),
                ExpressionFunctionInfo.parse("https://example.com/custom custom-alias")
        );
    }

    @Override
    public ExpressionFunctionAliases createPluginAliases() {
        return ExpressionFunctionAliases.parse("abs, min, max, custom-alias custom(1) https://example.com/custom , sum-alias sum");
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

    // toString...........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                ExpressionFunctionAliases.parse("sum, custom-alias custom(1) https://example.com/custom , abs, min"),
                "abs, custom-alias custom(1) https://example.com/custom , min, sum"
        );
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
