package parser.nodes

import errors.generator.InvalidArrayLengthException
import errors.generator.WrongTypeException
import generator.ASMBuilder
import generator.types.ArrayDescriptor
import generator.types.TypeDescriptor

class ArrayExpressionNode(private val values: List<ExpressionNode>): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder): TypeDescriptor {
        var lastType: TypeDescriptor? = null
        for (value in values.reversed()) {
            val type = value.evaluate(asmBuilder)
            if (lastType != null && lastType::class != type::class)
                throw WrongTypeException(lastType, type)
            lastType = type
        }

        if (lastType == null) throw InvalidArrayLengthException(0)

        return ArrayDescriptor(lastType, this.values.size)
    }

    override fun toString(): String {
        return "[${this.values.joinToString()}]"
    }
}