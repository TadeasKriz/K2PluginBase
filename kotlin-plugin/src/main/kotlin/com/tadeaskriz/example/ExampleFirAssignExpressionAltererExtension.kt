package com.tadeaskriz.example

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.fir.expressions.FirVariableAssignment
import org.jetbrains.kotlin.fir.extensions.FirAssignExpressionAltererExtension
import org.jetbrains.kotlin.fir.renderWithType

class ExampleFirAssignExpressionAltererExtension(session: FirSession): FirAssignExpressionAltererExtension(session) {
    override fun transformVariableAssignment(variableAssignment: FirVariableAssignment): FirStatement? {
        println("[TRANSFORM][ASSIGN]: ${variableAssignment.renderWithType()}")
        return null
    }
}
