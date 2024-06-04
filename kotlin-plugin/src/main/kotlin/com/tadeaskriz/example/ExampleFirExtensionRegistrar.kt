package com.tadeaskriz.example

import com.tadeaskriz.example.ExampleFirAssignExpressionAltererExtension
import com.tadeaskriz.example.ExampleFirFunctionCallRefinementExtension
import org.jetbrains.kotlin.fir.extensions.FirExtensionApiInternals
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class ExampleFirExtensionRegistrar: FirExtensionRegistrar() {
    @OptIn(FirExtensionApiInternals::class)
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::ExampleFirAdditionalCheckersExtension

        +::ExampleFirDeclarationGenerationExtension

        +::ExampleFirAssignExpressionAltererExtension

        +::ExampleFirFunctionCallRefinementExtension

        +::ExampleFirExpressionResolutionExtension
    }
}
