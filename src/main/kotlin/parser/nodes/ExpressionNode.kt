package parser.nodes

import generator.ASMBuilder
import generator.types.TypeDescriptor

abstract class ExpressionNode {
    abstract fun evaluate(asmBuilder: ASMBuilder): TypeDescriptor
}