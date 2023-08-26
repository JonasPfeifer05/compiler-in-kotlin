package parser.nodes

import generator.ASMBuilder
import generator.MemorySizes
import generator.types.StringDescriptor
import generator.types.TypeDescriptor

class StringLiteralExpressionNode(private val value: String): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder): TypeDescriptor {
        val chunk8 = this.value.chunked(8).toMutableList()
        var endOfString = chunk8.removeLast()
        if (endOfString.length == 8) {
            chunk8.add(endOfString)
            endOfString = ""
        }

        var chunk4: String? = null
        if (endOfString.length >= 4) {
            chunk4 = endOfString.substring(0, 4)
            endOfString = endOfString.removeRange(0,4)
        }

        var chunk2: String? = null
        if (endOfString.length >= 2) {
            chunk2 = endOfString.substring(0, 2)
            endOfString = endOfString.removeRange(0,2)
        }

        var chunk1: String? = null
        if (endOfString.length == 1) {
            chunk1 = endOfString
        }

        if (chunk1 != null) {
            asmBuilder.growStack(1)
            asmBuilder.mov(
                asmBuilder.pointerWithOffset("rsp", 0, MemorySizes.Byte),
                "`$chunk1`"
            )
        }

        if (chunk2 != null) {
            asmBuilder.growStack(2)
            asmBuilder.mov(
                asmBuilder.pointerWithOffset("rsp", 0, MemorySizes.Word),
                "`$chunk2`"
            )
        }

        if (chunk4 != null) {
            asmBuilder.growStack(4)
            asmBuilder.mov(
                asmBuilder.pointerWithOffset("rsp", 0, MemorySizes.DWord),
                "`$chunk4`"
            )
        }

        for (chunk in chunk8) {
            asmBuilder.mov("rax", "`$chunk`")
            asmBuilder.push("rax")
        }

        return StringDescriptor(this.value.length)
    }

    override fun toString(): String {
        return "\"${this.value}\""
    }
}