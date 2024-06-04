package com.tadeaskriz.assign

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class MutableStateFlowAssignFirExtensionRegistrar: FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::MutableStateFlowAssignFirAssignExpressionAltererExtension
    }
}
