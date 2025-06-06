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

import walkingkooka.plugin.PluginSelector;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Contains the {@link ExpressionFunctionName} and some parameters in text form.
 */
public final class ExpressionFunctionSelector implements PluginSelectorLike<ExpressionFunctionName> {

    /**
     * Parses the given text into a {@link ExpressionFunctionSelector}
     */
    public static ExpressionFunctionSelector parse(final String text,
                                                   final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        return new ExpressionFunctionSelector(
            PluginSelector.parse(
                text,
                (n) -> ExpressionFunctionName.with(n)
                    .setCaseSensitivity(caseSensitivity)
            )
        );
    }

    /**
     * Factory that creates a new {@link ExpressionFunctionSelector}.
     */
    public static ExpressionFunctionSelector with(final ExpressionFunctionName name,
                                                  final String text) {
        return new ExpressionFunctionSelector(
            PluginSelector.with(
                name,
                text
            )
        );
    }

    private ExpressionFunctionSelector(final PluginSelector<ExpressionFunctionName> selector) {
        this.selector = selector;
    }

    // HasName..........................................................................................................

    @Override
    public ExpressionFunctionName name() {
        return this.selector.name();
    }

    /**
     * Would be setter that returns a {@link ExpressionFunctionSelector} with the given {@link ExpressionFunctionName},
     * creating a new instance if necessary.
     */
    @Override
    public ExpressionFunctionSelector setName(final ExpressionFunctionName name) {
        Objects.requireNonNull(name, "name");

        return this.name().equals(name) ?
            this :
            new ExpressionFunctionSelector(
                PluginSelector.with(
                    name,
                    this.valueText()
                )
            );
    }

    /**
     * Function parameters in text form.
     */
    @Override
    public String valueText() {
        return this.selector.valueText();
    }

    @Override
    public ExpressionFunctionSelector setValueText(final String valueText) {
        final PluginSelector<ExpressionFunctionName> different = this.selector.setValueText(valueText);
        return this.selector.equals(different) ?
            this :
            new ExpressionFunctionSelector(different);
    }

    private final PluginSelector<ExpressionFunctionName> selector;

    // setValue.........................................................................................................

    @Override
    public ExpressionFunctionSelector setValues(final List<?> values) {
        final PluginSelector<ExpressionFunctionName> different = this.selector.setValues(values);
        return this.selector.equals(different) ?
            this :
            new ExpressionFunctionSelector(different);
    }

    /**
     * Parses the text as an expression that may contain String literals, numbers or {@link ExpressionFunctionName}.
     */
    public <C extends ExpressionEvaluationContext> ExpressionFunction<?, C> evaluateValueText(final ExpressionFunctionProvider<C> provider,
                                                                                              final ProviderContext context) {
        Objects.requireNonNull(provider, "provider");
        Objects.requireNonNull(context, "context");

        return this.selector.evaluateValueText(
            this::parseName,
            provider::expressionFunction,
            context
        );
    }

    private Optional<ExpressionFunctionName> parseName(final TextCursor cursor,
                                                       final ParserContext context) {
        return ExpressionFunctionName.PARSER.apply(
            cursor,
            context
        ).map(n -> n.setCaseSensitivity(
                this.selector.name()
                    .caseSensitivity()
            )
        );
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.selector.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof ExpressionFunctionSelector && this.equals0((ExpressionFunctionSelector) other);
    }

    private boolean equals0(final ExpressionFunctionSelector other) {
        return this.selector.equals(other.selector);
    }

    /**
     * Note it is intentional that the {@link #text()} is not quoted, to ensure {@link #parse(String, CaseSensitivity)} and {@link #toString()}
     * are roundtrippable.
     */
    @Override
    public String toString() {
        return this.selector.toString();
    }

    // JsonNodeContext..................................................................................................

    /**
     * Factory that creates a {@link ExpressionFunctionSelector} from a {@link JsonNode}.
     */
    static ExpressionFunctionSelector unmarshall(final JsonNode node,
                                                 final JsonNodeUnmarshallContext context) {
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(context, "context");

        String string = node.stringOrFail();
        CaseSensitivity caseSensitivity = CaseSensitivity.SENSITIVE;

        if (string.charAt(0) == CASE_INSENSITIVE_PREFIX) {
            caseSensitivity = CaseSensitivity.INSENSITIVE;
            string = string.substring(1); // remove leading AT-SIGN
        }

        return parse(
            string,
            caseSensitivity
        );
    }

    /**
     * Marshall the {@link PluginSelector#text()} adding the {@link #CASE_INSENSITIVE_PREFIX} if it is {@link CaseSensitivity#INSENSITIVE}.
     */
    private JsonNode marshall(final JsonNodeMarshallContext context) {
        final PluginSelector<ExpressionFunctionName> selector = this.selector;

        String text = selector.text();
        if (selector.name().caseSensitivity() == CaseSensitivity.INSENSITIVE) {
            text = CASE_INSENSITIVE_PREFIX + text;
        }

        return JsonNode.string(text);
    }

    /**
     * If this character appears at the start of a selector {@link JsonNode} it will be considered {@link CaseSensitivity#INSENSITIVE}.
     */
    private final static char CASE_INSENSITIVE_PREFIX = '@';

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(ExpressionFunctionSelector.class),
            ExpressionFunctionSelector::unmarshall,
            ExpressionFunctionSelector::marshall,
            ExpressionFunctionSelector.class
        );
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.selector.printTree(printer);
    }
}
