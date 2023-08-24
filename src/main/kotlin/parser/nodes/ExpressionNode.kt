package parser.nodes

import generator.ASMBuilder

abstract class ExpressionNode {
    abstract fun evaluate(asmBuilder: ASMBuilder)
}