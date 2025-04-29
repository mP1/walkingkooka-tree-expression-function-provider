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
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.FakeExpressionFunction;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MergedMappedExpressionFunctionProviderTest implements ExpressionFunctionProviderTesting<MergedMappedExpressionFunctionProvider<ExpressionEvaluationContext>, ExpressionEvaluationContext>,
    ToStringTesting<MergedMappedExpressionFunctionProvider<ExpressionEvaluationContext>> {

    private final static AbsoluteUrl RENAMED_URL = Url.parseAbsolute("https://example.com/renamed-function111");

    private final static ExpressionFunctionName RENAME_NAME = ExpressionFunctionName.with("rename-renamed-function-111")
        .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity);

    private final static ExpressionFunctionName RENAME_PROVIDER_NAME = ExpressionFunctionName.with("provider-renamed-function-111")
        .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity);

    private final static AbsoluteUrl PROVIDER_ONLY_URL = Url.parseAbsolute("https://example.com/provider-only-function-222");

    private final static ExpressionFunctionName PROVIDER_ONLY_NAME = ExpressionFunctionName.with("provider-only-function-222")
        .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity);

    private static ExpressionFunction<?, ExpressionEvaluationContext> function(final ExpressionFunctionName name) {
        return new FakeExpressionFunction<>() {
            @Override
            public Optional<ExpressionFunctionName> name() {
                return Optional.of(name);
            }

            @Override
            public int hashCode() {
                return name.hashCode();
            }

            @Override
            public boolean equals(final Object other) {
                return this == other || other instanceof ExpressionFunction && this.equals0((ExpressionFunction<?, ?>) other);
            }

            private boolean equals0(final ExpressionFunction<?, ?> other) {
                return this.name().equals(other.name());
            }

            @Override
            public String toString() {
                return name.toString();
            }
        };
    }

    private final static List<?> VALUES = Lists.empty();

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testWithNullInfosFails() {
        assertThrows(
            NullPointerException.class,
            () -> MergedMappedExpressionFunctionProvider.with(
                null,
                ExpressionFunctionProviders.fake()
            )
        );
    }

    @Test
    public void testWithNullProviderFails() {
        assertThrows(
            NullPointerException.class,
            () -> MergedMappedExpressionFunctionProvider.with(
                ExpressionFunctionInfoSet.EMPTY,
                null
            )
        );
    }

    @Test
    public void testExpressionFunctionNameRename() {
        this.expressionFunctionAndCheck(
            RENAME_NAME,
            VALUES,
            CONTEXT,
            function(RENAME_NAME)
        );
    }

    @Test
    public void testExpressionFunctionNameProviderOnly() {
        this.expressionFunctionAndCheck(
            PROVIDER_ONLY_NAME,
            VALUES,
            CONTEXT,
            function(PROVIDER_ONLY_NAME)
        );
    }

    @Test
    public void testExpressionFunctionNameUnknownFails() {
        this.expressionFunctionFails(
            ExpressionFunctionName.with("unknown")
                .setCaseSensitivity(ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity),
            VALUES,
            CONTEXT
        );
    }

    @Test
    public void testExpressionFunctionSelectorRenameName() {
        this.expressionFunctionAndCheck(
            ExpressionFunctionSelector.parse(RENAME_NAME + ""),
            CONTEXT,
            function(RENAME_NAME)
        );
    }

    @Test
    public void testExpressionFunctionSelectorProviderName() {
        this.expressionFunctionAndCheck(
            ExpressionFunctionSelector.parse(PROVIDER_ONLY_NAME + ""),
            CONTEXT,
            function(PROVIDER_ONLY_NAME)
        );
    }

    @Test
    public void testExpressionFunctionSelectorUnknownFails() {
        this.expressionFunctionFails(
            ExpressionFunctionSelector.parse("unknown"),
            CONTEXT
        );
    }

    @Test
    public void testInfos() {
        this.expressionFunctionInfosAndCheck(
            ExpressionFunctionInfo.with(
                RENAMED_URL,
                RENAME_NAME
            ),
            ExpressionFunctionInfo.with(
                PROVIDER_ONLY_URL,
                PROVIDER_ONLY_NAME
            )
        );
    }

    @Override
    public MergedMappedExpressionFunctionProvider createExpressionFunctionProvider() {
        return MergedMappedExpressionFunctionProvider.with(
            ExpressionFunctionInfoSet.with(
                Sets.of(
                    ExpressionFunctionInfo.with(
                        RENAMED_URL,
                        RENAME_NAME
                    )
                )
            ),
            new FakeExpressionFunctionProvider<>() {

                @Override
                public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name,
                                                                                             final List<?> values,
                                                                                             final ProviderContext context) {
                    if (name.equals(RENAME_PROVIDER_NAME)) {
                        return function(RENAME_PROVIDER_NAME);
                    }
                    if (name.equals(PROVIDER_ONLY_NAME)) {
                        return function(PROVIDER_ONLY_NAME);
                    }
                    throw new IllegalArgumentException("Unknown function " + name);
                }

                @Override
                public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionSelector selector,
                                                                                             final ProviderContext context) {
                    return selector.evaluateValueText(
                        this,
                        context
                    );
                }

                @Override
                public ExpressionFunctionInfoSet expressionFunctionInfos() {
                    return ExpressionFunctionInfoSet.with(
                        Sets.of(
                            ExpressionFunctionInfo.with(
                                RENAMED_URL,
                                RENAME_PROVIDER_NAME
                            ),
                            ExpressionFunctionInfo.with(
                                PROVIDER_ONLY_URL,
                                PROVIDER_ONLY_NAME
                            )
                        )
                    );
                }

                @Override
                public CaseSensitivity expressionFunctionNameCaseSensitivity() {
                    return ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity;
                }
            }
        );
    }

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return ExpressionFunctionPluginHelper.INSTANCE.caseSensitivity;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createExpressionFunctionProvider(),
            "https://example.com/provider-only-function-222 provider-only-function-222,https://example.com/renamed-function111 rename-renamed-function-111"
        );
    }

    // class............................................................................................................

    @Override
    public Class<MergedMappedExpressionFunctionProvider<ExpressionEvaluationContext>> type() {
        return Cast.to(MergedMappedExpressionFunctionProvider.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
