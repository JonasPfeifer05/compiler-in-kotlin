package parser.nodes

import general.Register
import generator.ASMBuilder

class Expression(private val value: ExpressionNode) {
    fun evaluate(asmBuilder: ASMBuilder, evalRegister: Register = Register.PrimaryCalculation) {
        this.value.evaluate(asmBuilder)
        asmBuilder.pop(evalRegister.register)
    }

    override fun toString(): String {
        return this.value.toString()
    }

}