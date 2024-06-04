package com.tadeaskriz.autofactory

import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

object AutoFactoryNames {
    object Annotation {
        val autoFactory = ClassId(FqName.ROOT, Name.identifier("AutoFactory"))
        val provided = ClassId(FqName.ROOT, Name.identifier("Provided"))
    }

    val factory = Name.identifier("Factory")
    val createFun = Name.identifier("create")
}
