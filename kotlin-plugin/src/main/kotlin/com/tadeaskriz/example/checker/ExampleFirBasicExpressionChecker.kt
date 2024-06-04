package com.tadeaskriz.example.checker

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirBasicExpressionChecker
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.fir.render

class ExampleFirBasicExpressionChecker: FirBasicExpressionChecker(MppCheckerKind.Platform) {
    override fun check(expression: FirStatement, context: CheckerContext, reporter: DiagnosticReporter) {
        println("[CHECK][EXPR]: ${expression.render()}")

    }
}
