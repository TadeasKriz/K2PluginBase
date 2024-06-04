package com.tadeaskriz.autofactory

import org.jetbrains.kotlin.backend.common.ClassLoweringPass
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addField
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.irConstructorCall
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

open class AutoFactoryIrGenerator(
    private val compilerContext: IrPluginContext
): IrElementTransformerVoid(), ClassLoweringPass {

    override fun lower(irClass: IrClass) {
        if (irClass.name != AutoFactoryNames.factory) { return }

        val autoFactoryConstructor = irClass.parentAutoFactoryConstructor ?: return

//        irClass.transformChildren(AutoFactoryFakeOverrideTransformer(anyClass = compilerContext.irBuiltIns.anyClass), null)

        val factoryConstructor = irClass.constructors.single { it.visibility == DescriptorVisibilities.PUBLIC }
        val factoryPrimaryConstructor = irClass.primaryConstructor!!
        val factoryCreateMethod = irClass.functions.single { it.name == AutoFactoryNames.createFun }
        // irClass.addFakeOverrides(compilerContext.irBuiltIns, listOf(factoryCreateMethod))

        // IrOverridingUtil(compilerContext.irBuiltIns, FakeOverrideBuilder())

        val newParametersAccess: List<FactoryInput> = autoFactoryConstructor.valueParameters.map { parameter ->
            if (parameter.hasAnnotation(AutoFactoryNames.Annotation.provided)) {
                val newParameter = factoryCreateMethod.valueParameters.single { it.name == parameter.name }
                FactoryInput.Provided(newParameter)
            } else {
                val field = irClass.addField {
                    updateFrom(parameter)
                    name = parameter.name
                    type = parameter.type
                    visibility = DescriptorVisibilities.PRIVATE
                }
                val constructorParameter = factoryConstructor.valueParameters.single { it.name == parameter.name }
                FactoryInput.Injected(field, constructorParameter)
            }
        }

        factoryConstructor.body = DeclarationIrBuilder(compilerContext, factoryConstructor.symbol).irBlockBody {
            +irDelegatingConstructorCall(
                if (factoryConstructor == factoryPrimaryConstructor) {
                    compilerContext.symbols.any.constructors.first().owner
                } else {
                    factoryPrimaryConstructor
                }
            )

            for (newParameterAccess in newParametersAccess) {
                if (newParameterAccess is FactoryInput.Injected) {
                    +irSetField(irGet(irClass.thisReceiver!!), newParameterAccess.field, irGet(newParameterAccess.parameter))
                }
            }
        }

        factoryCreateMethod.body = DeclarationIrBuilder(compilerContext, factoryCreateMethod.symbol).irBlockBody {
            +irReturn(
                irConstructorCall(
                    irCall(autoFactoryConstructor).also { call ->
                        for ((index, newParameterAccess) in newParametersAccess.withIndex()) {
                            when (newParameterAccess) {
                                is FactoryInput.Provided -> {
                                    call.putValueArgument(index, irGet(newParameterAccess.parameter))
                                }
                                is FactoryInput.Injected -> {
                                    call.putValueArgument(index, irGetField(irGet(factoryCreateMethod.dispatchReceiverParameter!!), newParameterAccess.field))
                                }
                            }
                        }
                    },
                    autoFactoryConstructor.symbol
                )
            )
        }
    }

    private sealed interface FactoryInput {
        data class Provided(val parameter: IrValueParameter): FactoryInput
        data class Injected(val field: IrField, val parameter: IrValueParameter): FactoryInput
    }
}
