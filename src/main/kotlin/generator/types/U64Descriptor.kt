package generator.types

import generator.*

class U64Descriptor : TypeDescriptor() {
    override fun sizeOf(): Int = 8
    override fun copyTo(to: AddressFrom, from: AddressFrom, asmBuilder: ASMBuilder) {
        asmBuilder.mov(
            Register.Rbx, from.withSize(MemorySizes.QWord)
        )
        asmBuilder.mov(
            to.withSize(MemorySizes.QWord), Register.Rbx
        )
    }

    override fun toString(): String {
        return "U64"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }


}