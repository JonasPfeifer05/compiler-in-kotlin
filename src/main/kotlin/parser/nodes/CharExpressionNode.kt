package parser.nodes

import errors.generator.InvalidCharLengthException
import generator.ASMBuilder
import generator.ConstantValue
import generator.Offset
import generator.Register
import generator.types.CharDescriptor
import generator.types.TypeDescriptor

class CharExpressionNode(private val char: String): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        asmBuilder.growStack(1)
        val code = if (this.char.length == 1) {
            char[0].code
        } else if (this.char.length == 2) {
            when (this.char) {
                "\\n" -> 10
                "\\r" -> 13
                "\\t" -> 9
                else -> throw InvalidCharLengthException(2)
            }
        } else throw InvalidCharLengthException(char.length)

        asmBuilder.append("mov BYTE [rsp], $code")

        return CharDescriptor()
    }

    override fun toString(): String {
        return this.char
    }
}