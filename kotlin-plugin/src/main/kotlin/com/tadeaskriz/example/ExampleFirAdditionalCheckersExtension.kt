package com.tadeaskriz.example

import com.tadeaskriz.example.checker.ExampleFirBasicDeclarationChecker
import com.tadeaskriz.example.checker.ExampleFirBasicExpressionChecker
import com.tadeaskriz.example.checker.ExampleFirTypeRefChecker
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirBasicDeclarationChecker
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirBasicExpressionChecker
import org.jetbrains.kotlin.fir.analysis.checkers.type.FirTypeRefChecker
import org.jetbrains.kotlin.fir.analysis.checkers.type.TypeCheckers
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension

class ExampleFirAdditionalCheckersExtension(session: FirSession): FirAdditionalCheckersExtension(session) {
    override val declarationCheckers: DeclarationCheckers = object: DeclarationCheckers() {
        override val basicDeclarationCheckers: Set<FirBasicDeclarationChecker> = setOf(
            ExampleFirBasicDeclarationChecker()
        )
    }

    override val expressionCheckers: ExpressionCheckers = object: ExpressionCheckers() {
        override val basicExpressionCheckers: Set<FirBasicExpressionChecker> = setOf(
            ExampleFirBasicExpressionChecker()
        )
    }

    override val typeCheckers: TypeCheckers = object: TypeCheckers() {
        override val typeRefCheckers: Set<FirTypeRefChecker> = setOf(
            ExampleFirTypeRefChecker()
        )
    }
}
