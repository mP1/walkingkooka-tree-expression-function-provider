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

import walkingkooka.collect.list.ImmutableList;
import walkingkooka.collect.list.ImmutableListDefaults;
import walkingkooka.collect.list.Lists;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

/**
 * {@link List} that holds multiple {@link ExpressionFunctionInfo} and is the container for an API returning all functions.
 */
public final class ExpressionFunctionInfoList extends AbstractList<ExpressionFunctionInfo>
        implements ImmutableListDefaults<ExpressionFunctionInfoList, ExpressionFunctionInfo> {
    public static ExpressionFunctionInfoList with(final List<ExpressionFunctionInfo> infos) {
        Objects.requireNonNull(infos, "infos");

        return infos instanceof ExpressionFunctionInfoList ?
                (ExpressionFunctionInfoList) infos :
                copyAndCreate(infos);
    }

    static ExpressionFunctionInfoList copyAndCreate(final List<ExpressionFunctionInfo> infos) {
        final ExpressionFunctionInfo[] copy = infos.toArray(
                new ExpressionFunctionInfo[infos.size()]
        );

        if (copy.length == 0) {
            throw new IllegalArgumentException("Expected several function got 0");
        }

        return new ExpressionFunctionInfoList(copy);
    }

    private ExpressionFunctionInfoList(final ExpressionFunctionInfo[] infos) {
        this.infos = infos;
    }

    @Override
    public ExpressionFunctionInfo get(final int index) {
        return this.infos[index];
    }

    @Override
    public int size() {
        return this.infos.length;
    }

    private final ExpressionFunctionInfo[] infos;

    // ImmutableList....................................................................................................

    @Override
    public ExpressionFunctionInfoList setElements(final List<ExpressionFunctionInfo> nodes) {
        final ExpressionFunctionInfoList copy = with(nodes);
        return this.equals(copy) ?
                this :
                copy;
    }
}
