package parser.statements

import generator.ASMBuilder

abstract class Statement {
    abstract fun toAssembly(asmBuilder: ASMBuilder)
}