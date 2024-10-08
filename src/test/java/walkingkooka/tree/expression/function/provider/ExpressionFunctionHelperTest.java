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

import walkingkooka.plugin.PluginHelperTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionFunctionName;

public final class ExpressionFunctionHelperTest implements PluginHelperTesting<ExpressionFunctionHelper, ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector> {

    @Override
    public void testParseNameWithNullContextFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionFunctionHelper createPluginHelper() {
        return ExpressionFunctionHelper.INSTANCE;
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunctionHelper> type() {
        return ExpressionFunctionHelper.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}