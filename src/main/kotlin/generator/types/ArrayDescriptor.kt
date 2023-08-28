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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArrayDescriptor

        if (content != other.content) return false
        if (length != other.length) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.hashCode()
        result = 31 * result + length
        return result
    }


}