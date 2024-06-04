package com.tadeaskriz.autofactory

import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.primaryConstructor

val IrClass.parentAutoFactoryConstructor: IrConstructor?
    get() = when (val parent = parent) {
        is IrClass -> {
            parent.constructors.firstOrNull {
                it.annotations.hasAnnotation(AutoFactoryNames.Annotation.autoFactory.asSingleFqName())
            } ?: if (parent.hasAnnotation(AutoFactoryNames.Annotation.autoFactory)) {
                parent.primaryConstructor
            } else {
                null
            }
        }
        else -> null
    }
