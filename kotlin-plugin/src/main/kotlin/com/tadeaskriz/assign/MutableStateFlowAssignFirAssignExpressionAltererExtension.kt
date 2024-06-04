package com.tadeaskriz.assign

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.fir.expressions.FirVariableAssignment
import org.jetbrains.kotlin.fir.expressions.builder.buildPropertyAccessExpression
import org.jetbrains.kotlin.fir.extensions.FirAssignExpressionAltererExtension
import org.jetbrains.kotlin.fir.references.builder.buildSimpleNamedReference
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.resolvedType
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class MutableStateFlowAssignFirAssignExpressionAltererExtension(session: FirSession): FirAssignExpressionAltererExtension(session) {

    override fun transformVariableAssignment(variableAssignment: FirVariableAssignment): FirStatement? {
        if (variableAssignment.lValue.resolvedType.classId != mutableStateFlow) {
            return null
        }

        variableAssignment.replaceLValue(
            buildPropertyAccessExpression {
                explicitReceiver = variableAssignment.lValue
                calleeReference = buildSimpleNamedReference {
                    source = variableAssignment.source
                    name = Name.identifier("value")
                }
            }
        )

        return variableAssignment
    }

    companion object {
        val mutableStateFlow = ClassId(FqName("kotlinx.coroutines.flow"), Name.identifier("MutableStateFlow"))
    }
}
