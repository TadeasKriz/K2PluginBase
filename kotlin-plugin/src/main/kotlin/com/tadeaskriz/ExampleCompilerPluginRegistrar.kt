package com.tadeaskriz

import com.tadeaskriz.assign.MutableStateFlowAssignFirExtensionRegistrar
import com.tadeaskriz.autoclosure.AutoClosureFirExtensionRegistrar
import com.tadeaskriz.autofactory.AutoFactoryFirExtensionRegistrar
import com.tadeaskriz.autofactory.AutoFactoryIrGenerationExtension
import com.tadeaskriz.example.ExampleFirExtensionRegistrar
import com.tadeaskriz.logger.LoggerFirExtensionRegistrar
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

@OptIn(ExperimentalCompilerApi::class)
class ExampleCompilerPluginRegistrar: CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        FirExtensionRegistrarAdapter.registerExtension(ExampleFirExtensionRegistrar())

        FirExtensionRegistrarAdapter.registerExtension(AutoFactoryFirExtensionRegistrar())
        IrGenerationExtension.registerExtension(AutoFactoryIrGenerationExtension())

        FirExtensionRegistrarAdapter.registerExtension(MutableStateFlowAssignFirExtensionRegistrar())

        FirExtensionRegistrarAdapter.registerExtension(LoggerFirExtensionRegistrar())

        FirExtensionRegistrarAdapter.registerExtension(AutoClosureFirExtensionRegistrar())
    }
}
