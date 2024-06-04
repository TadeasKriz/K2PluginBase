package com.tadeaskriz.example

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.extensions.FirExpressionResolutionExtension
import org.jetbrains.kotlin.fir.render
import org.jetbrains.kotlin.fir.types.ConeKotlinType

class ExampleFirExpressionResolutionExtension(session: FirSession): FirExpressionResolutionExtension(session) {
    override fun addNewImplicitReceivers(functionCall: FirFunctionCall): List<ConeKotlinType> {
        println("[EXPR]: ${functionCall.render()}")
        return emptyList()
    }
}
