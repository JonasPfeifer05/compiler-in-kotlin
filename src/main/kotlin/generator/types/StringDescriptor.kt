package generator.types

import generator.ASMBuilder

class StringDescriptor(private val length: UInt): TypeDescriptor() {
    override fun sizeOf(): UInt = this.length
    override fun copyTo(offsetTo: UInt, offsetFrom: UInt, asmBuilder: ASMBuilder) {
        asmBuilder.memcpy(
            offsetTo.toInt(), offsetFrom.toInt(), this.sizeOf()
        )
    }

    override fun toString(): String {
        return "String(${this.length})"
    }
}