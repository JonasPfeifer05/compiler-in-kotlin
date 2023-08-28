package parser.nodes

import generator.ASMBuilder
import generator.types.TypeDescriptor

abstract class ExpressionNode {
    abstract fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor
}