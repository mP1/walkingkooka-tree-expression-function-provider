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

import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Name;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.plugin.PluginHelper;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.UnknownExpressionFunctionException;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A {@link PluginHelper} that is hardcoded to use a {@link CaseSensitivity#INSENSITIVE} to match the expected behaviour of spreadsheet function name lookups.
 * <br>
 * TODO https://github.com/mP1/walkingkooka-tree-expression-function-provider/issues/118
 */
final class ExpressionFunctionPluginHelper implements PluginHelper<ExpressionFunctionName,
        ExpressionFunctionInfo,
        ExpressionFunctionInfoSet,
        ExpressionFunctionSelector,
        ExpressionFunctionAlias> {

    /**
     * Singleton
     */
    final static ExpressionFunctionPluginHelper INSTANCE = new ExpressionFunctionPluginHelper();

    private ExpressionFunctionPluginHelper() {
        super();
    }

    @Override
    public ExpressionFunctionName name(final String text) {
        return ExpressionFunctionName.with(text);
    }

    @Override
    public Optional<ExpressionFunctionName> parseName(final TextCursor cursor,
                                                      final ParserContext context) {
        return ExpressionFunctionName.PARSER.apply(
                cursor,
                context
        );
    }

    @Override
    public Set<ExpressionFunctionName> names(final Set<ExpressionFunctionName> names) {
        return Sets.immutable(names);
    }

    @Override
    public Comparator<ExpressionFunctionName> nameComparator() {
        return Name.comparator(CaseSensitivity.INSENSITIVE);
    }

    @Override
    public Function<ExpressionFunctionName, RuntimeException> unknownName() {
        return (n) -> new UnknownExpressionFunctionException(n);
    }

    @Override
    public ExpressionFunctionInfo parseInfo(final String text) {
        return ExpressionFunctionInfo.parse(text);
    }

    @Override
    public ExpressionFunctionInfo info(final AbsoluteUrl url,
                                       final ExpressionFunctionName name) {
        return ExpressionFunctionInfo.with(
                url,
                name
        );
    }

    @Override
    public ExpressionFunctionInfoSet infoSet(final Set<ExpressionFunctionInfo> infos) {
        return ExpressionFunctionInfoSet.with(infos);
    }

    @Override
    public ExpressionFunctionSelector parseSelector(final String text) {
        return ExpressionFunctionSelector.parse(text);
    }

    @Override
    public ExpressionFunctionAlias alias(final ExpressionFunctionName name,
                                         final Optional<ExpressionFunctionSelector> selector,
                                         final Optional<AbsoluteUrl> url) {
        return ExpressionFunctionAlias.with(
                name,
                selector,
                url
        );
    }

    @Override
    public String label() {
        return "Function";
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
