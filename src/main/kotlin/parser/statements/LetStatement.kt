package parser.statements

import generator.ASMBuilder
import parser.nodes.ExpressionNode

class LetStatement(private val name: String, private val expression: ExpressionNode): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        this.expression.evaluate(asmBuilder)
        asmBuilder.push("rax")
        asmBuilder.lookupTable.registerNewVariable(name)
    }
}