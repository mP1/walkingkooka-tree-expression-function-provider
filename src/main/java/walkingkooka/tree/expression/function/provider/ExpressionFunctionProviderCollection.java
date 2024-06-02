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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link ExpressionFunctionProvider} that accepts several {@link ExpressionFunctionProvider} and provides a unified {@link ExpressionFunctionProvider}.
 */
final class ExpressionFunctionProviderCollection implements ExpressionFunctionProvider {

    static ExpressionFunctionProvider with(final Set<ExpressionFunctionProvider> providers) {
        final Set<ExpressionFunctionProvider> copy = Sets.immutable(
                Objects.requireNonNull(providers, "providers")
        );

        ExpressionFunctionProvider provider;

        switch (providers.size()) {
            case 0:
                throw new IllegalArgumentException("Providers cannot be empty");
            case 1:
                provider = providers.iterator()
                        .next();
                break;
            default:
                provider = new ExpressionFunctionProviderCollection(copy);
                break;
        }

        return provider;
    }

    private ExpressionFunctionProviderCollection(final Set<ExpressionFunctionProvider> providers) {


        final Map<FunctionExpressionName, ExpressionFunction<?, ExpressionEvaluationContext>> nameToFunction = Maps.sorted();
        final Set<ExpressionFunctionInfo> infos = Sets.sorted();

        final Map<FunctionExpressionName, List<ExpressionFunctionInfo>> nameToInfos = Maps.sorted();

        for (final ExpressionFunctionProvider provider : providers) {
            for (final ExpressionFunctionInfo info : provider.expressionFunctionInfos()) {
                infos.add(info);

                final FunctionExpressionName name = info.name();
                final Optional<ExpressionFunction<?, ExpressionEvaluationContext>> function = provider.expressionFunction(name);
                nameToFunction.put(
                        name,
                        function.orElseThrow(() -> new IllegalStateException("Missing expressionFunction " + name)) // shouldnt happen
                );

                List<ExpressionFunctionInfo> infoForName = nameToInfos.get(name);
                if (null == infoForName) {
                    infoForName = Lists.array();
                    nameToInfos.put(
                            name,
                            infoForName
                    );
                }
                infoForName.add(info);
            }
        }

        // check for names mapped to multiple functions.
        final StringBuilder m = new StringBuilder();
        String separator = "";

        for (final Entry<FunctionExpressionName, List<ExpressionFunctionInfo>> nameAndInfo : nameToInfos.entrySet()) {
            final List<ExpressionFunctionInfo> infosForName = nameAndInfo.getValue();
            if (infosForName.size() > 1) {
                m.append(separator);
                separator = ", ";

                m.append(
                        nameAndInfo.getKey() +
                                " (" +
                                infosForName.stream()
                                        .map(i -> i.url().toString())
                                        .collect(Collectors.joining(", ")) +
                                ")"
                );
            }
        }
        if (m.length() > 0) {
            throw new IllegalArgumentException(m.toString()); // Custom Exception holding name to infos ?
        }

        this.nameToFunction = nameToFunction;
        this.expressionFunctionInfos = Sets.readOnly(infos);
    }

    @Override
    public Optional<ExpressionFunction<?, ExpressionEvaluationContext>> expressionFunction(final FunctionExpressionName name) {
        Objects.requireNonNull(name, "name");

        return Optional.ofNullable(
                this.nameToFunction.get(name)
        );
    }

    private final Map<FunctionExpressionName, ExpressionFunction<?, ExpressionEvaluationContext>> nameToFunction;

    @Override
    public Set<ExpressionFunctionInfo> expressionFunctionInfos() {
        return this.expressionFunctionInfos;
    }

    private final Set<ExpressionFunctionInfo> expressionFunctionInfos;

    @Override
    public String toString() {
        return this.nameToFunction.keySet()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }
}
