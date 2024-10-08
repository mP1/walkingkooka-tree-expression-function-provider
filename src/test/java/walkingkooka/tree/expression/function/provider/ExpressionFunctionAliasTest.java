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
import walkingkooka.plugin.PluginAliasLikeTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionAliasTest implements PluginAliasLikeTesting<ExpressionFunctionName, ExpressionFunctionSelector, ExpressionFunctionAlias> {

    private final static ExpressionFunctionName NAME = ExpressionFunctionName.with("Hello");

    private final static Optional<ExpressionFunctionSelector> SELECTOR = Optional.of(
            ExpressionFunctionSelector.parse("function123")
    );

    private final static Optional<AbsoluteUrl> URL = Optional.of(
            Url.parseAbsolute("https://example.com/function123")
    );

    // with.............................................................................................................

    @Test
    public void testWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionAlias.with(
                        null,
                        SELECTOR,
                        URL
                )
        );
    }

    @Test
    public void testWithNullSelectorFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionAlias.with(
                        NAME,
                        null,
                        URL
                )
        );
    }

    @Test
    public void testWithNullUrlFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionAlias.with(
                        NAME,
                        SELECTOR,
                        null
                )
        );
    }

    // Comparable.......................................................................................................

    @Override
    public ExpressionFunctionAlias createComparable() {
        return ExpressionFunctionAlias.with(
                NAME,
                SELECTOR,
                URL
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionAlias> type() {
        return ExpressionFunctionAlias.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}