package parser.nodes

import generator.ASMBuilder
import generator.types.TypeDescriptor

class Expression(private val value: ExpressionNode) {
    fun evaluate(asmBuilder: ASMBuilder): TypeDescriptor {
        return this.value.evaluate(asmBuilder)
    }

    override fun toString(): String {
        return this.value.toString()
    }

}