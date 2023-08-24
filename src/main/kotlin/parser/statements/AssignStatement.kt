package parser.statements

import generator.ASMBuilder
import parser.nodes.Expression

class AssignStatement(private val name: String, private val expression: Expression): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        this.expression.evaluate(asmBuilder)
        asmBuilder.mov(
            asmBuilder.stackAddrWithOffset(
                asmBuilder.getVariableOffset(name)
            ),
            "rax"
        )
    }

    override fun toString(): String {
        return "$name = $expression;"
    }
}