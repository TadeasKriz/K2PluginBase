@file:OptIn(FirExtensionApiInternals::class)

package com.tadeaskriz.example

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.extensions.FirExtensionApiInternals
import org.jetbrains.kotlin.fir.extensions.FirFunctionCallRefinementExtension
import org.jetbrains.kotlin.fir.resolve.calls.CallInfo
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol

// Experimental
class ExampleFirFunctionCallRefinementExtension(session: FirSession): FirFunctionCallRefinementExtension(session) {
    override fun intercept(
        callInfo: CallInfo,
        symbol: FirNamedFunctionSymbol
    ): CallReturnType? {
        println("[CALL][Intercept] $callInfo, $symbol")
        return null
    }

    override fun transform(
        call: FirFunctionCall,
        originalSymbol: FirNamedFunctionSymbol
    ): FirFunctionCall {
        println("[CALL][Transform] $call, $originalSymbol")
        return call
    }
}
