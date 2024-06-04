package com.tadeaskriz.logger

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.arguments
import org.jetbrains.kotlin.fir.expressions.buildResolvedArgumentList
import org.jetbrains.kotlin.fir.expressions.builder.buildFunctionCall
import org.jetbrains.kotlin.fir.extensions.FirExtensionApiInternals
import org.jetbrains.kotlin.fir.extensions.FirFunctionCallRefinementExtension
import org.jetbrains.kotlin.fir.references.builder.buildResolvedNamedReference
import org.jetbrains.kotlin.fir.resolve.calls.CallInfo
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

@FirExtensionApiInternals
class LoggerFirFunctionCallRefinementExtension(session: FirSession): FirFunctionCallRefinementExtension(session) {
    val logVerbose by lazy {
        session.symbolProvider.getTopLevelFunctionSymbols(FqName.ROOT, Name.identifier("logVerbose")).single()
    }

    override fun intercept(
        callInfo: CallInfo,
        symbol: FirNamedFunctionSymbol
    ): CallReturnType? {
        return if (symbol.name.asString() == "println" && callInfo.containingFile.name != "Logger.kt") {
            CallReturnType(
                typeRef = logVerbose.resolvedReturnTypeRef,
                callback = { symbol -> }
            )
        } else {
            null
        }
    }

    @OptIn(SymbolInternals::class)
    override fun transform(
        call: FirFunctionCall,
        originalSymbol: FirNamedFunctionSymbol
    ): FirFunctionCall {
        val logVerbose = logVerbose
        val parameter = logVerbose.valueParameterSymbols[0]

        val newCall = buildFunctionCall {
            this.source = call.source
            this.coneTypeOrNull = logVerbose.resolvedReturnType
            this.argumentList = buildResolvedArgumentList(original = call.argumentList, linkedMapOf(call.arguments[0] to parameter.fir))
            this.calleeReference = buildResolvedNamedReference {
                source = originalSymbol.source
                name = Name.identifier("logVerbose")
                resolvedSymbol = logVerbose
            }
        }
        return newCall
    }
}
