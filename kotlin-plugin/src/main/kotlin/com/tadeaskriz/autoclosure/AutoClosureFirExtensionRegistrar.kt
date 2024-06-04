package com.tadeaskriz.autoclosure

import org.jetbrains.kotlin.fir.extensions.FirExtensionApiInternals
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class AutoClosureFirExtensionRegistrar: FirExtensionRegistrar() {
    @OptIn(FirExtensionApiInternals::class)
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::AutoClosureFirFunctionCallRefinementExtension
        +::AutoClosureFirAdditionalCheckersExtension
    }
}
