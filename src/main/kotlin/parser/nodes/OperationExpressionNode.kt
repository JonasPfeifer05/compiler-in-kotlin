package parser.nodes

import errors.generator.CrossOperationException
import errors.generator.IllegalOperationException
import general.Register
import general.unreachable
import generator.ASMBuilder
import generator.types.StringDescriptor
import generator.types.TypeDescriptor
import generator.types.U64Descriptor
import lexer.TokenFlag

class OperationExpressionNode(
    private val left: ExpressionNode,
    private val operation: TokenFlag,
    private val right: ExpressionNode,
): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        val rightType = this.right.evaluateOntoStack(asmBuilder)
        val leftType = this.left.evaluateOntoStack(asmBuilder)

        if (leftType::class != rightType::class)
            throw CrossOperationException(this.operation, leftType, rightType)

        when (leftType) {
            is StringDescriptor -> {
                return when (this.operation) {
                    TokenFlag.Plus -> StringDescriptor(leftType.sizeOf() + rightType.sizeOf())
                    else -> throw IllegalOperationException(this.operation, leftType, rightType)
                }
            }
            is U64Descriptor -> {
                asmBuilder.pop(Register.PrimaryCalculation.register)
                asmBuilder.pop(Register.SecondaryCalculation.register)

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
                return U64Descriptor()
            }
            else -> unreachable()
        }
    }

    override fun toString(): String {
        return this.left.toString() + this.operation.getSymbol() + this.right.toString()
    }
}