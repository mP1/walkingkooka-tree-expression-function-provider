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
import walkingkooka.collect.list.ImmutableListTesting;
import walkingkooka.collect.list.ListTesting2;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.FunctionExpressionName;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionFunctionInfoListTest implements ImmutableListTesting<ExpressionFunctionInfoList, ExpressionFunctionInfo>,
        ClassTesting<ExpressionFunctionInfoList> {

    private final static ExpressionFunctionInfo INFO1 = ExpressionFunctionInfo.with(
            Url.parseAbsolute("https://example.com/function-1"),
            FunctionExpressionName.with("function1")
    );

    private final static ExpressionFunctionInfo INFO2 = ExpressionFunctionInfo.with(
            Url.parseAbsolute("https://example.com/function-2"),
            FunctionExpressionName.with("function2")
    );

    @Test
    public void testDoesntDoubleWrap() {
        final ExpressionFunctionInfoList list = this.createList();
        assertSame(
                list,
                ExpressionFunctionInfoList.with(list)
        );
    }

    @Test
    public void testGetIndex0() {
        this.getAndCheck(
                this.createList(),
                0, // index
               INFO1 // expected
        );
    }

    @Test
    public void testGetIndex1() {
        this.getAndCheck(
                this.createList(),
                1, // index
                INFO2 // expected
        );
    }

    @Test
    public void testSetFails() {
        this.setFails(
                this.createList(),
                0, // index
                ExpressionFunctionInfo.with(
                Url.parseAbsolute("https://example.com/function-set"),
                FunctionExpressionName.with("functionSet")
                )
        );
    }

    @Test
    public void testRemoveIndexFails() {
        final ExpressionFunctionInfoList list = this.createList();

        this.removeIndexFails(
                list,
                0
        );
    }

    @Test
    public void testRemoveElementFails() {
        final ExpressionFunctionInfoList list = this.createList();

        this.removeFails(
                list,
                list.get(0)
        );
    }

    @Test
    public void testSwap() {
        this.swapAndCheck(
                ExpressionFunctionInfoList.with(
                        Lists.of(
                                INFO1,
                                INFO2
                        )
                ),
                1,
                0,
                ExpressionFunctionInfoList.with(
                        Lists.of(
                                INFO2,
                                INFO1
                        )
                )
        );
    }

    @Override
    public ExpressionFunctionInfoList createList() {
        return Cast.to(
                ExpressionFunctionInfoList.with(
                        Lists.of(
                                INFO1,
                                INFO2
                        )
                )
        );
    }

    @Override
    public Class<ExpressionFunctionInfoList> type() {
        return ExpressionFunctionInfoList.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
