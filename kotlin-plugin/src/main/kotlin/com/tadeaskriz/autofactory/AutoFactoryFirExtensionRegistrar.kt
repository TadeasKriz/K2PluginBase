package com.tadeaskriz.autofactory

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class AutoFactoryFirExtensionRegistrar: FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::AutoFactoryFirDeclarationGenerationExtension
    }
}
