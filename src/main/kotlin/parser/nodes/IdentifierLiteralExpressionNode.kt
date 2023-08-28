package parser.nodes

import generator.ASMBuilder
import generator.ConstantValue
import generator.Register
import generator.types.PointerDescriptor
import generator.types.TypeDescriptor

class IdentifierLiteralExpressionNode(private val name: String): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        // Get the variable
        val variable = asmBuilder.getVariable(name)

        // Calculate the pointer to the variable start
        asmBuilder.lea(
            Register.Rax, Register.Rsp, ConstantValue(asmBuilder.offsetToVariable(variable))
        )

        // Push the address on the stack
        asmBuilder.push(
            Register.Rax
        )

        return PointerDescriptor(variable.second)
    }

    override fun toString(): String {
        return this.name
    }
}