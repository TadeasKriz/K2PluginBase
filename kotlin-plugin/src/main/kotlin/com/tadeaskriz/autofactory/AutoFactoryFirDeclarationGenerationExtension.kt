@file:OptIn(ExperimentalTopLevelDeclarationsGenerationApi::class)

package com.tadeaskriz.autofactory

import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.primaryConstructorSymbol
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingClassSymbol
import org.jetbrains.kotlin.fir.declarations.hasAnnotation
import org.jetbrains.kotlin.fir.extensions.ExperimentalTopLevelDeclarationsGenerationApi
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.NestedClassGenerationContext
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.plugin.createConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.plugin.createNestedClass
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.types.constructStarProjectedType
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

class AutoFactoryFirDeclarationGenerationExtension(session: FirSession): FirDeclarationGenerationExtension(session) {

    private val predicateBasedProvider = session.predicateBasedProvider
    private val matchedClasses by lazy {
        predicateBasedProvider.getSymbolsByPredicate(predicate).filterIsInstance<FirRegularClassSymbol>()
    }
    private val nestedClassIds by lazy {
        matchedClasses.map { it.classId.createNestedClassId(AutoFactoryNames.factory) }
    }

    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>,
        name: Name,
        context: NestedClassGenerationContext
    ): FirClassLikeSymbol<*>? {
        return if (matchedClasses.none { it == owner }) {
            null
        } else {
            createNestedClass(owner, name, Key).symbol
        }
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        val matchedClassSymbol = context.owner.getContainingClassSymbol(session) as? FirClassSymbol<*> ?: return emptyList()
        val primaryConstructor = matchedClassSymbol.primaryConstructorSymbol(session) ?: return emptyList()

        val constructor = createConstructor(context.owner, Key, isPrimary = true) {
            primaryConstructor
                .valueParameterSymbols
                .filter {
                    !it.hasAnnotation(AutoFactoryNames.Annotation.provided, session)
                }
                .forEach { parameter ->
                    valueParameter(
                        name = parameter.name,
                        type = parameter.resolvedReturnType,
                    )
                }
        }
        return listOf(
            constructor.symbol
        )
    }

    override fun generateFunctions(
        callableId: CallableId,
        context: MemberGenerationContext?
    ): List<FirNamedFunctionSymbol> {
        if (callableId.callableName != AutoFactoryNames.createFun) {
            return emptyList()
        }
        if (context == null) {
            return emptyList()
        }
        val matchedClassSymbol = (context.owner.getContainingClassSymbol(session) as? FirClassSymbol<*>) ?: return emptyList()
        val primaryConstructor = matchedClassSymbol.primaryConstructorSymbol(session) ?: return emptyList()

        val function = createMemberFunction(
            context.owner,
            Key,
            callableId.callableName,
            matchedClassSymbol.constructStarProjectedType()
        ) {
            primaryConstructor
                .valueParameterSymbols
                .filter {
                    it.hasAnnotation(AutoFactoryNames.Annotation.provided, session)
                }
                .forEach { parameter ->
                    valueParameter(
                        name = parameter.name,
                        type = parameter.resolvedReturnType,
                    )
                }
        }

        return listOf(function.symbol)
    }

    override fun getNestedClassifiersNames(
        classSymbol: FirClassSymbol<*>,
        context: NestedClassGenerationContext
    ): Set<Name> {
        return if (classSymbol in matchedClasses) {
            setOf(AutoFactoryNames.factory)
        } else {
            emptySet()
        }
    }

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        return if (classSymbol.classId in nestedClassIds) {
            setOf(
                SpecialNames.INIT,
                AutoFactoryNames.createFun,
            )
        } else {
            emptySet()
        }
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(
            predicate
        )
    }

    companion object {
        val predicate = LookupPredicate.create {
            annotated(AutoFactoryNames.Annotation.autoFactory.asSingleFqName())
        }
    }

    object Key: GeneratedDeclarationKey() {
        override fun toString(): String {
            return "AutoFactoryGeneratorKey"
        }
    }
}
