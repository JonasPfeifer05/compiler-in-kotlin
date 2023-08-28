package parser.nodes

import errors.generator.CrossOperationException
import general.unreachable
import generator.ASMBuilder
import generator.Register
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

        if (leftType != rightType) throw CrossOperationException(this.operation, leftType, rightType)

        when (leftType) {
            //is StringDescriptor -> {
            //    return when (this.operation) {
            //        TokenFlag.Plus -> StringDescriptor(leftType.sizeOf() + rightType.sizeOf())
            //        else -> throw IllegalOperationException(this.operation, leftType, rightType)
            //    }
            //}
            is U64Descriptor -> {
                asmBuilder.pop(
                    Register.PrimaryCalculation
                )
                asmBuilder.pop(
                    Register.SecondaryCalculation
                )

                when (this.operation) {
                    TokenFlag.Plus -> asmBuilder.add(
                        Register.PrimaryCalculation, Register.SecondaryCalculation
                    )
                    TokenFlag.Minus -> asmBuilder.sub(
                        Register.PrimaryCalculation, Register.SecondaryCalculation
                    )
                    TokenFlag.Mul -> asmBuilder.imul(
                        Register.PrimaryCalculation, Register.SecondaryCalculation
                    )
                    TokenFlag.Div -> {
                        // The rdx register is needed by the div operation because of this we set the register to 0 by xor with itself
                        asmBuilder.xor(
                            Register.Rdx, Register.Rdx
                        )
                        asmBuilder.div(
                            Register.SecondaryCalculation
                        )
                    }
                    else -> unreachable()
                }

                asmBuilder.push(
                    Register.PrimaryCalculation
                )
                return U64Descriptor()
            }
            else -> unreachable()
        }
    }


    override fun toString(): String {
        return this.left.toString() + this.operation.getSymbol() + this.right.toString()
    }
}