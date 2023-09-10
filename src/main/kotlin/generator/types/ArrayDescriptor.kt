package generator.types

import generator.ASMBuilder
import generator.AddressFrom

class ArrayDescriptor(val content: TypeDescriptor, private val length: Int): TypeDescriptor() {
    override fun sizeOf(): Int {
        return this.length * this.content.sizeOf()
    }

    override fun copyTo(to: AddressFrom, from: AddressFrom, asmBuilder: ASMBuilder) {
        asmBuilder.memcpy(
            to,
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