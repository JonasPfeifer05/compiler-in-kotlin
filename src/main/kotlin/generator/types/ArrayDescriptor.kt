package generator.types

import generator.ASMBuilder

class ArrayDescriptor(val content: TypeDescriptor, private val length: Int): TypeDescriptor() {
    override fun sizeOf(): Int {
        return this.length * this.content.sizeOf()
    }

    override fun copyTo(offsetTo: Int, offsetFrom: Int, asmBuilder: ASMBuilder) {
        asmBuilder.memcpy(
            offsetTo,
            offsetFrom,
            this.sizeOf(),
        )
    }

    override fun copyTo(offsetTo: Int, from: String, asmBuilder: ASMBuilder) {
        asmBuilder.memcpy(
            offsetTo,
            from,
            this.sizeOf(),
        )
    }

    override fun toString(): String {
        return "$content[${this.length}]"
    }
}