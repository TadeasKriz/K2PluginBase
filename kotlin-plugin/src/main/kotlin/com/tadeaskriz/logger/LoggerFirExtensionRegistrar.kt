package com.tadeaskriz.logger

import org.jetbrains.kotlin.fir.extensions.FirExtensionApiInternals
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class LoggerFirExtensionRegistrar: FirExtensionRegistrar() {
    @OptIn(FirExtensionApiInternals::class)
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::LoggerFirFunctionCallRefinementExtension
    }
}
