package com.tadeaskriz.example

import org.jetbrains.kotlin.diagnostics.DiagnosticFactory1DelegateProvider
import org.jetbrains.kotlin.diagnostics.Severity
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies

object ExamplePluginErrors {
    private val psiElementClass by lazy {
        try {
            Class.forName("com.intellij.psi.PsiElement")
        } catch (_: ClassNotFoundException) {
            Class.forName("org.jetbrains.kotlin.com.intellij.psi.PsiElement")
        }.kotlin
    }
//    val MISSING_EXAMPLE_ANNOTATION by error2<PsiElement, String, String>()
    val PASCAL_NOT_WELCOME by DiagnosticFactory1DelegateProvider<String>(
        severity = Severity.WARNING,
        positioningStrategy = SourceElementPositioningStrategies.DECLARATION_NAME,
        psiType = psiElementClass,
    )
}
