package generator

import parser.statements.Statement

class ASMGenerator(private val statements: List<Statement>) {
    private val asmBuilder = ASMBuilder(StringBuilder())

    init {
        asmBuilder.init()
    }

    fun generateAssembly(): String {

        for (node in this.statements) {
            node.toAssembly(asmBuilder)
        }

        return asmBuilder.toString()
    }
}