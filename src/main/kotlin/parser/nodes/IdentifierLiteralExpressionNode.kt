package parser.nodes

import generator.ASMBuilder
import generator.types.TypeDescriptor

class IdentifierLiteralExpressionNode(val name: String): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder): TypeDescriptor {
        val variable = asmBuilder.getVariable(name)
        val size = variable.second.sizeOf()

        asmBuilder.growStack(size)
        variable.second.copyTo(
            0,
            asmBuilder.offsetToVariable(variable),
            asmBuilder
        )

        return variable.second
    }

    override fun toString(): String {
        return this.name
    }
}