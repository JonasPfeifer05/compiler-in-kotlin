package parser.nodes

import general.Register
import general.unreachable
import generator.ASMBuilder
import generator.types.TypeDescriptor
import generator.types.U64Descriptor
import lexer.TokenFlag

class OperationExpressionNode(
    private val left: ExpressionNode,
    private val operation: TokenFlag,
    private val right: ExpressionNode,
): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder): TypeDescriptor {
        this.left.evaluate(asmBuilder)
        this.right.evaluate(asmBuilder)

        asmBuilder.pop(Register.SecondaryCalculation.register)
        asmBuilder.pop(Register.PrimaryCalculation.register)

        when (this.operation) {
            TokenFlag.Plus -> asmBuilder.add(Register.PrimaryCalculation.register, Register.SecondaryCalculation.register)
            TokenFlag.Minus -> asmBuilder.sub(Register.PrimaryCalculation.register, Register.SecondaryCalculation.register)
            TokenFlag.Mul -> asmBuilder.imul(Register.PrimaryCalculation.register, Register.SecondaryCalculation.register)
            TokenFlag.Div -> {
                // The rdx register is needed by the div operation because of this we set the register to 0 by xor with itself
                asmBuilder.xor("rdx", "rdx")
                asmBuilder.div(Register.SecondaryCalculation.register)
            }
            else -> unreachable()
        }

        asmBuilder.push(Register.PrimaryCalculation.register)

        return U64Descriptor
    }

    override fun toString(): String {
        return this.left.toString() + this.operation.getSymbol() + this.right.toString()
    }
}