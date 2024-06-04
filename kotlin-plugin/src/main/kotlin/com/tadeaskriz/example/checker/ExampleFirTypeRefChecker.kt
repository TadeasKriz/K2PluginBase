package com.tadeaskriz.example.checker

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.type.FirTypeRefChecker
import org.jetbrains.kotlin.fir.render
import org.jetbrains.kotlin.fir.types.FirTypeRef

class ExampleFirTypeRefChecker: FirTypeRefChecker(MppCheckerKind.Platform) {
    override fun check(typeRef: FirTypeRef, context: CheckerContext, reporter: DiagnosticReporter) {
        println("[CHECK][TYPE]: ${typeRef.render()}")
    }

}
