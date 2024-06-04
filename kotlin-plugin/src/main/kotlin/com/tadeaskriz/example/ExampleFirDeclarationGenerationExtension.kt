@file:OptIn(ExperimentalTopLevelDeclarationsGenerationApi::class)

package com.tadeaskriz.example

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.ExperimentalTopLevelDeclarationsGenerationApi
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension

class ExampleFirDeclarationGenerationExtension(session: FirSession): FirDeclarationGenerationExtension(session) {

}
