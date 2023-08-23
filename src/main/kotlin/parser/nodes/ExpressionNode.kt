package parser.nodes

import general.Register
import generator.ASMBuilder

abstract class ExpressionNode {
    abstract fun evaluate(asmBuilder: ASMBuilder, evalRegister: Register = Register.PrimaryCalculation)
}