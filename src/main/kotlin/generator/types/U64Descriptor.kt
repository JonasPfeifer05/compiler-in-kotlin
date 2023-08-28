package generator.types

import generator.*

class U64Descriptor : TypeDescriptor() {
    override fun sizeOf(): Int = 8
    override fun copyTo(to: DataSource, from: DataSource, asmBuilder: ASMBuilder) {
        asmBuilder.mov(
            Register.Rbx, from
        )
        asmBuilder.mov(
            to, Register.Rbx
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