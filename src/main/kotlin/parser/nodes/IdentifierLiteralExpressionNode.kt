package parser.nodes

import generator.ASMBuilder
import generator.types.PointerDescriptor
import generator.types.TypeDescriptor

class IdentifierLiteralExpressionNode(val name: String): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        val variable = asmBuilder.getVariable(name)

        asmBuilder.append("lea rax, [rsp + ${asmBuilder.offsetToVariable(variable)}]")
        asmBuilder.push("rax")

        return PointerDescriptor(variable.second)
    }

    override fun toString(): String {
        return this.name
    }
}