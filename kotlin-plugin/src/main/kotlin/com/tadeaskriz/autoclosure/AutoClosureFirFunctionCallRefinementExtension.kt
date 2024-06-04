package com.tadeaskriz.autoclosure

import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.contracts.description.EventOccurrencesRange
import org.jetbrains.kotlin.descriptors.EffectiveVisibility
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.EmptyDeprecationsProvider
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.declarations.FirResolvePhase
import org.jetbrains.kotlin.fir.declarations.InlineStatus
import org.jetbrains.kotlin.fir.declarations.builder.buildAnonymousFunction
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.declarations.impl.FirResolvedDeclarationStatusImpl
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.buildResolvedArgumentList
import org.jetbrains.kotlin.fir.expressions.builder.buildAnonymousFunctionExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildBlock
import org.jetbrains.kotlin.fir.expressions.builder.buildFunctionCall
import org.jetbrains.kotlin.fir.extensions.FirExtensionApiInternals
import org.jetbrains.kotlin.fir.extensions.FirFunctionCallRefinementExtension
import org.jetbrains.kotlin.fir.moduleData
import org.jetbrains.kotlin.fir.references.builder.buildResolvedNamedReference
import org.jetbrains.kotlin.fir.resolve.addReturnToLastStatementIfNeeded
import org.jetbrains.kotlin.fir.resolve.calls.CallInfo
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirAnonymousFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.types.builder.buildResolvedTypeRef
import org.jetbrains.kotlin.fir.types.resolvedType
import org.jetbrains.kotlin.fir.types.returnType
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

@FirExtensionApiInternals
class AutoClosureFirFunctionCallRefinementExtension(session: FirSession): FirFunctionCallRefinementExtension(session) {
    companion object {
        val autoClosure = ClassId(FqName.ROOT, Name.identifier("AutoClosure"))
    }

    override fun intercept(
        callInfo: CallInfo,
        symbol: FirNamedFunctionSymbol
    ): CallReturnType? {
        return if (symbol.valueParameterSymbols.any { it.hasAnnotation(autoClosure, session) }) {
            CallReturnType(
                typeRef = symbol.resolvedReturnTypeRef,
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
        val autoclosureParameters = originalSymbol.valueParameterSymbols.mapIndexed { index, parameter ->
            if (parameter.hasAnnotation(autoClosure, session)) {
                val returnType = parameter.resolvedReturnType.returnType(session)

                buildAnonymousFunctionExpression {
                    val fSymbol = FirAnonymousFunctionSymbol()
                    source = call.argumentList.arguments[index].source
                    isTrailingLambda = false
                    anonymousFunction = buildAnonymousFunction {
                        resolvePhase = FirResolvePhase.BODY_RESOLVE
                        moduleData = session.moduleData
                        origin = FirDeclarationOrigin.Plugin(Key)
                        status = FirResolvedDeclarationStatusImpl(
                            Visibilities.Local,
                            Modality.FINAL,
                            EffectiveVisibility.Local
                        )
                        deprecationsProvider = EmptyDeprecationsProvider
                        returnTypeRef = buildResolvedTypeRef {
                            type = returnType
                        }

                        this.symbol = fSymbol
                        isLambda = true
                        hasExplicitParameterList = false
                        typeRef = parameter.resolvedReturnTypeRef
                        invocationKind = EventOccurrencesRange.EXACTLY_ONCE
                        inlineStatus = InlineStatus.Unknown
                        body = buildBlock() {
                            this.coneTypeOrNull = returnType
                            statements += call.argumentList.arguments[index]
                        }
                    }.also {
                        it.addReturnToLastStatementIfNeeded(session)
                        fSymbol.bind(it)
                    }
                }
            } else {
                call.argumentList.arguments[index]
            } to parameter.fir
        }

        val newCall = buildFunctionCall {
            this.source = call.source
            this.coneTypeOrNull = call.resolvedType
            this.argumentList = buildResolvedArgumentList(original = null, linkedMapOf(*autoclosureParameters.toTypedArray()))
            this.calleeReference = buildResolvedNamedReference {
                source = originalSymbol.source
                name = originalSymbol.name
                resolvedSymbol = originalSymbol
            }
        }

        return newCall
    }

    object Key: GeneratedDeclarationKey() {
        override fun toString(): String {
            return "AutoClosureGeneratorKey"
        }
    }
}
