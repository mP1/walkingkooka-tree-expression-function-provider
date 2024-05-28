[![Build Status](https://github.com/mP1/walkingkooka-tree-expression-function-provider/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-tree-expression-function-provider/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-tree-expression-function-provider/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-tree-expression-function-provider?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-tree-expression-function-provider.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree-expression-function-provider/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-tree-expression-function-provider.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree-expression-function-provider/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

## Background

Minimal glue that defines a provider for ExpressionFunctions.

Ideally `ExpressionFunctionProvider` would live in [walkingkooka-tree](https://github.com/mP1/walkingkooka-tree) together
with `ExpressionFunction` but this is NOT possible because of a dependency on [walkingkooka-net](https://github.com/mP1/walkingkooka-net) which contains `AbsoluteUrl`.
In short its a chicken and egg problem.