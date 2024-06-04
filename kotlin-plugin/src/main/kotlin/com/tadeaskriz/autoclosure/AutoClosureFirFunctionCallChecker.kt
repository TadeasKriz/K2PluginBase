package com.tadeaskriz.autoclosure

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirFunctionCallChecker
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.expressions.FirAnonymousFunctionExpression
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.FirReturnExpression
import org.jetbrains.kotlin.fir.expressions.arguments
import org.jetbrains.kotlin.fir.extensions.FirExtensionApiInternals
import org.jetbrains.kotlin.fir.references.symbol
import org.jetbrains.kotlin.fir.render
import org.jetbrains.kotlin.fir.symbols.impl.FirFunctionSymbol
import org.jetbrains.kotlin.fir.types.resolvedType
import org.jetbrains.kotlin.fir.types.returnType
import org.jetbrains.kotlin.diagnostics.DiagnosticFactory1DelegateProvider
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies
import org.jetbrains.kotlin.diagnostics.Severity
import org.jetbrains.kotlin.fir.types.renderReadableWithFqNames

class AutoClosureFirFunctionCallChecker: FirFunctionCallChecker(MppCheckerKind.Common) {
    @OptIn(FirExtensionApiInternals::class)
    override fun check(
        expression: FirFunctionCall,
        context: CheckerContext,
        reporter: DiagnosticReporter
    ) {
        val functionSymbol = expression.calleeReference.symbol as? FirFunctionSymbol<*> ?: return

        functionSymbol.valueParameterSymbols.forEachIndexed { index, parameter ->
            if (parameter.hasAnnotation(AutoClosureFirFunctionCallRefinementExtension.autoClosure, context.session)) {
                val argument = expression.arguments[index]
                val anonymousFunctionExpression = argument as? FirAnonymousFunctionExpression ?: error("Expected argument $argument to be FirAnonymousFunctionExpression")
                val lastStatement = anonymousFunctionExpression.anonymousFunction.body?.statements?.lastOrNull() ?: error("Expected argument $argument to have at least one statement")
                val returnExpression = lastStatement as? FirReturnExpression ?: error("Expected argument $argument to have a return expression statement")

                val actualType = returnExpression.result.resolvedType
                val expectedType = parameter.resolvedReturnType.returnType(context.session)
                if (actualType != expectedType) {
                    reporter.reportOn(
                        argument.source,
                        incompatibleAutoClosureType,
                        "Argument type mismatch: actual type is '${actualType.renderReadableWithFqNames()}', but '${expectedType.renderReadableWithFqNames()}' was expected.",
                        context
                    )
                }
            }
        }
    }

    companion object {
        private val psiElementClass by lazy {
            try {
                Class.forName("com.intellij.psi.PsiElement")
            } catch (_: ClassNotFoundException) {
                Class.forName("org.jetbrains.kotlin.com.intellij.psi.PsiElement")
            }.kotlin
        }
        //    val MISSING_EXAMPLE_ANNOTATION by error2<PsiElement, String, String>()
        val incompatibleAutoClosureType by DiagnosticFactory1DelegateProvider<String>(
            severity = Severity.ERROR,
            positioningStrategy = SourceElementPositioningStrategies.DEFAULT,
            psiType = psiElementClass,
        )
    }
}
