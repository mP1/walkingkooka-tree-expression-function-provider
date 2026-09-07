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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.UrlPath;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctions;

import java.util.List;
import java.util.Objects;

/**
 * A {@link ExpressionFunctionProvider} for {@link ExpressionFunctions}.
 */
final class TreeExpressionFunctionProvider<C extends ExpressionEvaluationContext> implements ExpressionFunctionProvider<C>,
    TreePrintable {

    /**
     * Type safe getter.
     */
    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionProvider<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionProvider<?> INSTANCE = new TreeExpressionFunctionProvider<>();

    private TreeExpressionFunctionProvider() {
        super();

        this.infos = ExpressionFunctionInfoSet.empty(CaseSensitivity.SENSITIVE)
            .setElements(
                Sets.of(
                    nameToExpressionFunctionInfo(ExpressionFunctions.node()),
                    nameToExpressionFunctionInfo(ExpressionFunctions.nodeName()),
                    nameToExpressionFunctionInfo(ExpressionFunctions.typeName())
                )
            );
    }

    private static ExpressionFunctionInfo nameToExpressionFunctionInfo(final ExpressionFunction<?, ?> function) {
        final ExpressionFunctionName name = function.name()
            .get();

        return ExpressionFunctionInfo.with(
            ExpressionFunctionProviders.BASE_URL.appendPath(
                UrlPath.parse(
                    name.value()
                )
            ),
            name
        );
    }

    @Override
    public ExpressionFunctionInfoSet expressionFunctionInfos() {
        return this.infos;
    }

    private final ExpressionFunctionInfoSet infos;

    @Override
    public ExpressionFunction<?, C> expressionFunction(final ExpressionFunctionSelector selector,
                                                       final ProviderContext context) {
        return selector.evaluateValueText(
            this,
            context
        );
    }

    @Override
    public ExpressionFunction<?, C> expressionFunction(final ExpressionFunctionName name,
                                                       final List<?> values,
                                                       final ProviderContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(context, "context");

        final List<?> copy = Lists.immutable(values);

        final ExpressionFunction<?, C> function;

        switch (name.value()) {
            case "name":
                this.noParameterCheck(copy);

                function = ExpressionFunctions.nodeName();
                break;
            case "node":
                this.noParameterCheck(copy);

                function = Cast.to(
                    ExpressionFunctions.node()
                );
                break;
            case "typeName":
                this.noParameterCheck(copy);

                function = ExpressionFunctions.typeName();
                break;
            default:
                throw name.unknownExpressionFunctionException();
        }

        return Cast.to(function);
    }

    @Override
    public CaseSensitivity expressionFunctionNameCaseSensitivity() {
        return ExpressionFunctionName.DEFAULT_CASE_SENSITIVITY;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        printer.indent();
        {
            TreePrintable.printTreeOrToString(
                this.expressionFunctionInfos(),
                printer
            );
        }
        printer.outdent();
    }
}
