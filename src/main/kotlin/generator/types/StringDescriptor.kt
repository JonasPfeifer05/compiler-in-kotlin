package generator.types

import generator.ASMBuilder

class StringDescriptor(private val length: Int): TypeDescriptor() {
    override fun sizeOf(): Int = this.length
    override fun copyTo(offsetTo: Int, offsetFrom: Int, asmBuilder: ASMBuilder) {
        asmBuilder.memcpy(
            offsetTo, offsetFrom, this.sizeOf()
        )
    }

    override fun copyTo(offsetTo: Int, from: String, asmBuilder: ASMBuilder) {
        asmBuilder.memcpy(
            offsetTo, from, this.sizeOf()
        )
    }

    override fun toString(): String {
        return "String(${this.length})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StringDescriptor

        return length == other.length
    }

    override fun hashCode(): Int {
        return length
    }


}