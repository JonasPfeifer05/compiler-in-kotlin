package parser.nodes

import errors.generator.ArrayCanOnlyStoreOneType
import errors.generator.InvalidArrayLengthException
import generator.ASMBuilder
import generator.types.ArrayDescriptor
import generator.types.TypeDescriptor

class ArrayExpressionNode(private val values: List<ExpressionNode>): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        var lastType: TypeDescriptor? = null

        // Reversed to correct for the stack layout
        for (value in values.reversed()) {

            val valueType = value.evaluateOntoStack(asmBuilder)
            // Check if the array only contains one type only
            if (lastType != null && lastType != valueType) throw ArrayCanOnlyStoreOneType(lastType, valueType)

            lastType = valueType
        }

        // Array must have at least 1 item
        if (lastType == null) throw InvalidArrayLengthException(0)

        return ArrayDescriptor(lastType, this.values.size)
    }

    override fun toString(): String {
        return "[${this.values.joinToString()}]"
    }
}